package app.rbzeta.postaq.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 16/10/2016.
 */
public class FileProperties {

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("file_path")
    private String filePath;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
