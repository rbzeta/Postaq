package app.rbzeta.postaq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.custom.CircleTransform;
import app.rbzeta.postaq.model.ProfileHeader;
import app.rbzeta.postaq.rest.model.UserForm;

/**
 * Created by Robyn on 14/11/2016.
 */

public class PostProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<UserForm> mPostProfileList;
    private ProfileHeader mProfileHeader;
    private Context mContext;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public PostProfileAdapter(Context context,List<UserForm> postList,ProfileHeader profileHeader) {
        mContext = context;
        mPostProfileList = postList;
        mProfileHeader = profileHeader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_item, parent, false);

            return new MyViewHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_profile_item, parent, false);

            return new HeaderView(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderView){
            //set header widget value
            HeaderView vh = (HeaderView)holder;
            vh.textHeaderUserName.setText("Robyn Ezio Eiji");
            Glide.with(mContext).load(R.drawable.img_profile_test)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .placeholder(R.drawable.img_user_profile_default)
                    .error(R.drawable.img_user_profile_default)
                    .bitmapTransform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(vh.imgHeaderProfile);
        }else{
            //set item widget value

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position)
    {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return mPostProfileList.size()+1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgProfile;
        public TextView textNotification,textNotificationTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgProfile = (ImageView)itemView.findViewById(R.id.img_item_notification_profile);
            textNotification = (TextView) itemView.findViewById(R.id.text_item_notification);
            textNotificationTime = (TextView) itemView.findViewById(R.id.text_item_notification_time);
        }
    }

    public class HeaderView extends RecyclerView.ViewHolder {
        public ImageView imgHeaderProfile;
        public TextView textHeaderUserName;

        public HeaderView(View itemView) {
            super(itemView);
            imgHeaderProfile = (ImageView)itemView.findViewById(R.id.img_header_profile_avatar);
            textHeaderUserName = (TextView) itemView.findViewById(R.id.text_profile_header_user_name);
        }
    }


}
