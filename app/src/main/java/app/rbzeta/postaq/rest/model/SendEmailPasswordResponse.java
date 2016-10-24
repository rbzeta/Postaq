package app.rbzeta.postaq.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 05/10/2016.
 */
public class SendEmailPasswordResponse {

    @SerializedName("response_message")
    private ResponseMessage responseMessage;

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

}
