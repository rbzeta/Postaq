package app.rbzeta.postaq.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.application.AppConfig;
import app.rbzeta.postaq.application.MyApplication;
import app.rbzeta.postaq.custom.CircleTransform;
import app.rbzeta.postaq.helper.FileUtils;
import app.rbzeta.postaq.helper.PermissionHelper;
import app.rbzeta.postaq.helper.SessionManager;
import app.rbzeta.postaq.helper.StringUtils;
import app.rbzeta.postaq.home.view.NewPostFragment;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class NewQuestionActivity extends AppCompatActivity {
    private static final String TAG = NewPostFragment.class.getSimpleName();

    private ImageView imgUserAvatar,imgPost;
    private TextView textUserName;
    private EditText editPostText;
    private Button buttonSubmitPost;
    private ImageButton buttonAddImage,buttonDeleteImage;
    private View imgPostContainer;
    private NestedScrollView scrollView;
    private boolean isTextPostIsFilled = false;

    private PermissionHelper permissionHelper;

    private static final int REQUEST_IMAGE_FROM_CAMERA = 10;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 11;
    public static final int REQUEST_USE_CAMERA = 1;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    private Uri photoURI;
    private File imageFromCamera = null;
    private File fileToUpload = null;
    private String imageFileName;
    private File storageDir;
    private String mCurrentPhotoPath;
    private final int mScaleFactor = 5;

    private SessionManager session = MyApplication.getInstance().getSessionManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        permissionHelper = new PermissionHelper(this);

        imgUserAvatar = (ImageView)findViewById(R.id.img_new_post_avatar);
        imgPost = (ImageView)findViewById(R.id.img_new_post_question);
        textUserName = (TextView)findViewById(R.id.text_new_post_user_name);
        editPostText = (EditText)findViewById(R.id.edit_new_post_text);
        buttonSubmitPost = (Button)findViewById(R.id.button_new_post_ask_question);
        buttonAddImage = (ImageButton)findViewById(R.id.button_new_post_image);
        buttonDeleteImage = (ImageButton)findViewById(R.id.button_new_post_delete_image);
        imgPostContainer = findViewById(R.id.container_img_new_post_question);
        scrollView = (NestedScrollView) findViewById(R.id.scroll_new_post_text);

        String url = session.getUserProfilePictureUrl();
        if (url == null) {
            Glide.with(this).load(R.drawable.img_user_profile_default)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imgUserAvatar);
        }else
            Glide.with(this).load(url)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .error(R.drawable.img_user_profile_default)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imgUserAvatar);

        textUserName.setText(session.getUserName());

        editPostText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0){
                    buttonSubmitPost.setEnabled(true);
                    isTextPostIsFilled = true;
                }else {
                    buttonSubmitPost.setEnabled(false);
                    isTextPostIsFilled = false;
                }
            }
        });
        buttonDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgPostContainer.setVisibility(View.GONE);
                //delete image
                if (fileToUpload != null)
                    fileToUpload.delete();
            }
        });

        editPostText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //if(view.getId() == R.id.edit_new_post_text){
                //  view.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction()&MotionEvent.ACTION_MASK){
                    case    MotionEvent.ACTION_UP:
                        view.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                //}
                return false;
            }
        });


        buttonSubmitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogImagePicker(view);
            }
        });
    }

    private void showDialogImagePicker(final View view) {
        if(!permissionHelper.mayRequestCamera(view))
            return;
        if(!permissionHelper.mayRequestWriteExternalStorage(view))
            return;

        final CharSequence[] items = {getString(R.string.text_camera),getString(R.string.text_gallery),
                getString(R.string.action_cancel)};
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(getString(R.string.dialog_title_select_image_from));
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (items[which].equals(getString(R.string.text_camera))){
                    takePictureFromCamera(view);
                }else if (items[which].equals(getString(R.string.text_gallery))){
                    takePictureFromGallery(view);
                }else
                    dialog.dismiss();
            }
        });
        alertBuilder.show();
    }

    @Override
    protected void onDestroy() {
        if (fileToUpload != null)
            fileToUpload.delete();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isTextPostIsFilled){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.dialog_msg_discard_post_confirmation));
            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (fileToUpload != null)
                        fileToUpload.delete();
                    dialogInterface.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void takePictureFromCamera(View view) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                try{
                    imageFromCamera = createImageFile();
                }catch (IOException ex){
                    Log.d("ERROR : ",ex.getLocalizedMessage());
                }

                if (imageFromCamera != null) {

                    photoURI = FileProvider.getUriForFile(this,
                            AppConfig.STR_FILE_PROVIDER,
                            imageFromCamera);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_FROM_CAMERA);

                }

            }
        }else {
            Snackbar.make(view,R.string.msg_device_no_camera,Snackbar.LENGTH_LONG).show();
        }
    }

    private void takePictureFromGallery(View view) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        /*intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);*/
        startActivityForResult(Intent.createChooser(intent, getString(R.string.dialog_title_select_picture))
                , REQUEST_IMAGE_FROM_GALLERY);

    }

    private File createImageFile() throws IOException {

        String timeStamp = StringUtils.getStringTimeStamp();
        imageFileName = AppConfig.PREFIX_IMG_FILE_NAME + timeStamp;
        storageDir = FileUtils.getImageStorageDirectory();

        if (!storageDir.exists()){
            if (!storageDir.mkdirs()){
                //Log.d(TAG,"Failed to create directory "+storageDir.getAbsolutePath());
                return null;
            }
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );



        // Save a file: path for use with ACTION_VIEW intents
        // mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (fileToUpload != null)
                fileToUpload.delete();

            if (requestCode == REQUEST_IMAGE_FROM_CAMERA){

                fileToUpload = prepareImageCameraForUpload();

                if (fileToUpload != null) {
                    //uploadImageToServer();
                    reloadImagePost();
                }


            }else if(requestCode == REQUEST_IMAGE_FROM_GALLERY){

                fileToUpload = prepareImageGalleryForUpload(data);

                if (fileToUpload != null) {
                    //uploadImageToServer();
                    reloadImagePost();
                }

            }
        }else if (resultCode == RESULT_CANCELED){
            if (requestCode == REQUEST_IMAGE_FROM_CAMERA) {
                imageFromCamera.delete();
            }
        }
    }

    private void reloadImagePost() {
        imgPostContainer.setVisibility(View.VISIBLE);
        imgPostContainer.setVisibility(View.VISIBLE);
        View lastChild = scrollView.getChildAt(scrollView.getChildCount() - 1);
        int bottom = lastChild.getBottom() + scrollView.getPaddingBottom();
        int sy = scrollView.getScrollY();
        int sh = scrollView.getHeight();
        int delta = bottom - (sy + sh);

        scrollView.smoothScrollBy(0, delta);

        Glide.with(this).load(fileToUpload.getAbsolutePath())
                .crossFade()
                .thumbnail(0.2f)
                .centerCrop()
                .error(R.drawable.ic_error_outline_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgPost);
    }

    private File prepareImageGalleryForUpload(Intent data) {

        File file = null;
        OutputStream os;
        if (data != null){

            try {
                /*Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),
                        data.getData());*/
                Uri selectedImage = data.getData();
                mCurrentPhotoPath = getRealPathFromURI(this,selectedImage);

                // Get the dimensions of the View
                int targetW = imgPost.getWidth();
                int targetH = imgPost.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                //int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                //bmOptions.inSampleSize = scaleFactor;
                bmOptions.inSampleSize = mScaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

                ExifInterface ei = null;
                ei = new ExifInterface(mCurrentPhotoPath);

                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        bitmap = rotateImage(bitmap, 90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        bitmap = rotateImage(bitmap, 180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        bitmap = rotateImage(bitmap, 270);
                        break;
                    case ExifInterface.ORIENTATION_NORMAL:
                        bitmap = bitmap;
                        break;
                    default:
                        bitmap = bitmap;
                        break;
                }

                file = createImageFile();

                os = new FileOutputStream(file);

                bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
                os.flush();
                os.close();

                mCurrentPhotoPath = file.getAbsolutePath();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //Log.d(TAG,"File not found : " + file.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                //Log.d(TAG,e.getLocalizedMessage());
            }
        }

        return file;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private File prepareImageCameraForUpload() {
        // Get the dimensions of the View
        int targetW = imgPost.getWidth();
        int targetH = imgPost.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        //int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        //bmOptions.inSampleSize = scaleFactor;

        bmOptions.inSampleSize = mScaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap resultBitmap;
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                resultBitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                resultBitmap = rotateImage(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                resultBitmap = rotateImage(bitmap, 270);
                break;
            case ExifInterface.ORIENTATION_NORMAL:
                resultBitmap = bitmap;
                break;
            default:
                resultBitmap = bitmap;
                break;
        }

        imageFromCamera.delete();

        OutputStream os;
        //file = new File(storageDir,imageFileName);
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            os = new FileOutputStream(file);
            resultBitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
            os.flush();
            os.close();

            mCurrentPhotoPath = file.getAbsolutePath();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Log.d(TAG,"File not found : " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            //Log.d(TAG,e.getLocalizedMessage());
        }

        return file;
        //imageProfilePicture.setImageBitmap(resultBitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_USE_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDialogImagePicker(buttonAddImage);
            }
        }

        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDialogImagePicker(buttonAddImage);
            }
        }
    }
}
