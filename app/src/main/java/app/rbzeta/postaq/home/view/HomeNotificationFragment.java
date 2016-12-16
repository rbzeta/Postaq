package app.rbzeta.postaq.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.adapter.NotificationAdapter;
import app.rbzeta.postaq.listener.RecyclerTouchListener;
import app.rbzeta.postaq.rest.message.UserForm;

/**
 * Created by Robyn on 08/11/2016.
 */

public class HomeNotificationFragment extends Fragment implements RecyclerTouchListener.OnItemClickListener {
    private RecyclerView recyclerView;

    private List<UserForm> list = new ArrayList<>();
    private NotificationAdapter adapter;

    public HomeNotificationFragment instance(Bundle bundle){
        HomeNotificationFragment frag = new HomeNotificationFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_home,container,false);

        recyclerView = (RecyclerView)view.findViewById(R.id.homeRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),this));
        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = new ArrayList<>();
        adapter = new NotificationAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);
        UserForm a;
        a = new UserForm();
        a.setName("Text ini adalah test untuk text view tanpa elipsis");
        list.add(a);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(),"Go to detail post notification",Toast.LENGTH_SHORT).show();
    }
}