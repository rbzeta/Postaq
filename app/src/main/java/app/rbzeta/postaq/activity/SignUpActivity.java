package app.rbzeta.postaq.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.bc.ConnectivityReceiver;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.helper.ValidationHelper;
import app.rbzeta.postaq.rest.ApiClient;
import app.rbzeta.postaq.rest.ApiInterface;
import app.rbzeta.postaq.rest.model.ResponseMessage;
import app.rbzeta.postaq.rest.model.SignUpResponse;
import app.rbzeta.postaq.rest.model.User;
import app.rbzeta.postaq.rest.model.UserForm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    public static final String KEY_MSG_EMAIL_ADDRESS = "package app.rbzeta.jak3os.activity.KEY_MSG_EMAIL_ADDRESS";
    public static final String KEY_MSG_VERIFICATION_CODE = "package app.rbzeta.jak3os.activity.KEY_MSG_VERIFICATION_CODE";
    public static final String KEY_MSG_UUID = "package app.rbzeta.jak3os.activity.KEY_MSG_UUID";
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mNameView;
    private EditText mPhoneNumberView;
    private EditText mEmpIdView;
    private EditText mBranchIdView;
    private Button mSignUpView;
    private TextInputLayout mTilPasswordView;
    private View mProgressView;
    private View mSignUpFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /*Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        anim.reset();
        LinearLayout iv = (LinearLayout) findViewById(R.id.frameSignUp);
        iv.clearAnimation();
        iv.startAnimation(anim);*/

        mSignUpFormView = findViewById(R.id.activity_sign_up);
        mProgressView = findViewById(R.id.progressBarSignUp);

        mEmailView = (EditText)findViewById(R.id.textEmailSignUp);
        mPasswordView = (EditText)findViewById(R.id.textPasswordSignUp);
        mNameView = (EditText)findViewById(R.id.textNameSignUp);
        mPhoneNumberView = (EditText)findViewById(R.id.textPhoneNumSignUp);
        mEmpIdView = (EditText)findViewById(R.id.textEmpIdSignUp);
        mBranchIdView = (EditText)findViewById(R.id.textBranchIdSignUp);

        mTilPasswordView = (TextInputLayout)findViewById(R.id.tilPasswordSignUp);

        mEmailView.addTextChangedListener(new MyTextWatcher(mEmailView));
        //mPasswordView.addTextChangedListener(new MyTextWatcher((mPasswordView)));

        mEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                validateEmailAddress();
            }
        });

        mSignUpView = (Button)findViewById(R.id.verivy_sign_up_button);

        mSignUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });




    }

    private void submitForm() {
        if (!validateRequiredField(mEmailView)) return;
        if (!TextUtils.isEmpty(mEmailView.getText().toString().trim()) &&
                !ValidationHelper.isValidEmail(mEmailView.getText().toString().trim())) {
            mEmailView.requestFocus();
            mEmailView.setError(getString(R.string.err_msg_not_valid_email));
            mEmailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_transparent_bottom_radius, 0);

            return;
        }
        if (!validatePassword())return;
        if (!validateRequiredField(mNameView)) return;
        if (!validateRequiredField(mPhoneNumberView)) return;
        if (!validatePhoneNumberLength(mPhoneNumberView)) return;
        if (!validateRequiredField(mEmpIdView)) return;
        if (!validateRequiredField(mBranchIdView)) return;
        if (!validateNoZeroField(mEmpIdView)) return;
        if (!validateNoZeroField(mBranchIdView)) return;


        if(!ConnectivityReceiver.isConnected()){
            UIHelper.showCustomSnackBar(this.findViewById(R.id.activity_sign_up),
                    getString(R.string.err_no_network_connection), Color.WHITE);

        }else{
            signingUp();
        }
    }

    private boolean validatePhoneNumberLength(EditText editText) {
        editText.setError(null);

        if (editText.getText().toString().length() < 6) {
            editText.requestFocus();
            editText.setError(getString(R.string.err_user_phone_length));
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_transparent_bottom_radius, 0);
            return false;
        }

        return true;
    }

    private boolean validateNoZeroField(EditText editText) {
        editText.setError(null);

        if (Integer.valueOf(editText.getText().toString().trim()) == 0) {
            editText.requestFocus();
            editText.setError(getString(R.string.error_field_cannot_zero));
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_transparent_bottom_radius, 0);
            return false;
        }

        return true;
    }

    private void signingUp() {
        final ProgressDialog progress= ProgressDialog.show(this,
                getString(R.string.txt_title_signing_up),
                getString(R.string.txt_dialog_please_wait),true,false);

        final UserForm form = new UserForm();
        form.setEmail(mEmailView.getText().toString().trim());
        form.setPassword(mPasswordView.getText().toString().trim());
        form.setName(mNameView.getText().toString().trim());
        form.setPhoneNumber(mPhoneNumberView.getText().toString().trim());
        form.setEmployeeId(Integer.valueOf(mEmpIdView.getText().toString().trim()));
        form.setBranchId(Integer.valueOf(mBranchIdView.getText().toString().trim()));

        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<SignUpResponse> call = apiService.signUpProcess(form);
            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {

                    if (response.body() != null) {

                        ResponseMessage msg = response.body().getResponseMessage();

                        User user = msg.getUser();

                        if (msg.isSuccess()){
                            progress.dismiss();

                            if (user != null
                                    && user.getVerificationCode() != null
                                    && !TextUtils.isEmpty(user.getVerificationCode())
                                    && user.getEmail()!=null
                                    && !TextUtils.isEmpty(user.getEmail())
                                    && user.getUuid()!=null
                                    && !TextUtils.isEmpty(user.getUuid())) {

                                //go to input verification activity
                                Intent intent = new Intent(SignUpActivity.this,
                                        VerifyCodeActivity.class);
                                intent.putExtra(SignUpActivity.KEY_MSG_EMAIL_ADDRESS,user.getEmail());
                                intent.putExtra(SignUpActivity.KEY_MSG_VERIFICATION_CODE,
                                        user.getVerificationCode());
                                intent.putExtra(SignUpActivity.KEY_MSG_UUID,user.getUuid());

                                startActivity(intent);

                            }else {

                                UIHelper.showGeneralErrorDialog(getSupportFragmentManager(),getResources());

                            }
                        }else{
                            progress.dismiss();

                            UIHelper.showErrorDialog(getSupportFragmentManager(),getResources(),
                                    msg.getTitle(),msg.getMessage());

                        }

                    }else {
                        progress.dismiss();

                        UIHelper.showGeneralErrorDialog(getSupportFragmentManager(),getResources());
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    progress.dismiss();
                    UIHelper.showGeneralErrorDialog(getSupportFragmentManager(),getResources());

                    /*DialogFragment dialog = new GeneralDialogFragment()
                            .newInstance(getString(R.string.err_msg_title_general),
                                    t.getLocalizedMessage());
                    dialog.show(getSupportFragmentManager(),
                            getString(R.string.err_dialog_fragment_tag));*/
                }
            });

        }finally {

        }
    }


    @Override
    public void onBackPressed(){
/*
        overridePendingTransition(R.anim.enter_center, R.anim.exit_bottom);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.exit_bottom);
        anim.reset();
        FrameLayout iv = (FrameLayout) findViewById(R.id.frameSignUp);
        iv.clearAnimation();
        iv.startAnimation(anim);*/

        finish();
    }

    private boolean validatePassword(){
        if (!TextUtils.isEmpty(mPasswordView.getText().toString().trim()) &&
                !isPasswordValid(mPasswordView.getText().toString().trim())) {
            mPasswordView.requestFocus();
            mPasswordView.setError(getString(R.string.err_msg_password_six_char));
            //mPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            return false;
        } else if (TextUtils.isEmpty(mPasswordView.getText().toString().trim())) {
            mPasswordView.requestFocus();
            mPasswordView.setError(getString(R.string.error_field_required));
           // mPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            return false;
        }else mPasswordView.setError(null);

        return true;
    }
    private boolean isPasswordValid(String password) {
        return password.length() >6;
    }

    private boolean validateEmailAddress() {
        if (!TextUtils.isEmpty(mEmailView.getText().toString().trim()) &&
                !ValidationHelper.isValidEmail(mEmailView.getText().toString().trim())) {

            mEmailView.setError(getString(R.string.err_msg_not_valid_email));
            mEmailView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_transparent_bottom_radius, 0);

            return false;
        } else {
            mEmailView.setError(null);
        }

        return true;
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
                case R.id.textEmailSignUp:
                    mEmailView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    mEmailView.setError(null);
                    break;
               /* case R.id.textPasswordSignUp:
                    //mPasswordView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    //mPasswordView.setError(null);
                    break;*/
            }
        }
    }
}
