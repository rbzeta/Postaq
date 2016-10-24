package app.rbzeta.postaq.app;

/**
 * Created by Robyn on 03/10/2016.
 */

public class AppConfig {

    //URL Services related constants
    //public static final String BASE_URL = "http://1.132.218.71/";
    //public static final String BASE_URL = "http://192.168.43.81/";
    //public static final String BASE_URL = "http://1.132.218.3/";
    public static final String BASE_URL = "http://202.169.39.114/";
    //public static final String SIGN_UP_URL = "postaq/ws/sign_up_proses.php";
    public static final String SIGN_UP_URL = "postaq/registration/ws_register_user.php";
    //public static final String SEND_VERIFICATION_URL = "postaq/ws/send_verification_code.php";
    public static final String SEND_VERIFICATION_URL = "postaq/registration/ws_send_verification_code.php";
    //public static final String SIGN_IN_URL = "postaq/ws/sign_in_proses.php";
    public static final String SIGN_IN_URL = "postaq/login/ws_login_user.php";
    //public static final String SEND_EMAIL_PASSWORD_URL = "postaq/ws/send_email_forgot_password.php";
    public static final String SEND_EMAIL_PASSWORD_URL = "postaq/ws/ws_send_email_forgot_password.php";
    //public static final String SIGN_IN_AFTER_SIGN_UP_URL = "postaq/ws/sign_in_after_sign_up_proses.php";
    public static final String SIGN_IN_AFTER_SIGN_UP_URL = "postaq/registration/ws_sign_in_after_sign_up.php";
    public static final String UPLOAD_IMAGE_URL = "postaq/ws/ws_upload_image_file.php";
    public static final String UPDATE_PROFILE_URL = "postaq/ws/ws_update_user_profile.php";

    public static final int ACCOUNT_STATUS_REGISTERED = 0;
    public static final int ACCOUNT_STATUS_ACTIVATED = 1;
    public static final int ACCOUNT_STATUS_SIGN_IN = 2;
    public static final int ACCOUNT_STATUS_SIGN_OUT = 3;
    public static final int ACCOUNT_STATUS_EXIST_NOT_ACTIVATED = 97;
    public static final int ACCOUNT_STATUS_NOT_FOUND = 98;
    public static final int ACCOUNT_STATUS_INACTIVE = 99;

    //preference related constants
    public static final String PREF_NAME = "jakers_pref_name";
    public static final String PREF_KEY_IS_LOGGED_IN = "pref_key_user_is_logged_in";
    public static final String PREF_KEY_USER_STATUS = "pref_key_user_status";
    public static final String PREF_KEY_VERIFICATION_CODE ="pref_key_user_verification_code" ;
    public static final String PREF_KEY_EMAIL_ADDRESS = "pref_key_user_email_address";
    public static final String PREF_KEY_NAME = "pref_key_user_name";
    public static final String PREF_KEY_PHONE_NUMBER = "pref_key_user_phone_number";
    public static final String PREF_KEY_EMPLOYEE_ID = "pref_key_user_employee_id";
    public static final String PREF_KEY_BRANCH_ID = "pref_key_user_branch_id";
    public static final String PREF_KEY_BRANCH_NAME = "pref_key_user_branch_name";
    public static final String PREF_KEY_UUID = "pref_key_user_uuid";
    public static final String PREF_KEY_PROFILE_PICTURE_URL = "pref_key_user_profile_picture_url";
    public static final String PREF_KEY_USER_INFO_IS_CHANGED = "pref_key_user_profile_is_change";
    public static final String PREF_KEY_USER_INFO_IS_SYNCED = "pref_key_user_profile_is_synced";
    public static final String PREF_KEY_USER_PROFILE_PICTURE_IS_CHANGED = "pref_key_user_profile_picture_is_change";


    //File related constants
    public static final String DIR_IMG_FILE_NAME = "/Jakers";
    public static final String EMPTY_STRING = "";
    public static final String STR_FILE_PROVIDER = "app.rbzeta.postaq.fileprovider";
    public static  String PREFIX_IMG_FILE_NAME = "IMG_";


    public static long TIME_INTERVAL = 2000;
}
