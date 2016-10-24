package app.rbzeta.postaq.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 16/10/2016.
 */

public class FileUploadResponseMessage {
    @SerializedName("code")
    private String code;

    @SerializedName("title")
    private String title;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("file")
    private FileProperties fileProperties;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FileProperties getFileProperties() {
        return fileProperties;
    }

    public void setFileProperties(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }
}
