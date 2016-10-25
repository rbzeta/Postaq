package app.rbzeta.postaq.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.rest.model.UserForm;

/**
 * Created by Robyn on 25/10/2016.
 */

public class PostQuestionAdapter extends RecyclerView.Adapter<PostQuestionAdapter.MyViewHolder> {

    private List<UserForm> mPostList;
    private Context mContext;

    public PostQuestionAdapter(Context context,List<UserForm> postList) {
        mContext = context;
        mPostList = postList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView overflow;

        public MyViewHolder(View itemView) {
            super(itemView);
            overflow = (ImageView)itemView.findViewById(R.id.img_post_overflow);
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

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.overflow,position);
            }
        });

    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_post_question, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{
        private int position;
        public MyMenuItemClickListener(int position){
            this.position = position;
        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete_question:
                    UIHelper.showToastLong(mContext,"Your post has been deleted");
                    mPostList.remove(position);
                    notifyDataSetChanged();
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
