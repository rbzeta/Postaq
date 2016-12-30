package app.rbzeta.postaq.rest.message;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.rbzeta.postaq.model.Answer;

/**
 * Created by Robyn on 12/17/2016.
 */
public class AnswerResponseMessage {
    @SerializedName("code")
    private String code;

    @SerializedName("title")
    private String title;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("answer")
    private Answer answer;

    @SerializedName("answers")
    private List<Answer> answerList;

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

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

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
