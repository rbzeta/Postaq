package app.rbzeta.postaq.rest.model;

import com.google.gson.annotations.SerializedName;

import app.rbzeta.postaq.contract.JakersContract;

/**
 * Created by Robyn on 02/10/2016.
 */

public class User {
    @SerializedName(JakersContract.User.COLUMN_EMAIL)
    private String email;

    @SerializedName(JakersContract.User.COLUMN_NAME)
    private String name;

    @SerializedName(JakersContract.User.COLUMN_PHONE)
    private String phoneNumber;

    @SerializedName(JakersContract.User.COLUMN_EMP_ID)
    private int employeeId;

    @SerializedName(JakersContract.User.COLUMN_BRANCH_ID)
    private int branchId;

    @SerializedName(JakersContract.User.COLUMN_BRANCH_NAME)
    private String branchName;

    @SerializedName(JakersContract.User.COLUMN_UUID)
    private String uuid;

    @SerializedName(JakersContract.User.COLUMN_STATUS)
    private int status;

    @SerializedName(JakersContract.User.COLUMN_VERIFICATION_CODE)
    private String verificationCode;

    @SerializedName(JakersContract.User.COLUMN_PROFILE_PICTURE_URL)
    private String profilePictureUrl;

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
