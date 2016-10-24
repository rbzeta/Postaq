package app.rbzeta.postaq.rest.model;

import com.google.gson.annotations.SerializedName;

import app.rbzeta.postaq.contract.JakersContract;

/**
 * Created by Robyn on 01/10/2016.
 */

public class UserForm {
    @SerializedName(JakersContract.User.COLUMN_EMAIL)
    private String email;
    @SerializedName(JakersContract.User.COLUMN_PASSWORD)
    private String password;
    @SerializedName(JakersContract.User.COLUMN_NAME)
    private String name;
    @SerializedName(JakersContract.User.COLUMN_PHONE)
    private String phoneNumber;
    @SerializedName(JakersContract.User.COLUMN_EMP_ID)
    private int employeeId;
    @SerializedName(JakersContract.User.COLUMN_BRANCH_ID)
    private int branchId;
    @SerializedName(JakersContract.User.COLUMN_UUID)
    private String uuid;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
