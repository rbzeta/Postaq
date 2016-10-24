package app.rbzeta.postaq.helper;

import android.content.Context;
import android.content.SharedPreferences;

import app.rbzeta.postaq.app.AppConfig;
import app.rbzeta.postaq.rest.model.User;

/**
 * Created by Robyn on 03/10/2016.
 */

public class SessionManager {

    private String TAG = SessionManager.class.getSimpleName();

    SharedPreferences preferences;

    SharedPreferences.Editor editor;

    Context mContext;

    public SessionManager(Context context, int mode){
        mContext = context;
        preferences = mContext.getSharedPreferences(AppConfig.PREF_NAME,mode);
        editor = preferences.edit();
    }

    public void setUserLogin(boolean isLoggedIn){

        editor.putBoolean(AppConfig.PREF_KEY_IS_LOGGED_IN,isLoggedIn);

        editor.commit();
    }

    public boolean isUserLogin(){
        return preferences.getBoolean(AppConfig.PREF_KEY_IS_LOGGED_IN,false);
    }

    public void setUserStatus(int status){
        editor.putInt(AppConfig.PREF_KEY_USER_STATUS,status);
        editor.commit();
    }

    public int getUserStatus(){
        return preferences.getInt(AppConfig.PREF_KEY_USER_STATUS,0);
    }

    public void setVerificationCode(String code){
        editor.putString(AppConfig.PREF_KEY_VERIFICATION_CODE,code);
        editor.commit();
    }

    public String getUserVerificationCode(){
        return preferences.getString(AppConfig.PREF_KEY_VERIFICATION_CODE,null);
    }

    public void setUserEmailAddress(String email){
        editor.putString(AppConfig.PREF_KEY_EMAIL_ADDRESS,email);
        editor.commit();
    }

    public String getUserEmailAddress(){
        return preferences.getString(AppConfig.PREF_KEY_EMAIL_ADDRESS,null);
    }

    public void deleteSharedPreference(){
        editor.clear();
        editor.commit();
    }

    public void setUserName(String name) {
        editor.putString(AppConfig.PREF_KEY_NAME,name);
        editor.commit();
    }

    public void setUserPhone(String phone) {
        editor.putString(AppConfig.PREF_KEY_PHONE_NUMBER,phone);
        editor.commit();
    }

    public String getUserPhone(){
        return preferences.getString(AppConfig.PREF_KEY_PHONE_NUMBER,null);
    }

    public void setUserEmployeeId(int employeeId) {
        editor.putInt(AppConfig.PREF_KEY_EMPLOYEE_ID,employeeId);
        editor.commit();
    }
    public int getUserEmployeeId(){
        return preferences.getInt(AppConfig.PREF_KEY_EMPLOYEE_ID,0);
    }

    public void setUserBranchId(int branchId) {
        editor.putInt(AppConfig.PREF_KEY_BRANCH_ID,branchId);
        editor.commit();
    }

    public int getUserBranchId(){
        return preferences.getInt(AppConfig.PREF_KEY_BRANCH_ID,0);
    }

    public void setUserBranchName(String branchName) {
        editor.putString(AppConfig.PREF_KEY_BRANCH_NAME,branchName);
        editor.commit();
    }

    public void setUserUuid(String uuid) {
        editor.putString(AppConfig.PREF_KEY_UUID,uuid);
        editor.commit();
    }

    public String getUserUuid(){
        return preferences.getString(AppConfig.PREF_KEY_UUID,AppConfig.EMPTY_STRING);
    }

    public void setUserSharedPreferences(User user) {
        this.setUserEmailAddress(user.getEmail());
        this.setUserName(user.getName());
        this.setUserPhone(user.getPhoneNumber());
        this.setUserEmployeeId(user.getEmployeeId());
        this.setUserBranchId(user.getBranchId());
        this.setUserBranchName(user.getBranchName());
        this.setUserUuid(user.getUuid());
        this.setUserStatus(user.getStatus());
        this.setUserProfilePictureUrl(user.getProfilePictureUrl());
    }

    public String getUserName() {
        return preferences.getString(AppConfig.PREF_KEY_NAME,null);
    }

    public String getBranchName() {
        return preferences.getString(AppConfig.PREF_KEY_BRANCH_NAME,null);
    }

    public void setUserProfilePictureUrl(String userProfilePictureUrl) {
        editor.putString(AppConfig.PREF_KEY_PROFILE_PICTURE_URL,userProfilePictureUrl);
        editor.commit();
    }

    public String getUserProfilePictureUrl(){
        return preferences.getString(AppConfig.PREF_KEY_PROFILE_PICTURE_URL,null);
    }

    public void setUserInfoChanged(boolean b) {

        editor.putBoolean(AppConfig.PREF_KEY_USER_INFO_IS_CHANGED,b);
        editor.commit();
    }

    public boolean isUserInfoChanged(){
        return preferences.getBoolean(AppConfig.PREF_KEY_USER_INFO_IS_CHANGED,false);
    }

    public void setUserInfoSynced(boolean b) {

        editor.putBoolean(AppConfig.PREF_KEY_USER_INFO_IS_SYNCED,b);
        editor.commit();
    }

    public boolean isUserInfoSynced(){
        return preferences.getBoolean(AppConfig.PREF_KEY_USER_INFO_IS_SYNCED,false);
    }

    public void setUserProfilePictureChanged(boolean b) {
        editor.putBoolean(AppConfig.PREF_KEY_USER_PROFILE_PICTURE_IS_CHANGED,b);
        editor.commit();
    }

    public boolean isUserProfilePictureChanged(){
        return preferences.getBoolean(AppConfig.PREF_KEY_USER_PROFILE_PICTURE_IS_CHANGED,false);
    }
}
