package app.rbzeta.postaq.contract;

import android.provider.BaseColumns;

/**
 * Created by Robyn on 01/10/2016.
 */

public class JakersContract {
    public static final  int    DATABASE_VERSION   = 6;
    public static final  String DATABASE_NAME      = "postaq.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    private JakersContract(){

    }

    public static class User implements BaseColumns {


        private User(){}

        public static final String TABLE_NAME = "User";
        public static final String COLUMN_ID = "id";
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
                        COLUMN_ID + " INTEGER," +
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

    public static class Question implements BaseColumns {

        private Question(){}

        public static final String TABLE_NAME = "Question";
        public static final String COLUMN_UUID = "uuid";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_PICTURE_URL = "picture_url";
        public static final String COLUMN_IS_ANSWERED = "is_answered";
        public static final String COLUMN_CREATE_DT = "create_dt";
        public static final String COLUMN_UPDATE_DT = "update_dt";
        public static final String COLUMN_CREATE_USR = "create_usr";
        public static final String COLUMN_UPDATE_USR = "update_usr";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_POST_TIME = "post_time";
        public static final String COLUMN_TOTAL_ANSWER = "total_answer";
        public static final String COLUMN_USER_NAME = "name";
        public static final String COLUMN_USER_AVATAR_URL = "profile_picture_url";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_ID + " INTEGER," +
                        COLUMN_UUID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_QUESTION + TEXT_TYPE + COMMA_SEP +
                        COLUMN_PICTURE_URL + TEXT_TYPE + COMMA_SEP +
                        COLUMN_IS_ANSWERED + " INTEGER " + COMMA_SEP +
                        COLUMN_CREATE_DT + TEXT_TYPE + COMMA_SEP +
                        COLUMN_UPDATE_DT + TEXT_TYPE + COMMA_SEP +
                        COLUMN_CREATE_USR + TEXT_TYPE + COMMA_SEP +
                        COLUMN_UPDATE_USR + TEXT_TYPE + COMMA_SEP +
                        COLUMN_POST_TIME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_TOTAL_ANSWER + TEXT_TYPE + COMMA_SEP +
                        COLUMN_USER_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_USER_AVATAR_URL + TEXT_TYPE + COMMA_SEP +
                        COLUMN_STATUS + " INTEGER " + " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
