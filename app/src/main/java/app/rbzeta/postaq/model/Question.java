package app.rbzeta.postaq.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 11/17/2016.
 */

public class Question {
    @SerializedName("id")
    private int id;
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("question")
    private String question;
    @SerializedName("picture_url")
    private String pictureUrl;
    @SerializedName("is_answered")
    private int isAnswered;
    @SerializedName("status")
    private int status;
    @SerializedName("create_dt")
    private String createDt;
    @SerializedName("update_dt")
    private String updateDt;
    @SerializedName("create_usr")
    private String createUsr;
    @SerializedName("update_usr")
    private String updateUsr;
    @SerializedName("profile_picture_url")
    private String avatarUrl;
    @SerializedName("name")
    private String userName;
    @SerializedName("post_time")
    private String postTime;
    @SerializedName("total_answer")
    private String totalAnswer;


    private int postType;

    private String postAnswer;
    private String avatarAnswerUrl;
    private String userNameAnswer;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAvatarAnswerUrl() {
        return avatarAnswerUrl;
    }

    public void setAvatarAnswerUrl(String avatarAnswerUrl) {
        this.avatarAnswerUrl = avatarAnswerUrl;
    }

    public String getUserNameAnswer() {
        return userNameAnswer;
    }

    public void setUserNameAnswer(String userNameAnswer) {
        this.userNameAnswer = userNameAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getTotalAnswer() {
        return totalAnswer;
    }

    public void setTotalAnswer(String totalAnswer) {
        this.totalAnswer = totalAnswer;
    }

    public int isAnswered() {
        return isAnswered;
    }

    public void setAnswered(int answered) {
        isAnswered = answered;
    }

    public String getPostAnswer() {
        return postAnswer;
    }

    public void setPostAnswer(String postAnswer) {
        this.postAnswer = postAnswer;
    }
}
