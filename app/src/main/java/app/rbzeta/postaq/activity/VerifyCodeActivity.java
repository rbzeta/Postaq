package app.rbzeta.postaq.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.bc.ConnectivityReceiver;
import app.rbzeta.postaq.database.MyDBHandler;
import app.rbzeta.postaq.helper.SessionManager;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.home.HomeActivity;
import app.rbzeta.postaq.rest.ApiClient;
import app.rbzeta.postaq.rest.ApiInterface;
import app.rbzeta.postaq.rest.model.ResponseMessage;
import app.rbzeta.postaq.rest.model.SendVerificationResponse;
import app.rbzeta.postaq.rest.model.SignInResponse;
import app.rbzeta.postaq.rest.model.User;
import app.rbzeta.postaq.rest.model.UserForm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyCodeActivity extends AppCompatActivity {

    private TextView mTextEmail;
    private Button mButtonEditEmail;
    private Button mButtonSendCode;
    private EditText mEditCode;
    private String mVerificationCode;
    private String mUuid;
    private String mEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        /*Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        anim.reset();
        LinearLayout iv = (LinearLayout) findViewById(R.id.frameVerifyAccount);
        iv.clearAnimation();
        iv.startAnimation(anim);*/

        mTextEmail = (TextView)findViewById(R.id.textEmailVerifyAccount) ;
        mButtonEditEmail = (Button)findViewById(R.id.buttonEditVerifyAccount);
        mButtonSendCode = (Button)findViewById(R.id.buttonSendVerifyAccount);
        mEditCode = (EditText) findViewById(R.id.editCodeVerifyAccount);
        mEditCode.addTextChangedListener(new MyTextWatcher(mEditCode));

        Bundle extras = getIntent().getExtras();
        mEmailAddress = extras.getString(SignUpActivity.KEY_MSG_EMAIL_ADDRESS);
        mVerificationCode = extras.getString(SignUpActivity.KEY_MSG_VERIFICATION_CODE);
        mUuid = extras.getString(SignUpActivity.KEY_MSG_UUID);
        mTextEmail.setText(mEmailAddress);



        mButtonEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mButtonSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               submitSendVerificationCode();

            }
        });

    }

    private void submitSendVerificationCode() {

        if(!ConnectivityReceiver.isConnected()){
            UIHelper.showCustomSnackBar(this.findViewById(R.id.activity_verify_account),
                    getString(R.string.err_no_network_connection), Color.WHITE);

        }else{
            sendVerificationCode();
        }
    }

    private void sendVerificationCode() {
        final ProgressDialog progress = new ProgressDialog(getBaseContext())
                .show(this,
                        getString(R.string.txt_title_sending_code),
                        getString(R.string.txt_dialog_please_wait),true,false);

        UserForm form = new UserForm();
        form.setEmail(mEmailAddress);
        form.setUuid(mUuid);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SendVerificationResponse> call = apiService
                .sendVerificationCode(form);
        call.enqueue(new Callback<SendVerificationResponse>() {
            @Override
            public void onResponse(Call<SendVerificationResponse> call,
                                   Response<SendVerificationResponse> response) {
                progress.dismiss();

                if (response.body() != null) {

                    ResponseMessage msg = response.body().getResponseMessage();

                    if (msg.isSuccess()){

                        if (!TextUtils.isEmpty(msg.getMessage())
                                && msg.getMessage() != null){

                            mVerificationCode = msg.getMessage();
                            mEditCode.setText(null);

                            //debugging
                            //mTextEmail.setText(mVerificationCode);
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
            public void onFailure(Call<SendVerificationResponse> call, Throwable t) {
                progress.dismiss();
                UIHelper.showGeneralErrorDialog(getSupportFragmentManager(),getResources());
            }
        });

    }

    private void registerUserAction() {
        final ProgressDialog progress= ProgressDialog.show(this,
                getString(R.string.txt_title_signing_in),
                getString(R.string.txt_dialog_please_wait),true,false);

        mEditCode.setText(null);

        UserForm form = new UserForm();
        form.setEmail(mEmailAddress);
        form.setUuid(mUuid);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<SignInResponse> call = apiService
                .signInAfterSignUpProcess(form);
        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                progress.dismiss();

                if (response.body() != null) {

                    ResponseMessage msg = response.body().getResponseMessage();

                    User user = msg.getUser();

                    if (msg.isSuccess()){

                        if (checkRequiredFields(user)) {

                            saveUserIntoLocalDatabase(user);
                            SessionManager session = new SessionManager(getBaseContext(),
                                    getBaseContext().MODE_PRIVATE);
                            session.deleteSharedPreference();
                            session.setUserLogin(true );
                            session.setUserInfoChanged(false);
                            session.setUserSharedPreferences(user);

                            //go to Home activity
                            Intent intent = new Intent(VerifyCodeActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }else {

                            UIHelper.showGeneralErrorDialog(getSupportFragmentManager(),getResources());

                        }
                    }else{

                        UIHelper.showErrorDialog(getSupportFragmentManager(),getResources(),
                                msg.getTitle(),msg.getMessage());

                    }

                }else {

                    UIHelper.showGeneralErrorDialog(getSupportFragmentManager(),getResources());
                }

            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                progress.dismiss();
                UIHelper.showGeneralErrorDialog(getSupportFragmentManager(),getResources());
                mEditCode.setSelectAllOnFocus(true);
                UIHelper.requestFocus(getWindow(),mEditCode);
            }
        });

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

    @Override
    public void onBackPressed(){
        finish();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view){
            this.view = view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.editCodeVerifyAccount:
                    if (s.toString().equals(mVerificationCode)){
                        registerUserAction();
                    }
                    break;
            }
        }
    }


}
