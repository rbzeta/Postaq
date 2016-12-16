package app.rbzeta.postaq.rest;

import android.support.annotation.Nullable;

import app.rbzeta.postaq.application.AppConfig;
import app.rbzeta.postaq.model.Question;
import app.rbzeta.postaq.rest.message.FileUploadResponseMessage;
import app.rbzeta.postaq.rest.message.QuestionResponseMessage;
import app.rbzeta.postaq.rest.message.SendEmailPasswordResponse;
import app.rbzeta.postaq.rest.message.SendVerificationResponse;
import app.rbzeta.postaq.rest.message.SignInResponse;
import app.rbzeta.postaq.rest.message.SignUpResponse;
import app.rbzeta.postaq.rest.message.User;
import app.rbzeta.postaq.rest.message.UserForm;
import app.rbzeta.postaq.rest.message.UserProfileResponseMessage;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by Robyn on 11/29/2016.
 */

public interface NetworkApi {
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

    @Multipart
    @POST(AppConfig.UPLOAD_IMAGE_POST_URL)
    Observable<FileUploadResponseMessage> uploadImagePostToServer(@Part MultipartBody.Part image, @Part("uuid") RequestBody uuid);

    @POST(AppConfig.UPDATE_PROFILE_URL)
    Call<UserProfileResponseMessage> updateUserProfile(@Body User user);

    @Multipart
    @POST(AppConfig.UPLOAD_POST_QUESTION_URL)
    Observable<QuestionResponseMessage> uploadPostQuestion(@Nullable @Part MultipartBody.Part image, @Part("uuid") RequestBody uuid, @Part("question") Question post);

}
