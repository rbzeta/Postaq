package app.rbzeta.postaq.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 11/17/2016.
 */

public class Question implements Parcelable {
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
    @SerializedName("answer")
    private String answer;
    @SerializedName("answer_user_profile_picture")
    private String answerUserProfilePicture;
    @SerializedName("answer_user_name")
    private String answerUserName;
    @SerializedName("answer_id")
    private String answerId;
    @SerializedName("last_answer_id")
    private String lastAnswerId;
    @SerializedName("last_answer")
    private String lastAnswer;
    @SerializedName("last_answer_user_profile_picture")
    private String lastAnswerUserProfilePicture;
    @SerializedName("last_answer_user_name")
    private String lastAnswerUserName;

    


    private int postType;

    public Question() {

    }


    protected Question(Parcel in) {
        id = in.readInt();
        uuid = in.readString();
        question = in.readString();
        pictureUrl = in.readString();
        isAnswered = in.readInt();
        status = in.readInt();
        createDt = in.readString();
        updateDt = in.readString();
        createUsr = in.readString();
        updateUsr = in.readString();
        avatarUrl = in.readString();
        userName = in.readString();
        postTime = in.readString();
        totalAnswer = in.readString();
        answer = in.readString();
        answerUserProfilePicture = in.readString();
        answerUserName = in.readString();
        answerId = in.readString();
        lastAnswer = in.readString();
        lastAnswerId = in.readString();
        lastAnswerUserProfilePicture = in.readString();
        lastAnswerUserName = in.readString();
        postType = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getLastAnswer() {
        return lastAnswer;
    }

    public void setLastAnswer(String lastAnswer) {
        this.lastAnswer = lastAnswer;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getLastAnswerId() {
        return lastAnswerId;
    }

    public void setLastAnswerId(String lastAnswerId) {
        this.lastAnswerId = lastAnswerId;
    }

    public String getLastAnswerUserProfilePicture() {
        return lastAnswerUserProfilePicture;
    }

    public void setLastAnswerUserProfilePicture(String lastAnswerUserProfilePicture) {
        this.lastAnswerUserProfilePicture = lastAnswerUserProfilePicture;
    }

    public String getLastAnswerUserName() {
        return lastAnswerUserName;
    }

    public void setLastAnswerUserName(String lastAnswerUserName) {
        this.lastAnswerUserName = lastAnswerUserName;
    }

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

    public String getAnswerUserProfilePicture() {
        return answerUserProfilePicture;
    }

    public void setAnswerUserProfilePicture(String answerUserProfilePicture) {
        this.answerUserProfilePicture = answerUserProfilePicture;
    }

    public String getAnswerUserName() {
        return answerUserName;
    }

    public void setAnswerUserName(String answerUserName) {
        this.answerUserName = answerUserName;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(uuid);
        parcel.writeString(question);
        parcel.writeString(pictureUrl);
        parcel.writeInt(isAnswered);
        parcel.writeInt(status);
        parcel.writeString(createDt);
        parcel.writeString(updateDt);
        parcel.writeString(createUsr);
        parcel.writeString(updateUsr);
        parcel.writeString(avatarUrl);
        parcel.writeString(userName);
        parcel.writeString(postTime);
        parcel.writeString(totalAnswer);
        parcel.writeString(answer);
        parcel.writeString(answerUserProfilePicture);
        parcel.writeString(answerUserName);
        parcel.writeString(answerId);
        parcel.writeString(lastAnswerId);
        parcel.writeString(lastAnswer);
        parcel.writeString(lastAnswerUserProfilePicture);
        parcel.writeString(lastAnswerUserName);
        parcel.writeInt(postType);
    }
}
