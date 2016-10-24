package app.rbzeta.postaq.contract;

import android.provider.BaseColumns;

/**
 * Created by Robyn on 01/10/2016.
 */

public class JakersContract {
    public static final  int    DATABASE_VERSION   = 2;
    public static final  String DATABASE_NAME      = "postaq.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    private JakersContract(){

    }

    public static class User implements BaseColumns {


        private User(){}

        public static final String TABLE_NAME = "User";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone_number";
        public static final String COLUMN_EMP_ID = "employee_id";
        public static final String COLUMN_BRANCH_ID = "branch_id";
        public static final String COLUMN_BRANCH_NAME = "branch_name";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_UUID = "uuid";
        public static final String COLUMN_VERIFICATION_CODE = "verification_code";
        public static final String COLUMN_PROFILE_PICTURE_URL = "profile_picture_url";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_PHONE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_EMP_ID + " INTEGER " + COMMA_SEP +
                        COLUMN_BRANCH_ID + " INTEGER " + COMMA_SEP +
                        COLUMN_UUID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_BRANCH_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_STATUS + " INTEGER " + COMMA_SEP +
                        COLUMN_PROFILE_PICTURE_URL + TEXT_TYPE + " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
