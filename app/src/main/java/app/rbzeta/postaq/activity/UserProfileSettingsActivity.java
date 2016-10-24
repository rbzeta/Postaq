package app.rbzeta.postaq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import app.rbzeta.postaq.app.AppConfig;
import app.rbzeta.postaq.database.MyDBHandler;
import app.rbzeta.postaq.dialog.EditTextDialogFragment;
import app.rbzeta.postaq.helper.PermissionHelper;
import app.rbzeta.postaq.helper.SessionManager;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.rest.ApiClient;
import app.rbzeta.postaq.rest.ApiInterface;
import app.rbzeta.postaq.rest.model.FileProperties;
import app.rbzeta.postaq.rest.model.FileUploadResponseMessage;
import app.rbzeta.postaq.rest.model.ResponseMessage;
import app.rbzeta.postaq.rest.model.User;
import app.rbzeta.postaq.rest.model.UserProfileResponseMessage;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class UserProfileSettingsActivity extends AppCompatActivity
        implements EditTextDialogFragment.ButtonOKDialogListener{

    // LogCat tag
    private static final String TAG = UserProfileSettingsActivity.class.getSimpleName();

    private static final int REQUEST_IMAGE_FROM_CAMERA = 10;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 11;
    public static final int REQUEST_USE_CAMERA = 1;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    private Uri photoURI;
    private File imageFromCamera = null;
    private File fileToUpload = null;
    private String imageFileName;
    private File storageDir;


    private SessionManager session;
    private TextView textNameValue;
    private TextView textEmailValue;
    private TextView textPhoneValue;
    private TextView textEmployeeIdValue;
    private TextView textBranchIdValue;
    private CircleImageView toolbarAvatar;
    private FloatingActionButton fabPicture;
    private PermissionHelper  permissionHelper;
    private String mCurrentPhotoPath;

    private String mNewNameValue;
    private String mNewPhoneValue;
    private String mNewEmployeeIdValue;
    private String mNewBranchIdValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);
        permissionHelper= new PermissionHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarUserProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textNameValue = (TextView)findViewById(R.id.textUserInfoNameValue);
        textEmailValue = (TextView)findViewById(R.id.textUserInfoEmailValue);
        textPhoneValue = (TextView)findViewById(R.id.textUserInfoPhoneValue);
        textEmployeeIdValue = (TextView)findViewById(R.id.textUserInfoEmployeeValue);
        textBranchIdValue = (TextView)findViewById(R.id.textUserInfoBranchIdValue);

        LinearLayout linearUserNameSetting = (LinearLayout)findViewById(R.id.linearUserNameSetting);
        LinearLayout linearUserPhoneSetting = (LinearLayout)findViewById(R.id.linearUserPhoneSetting);
        LinearLayout linearUserEmpIdSetting = (LinearLayout)findViewById(R.id.linearUserEmpIdSetting);
        LinearLayout linearUserBranchIdSetting = (LinearLayout)findViewById(R.id.linearUserBranchIdSetting);

        linearUserNameSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditTextDialogFragment dialog = new EditTextDialogFragment().newInstance(
                        getString(R.string.dialog_title_input_name),
                        getString(R.string.dialog_title_input_name_hint),
                        textNameValue.getText().toString(),
                        getString(R.string.dialog_title_input_name_hint),
                        getString(R.string.text));
                dialog.show(getSupportFragmentManager(),"edit_text_dialog");

            }
        });

        linearUserPhoneSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextDialogFragment dialog = new EditTextDialogFragment().newInstance(
                        getString(R.string.dialog_title_input_phone),
                        getString(R.string.dialog_title_input_phone_hint),
                        textPhoneValue.getText().toString(),
                        getString(R.string.dialog_title_input_phone_hint),
                        getString(R.string.phone));
                dialog.show(getSupportFragmentManager(),"edit_text_dialog");
            }
        });

        linearUserEmpIdSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextDialogFragment dialog = new EditTextDialogFragment().newInstance(
                        getString(R.string.dialog_title_input_empid),
                        getString(R.string.dialog_title_input_empid_hint),
                        textEmployeeIdValue.getText().toString(),
                        getString(R.string.dialog_title_input_empid_hint),
                        getString(R.string.number));
                dialog.show(getSupportFragmentManager(),"edit_text_dialog");
            }
        });

        linearUserBranchIdSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextDialogFragment dialog = new EditTextDialogFragment().newInstance(
                        getString(R.string.dialog_title_input_branchid),
                        getString(R.string.dialog_title_input_branchid_hint),
                        textBranchIdValue.getText().toString(),
                        getString(R.string.dialog_title_input_branchid_hint),
                        getString(R.string.number));
                dialog.show(getSupportFragmentManager(),"edit_text_dialog");
            }
        });



        fabPicture = (FloatingActionButton) findViewById(R.id.fabUserProfilePhoto);
        fabPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                showDialogImagePicker(view);
            }
        });

        session = new SessionManager(getBaseContext(),getBaseContext().MODE_PRIVATE);

        setToolbarUserProperties();

        setUserInfoSettingProperties();

    }

    private void showDialogImagePicker(final View view) {

        if(!permissionHelper.mayRequestCamera(view))
            return;
        if(!permissionHelper.mayRequestWriteExternalStorage(view))
            return;

        final CharSequence[] items = {getString(R.string.text_camera),getString(R.string.text_gallery),
                getString(R.string.action_cancel)};
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(UserProfileSettingsActivity.this);
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

    private void takePictureFromGallery(View view) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        /*intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);*/
        startActivityForResult(Intent.createChooser(intent, getString(R.string.dialog_title_select_picture))
                , REQUEST_IMAGE_FROM_GALLERY);

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

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = AppConfig.PREFIX_IMG_FILE_NAME + timeStamp;
        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + AppConfig.DIR_IMG_FILE_NAME);

        if (!storageDir.exists()){
            if (!storageDir.mkdirs()){
                Log.d(TAG,"Failed to create directory "+storageDir.getAbsolutePath());
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
            if (requestCode == REQUEST_IMAGE_FROM_CAMERA){

                fileToUpload = prepareImageCameraForUpload();

                if (fileToUpload != null) {
                    uploadImageToServer();
                }


            }else if(requestCode == REQUEST_IMAGE_FROM_GALLERY){

                fileToUpload = prepareImageGalleryForUpload(data);

                if (fileToUpload != null) {
                    uploadImageToServer();
                }

            }
        }else if (resultCode == RESULT_CANCELED){
            if (requestCode == REQUEST_IMAGE_FROM_CAMERA) {
                imageFromCamera.delete();
            }
        }

    }

    private File prepareImageGalleryForUpload(Intent data) {

        File file = null;
        OutputStream os;
        if (data != null){

            try {
                /*Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),
                        data.getData());*/
                Uri selectedImage = data.getData();
                mCurrentPhotoPath = getRealPathFromURI(UserProfileSettingsActivity.this,selectedImage);

                // Get the dimensions of the View
                int targetW = toolbarAvatar.getWidth();
                int targetH = toolbarAvatar.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                //bmOptions.inSampleSize = 100;
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
                Log.d(TAG,"File not found : " + file.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG,e.getLocalizedMessage());
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

    /*private File prepareImageGalleryForUpload(Intent data) {

        File file = null;
        OutputStream os;
        if (data != null){

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),
                        data.getData());

                //bitmap = BitmapFactory.decodeFile(, bmOptions);

                file = createImageFile();

                int targetW = toolbarAvatar.getWidth();
                int targetH = toolbarAvatar.getHeight();

                int photoW = bitmap.getWidth();
                int photoH = bitmap.getHeight();

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
                int compress = 100 * 1/scaleFactor;

                os = new FileOutputStream(file);

                bitmap.compress(Bitmap.CompressFormat.JPEG,compress,os);
                os.flush();
                os.close();

                mCurrentPhotoPath = file.getAbsolutePath();

                Bitmap resultBitmap;

                try {

                } catch (IOException e) {
                    e.printStackTrace();
                }

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

                file.delete();

                file = createImageFile();
                os = new FileOutputStream(file);

                bitmap.compress(Bitmap.CompressFormat.JPEG,compress,os);
                os.flush();
                os.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d(TAG,"File not found : " + file.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG,e.getLocalizedMessage());
            }
        }

        return file;
    }*/

    private void uploadImageToServer() {
        final ProgressDialog progress= ProgressDialog.show(this,
                getString(R.string.txt_title_upload_image),
                getString(R.string.txt_dialog_please_wait),true,false);


        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),fileToUpload);

        MultipartBody.Part image = MultipartBody.Part
                .createFormData("picture",
                        fileToUpload.getName(),
                        requestFile);

        RequestBody uuid = RequestBody
                .create(MediaType.parse("multipart/form-data"),
                        session.getUserUuid());

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<FileUploadResponseMessage> call = apiService.uploadImageToServer(image,uuid);
        call.enqueue(new Callback<FileUploadResponseMessage>() {
            @Override
            public void onResponse(Call<FileUploadResponseMessage> call, Response<FileUploadResponseMessage> response) {

                progress.dismiss();

                if (response.body() != null){

                    FileUploadResponseMessage msg = response.body();

                    if (msg.isSuccess()){

                        FileProperties fileProperties = response.body().getFileProperties();

                        session.setUserProfilePictureUrl(fileProperties.getFilePath());

                        galleryAddPic();

                        reloadProfilePicture();

                        session.setUserProfilePictureChanged(true);

                        //make sure image was loaded
                        //reloadProfilePicture();

                        UIHelper.showCustomSnackBar(fabPicture,
                                getString(R.string.text_msg_change_avatar_success), Color.WHITE);

                    }else{
                        fileToUpload.delete();
                        UIHelper.showErrorDialog(getSupportFragmentManager(),getResources(),
                                msg.getTitle(),msg.getMessage());

                    }

                }else {
                    fileToUpload.delete();
                    UIHelper.showErrorDialog(getSupportFragmentManager(),getResources(),
                            getString(R.string.dialog_title_error_upload_profile_picture),
                            getString(R.string.dialog_msg_error_upload_profile_picture));
                    Log.d(TAG,"Response Body is Null");
                }
            }

            @Override
            public void onFailure(Call<FileUploadResponseMessage> call, Throwable t) {
                progress.dismiss();
                fileToUpload.delete();
                UIHelper.showErrorDialog(getSupportFragmentManager(),getResources(),
                        getString(R.string.dialog_title_error_upload_profile_picture),
                        getString(R.string.dialog_msg_error_upload_profile_picture));
                //Log.d(TAG,t.getLocalizedMessage());
            }
        });
    }

    private void reloadProfilePicture() {
        Glide.with(getApplicationContext())
                .load(session.getUserProfilePictureUrl())
                .placeholder(R.drawable.img_user_profile_default)
                .error(R.drawable.img_user_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .centerCrop()
                .thumbnail(0.2f)
                .into(toolbarAvatar);
    }

    private File prepareImageCameraForUpload() {
        // Get the dimensions of the View
        int targetW = toolbarAvatar.getWidth();
        int targetH = toolbarAvatar.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        //bmOptions.inSampleSize = 100;
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
            Log.d(TAG,"File not found : " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,e.getLocalizedMessage());
        }

        return file;
        //toolbarAvatar.setImageBitmap(resultBitmap);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_USE_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDialogImagePicker(fabPicture);
            }
        }

        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDialogImagePicker(fabPicture);
            }
        }
    }

    private void setUserInfoSettingProperties() {
        textNameValue.setText(session.getUserName());
        textEmailValue.setText(session.getUserEmailAddress());
        textPhoneValue.setText(session.getUserPhone());
        textEmployeeIdValue.setText(String.valueOf(session.getUserEmployeeId()));
        textBranchIdValue.setText(String.valueOf(session.getUserBranchId()));
    }

    private void setToolbarUserProperties() {
        TextView toolbarTitle = (TextView) findViewById(R.id.textToolbarTitleName);
        TextView toolbarSubtitle = (TextView) findViewById(R.id.textToolbarSubtitleName);
        toolbarAvatar = (CircleImageView)findViewById(R.id.toolbarAvatar);

        toolbarTitle.setText(session.getUserName());
        toolbarSubtitle.setText(session.getUserEmailAddress());

        String url = session.getUserProfilePictureUrl();
        Glide.with(getApplicationContext())
                .load(url)
                .placeholder(R.drawable.img_user_profile_default)
                .error(R.drawable.img_user_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .centerCrop()
                .into(toolbarAvatar);
    }

    @Override
    public void onBackPressed() {
        if(session.isUserInfoChanged()){
            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileSettingsActivity.this);
            builder.setTitle(getString(R.string.dialog_title_confirm));
            builder.setMessage(getString(R.string.msg_save_changed_value_confirmation));
            builder.setCancelable(false);
            builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    User user = new User();
                    user.setUuid(session.getUserUuid());

                    if (mNewNameValue != null){
                        //session.setUserName(mNewNameValue);
                        user.setName(mNewNameValue);
                    }

                    if (mNewPhoneValue != null){
                        //session.setUserPhone(mNewPhoneValue);
                        user.setPhoneNumber(mNewPhoneValue);
                    }

                    if (mNewEmployeeIdValue != null){
                        //session.setUserEmployeeId(Integer.valueOf(mNewEmployeeIdValue));
                        user.setEmployeeId(Integer.valueOf(mNewEmployeeIdValue));
                    }

                    if (mNewBranchIdValue != null){
                        //session.setUserBranchId(Integer.valueOf(mNewBranchIdValue));
                        user.setBranchId(Integer.valueOf(mNewBranchIdValue));
                    }

                    uploadUserInfoChanges(user);
                    dialog.dismiss();

                }
            });
            builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    session.setUserInfoChanged(false);
                    finish();
                }
            });
            builder.show();

        }else{
            finish();
        }

        //super.onBackPressed();
    }

    private void uploadUserInfoChanges(User user) {
        final ProgressDialog progress= ProgressDialog.show(this,
                getString(R.string.txt_title_save_changes),
                getString(R.string.txt_dialog_please_wait),true,false);

        //for debug purpose, using simulator msg response
        //*******************************************************************
        //user.setEmail(session.getUserEmailAddress());
        //user.setProfilePictureUrl(session.getUserProfilePictureUrl());
        if (user.getName() == null)
            user.setName(session.getUserName());
        if (user.getPhoneNumber() == null)
            user.setPhoneNumber(session.getUserPhone());
        if (user.getEmployeeId() == 0)
            user.setEmployeeId(session.getUserEmployeeId());
        if (user.getBranchId() == 0)
            user.setBranchId(session.getUserBranchId());
        //*******************************************************************

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<UserProfileResponseMessage> call = apiService.updateUserProfile(user);
        call.enqueue(new Callback<UserProfileResponseMessage>() {
            @Override
            public void onResponse(Call<UserProfileResponseMessage> call, Response<UserProfileResponseMessage> response) {
                progress.dismiss();

                if (response.body() != null) {

                    ResponseMessage msg = response.body().getResponseMessage();

                    User user = msg.getUser();

                    if (msg.isSuccess()){

                        if (checkRequiredFields(user)) {

                            saveUserIntoLocalDatabase(user);
                            session.setUserSharedPreferences(user);
                            session.setUserInfoChanged(true);
                            finish();

                        }else {

                            UIHelper.showErrorDialog(getSupportFragmentManager(),getResources(),
                                    getString(R.string.dialog_title_error_save_user_profile),
                                    getString(R.string.dialog_msg_error_save_user_profile));
                            session.setUserInfoChanged(false);
                            setUserInfoSettingProperties();


                        }
                    }else{

                        UIHelper.showErrorDialog(getSupportFragmentManager(),getResources(),
                                msg.getTitle(),msg.getMessage());
                        session.setUserInfoChanged(false);
                        setUserInfoSettingProperties();

                    }

                }else {

                    UIHelper.showErrorDialog(getSupportFragmentManager(),getResources(),
                            getString(R.string.dialog_title_error_save_user_profile),
                            getString(R.string.dialog_msg_error_save_user_profile));
                    session.setUserInfoChanged(false);
                    setUserInfoSettingProperties();

                }

            }

            @Override
            public void onFailure(Call<UserProfileResponseMessage> call, Throwable t) {

                progress.dismiss();
                UIHelper.showErrorDialog(getSupportFragmentManager(),getResources(),
                        getString(R.string.dialog_title_error_save_user_profile),
                        getString(R.string.dialog_msg_error_save_user_profile));
                session.setUserInfoChanged(false);
                setUserInfoSettingProperties();
            }
        });
    }

    @Override
    public void onButtonOKPressed(String text,String editTextType) {
        text = text.trim();

        if (!text.equals("")){
           if (editTextType.equals(getString(R.string.dialog_title_input_name_hint))) {
               if (!session.getUserName().equals(text)){
                   if (text.length() > getResources().getInteger(R.integer.max_name)){
                       UIHelper.showCustomSnackBar(fabPicture,
                               getString(R.string.err_user_name_length),Color.RED);
                       return;
                   }

                   mNewNameValue = text;
                   textNameValue.setText(text);
                   session.setUserInfoChanged(true);
               }

           }else if (editTextType.equals(getString(R.string.dialog_title_input_phone_hint))) {
               if (!session.getUserPhone().equals(text)) {
                   if (text.length() > getResources().getInteger(R.integer.max_phone_number) || text.length() < 6){
                       UIHelper.showCustomSnackBar(fabPicture,
                               getString(R.string.err_user_phone_length),Color.RED);
                       return;
                   }else if (Long.parseLong(text) == 0)return;

                   mNewPhoneValue = text;
                   textPhoneValue.setText(text);
                   session.setUserInfoChanged(true);
               }
           }else if (editTextType.equals(getString(R.string.dialog_title_input_empid_hint))) {
               if (!String.valueOf(session.getUserEmployeeId()).equals(text)) {
                   if (text.length() > getResources().getInteger(R.integer.max_emp_id)){
                       UIHelper.showCustomSnackBar(fabPicture,
                               getString(R.string.err_user_empid_length),Color.RED);
                       return;
                   }else if (Integer.valueOf(text) == 0)return;

                   mNewEmployeeIdValue = text;
                   textEmployeeIdValue.setText(text);
                   session.setUserInfoChanged(true);
               }
           }else if (editTextType.equals(getString(R.string.dialog_title_input_branchid_hint))) {
               if (!String.valueOf(session.getUserBranchId()).equals(text)) {
                   if (text.length() > getResources().getInteger(R.integer.max_branch_id)){
                       UIHelper.showCustomSnackBar(fabPicture,
                               getString(R.string.err_user_branchid_length),Color.RED);
                       return;
                   }else if (Integer.valueOf(text) == 0)return;

                   mNewBranchIdValue = text;
                   textBranchIdValue.setText(text);
                   session.setUserInfoChanged(true);
               }
           }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("file_uri",photoURI);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        photoURI = savedInstanceState.getParcelable("file_uri");
    }

    private void saveUserIntoLocalDatabase(User user) {
        MyDBHandler mHandler = new MyDBHandler(getBaseContext());
        mHandler.deleteUserData();
        mHandler.saveUserData(user);
    }

    private boolean checkRequiredFields(User user) {
        return user != null
                && user.getEmail()!=null
                && !TextUtils.isEmpty(user.getEmail())
                && user.getUuid()!=null
                && !TextUtils.isEmpty(user.getUuid())
                && user.getName()!=null
                && !TextUtils.isEmpty(user.getName())
                && user.getPhoneNumber()!=null
                && !TextUtils.isEmpty(user.getPhoneNumber())
                && user.getBranchName()!=null
                && !TextUtils.isEmpty(user.getBranchName())
                && user.getStatus()!=0
                && user.getBranchId()!=0
                && user.getEmployeeId()!=0;

    }
}
