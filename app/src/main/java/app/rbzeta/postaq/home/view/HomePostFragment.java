package app.rbzeta.postaq.home.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import app.rbzeta.postaq.helper.SessionManager;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.listener.EndlessRecyclerViewScrollListener;
import app.rbzeta.postaq.model.Answer;
import app.rbzeta.postaq.model.Question;
import app.rbzeta.postaq.rest.NetworkService;
import app.rbzeta.postaq.rest.message.QuestionResponseMessage;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by Robyn on 08/11/2016.
 */

public class HomePostFragment extends Fragment implements AnswerPostFragment.PostAnswerListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Question> postQuestionList = new ArrayList<>();
    private PostQuestionAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    //private SessionManager session;
    private Subscription subscription;
    private NetworkService networkService;
    private MyDBHandler dbHandler;

    private boolean isLoadQuestionFromServerSuccess;



    public static HomePostFragment newInstance(Bundle bundle){
        HomePostFragment frag = new HomePostFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = MyApplication.getInstance().getDBHandler();
        //session = MyApplication.getInstance().getSessionManager();
        networkService = MyApplication.getInstance().getNetworkService();
        postQuestionList = new ArrayList<>();
        adapter = new PostQuestionAdapter(getContext(),postQuestionList,this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void loadPostQuestionsFromServer() {
        swipeRefreshLayout.setRefreshing(true);
        fetchListFromServer(0);
    }

    private void loadQuestionFromDatabase() {
        postQuestionList.clear();
        List<Question> localQuestions = dbHandler.getQuestionList();

        postQuestionList.addAll(localQuestions);

        adapter.notifyDataSetChanged();
        scrollListener.resetState();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadPostQuestionsFromServer();

        if (!isLoadQuestionFromServerSuccess){
            loadQuestionFromDatabase();
        }
        //loadDummyData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_home,container,false);

        recyclerView = (RecyclerView)view.findViewById(R.id.homeRecycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return -1;
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchListFromServer(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_home);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(),R.color.colorPrimary));

        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(() -> fetchListFromServer(0));
    }

    private void fetchListFromServer(int page) {
        if (page != 0)
            showProgressBar();

        Observable<QuestionResponseMessage> observable = (Observable<QuestionResponseMessage>)
                networkService.getPreparedObservable(networkService.getNetworkAPI().getQuestionListFromServer(page),
                        QuestionResponseMessage.class,
                        false,false);

        subscription = observable.subscribe(new Observer<QuestionResponseMessage>() {
            @Override
            public void onCompleted() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                swipeRefreshLayout.setRefreshing(false);
                if (page != 0)
                    dismissProgressBar();

                rxUnSubscribe();
                UIHelper.showCustomSnackBar(recyclerView,
                        getString(R.string.dialog_msg_error_fetch_question),
                        ContextCompat.getColor(getContext(),R.color.white));
                /*UIHelper.showErrorDialog(getFragmentManager(),getResources(),
                        getString(R.string.dialog_title_error_fetch_question),
                        getString(R.string.dialog_msg_error_fetch_question));*/

                isLoadQuestionFromServerSuccess = false;
            }

            @Override
            public void onNext(QuestionResponseMessage response) {
                if (page != 0)
                    dismissProgressBar();

                if (response.isSuccess()){

                    List<Question> questions = response.getQuestionList();

                    isLoadQuestionFromServerSuccess = true;

                    if (questions != null){
                        if(page == 0){
                            postQuestionList.clear();
                            dbHandler.deleteQuestions();
                            scrollListener.resetState();
                        }

                        for (Question question: questions) {
                            dbHandler.saveQuestion(question);
                            postQuestionList.add(question);
                        }

                        if(page == 0)
                            adapter.notifyDataSetChanged();
                        else adapter.notifyItemRangeInserted(adapter.getItemCount()+1,questions.size());
                    }

                    //fillDataAdapter(questions);


                }else {
                    UIHelper.showCustomSnackBar(recyclerView,
                            response.getMessage(),
                            ContextCompat.getColor(getContext(),R.color.white));
                    /*UIHelper.showErrorDialog(getFragmentManager(),getResources(),
                            response.getTitle(),response.getMessage());*/
                }

            }
        });

    }

    private void showProgressBar() {
        postQuestionList.add(null);
        adapter.notifyItemInserted(postQuestionList.size() - 1);
    }

    private void dismissProgressBar() {
        postQuestionList.remove(postQuestionList.size() - 1);
        adapter.notifyItemRemoved(postQuestionList.size());
    }

    public void onSuccessPostQuestion(Question question){
        postQuestionList.add(0,question);
        adapter.notifyItemInserted(0);
        //adapter.notifyItemRangeInserted(0,adapter.getItemCount());
        recyclerView.scrollToPosition(0);

    }

    @Override
    public void onSuccessPostAnswer(Answer answer) {
        UIHelper.showToastLong(getContext(),"Your answer has been saved.");
    }

    /*@Override
    public void onErrorPostAnswer() {

    }*/

    private void rxUnSubscribe() {
        if(subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
