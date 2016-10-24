package app.rbzeta.postaq.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.bc.ConnectivityReceiver;
import app.rbzeta.postaq.database.MyDBHandler;
import app.rbzeta.postaq.helper.PermissionHelper;
import app.rbzeta.postaq.helper.SessionManager;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.helper.ValidationHelper;
import app.rbzeta.postaq.rest.ApiClient;
import app.rbzeta.postaq.rest.ApiInterface;
import app.rbzeta.postaq.rest.model.ResponseMessage;
import app.rbzeta.postaq.rest.model.SignInResponse;
import app.rbzeta.postaq.rest.model.User;
import app.rbzeta.postaq.rest.model.UserForm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private UserLoginTask mAuthTask = null;
    public static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEditEmail;
    private EditText mEditPassword;
    private View mProgressView;
    private View mLoginFormView;
    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        permissionHelper= new PermissionHelper(this);

        TextView mTextLogo = (TextView)findViewById(R.id.textAppLogo);
        Typeface mFace = Typeface.createFromAsset(getAssets(),getString(R.string.font_path_actonia));
        mTextLogo.setTypeface(mFace);
        TextView tvLogoSub = (TextView)findViewById(R.id.textAppLogoDesc) ;


        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        anim.reset();
        mTextLogo.clearAnimation();
        mTextLogo.startAnimation(anim);
        tvLogoSub.clearAnimation();
        tvLogoSub.startAnimation(anim);

        mEditEmail = (AutoCompleteTextView)findViewById(R.id.editEmailLogin);
        mEditPassword = (EditText) findViewById(R.id.editPasswordLogin);
        Button mEmailSignInButton = (Button) findViewById(R.id.login_button);
        Button mSignUpButton = (Button) findViewById(R.id.signup_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        TextView mForgotPasswordView = (TextView)findViewById(R.id.linkForgotPassword);
        //populateAutoComplete();

        mEditPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.editPasswordLoginIme || id == EditorInfo.IME_NULL) {
                    submitLoginAction();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLoginAction();
            }
        });

        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        mForgotPasswordView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitLoginAction() {

        if (!validateRequiredField(mEditEmail)) return;
        if (!TextUtils.isEmpty(mEditEmail.getText().toString().trim()) &&
                !ValidationHelper.isValidEmail(mEditEmail.getText().toString().trim())) {
            mEditEmail.requestFocus();
            mEditEmail.setError(getString(R.string.err_msg_not_valid_email));
            mEditEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.button_transparent_bottom_radius, 0);

            return;
        }
        if (!validatePassword())return;

        if(!ConnectivityReceiver.isConnected()){
            UIHelper.showCustomSnackBar(this.findViewById(R.id.login_form),
                    getString(R.string.err_no_network_connection), Color.WHITE);

        }else{
            loginProcess();
        }


        /*if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEditEmail.setError(null);
        mEditPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mEditEmail.getText().toString();
        String password = mEditPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mEditPassword.setError(getString(R.string.error_invalid_password));
            focusView = mEditPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEditEmail.setError(getString(R.string.error_field_required));
            focusView = mEditEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEditEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEditEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }*/
    }

    private void loginProcess() {
        showProgress(true);

        UserForm form  = new UserForm();
        form.setEmail(mEditEmail.getText().toString().trim());
        form.setPassword(mEditPassword.getText().toString().trim());

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<SignInResponse> call = apiService
                .signInProcess(form);
        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                showProgress(false);

                if (response.body() != null) {

                    ResponseMessage msg = response.body().getResponseMessage();

                    User user = msg.getUser();

                    if (msg.isSuccess()){

                        if (checkRequiredFields(user)) {

                            saveUserIntoLocalDatabase(user);
                            SessionManager session = new SessionManager(getBaseContext(),
                                    getBaseContext().MODE_PRIVATE);
                            session.deleteSharedPreference();
                            session.setUserLogin(true);
                            session.setUserInfoChanged(false);
                            session.setUserSharedPreferences(user);

                            //go to Home activity
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

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
                showProgress(false);
                UIHelper.showGeneralErrorDialog(getSupportFragmentManager(),getResources());
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

    private boolean validatePassword(){
        if (!TextUtils.isEmpty(mEditPassword.getText().toString().trim()) &&
                !isPasswordValid(mEditPassword.getText().toString().trim())) {
            mEditPassword.requestFocus();
            mEditPassword.setError(getString(R.string.err_msg_password_six_char));
            //mPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            return false;
        } else if (TextUtils.isEmpty(mEditPassword.getText().toString().trim())) {
            mEditPassword.requestFocus();
            mEditPassword.setError(getString(R.string.error_field_required));
            // mPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            return false;
        }else mEditPassword.setError(null);

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

    private void populateAutoComplete() {
        if (!permissionHelper.mayRequestContacts(mEditEmail)) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }



    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEditEmail.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                return false;
            }

            /*for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }*/

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mEditPassword.setError(getString(R.string.error_incorrect_password));
                mEditPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

