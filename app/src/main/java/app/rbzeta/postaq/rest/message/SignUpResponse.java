package app.rbzeta.postaq.rest.message;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 01/10/2016.
 */

public class SignUpResponse {
    @SerializedName("response_message")
    private ResponseMessage responseMessage;

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }
}
