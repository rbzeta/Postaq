package app.rbzeta.postaq.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import app.rbzeta.postaq.model.Answer;
import app.rbzeta.postaq.model.Question;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.rbzeta.postaq.adapter.PostQuestionAdapter.VIEW_TYPE_ITEM;
import static app.rbzeta.postaq.adapter.PostQuestionAdapter.VIEW_TYPE_LOADING;

/**
 * Created by Robyn on 12/17/2016.
 */

public class AnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Answer> dataList;
    private Context mContext;
    private Question mQuestion;
    private SessionManager session;

    public AnswerAdapter (Context context, List<Answer> listAnswer, Question question){
        mContext = context;
        dataList = listAnswer;
        mQuestion = question;
        session = MyApplication.getInstance().getSessionManager();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img_item_answer_profile)
        ImageView avatarImg;
        @OnClick(R.id.img_item_answer_profile)
        public void avatarImageClicked(){
            Toast.makeText(mContext,"Go to view user profile intent",Toast.LENGTH_LONG).show();
        }

        @BindView(R.id.img_item_answer_mark_favorite)
        ImageView imgMarkAsAnswer;
        @OnClick(R.id.img_item_answer_mark_favorite)
        public void favoriteImageClicked(){
            setAnswerAsFavorite();
        }

        @BindView(R.id.text_item_answer_user_name)
        TextView textUserName;
        @OnClick(R.id.text_item_answer_user_name)
        public void textUserNameClicked() {
            Toast.makeText(mContext, "Go to view user profile intent", Toast.LENGTH_LONG).show();
        }

        @BindView(R.id.text_item_answer)
        TextView textAnswer;

        @BindView(R.id.text_item_answer_time)
        TextView textAnswerTime;

        /*public ImageView avatarImg;
        public TextView textUserName, textAnswer, textAnswerTime;*/

        private ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
           /* avatarImg = (ImageView)itemView.findViewById(R.id.img_item_answer_profile);
            textUserName = (TextView)itemView.findViewById(R.id.text_item_answer_user_name);
            textAnswer = (TextView)itemView.findViewById(R.id.text_item_answer);
            textAnswerTime = (TextView)itemView.findViewById(R.id.text_item_answer_time);*/
            //imgMarkAsAnswer = (ImageView)itemView.findViewById(R.id.img_item_answer_mark_favorite);
            /*imgMarkAsAnswer.bringToFront();
            imgMarkAsAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAnswerAsFavorite();
                }
            });*/
        }
    }

    private void setAnswerAsFavorite() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getString(R.string.dialog_msg_mark_answer_as_favorite));
        builder.setNegativeButton(mContext.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton(mContext.getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UIHelper.showToastLong(mContext,"This answer has been mark as answer");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_item)
        ProgressBar progress;
        private LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(viewType == VIEW_TYPE_ITEM) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_answer, parent, false);

            return new AnswerAdapter.ItemViewHolder(itemView);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);

            return new AnswerAdapter.LoadingViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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

    private void setItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ItemViewHolder holder = (ItemViewHolder)viewHolder;

        Answer item = dataList.get(position);
        if (item.getUserAvatarUrl() == null) {
            Glide.with(mContext).load(R.drawable.img_user_profile_default)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .bitmapTransform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.avatarImg);
        }else
            Glide.with(mContext).load(item.getUserAvatarUrl())
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .error(R.drawable.img_user_profile_default)
                    .bitmapTransform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.avatarImg);

        holder.textUserName.setText(item.getUserName());
        holder.textAnswer.setText(item.getAnswer());
        holder.textAnswerTime.setText("Answered at " + item.getAnswerTime());

        if (mQuestion.getUuid().equals(session.getUserUuid())) {
            holder.imgMarkAsAnswer.setVisibility(View.VISIBLE);
            if (item.getIsAnswer() == 1) {
                holder.imgMarkAsAnswer.setImageResource(R.drawable.ic_star_black_24dp);
            } else
                holder.imgMarkAsAnswer.setImageResource(R.drawable.ic_star_border_black_24dp);
        }else
            holder.imgMarkAsAnswer.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (dataList.get(position) == null) ? VIEW_TYPE_LOADING
                : VIEW_TYPE_ITEM;
    }
}
