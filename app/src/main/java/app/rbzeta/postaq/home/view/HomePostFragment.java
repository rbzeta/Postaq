package app.rbzeta.postaq.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.adapter.PostQuestionAdapter;
import app.rbzeta.postaq.application.MyApplication;
import app.rbzeta.postaq.database.MyDBHandler;
import app.rbzeta.postaq.model.Question;

/**
 * Created by Robyn on 08/11/2016.
 */

public class HomePostFragment extends Fragment {

    private RecyclerView recyclerView;

    private List<Question> postQuestionList = new ArrayList<>();
    private PostQuestionAdapter adapter;

    private boolean isLoadQuestionFromServerSuccess;

    private MyDBHandler dbHandler;

    public HomePostFragment instance(Bundle bundle){
        HomePostFragment frag = new HomePostFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = MyApplication.getInstance().getDBHandler();
    }

    @Override
    public void onResume() {
        super.onResume();
        
        if (isLoadQuestionFromServerSuccess){
            
        }else{
            loadQuestionFromDatabase();
        }
    }

    private void loadQuestionFromDatabase() {
        postQuestionList.clear();
        List<Question> localQuestions = dbHandler.getQuestionList();

        for(Question question: localQuestions){
            postQuestionList.add(question);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        postQuestionList = new ArrayList<>();
        adapter = new PostQuestionAdapter(getContext(),postQuestionList);
        recyclerView.setAdapter(adapter);

        //loadDummyData();
    }

    private void loadDummyData() {
        Question post = new Question();
        post.setAvatarUrl("http://202.169.39.114/jakers/media/5813856f7de291.90695269/profile_picture/Postaq_20161029_210933-851002279.jpg");
        post.setUserName("Robyn Ezio Eiji Bagus Seta");
        post.setPostTime("Questioned at 7.15 PM");
        post.setQuestion("Halaman error pada saat login LAS knp ya?");
        post.setPostType(2);
        post.setPictureUrl("");
        post.setPostAnswer("Coba tutup browser kemudian buka kembali, login LAS kembali");
        post.setAnswered(0);
        post.setTotalAnswer("13 Answers");

        Question post2 = new Question();
        post2.setAvatarUrl("http://202.169.39.114/jakers/media/5816f5e2e8b255.83538496/profile_picture/Postaq_20161031_1502241524108315.jpg");
        post2.setUserName("Paramitha Ayuningtyas");
        post2.setPostTime("Questioned at 5.46 PM");
        post2.setQuestion("Klo mau cleansing TID EDC BRilink alur prosesnya ada yang tau?");
        post2.setPostType(2);
        post2.setPictureUrl("");
        post2.setPostAnswer("Ajukan ke bagian E-Channel dan TSI Kanwil aja mba");
        post2.setAnswered(1);
        post2.setTotalAnswer("2 Answers");
        post2.setAvatarAnswerUrl("http://202.169.39.114/jakers/media/5813856f7de291.90695269/profile_picture/Postaq_20161029_210933-851002279.jpg");
        post2.setUserNameAnswer("Robyn Ezio Eiji Bagus Seta");

        Question post3 = new Question();
        post3.setAvatarUrl("http://202.169.39.114/jakers/media/5809ad207270e2.64152542/profile_picture/IMG_20161021_1317521217148072.jpg");
        post3.setUserName("Kristall Bawono");
        post3.setPostTime("Questioned at 9.52 PM");
        post3.setQuestion("Ada yang tau knp error saat login bristars seperti gambar ini?");
        post3.setPostType(1);
        post3.setPictureUrl("http://202.169.39.114/jakers/media/error_sample.png");
        post3.setPostAnswer("Belum terdaftar di BRIHC, minta sama admin cabang aja");
        post3.setAnswered(1);
        post3.setTotalAnswer("6 Answers");
        post3.setAvatarAnswerUrl("http://202.169.39.114/jakers/media/5813856f7de291.90695269/profile_picture/Postaq_20161029_210933-851002279.jpg");
        post3.setUserNameAnswer("Robyn Ezio Eiji Bagus Seta");

        postQuestionList.add(post);
        postQuestionList.add(post2);
        postQuestionList.add(post3);


        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_home,container,false);

        recyclerView = (RecyclerView)view.findViewById(R.id.homeRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);
        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onSuccessPostQuestion(Question question){
        postQuestionList.add(0,question);
        adapter.notifyItemInserted(0);
        //adapter.notifyItemRangeInserted(0,adapter.getItemCount());
        recyclerView.scrollToPosition(0);

    }
}
