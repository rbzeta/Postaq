package app.rbzeta.postaq.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 12/17/2016.
 */
public class Answer {

    @SerializedName("profile_picture_url")
    private String userAvatarUrl;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("answer")
    private String answer;
    @SerializedName("answer_time")
    private String answerTime;
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("question_id")
    private int questionId;
    @SerializedName("is_answer")
    private int isAnswer;
    @SerializedName("create_dt")
    private String createDt;
    @SerializedName("update_dt")
    private String updateDt;
    @SerializedName("create_usr")
    private String createUsr;
    @SerializedName("update_usr")
    private String updateUsr;
    @SerializedName("status")
    private int status;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(int isAnswer) {
        this.isAnswer = isAnswer;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }

    public String getCreateUsr() {
        return createUsr;
    }

    public void setCreateUsr(String createUsr) {
        this.createUsr = createUsr;
    }

    public String getUpdateUsr() {
        return updateUsr;
    }

    public void setUpdateUsr(String updateUsr) {
        this.updateUsr = updateUsr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }
}
