package app.rbzeta.postaq.adapter;

import android.content.Context;
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
import app.rbzeta.postaq.model.Question;

/**
 * Created by Robyn on 25/10/2016.
 */

public class PostQuestionAdapter extends RecyclerView.Adapter<PostQuestionAdapter.MyViewHolder> {

    private List<Question> mPostList;
    private Context mContext;
    private static final int TYPE_IMAGE_POST = 1;
    private static final int TYPE_TEXT_POST = 2;
    private SessionManager session;

    public PostQuestionAdapter(Context context,List<Question> postList) {
        mContext = context;
        mPostList = postList;
        session = MyApplication.getInstance().getSessionManager();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView overflow,postAvatar,postImage,postAvatarAnswer;
        private TextView textPostQuestion,textPostTime,textPostUserName,textPostAnswerCount;
        private TextView textPostIsAnswered,textPostAnswer,textPostUserNameAnswer;
        private Button btnAnswer;
        private View answerContainer,buttonAnswerContainer;
        private LinearLayout textAnswerFlagContainer;

        private MyViewHolder(View itemView) {
            super(itemView);
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
                    Toast.makeText(mContext,"Go to answer question intent",Toast.LENGTH_LONG).show();
                }
            });

            textPostAnswerCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"Open answer fragment",Toast.LENGTH_LONG).show();
                }
            });

            textPostUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"Go to view user profile intent",Toast.LENGTH_LONG).show();
                }
            });

            textPostQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"Go to view question intent",Toast.LENGTH_LONG).show();
                }
            });

            overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(overflow,getLayoutPosition());
                }
            });

            textPostAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"Open answer fragment",Toast.LENGTH_LONG).show();
                }
            });

            postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"Open image post activity or fragment",Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Question postItem = mPostList.get(position);

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
        if(postItem.isAnswered() == 1){
            isAnswered = mContext.getString(R.string.text_answered);
            if (postItem.getAvatarAnswerUrl() == null) {
                Glide.with(mContext).load(R.drawable.img_user_profile_default)
                        .crossFade()
                        .thumbnail(0.2f)
                        .centerCrop()
                        .bitmapTransform(new CircleTransform(mContext))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.postAvatarAnswer);
            }else
                Glide.with(mContext).load(postItem.getAvatarAnswerUrl())
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
            holder.textPostUserNameAnswer.setText(postItem.getUserNameAnswer());
            holder.textPostAnswer.setText(postItem.getPostAnswer());

        }
        holder.textPostIsAnswered.setText(isAnswered);
        holder.textPostTime.setText("Questioned at "+postItem.getPostTime());
        holder.textPostAnswerCount.setText(postItem.getTotalAnswer());
        holder.textPostUserName.setText(postItem.getUserName());
        holder.textPostQuestion.setText(postItem.getQuestion());

        if(!postItem.getUuid().equals(session.getUserUuid()) ){
            holder.overflow.setVisibility(View.GONE);
        }
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

                    mPostList.remove(position);
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
        return mPostList.size();
    }

}
