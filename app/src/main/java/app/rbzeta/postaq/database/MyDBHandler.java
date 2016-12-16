package app.rbzeta.postaq.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import app.rbzeta.postaq.contract.JakersContract;
import app.rbzeta.postaq.model.Question;
import app.rbzeta.postaq.rest.message.User;

/**
 * Created by Robyn on 02/10/2016.
 */

public class MyDBHandler extends SQLiteOpenHelper{

    public static MyDBHandler sInstance;

    public static synchronized MyDBHandler getInstance(Context context){

        if (sInstance == null) {
            sInstance = new MyDBHandler(context);
        }
        return sInstance;
    }

    private MyDBHandler(Context context) {
        super(context, JakersContract.DATABASE_NAME, null, JakersContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JakersContract.User.CREATE_TABLE);
        db.execSQL(JakersContract.Question.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(JakersContract.User.DELETE_TABLE);
        db.execSQL(JakersContract.Question.DELETE_TABLE);
        onCreate(db);

    }

    public boolean saveQuestion(Question question){

        ContentValues values = new ContentValues();
        values.put(JakersContract.Question.COLUMN_ID,question.getId());
        values.put(JakersContract.Question.COLUMN_UUID,question.getUuid());
        values.put(JakersContract.Question.COLUMN_QUESTION,question.getQuestion());
        values.put(JakersContract.Question.COLUMN_PICTURE_URL,question.getPictureUrl());
        values.put(JakersContract.Question.COLUMN_IS_ANSWERED,question.isAnswered());
        values.put(JakersContract.Question.COLUMN_STATUS,question.getStatus());
        values.put(JakersContract.Question.COLUMN_CREATE_DT,question.getCreateDt());
        values.put(JakersContract.Question.COLUMN_UPDATE_DT,question.getUpdateDt());
        values.put(JakersContract.Question.COLUMN_CREATE_USR,question.getCreateUsr());
        values.put(JakersContract.Question.COLUMN_UPDATE_USR,question.getUpdateUsr());
        values.put(JakersContract.Question.COLUMN_POST_TIME,question.getPostTime());
        values.put(JakersContract.Question.COLUMN_TOTAL_ANSWER,question.getTotalAnswer());
        values.put(JakersContract.Question.COLUMN_USER_NAME,question.getUserName());
        values.put(JakersContract.Question.COLUMN_USER_AVATAR_URL,question.getAvatarUrl());

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.insert(JakersContract.Question.TABLE_NAME,null,values);
            return true;
        }catch (SQLiteException e){
            //Log.e("ERROR : " ,e.getLocalizedMessage());
            return false;
        }finally {
            db.close();
        }

    }

    public List<Question> getQuestionList(){
        List<Question> listContact = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                JakersContract.Question._ID,
                JakersContract.Question.COLUMN_ID,
                JakersContract.Question.COLUMN_UUID,
                JakersContract.Question.COLUMN_QUESTION,
                JakersContract.Question.COLUMN_PICTURE_URL,
                JakersContract.Question.COLUMN_IS_ANSWERED,
                JakersContract.Question.COLUMN_STATUS,
                JakersContract.Question.COLUMN_CREATE_DT,
                JakersContract.Question.COLUMN_UPDATE_DT,
                JakersContract.Question.COLUMN_CREATE_USR,
                JakersContract.Question.COLUMN_UPDATE_USR,
                JakersContract.Question.COLUMN_POST_TIME,
                JakersContract.Question.COLUMN_TOTAL_ANSWER,
                JakersContract.Question.COLUMN_USER_NAME,
                JakersContract.Question.COLUMN_USER_AVATAR_URL
        };

        String selection = JakersContract.Question._ID + " = ?";
        String[] selectionArgs = { null };

        String sortOrder = JakersContract.Question.COLUMN_ID + " DESC";

        Cursor c = db.query(
                JakersContract.Question.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        //c.moveToFirst();
        try{
            while (c.moveToNext()){

                Question question = new Question();
                question.setId(c.getInt(1));
                question.setUuid(c.getString(2));
                question.setQuestion(c.getString(3));
                question.setPictureUrl(c.getString(4));
                question.setAnswered(c.getInt(5));
                question.setStatus(c.getInt(6));
                question.setCreateDt(c.getString(7));
                question.setUpdateDt(c.getString(8));
                question.setCreateUsr(c.getString(9));
                question.setUpdateUsr(c.getString(10));
                question.setPostTime(c.getString(11));
                question.setTotalAnswer(c.getString(12));
                question.setUserName(c.getString(13));
                question.setAvatarUrl(c.getString(14));
                listContact.add(question);
            }


        }catch (SQLiteException e){

        }
        finally {
            c.close();
            db.close();
        }
        return listContact;
    }

    public void deleteQuestions(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(JakersContract.Question.TABLE_NAME, null, null);
        db.close();
    }

    public boolean saveUserData(User user){

        ContentValues values = new ContentValues();
        values.put(JakersContract.User.COLUMN_EMAIL,user.getEmail());
        values.put(JakersContract.User.COLUMN_NAME,user.getName());
        values.put(JakersContract.User.COLUMN_PHONE,user.getPhoneNumber());
        values.put(JakersContract.User.COLUMN_EMP_ID,user.getEmployeeId());
        values.put(JakersContract.User.COLUMN_BRANCH_ID,user.getBranchId());
        values.put(JakersContract.User.COLUMN_BRANCH_NAME,user.getBranchName());
        values.put(JakersContract.User.COLUMN_UUID,user.getUuid());
        values.put(JakersContract.User.COLUMN_STATUS,user.getStatus());
        values.put(JakersContract.User.COLUMN_PROFILE_PICTURE_URL,user.getProfilePictureUrl());

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.insert(JakersContract.User.TABLE_NAME,null,values);
            return true;
        }catch (SQLiteException e){
            return false;
        }finally {
            db.close();
        }

    }

    public User getUserData(){
        User user = new User();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                JakersContract.User.COLUMN_EMAIL,
                JakersContract.User.COLUMN_NAME,
                JakersContract.User.COLUMN_PHONE,
                JakersContract.User.COLUMN_EMP_ID,
                JakersContract.User.COLUMN_BRANCH_ID,
                JakersContract.User.COLUMN_BRANCH_NAME,
                JakersContract.User.COLUMN_UUID,
                JakersContract.User.COLUMN_STATUS,
                JakersContract.User.COLUMN_PROFILE_PICTURE_URL

        };

        String selection = JakersContract.User.COLUMN_EMAIL + " LIKE ?";
        String[] selectionArgs = { "" };

        String sortOrder = JakersContract.User.COLUMN_EMAIL + " DESC";

        Cursor c = db.query(
                JakersContract.User.TABLE_NAME,                           // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        try{
            while (c.moveToNext()){
                c.moveToFirst();

                user.setEmail(c.getString(0));
                user.setName(c.getString(1));
                user.setPhoneNumber(c.getString(2));
                user.setEmployeeId(c.getInt(3));
                user.setBranchId(c.getInt(4));
                user.setBranchName(c.getString(5));
                user.setUuid(c.getString(6));
                user.setStatus(c.getInt(7));
            }


        }catch (SQLiteException e){

        }
        finally {
            c.close();
            db.close();
        }
        return user;
    }

    public void deleteUserData(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(JakersContract.User.TABLE_NAME, null, null);
        db.close();
    }
}
