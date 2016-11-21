package app.rbzeta.postaq.rest;

import app.rbzeta.postaq.app.AppConfig;
import app.rbzeta.postaq.rest.model.FileUploadResponseMessage;
import app.rbzeta.postaq.rest.model.SendEmailPasswordResponse;
import app.rbzeta.postaq.rest.model.SendVerificationResponse;
import app.rbzeta.postaq.rest.model.SignInResponse;
import app.rbzeta.postaq.rest.model.SignUpResponse;
import app.rbzeta.postaq.rest.model.User;
import app.rbzeta.postaq.rest.model.UserForm;
import app.rbzeta.postaq.rest.model.UserProfileResponseMessage;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Robyn on 01/10/2016.
 */

public interface ApiInterface {

    @POST(AppConfig.SIGN_UP_URL)
    Call<SignUpResponse> signUpProcess(@Body UserForm form);

    @POST(AppConfig.SIGN_IN_URL)
    Call<SignInResponse> signInProcess(@Body UserForm form);

    @POST(AppConfig.SIGN_IN_AFTER_SIGN_UP_URL)
    Call<SignInResponse> signInAfterSignUpProcess(@Body UserForm form);


    @POST(AppConfig.SEND_VERIFICATION_URL)
    Call<SendVerificationResponse> sendVerificationCode(@Body UserForm form);

    @POST(AppConfig.SEND_EMAIL_PASSWORD_URL)
    Call<SendEmailPasswordResponse> sendEmailForgotPasswordCode(@Body UserForm form);

    @Multipart
    @POST(AppConfig.UPLOAD_IMAGE_URL)
    Call<FileUploadResponseMessage> uploadImageToServer(@Part MultipartBody.Part image, @Part("uuid") RequestBody uuid);

    @POST(AppConfig.UPDATE_PROFILE_URL)
    Call<UserProfileResponseMessage> updateUserProfile(@Body User user);
}
