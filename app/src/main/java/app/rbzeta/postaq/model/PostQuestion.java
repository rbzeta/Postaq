package app.rbzeta.postaq.model;

/**
 * Created by Robyn on 11/17/2016.
 */

public class PostQuestion {
    private int postId;
    private String avatarUrl;
    private String userName;
    private String postTime;
    private String postText;
    private int postType;
    private String postImageUrl;
    private String totalAnswer;
    private boolean isAnswered;
    private String postAnswer;
    private String avatarAnswerUrl;
    private String userNameAnswer;

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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getTotalAnswer() {
        return totalAnswer;
    }

    public void setTotalAnswer(String totalAnswer) {
        this.totalAnswer = totalAnswer;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public String getPostAnswer() {
        return postAnswer;
    }

    public void setPostAnswer(String postAnswer) {
        this.postAnswer = postAnswer;
    }
}
