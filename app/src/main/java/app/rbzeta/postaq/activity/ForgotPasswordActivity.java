package app.rbzeta.postaq.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.bc.ConnectivityReceiver;
import app.rbzeta.postaq.dialog.ResetPasswordDialogFragment;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.helper.ValidationHelper;
import app.rbzeta.postaq.rest.ApiClient;
import app.rbzeta.postaq.rest.ApiInterface;
import app.rbzeta.postaq.rest.model.ResponseMessage;
import app.rbzeta.postaq.rest.model.SendEmailPasswordResponse;
import app.rbzeta.postaq.rest.model.UserForm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button mButtonCancel;
    private Button mButtonNext;
    private EditText mEditEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        /*Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        anim.reset();
        LinearLayout iv = (LinearLayout) findViewById(R.id.frameForgotPassword);
        iv.clearAnimation();
        iv.startAnimation(anim);*/

        mButtonCancel = (Button)findViewById(R.id.buttonCancelForgotPassword);
        mButtonNext = (Button)findViewById(R.id.buttonNextForgotPassword);
        mEditEmail = (EditText) findViewById(R.id.editEmailForgotPassword);

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSendPassword();


            }
        });


    }

    private void submitSendPassword() {
        if (!validateRequiredField(mEditEmail)) return;
        if (!TextUtils.isEmpty(mEditEmail.getText().toString().trim()) &&
                !ValidationHelper.isValidEmail(mEditEmail.getText().toString().trim())) {
            mEditEmail.requestFocus();
            mEditEmail.setError(getString(R.string.err_msg_not_valid_email));
            mEditEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_transparent_bottom_radius, 0);
            return;
        }

        if(!ConnectivityReceiver.isConnected()){
            UIHelper.showCustomSnackBar(this.findViewById(R.id.activity_forgot_password),
                    getString(R.string.err_no_network_connection), Color.WHITE);

        }else{
            sendEmailAction();
        }
    }

    private boolean validateRequiredField(EditText editText) {
        editText.setError(null);

        if (editText.getText().toString().trim().isEmpty()) {
            editText.requestFocus();
            editText.setError(getString(R.string.error_field_required));
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_transparent_bottom_radius, 0);
            return false;
        }

        return true;
    }

    private void sendEmailAction() {
        final ProgressDialog progress = new ProgressDialog(getBaseContext())
                .show(this,getString(R.string.txt_title_sending_email),
                        getString(R.string.txt_dialog_please_wait),true,false);

        UserForm user = new UserForm();
        user.setEmail(mEditEmail.getText().toString().trim());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SendEmailPasswordResponse> call = apiService
                .sendEmailForgotPasswordCode(user);

        call.enqueue(new Callback<SendEmailPasswordResponse>() {
            @Override
            public void onResponse(Call<SendEmailPasswordResponse> call,
                                   Response<SendEmailPasswordResponse> response) {
                progress.dismiss();

                if (response.body() != null) {

                    ResponseMessage msg = response.body().getResponseMessage();

                    if (msg.isSuccess()){

                        if (!TextUtils.isEmpty(msg.getMessage())
                                && msg.getMessage() != null){

                            StringBuilder b = new StringBuilder();
                            b.append(getString(R.string.text_send_forgot_password_email));
                            b.append(" ");
                            b.append(mEditEmail.getText().toString().trim());
                            b.append(" ");
                            b.append(getString(R.string.text_send_forgot_password_email2));


                            String textMsg = b.toString();

                            DialogFragment dialog = new ResetPasswordDialogFragment()
                                    .newInstance(getString(R.string.check_your_email),textMsg);
                            dialog.show(getSupportFragmentManager(),getString(R.string.err_dialog_fragment_tag));

                        }else
                            UIHelper.showGeneralErrorDialog(getSupportFragmentManager(),getResources());

                    }else{
                        UIHelper.showErrorDialog(getSupportFragmentManager(),getResources(),
                                msg.getTitle(),msg.getMessage());
                    }

                }else{
                    UIHelper.showGeneralErrorDialog(getSupportFragmentManager(),getResources());
                }

            }

            @Override
            public void onFailure(Call<SendEmailPasswordResponse> call, Throwable t) {
                progress.dismiss();
                UIHelper.showGeneralErrorDialog(getSupportFragmentManager(),getResources());

            }
        });


    }

    @Override
    public void onBackPressed(){
        finish();
    }
}