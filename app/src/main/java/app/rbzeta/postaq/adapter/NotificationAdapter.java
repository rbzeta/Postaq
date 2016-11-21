package app.rbzeta.postaq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.rest.model.UserForm;

/**
 * Created by Robyn on 10/11/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{
    private List<UserForm> mNotificationList;
    private Context mContext;

    public NotificationAdapter(Context context,List<UserForm> postList) {
        mContext = context;
        mNotificationList = postList;
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

    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item,parent,false);

        return new NotificationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.MyViewHolder holder, final int position) {

        UserForm item = mNotificationList.get(position);
        //String textPost = item.getName();
        //holder.textNotification.setText(textPost);

    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }
}