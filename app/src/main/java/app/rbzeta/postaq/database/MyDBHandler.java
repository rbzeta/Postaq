package app.rbzeta.postaq.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import app.rbzeta.postaq.contract.JakersContract;
import app.rbzeta.postaq.rest.model.User;

/**
 * Created by Robyn on 02/10/2016.
 */

public class MyDBHandler extends SQLiteOpenHelper{

    public MyDBHandler(Context context) {
        super(context, JakersContract.DATABASE_NAME, null, JakersContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JakersContract.User.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(JakersContract.User.DELETE_TABLE);
        onCreate(db);

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
