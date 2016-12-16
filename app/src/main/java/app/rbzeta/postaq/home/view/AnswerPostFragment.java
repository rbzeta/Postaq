package app.rbzeta.postaq.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.listener.RecyclerTouchListener;

/**
 * Created by Robyn on 12/7/2016.
 */

public class AnswerPostFragment extends DialogFragment implements RecyclerTouchListener.OnItemClickListener {

    private RecyclerView recyclerView;

    public AnswerPostFragment instance(Bundle bundle){
        AnswerPostFragment frag = new AnswerPostFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_question,container,false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_answer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),this));
        return  view;
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
