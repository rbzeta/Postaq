package app.rbzeta.postaq.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.application.MyApplication;
import app.rbzeta.postaq.custom.CircleTransform;
import app.rbzeta.postaq.helper.SessionManager;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.home.view.AnswerPostFragment;
import app.rbzeta.postaq.model.Question;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Robyn on 25/10/2016.
 */

public class PostQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Question> dataList;
    private Context mContext;
    private Fragment mParent;

    private static final int TYPE_IMAGE_POST = 1;
    private static final int TYPE_TEXT_POST = 2;

    private static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_LOADING = 2;

    private SessionManager session;

    public PostQuestionAdapter(Context context, List<Question> postList, Fragment parent) {
        mContext = context;
        dataList = postList;
        mParent = parent;
        session = MyApplication.getInstance().getSessionManager();
    }
    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_item) ProgressBar progress;
        private LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img_post_overflow)
        ImageView overflow;
        @OnClick(R.id.img_post_overflow)
        public void overFlowOnClicked(){
            showPopupMenu(overflow,getLayoutPosition());
        }

        @BindView(R.id.img_post_avatar)
        ImageView postAvatar;
        @OnClick(R.id.img_post_avatar)
        public void imageAvatarClicked(){
            Toast.makeText(mContext,"Go to view user profile intent",Toast.LENGTH_LONG).show();
        }


        @BindView(R.id.img_post_question)
        ImageView postImage;
        @OnClick(R.id.img_post_question)
        public void postImageClicked(){
            Toast.makeText(mContext,"Open image post activity or fragment",Toast.LENGTH_LONG).show();
        }

        @BindView(R.id.img_post_avatar_answer)
        ImageView postAvatarAnswer;
        @OnClick(R.id.img_post_avatar_answer)
        public void imageAvatarAnswerClicked(){
            Toast.makeText(mContext,"Go to view user profile intent",Toast.LENGTH_LONG).show();
        }

        @BindView(R.id.text_post_text)
        TextView textPostQuestion;
        @OnClick(R.id.text_post_text)
        public void questionTextClicked(){
            Toast.makeText(mContext,"Go to view question intent",Toast.LENGTH_LONG).show();
        }

        @BindView(R.id.text_post_time)
        TextView textPostTime;

        @BindView(R.id.text_post_user_name)
        TextView textPostUserName;
        @OnClick(R.id.text_post_user_name)
        public void userNameClicked(){
            Toast.makeText(mContext,"Go to view user profile intent",Toast.LENGTH_LONG).show();
        }

        @BindView(R.id.text_post_total_answer)
        TextView textPostAnswerCount;
        @OnClick(R.id.text_post_total_answer)
        public void textTotalAnswerClicked(){
            navigateToAnswer(getAdapterPosition());
        }

        @BindView(R.id.text_post_is_answered)
        TextView textPostIsAnswered;

        @BindView(R.id.text_post_answer)
        TextView textPostAnswer;
        @OnClick(R.id.text_post_answer)
        public void textAnswerClicked(){
            navigateToAnswer(getAdapterPosition());
        }

        @BindView(R.id.text_post_user_name_answer)
        TextView textPostUserNameAnswer;
        @OnClick(R.id.text_post_user_name_answer)
        public void textUserNameAnswer(){
            Toast.makeText(mContext,"Go to view user profile intent",Toast.LENGTH_LONG).show();
        }

        @BindView(R.id.button_post_answer)
        Button btnAnswer;
        @OnClick(R.id.button_post_answer)
        public void btnAnswerClicked(){
            navigateToAnswer(getAdapterPosition());
        }

        @BindView(R.id.container_post_answer)
        View answerContainer;
        @OnClick(R.id.container_post_answer)
        public void containerAnswerClicked(){
            navigateToAnswer(getAdapterPosition());
        }

        @BindView(R.id.container_post_button_answer)
        View buttonAnswerContainer;
        @OnClick(R.id.container_post_button_answer)
        public void containerButtonAnswerClicked(){
            navigateToAnswer(getAdapterPosition());
        }

        @BindView(R.id.container_post_text_answer_flag)
        LinearLayout textAnswerFlagContainer;

        private ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            /*
            answerContainer = itemView.findViewById(R.id.container_post_answer);
            buttonAnswerContainer = itemView.findViewById(R.id.container_post_button_answer);
            textAnswerFlagContainer = (LinearLayout)itemView.findViewById(R.id.container_post_text_answer_flag);
            overflow = (ImageView)itemView.findViewById(R.id.img_post_overflow);
            postAvatar = (ImageView)itemView.findViewById(R.id.img_post_avatar);
            postImage = (ImageView)itemView.findViewById(R.id.img_post_question);
            postAvatarAnswer = (ImageView)itemView.findViewById(R.id.img_post_avatar_answer);
            textPostQuestion = (TextView) itemView.findViewById(R.id.text_post_text);
            textPostTime = (TextView) itemView.findViewById(R.id.text_post_time);
            textPostUserName = (TextView) itemView.findViewById(R.id.text_post_user_name);
            textPostAnswerCount = (TextView) itemView.findViewById(R.id.text_post_total_answer);
            textPostIsAnswered = (TextView) itemView.findViewById(R.id.text_post_is_answered);
            textPostAnswer = (TextView) itemView.findViewById(R.id.text_post_answer);
            textPostUserNameAnswer = (TextView) itemView.findViewById(R.id.text_post_user_name_answer);
            btnAnswer = (Button)itemView.findViewById(R.id.button_post_answer);


            btnAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateToAnswer(getAdapterPosition());
                    //Toast.makeText(mContext,"Go to answer question intent",Toast.LENGTH_LONG).show();
                }
            });

            textPostAnswerCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateToAnswer(getAdapterPosition());
                    //Toast.makeText(mContext,"Open answer fragment",Toast.LENGTH_LONG).show();
                }
            });*/

            /*textPostUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"Go to view user profile intent",Toast.LENGTH_LONG).show();
                }
            });*/

            /*textPostQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"Go to view question intent",Toast.LENGTH_LONG).show();
                }
            });*/

            /*overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(overflow,getLayoutPosition());
                }
            });

            textPostAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateToAnswer(getAdapterPosition());
                    //Toast.makeText(mContext,"Open answer fragment",Toast.LENGTH_LONG).show();
                }
            });*/

            /*postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"Open image post activity or fragment",Toast.LENGTH_LONG).show();
                }
            });*/

        }

    }

    private void navigateToAnswer(int position) {
        Question question = dataList.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("question_object", question);
        AnswerPostFragment frag = AnswerPostFragment.newInstance(bundle);
        frag.show(mParent.getChildFragmentManager(),"fragment_answer");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(viewType == VIEW_TYPE_ITEM) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_question, parent, false);

            return new ItemViewHolder(itemView);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);

            return new LoadingViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ItemViewHolder){
            setItemViewHolder(holder,position);
        }else if(holder instanceof LoadingViewHolder){
            setLoadingViewHolder(holder);
        }

    }

    private void setLoadingViewHolder(RecyclerView.ViewHolder viewHolder) {
        LoadingViewHolder holder = (LoadingViewHolder)viewHolder;
        holder.progress.setIndeterminate(true);

    }

    private void setItemViewHolder(RecyclerView.ViewHolder viewHolder,int position) {
        ItemViewHolder holder = (ItemViewHolder)viewHolder;

        Question postItem = dataList.get(position);

        if (postItem.getAvatarUrl() == null) {
            Glide.with(mContext).load(R.drawable.img_user_profile_default)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .bitmapTransform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.postAvatar);
        }else
            Glide.with(mContext).load(postItem.getAvatarUrl())
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .error(R.drawable.img_user_profile_default)
                    .bitmapTransform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.postAvatar);

        if(postItem.getPictureUrl() != null){
            holder.postImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(postItem.getPictureUrl())
                    .crossFade()
                    .thumbnail(0.2f)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.postImage);
        }else holder.postImage.setVisibility(View.GONE);

        String isAnswered = mContext.getString(R.string.text_unanswered);
        if(postItem.isAnswered() == 1 && postItem.getAnswerId() != null){
            isAnswered = mContext.getString(R.string.text_answered);
            if (postItem.getAnswerUserProfilePicture() == null) {
                Glide.with(mContext).load(R.drawable.img_user_profile_default)
                        .crossFade()
                        .thumbnail(0.2f)
                        .centerCrop()
                        .bitmapTransform(new CircleTransform(mContext))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.postAvatarAnswer);
            }else
                Glide.with(mContext).load(postItem.getAnswerUserProfilePicture())
                        .crossFade()
                        .thumbnail(0.2f)
                        .centerCrop()
                        .error(R.drawable.img_user_profile_default)
                        .bitmapTransform(new CircleTransform(mContext))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.postAvatarAnswer);

            holder.answerContainer.setVisibility(View.VISIBLE);
            holder.buttonAnswerContainer.setVisibility(View.GONE);
            holder.textAnswerFlagContainer.setBackgroundResource(R.drawable.button_radius_blue);
            holder.textPostUserNameAnswer.setText(postItem.getAnswerUserName());
            holder.textPostAnswer.setText(postItem.getAnswer());

        }else if (postItem.getLastAnswerId() != null){
            if (postItem.getLastAnswerUserProfilePicture() == null) {
                Glide.with(mContext).load(R.drawable.img_user_profile_default)
                        .crossFade()
                        .thumbnail(0.2f)
                        .centerCrop()
                        .bitmapTransform(new CircleTransform(mContext))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.postAvatarAnswer);
            }else
                Glide.with(mContext).load(postItem.getLastAnswerUserProfilePicture())
                        .crossFade()
                        .thumbnail(0.2f)
                        .centerCrop()
                        .error(R.drawable.img_user_profile_default)
                        .bitmapTransform(new CircleTransform(mContext))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.postAvatarAnswer);

            holder.answerContainer.setVisibility(View.VISIBLE);
            holder.buttonAnswerContainer.setVisibility(View.VISIBLE);
            holder.textAnswerFlagContainer.setBackgroundResource(R.drawable.button_radius_red);
            holder.textPostUserNameAnswer.setText(postItem.getLastAnswerUserName());
            holder.textPostAnswer.setText(postItem.getLastAnswer());

        }else{
            holder.answerContainer.setVisibility(View.GONE);
            holder.buttonAnswerContainer.setVisibility(View.VISIBLE);
            holder.textAnswerFlagContainer.setBackgroundResource(R.drawable.button_radius_red);
            holder.textPostAnswer.setText(postItem.getLastAnswer());
            holder.textPostUserNameAnswer.setText(postItem.getLastAnswerUserName());
        }
        holder.textPostIsAnswered.setText(isAnswered);
        holder.textPostTime.setText("Questioned at "+postItem.getPostTime());
        holder.textPostAnswerCount.setText(postItem.getTotalAnswer());
        holder.textPostUserName.setText(postItem.getUserName());
        holder.textPostQuestion.setText(postItem.getQuestion());

        if(!postItem.getUuid().equals(session.getUserUuid()) ){
            holder.overflow.setVisibility(View.GONE);
        }else
            holder.overflow.setVisibility(View.VISIBLE);
    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_post_question, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{
        private int position;
        private MyMenuItemClickListener(int position){
            this.position = position;
        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete_question:
                    UIHelper.showToastLong(mContext,"Position "+position+" has been deleted");

                    dataList.remove(position);
                    notifyItemRemoved(position);
                    //notifyItemRangeRemoved(position,getItemCount());
                    return true;
                default:
            }
            return false;
        }
    }
    @Override
    public int getItemCount() {
        //list without header and footer
        return dataList.size();

        //list with header or footer only
        //return dataList.size() + 1;

        //list with header and footer
        //return dataList.size() + 2;
    }

    public int getItemCountWhenLoading() {
        return dataList.size() - 1;
    }

    @Override
    public int getItemViewType(int position) {
        //uncomment if there is a header item
        //if(position == 0)return VIEW_TYPE_HEADER;

        //item view and loading view only
       /* return (position >= dataList.size()) ? VIEW_TYPE_LOADING
                : VIEW_TYPE_ITEM;*/
        return (dataList.get(position) == null) ? VIEW_TYPE_LOADING
                : VIEW_TYPE_ITEM;
    }

    /*@Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ITEM) ? position
                : -1;
    }*/

}
