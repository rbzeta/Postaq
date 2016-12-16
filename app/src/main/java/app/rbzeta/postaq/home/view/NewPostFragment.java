package app.rbzeta.postaq.home.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.application.AppConfig;
import app.rbzeta.postaq.application.MyApplication;
import app.rbzeta.postaq.custom.CircleTransform;
import app.rbzeta.postaq.database.MyDBHandler;
import app.rbzeta.postaq.helper.FileUtils;
import app.rbzeta.postaq.helper.PermissionHelper;
import app.rbzeta.postaq.helper.SessionManager;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.model.Question;
import app.rbzeta.postaq.rest.NetworkService;
import app.rbzeta.postaq.rest.message.QuestionResponseMessage;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
/**
 * Created by Robyn on 11/19/2016.
 */

public class NewPostFragment extends DialogFragment {
    private static final String TAG = NewPostFragment.class.getSimpleName();
    private static final int MAX_IMAGE_WIDTH = 500;
    private static final int MAX_IMAGE_HEIGHT = 500;
    private static int mScaleFactor = 7;

    private ImageView imgUserAvatar,imgPost;
    private TextView textUserName;
    private EditText editPostText;
    private Button buttonSubmitPost;
    private ImageButton buttonAddImage,buttonDeleteImage;
    private View imgPostContainer;
    private NestedScrollView scrollView;
    private boolean isTextPostIsFilled = false;

    private PermissionHelper permissionHelper;
    private SessionManager session;
    private Subscription subscription;
    private NetworkService networkService;
    private MyDBHandler dbHandler;

    private static final int REQUEST_IMAGE_FROM_CAMERA = 10;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 11;
    public static final int REQUEST_USE_CAMERA = 1;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    private Uri photoURI;
    private File imageFromCamera = null;
    private File fileToUpload = null;
    private String mCurrentPhotoPath;

    public static NewPostFragment newInstance(Bundle bundle){
        NewPostFragment fragment = new NewPostFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getContext(),getTheme()){
            @Override
            public void onBackPressed() {
                if(isTextPostIsFilled){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(getString(R.string.dialog_msg_discard_post_confirmation));
                    builder.setNegativeButton(getString(R.string.no), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.setPositiveButton(getString(R.string.action_yes), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (fileToUpload != null)
                                fileToUpload.delete();
                            getDialog().dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else getDialog().dismiss();
            }
        };
    }

    @Override
    public void onDestroy() {
        if (fileToUpload != null)
            fileToUpload.delete();
        rxUnSubscribe();
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyDialogFullScreen);

        permissionHelper = new PermissionHelper((AppCompatActivity)getActivity());
        session = MyApplication.getInstance().getSessionManager();
        networkService = MyApplication.getInstance().getNetworkService();
        dbHandler = MyApplication.getInstance().getDBHandler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_question,container,false);
        imgUserAvatar = (ImageView)view.findViewById(R.id.img_new_post_avatar);
        imgPost = (ImageView)view.findViewById(R.id.img_new_post_question);
        textUserName = (TextView)view.findViewById(R.id.text_new_post_user_name);
        editPostText = (EditText)view.findViewById(R.id.edit_new_post_text);
        buttonSubmitPost = (Button)view.findViewById(R.id.button_new_post_ask_question);
        buttonAddImage = (ImageButton)view.findViewById(R.id.button_new_post_image);
        buttonDeleteImage = (ImageButton)view.findViewById(R.id.button_new_post_delete_image);
        imgPostContainer = view.findViewById(R.id.container_img_new_post_question);
        scrollView = (NestedScrollView) view.findViewById(R.id.scroll_new_post_text);

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
                submitQuestion();
            }
        });

        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogImagePicker(view);
            }
        });
        return view;
    }

    private void submitQuestion() {
        final ProgressDialog progress= ProgressDialog.show(getContext(),
                getString(R.string.txt_title_upload_question),
                getString(R.string.txt_dialog_please_wait),true,false);

        Question newPost = new Question();
        newPost.setUuid(session.getUserUuid());
        newPost.setQuestion(editPostText.getText().toString().trim());

        MultipartBody.Part image = null;

        if(fileToUpload != null){

            //fileToUpload = FileUtils.transformImageForUpload(fileToUpload,imgPost.getWidth(),imgPost.getHeight());

            newPost.setPostType(AppConfig.TYPE_IMAGE_POST);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),fileToUpload);

            image = MultipartBody.Part
                    .createFormData("picture",
                            fileToUpload.getName(),
                            requestFile);
        }

        RequestBody uuid = RequestBody
                .create(MediaType.parse("multipart/form-data"),
                        session.getUserUuid());

        Observable<QuestionResponseMessage> imageObservable = (Observable<QuestionResponseMessage>)
                networkService.getPreparedObservable(
                        networkService.getNetworkAPI().uploadPostQuestion(image,uuid,newPost),
                        QuestionResponseMessage.class,
                        false,
                        false
                );

        subscription = imageObservable.subscribe(new Observer<QuestionResponseMessage>() {
            @Override
            public void onCompleted() {
                progress.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progress.dismiss();
                rxUnSubscribe();
                UIHelper.showErrorDialog(getFragmentManager(),getResources(),
                        getString(R.string.dialog_title_error_upload_question),
                        getString(R.string.dialog_msg_error_upload_question));
            }

            @Override
            public void onNext(QuestionResponseMessage msg) {
                if (msg.isSuccess()){

                    Question question = msg.getQuestion();

                    if (question != null) {
                        dbHandler.saveQuestion(question);
                    }
                    if(mCurrentPhotoPath != null)
                        galleryAddPic();

                    OnSuccessPostQuestionListener listener = (OnSuccessPostQuestionListener)getActivity();
                    listener.onSuccessPostQuestion(question);

                    dismiss();

                }else{
                    /*if(fileToUpload != null)
                        fileToUpload.delete();*/
                    UIHelper.showErrorDialog(getFragmentManager(),getResources(),
                            msg.getTitle(),msg.getMessage());

                }
            }
        });

    }

    private void rxUnSubscribe() {
        if(subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    private void showDialogImagePicker(final View view) {
        if(!permissionHelper.mayRequestCamera(view))
            return;
        if(!permissionHelper.mayRequestWriteExternalStorage(view))
            return;

        final CharSequence[] items = {getString(R.string.text_camera),getString(R.string.text_gallery),
                getString(R.string.action_cancel)};
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url = session.getUserProfilePictureUrl();
        if (url == null) {
            Glide.with(this).load(R.drawable.img_user_profile_default)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imgUserAvatar);
        }else
            Glide.with(this).load(url)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .error(R.drawable.img_user_profile_default)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imgUserAvatar);

        textUserName.setText(session.getUserName());

    }

    private void takePictureFromCamera(View view) {
        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {

                try{
                    imageFromCamera = FileUtils.createImageFile();
                    mCurrentPhotoPath = imageFromCamera.getAbsolutePath();
                }catch (IOException ex){
                    Log.e("ERROR : ",ex.getLocalizedMessage());
                }

                if (imageFromCamera != null) {

                    photoURI = FileProvider.getUriForFile(getContext(),
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        View lastChild = scrollView.getChildAt(scrollView.getChildCount() - 1);
        int bottom = lastChild.getBottom() + scrollView.getPaddingBottom();
        int sy = scrollView.getScrollY();
        int sh = scrollView.getHeight();
        int delta = bottom - (sy + sh);

        scrollView.smoothScrollBy(0, delta);

        Glide.with(this).load(fileToUpload.getAbsolutePath())
                .crossFade()
                .thumbnail(0.2f)
                .fitCenter()
                .error(R.drawable.ic_error_outline_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgPost);
    }

    private File prepareImageGalleryForUpload(Intent data) {

        File file = null;
        OutputStream os;
        if (data != null){

            try {

                Uri selectedImage = data.getData();
                mCurrentPhotoPath = getRealPathFromURI(getContext(),selectedImage);

                // Get the dimensions of the View
                int targetW;
                int targetH;

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;

                if (photoH > 1000 && photoW > 1000){
                    int divider;
                    if(photoH > photoW){
                        divider = photoH/MAX_IMAGE_HEIGHT;
                        targetH = MAX_IMAGE_HEIGHT;
                        targetW = photoW/divider;
                    }else {
                        divider = photoW/MAX_IMAGE_WIDTH;
                        targetW = MAX_IMAGE_WIDTH;
                        targetH = photoH/divider;
                    }
                    // Determine how much to scale down the image
                    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
                    bmOptions.inSampleSize = scaleFactor;

                    //bmOptions.inSampleSize = mScaleFactor;
                }

                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

                bitmap = FileUtils.getCorrectedOrientationBitmap(bitmap,mCurrentPhotoPath);

                file = FileUtils.createImageFile();

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
            String[] projection = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  projection, null, null, null);
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
        int targetW;
        int targetH;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;

        if (photoH > 1000 && photoW > 1000){
            int divider;
            if(photoH > photoW){
                divider = photoH/MAX_IMAGE_HEIGHT;
                targetH = MAX_IMAGE_HEIGHT;
                targetW = photoW/divider;
            }else {
                divider = photoW/MAX_IMAGE_WIDTH;
                targetW = MAX_IMAGE_WIDTH;
                targetH = photoH/divider;
            }
            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            bmOptions.inSampleSize = scaleFactor;

            //bmOptions.inSampleSize = mScaleFactor;
        }
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        bitmap = FileUtils.getCorrectedOrientationBitmap(bitmap,mCurrentPhotoPath);

        imageFromCamera.delete();

        OutputStream os;

        File file = null;
        try {
            file = FileUtils.createImageFile();

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

        return file;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public interface OnSuccessPostQuestionListener{
        void onSuccessPostQuestion(Question question);
    }

}
