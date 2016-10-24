package app.rbzeta.postaq.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 19/10/2016.
 */

public class UserProfileResponseMessage {
    @SerializedName("response_message")
    private ResponseMessage responseMessage;

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }
}
