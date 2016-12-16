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

import java.util.ArrayList;
import java.util.List;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.adapter.PostProfileAdapter;
import app.rbzeta.postaq.listener.RecyclerTouchListener;
import app.rbzeta.postaq.model.ProfileHeader;
import app.rbzeta.postaq.rest.message.UserForm;

/**
 * Created by Robyn on 08/11/2016.
 */

public class HomeProfileFragment extends Fragment implements RecyclerTouchListener.OnItemClickListener{
    private RecyclerView recyclerView;

    private List<UserForm> itemList = new ArrayList<>();
    private PostProfileAdapter adapter;

    public HomeProfileFragment instance(Bundle bundle){
        HomeProfileFragment frag = new HomeProfileFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.content_tab_profile,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.homeRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),this));
        adapter = new PostProfileAdapter(getContext(),itemList,new ProfileHeader());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        itemList.clear();
        itemList.add(new UserForm());
        itemList.add(new UserForm());
        itemList.add(new UserForm());
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
