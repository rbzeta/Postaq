package app.rbzeta.postaq.home.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.adapter.AnswerAdapter;
import app.rbzeta.postaq.application.MyApplication;
import app.rbzeta.postaq.custom.CircleTransform;
import app.rbzeta.postaq.helper.SessionManager;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.listener.EndlessRVAnswerScrollListener;
import app.rbzeta.postaq.listener.EndlessRecyclerViewScrollListener;
import app.rbzeta.postaq.listener.RecyclerTouchListener;
import app.rbzeta.postaq.model.Answer;
import app.rbzeta.postaq.model.Question;
import app.rbzeta.postaq.rest.NetworkService;
import app.rbzeta.postaq.rest.message.AnswerResponseMessage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by Robyn on 12/7/2016.
 */

//public class AnswerPostFragment extends DialogFragment implements RecyclerTouchListener.OnItemClickListener {
public class AnswerPostFragment extends DialogFragment{

    @BindView(R.id.recycler_answer)
    RecyclerView recyclerView;
    @BindView(R.id.img_answer_avatar)
    ImageView userAvatarImg;
    @BindView(R.id.text_answer_user_name)
    TextView textUserName;
    @BindView(R.id.button_submit_answer)
    Button buttonAnswer;
    @OnClick(R.id.button_submit_answer)
    public void buttonAnswerClicked(){
        submitAnswer();
    }

    @BindView(R.id.edit_answer_question)
    EditText editAnswer;

    @BindView(R.id.progress_answer)
    ProgressBar progressBar;

    private List<Answer> mListAnswer;
    private AnswerAdapter mAnswerAdapter;
    private Question mQuestion;
    Unbinder unbinder;
    private EndlessRVAnswerScrollListener scrollListener;

    private boolean isTextPostIsFilled = false;

    private SessionManager session;
    private Subscription subscription;
    private NetworkService networkService;
    private PostAnswerListener listener;

    public static AnswerPostFragment newInstance(Bundle bundle){
        AnswerPostFragment frag = new AnswerPostFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        mQuestion = args.getParcelable("question_object");
        listener = (PostAnswerListener)getParentFragment();
        mListAnswer = new ArrayList<>();
        mAnswerAdapter = new AnswerAdapter(context,mListAnswer,mQuestion);
        session = MyApplication.getInstance().getSessionManager();
        networkService = MyApplication.getInstance().getNetworkService();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyDialogFullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_question,container,false);
        unbinder = ButterKnife.bind(this, view);
        //userAvatarImg = (ImageView)view.findViewById(R.id.img_answer_avatar);
        //textUserName = (TextView)view.findViewById(R.id.text_answer_user_name);
        //buttonAnswer = (Button)view.findViewById(R.id.button_submit_answer);
        //editAnswer = (EditText)view.findViewById(R.id.edit_answer_question);
        /*buttonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAnswer();
            }
        });*/

        //recyclerView = (RecyclerView)view.findViewById(R.id.recycler_answer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRVAnswerScrollListener(linearLayoutManager) {
            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return -1;
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadAnswerFromServer(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),this));
        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadAnswerFromServer(0);
    }

    private void loadAnswerFromServer(int page) {
        if (page != 0)
            showProgressBar();

        Observable<AnswerResponseMessage> observable = (Observable<AnswerResponseMessage>)
                networkService.getPreparedObservable(networkService.getNetworkAPI().getAnswerListFromServer(page,mQuestion.getId()),
                        AnswerResponseMessage.class,false,false);
        subscription = observable.subscribe(new Observer<AnswerResponseMessage>() {
            @Override
            public void onCompleted() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                if (page != 0)
                    dismissProgressBar();
                else
                    progressBar.setVisibility(View.GONE);

                rxUnSubscribe();
                UIHelper.showCustomSnackBar(recyclerView,
                        getString(R.string.dialog_msg_error_fetch_answer),
                        ContextCompat.getColor(getContext(),R.color.white));
            }

            @Override
            public void onNext(AnswerResponseMessage response) {
                if (page != 0)
                    dismissProgressBar();

                if (response.isSuccess()){

                    List<Answer> answers = response.getAnswerList();

                    if (answers != null){
                        if(page == 0){
                            mListAnswer.clear();
                            scrollListener.resetState();
                        }

                        mListAnswer.addAll(answers);
                        if(page == 0)
                            mAnswerAdapter.notifyDataSetChanged();
                        else mAnswerAdapter.notifyItemRangeInserted(mAnswerAdapter.getItemCount()+1,answers.size());
                    }


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
        mListAnswer.add(null);
        mAnswerAdapter.notifyItemInserted(mListAnswer.size() - 1);
    }
    private void dismissProgressBar() {
        mListAnswer.remove(mListAnswer.size() - 1);
        mAnswerAdapter.notifyItemRemoved(mListAnswer.size());
    }

    private void submitAnswer() {
        final ProgressDialog progress = ProgressDialog.show(getContext(),
                getString(R.string.txt_title_upload_answer),
                getString(R.string.txt_dialog_please_wait),true,false);

        Answer answer = new Answer();
        answer.setAnswer(editAnswer.getText().toString().trim());
        answer.setUuid(session.getUserUuid());
        answer.setQuestionId(mQuestion.getId());

        Observable<AnswerResponseMessage> observable = (Observable<AnswerResponseMessage>)
                networkService.getPreparedObservable(
                        networkService.getNetworkAPI().uploadAnswerQuestion(answer),
                        AnswerResponseMessage.class,
                        false,false);
        subscription = observable.subscribe(new Observer<AnswerResponseMessage>() {
            @Override
            public void onCompleted() {
                progress.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progress.dismiss();
                rxUnSubscribe();
                UIHelper.showCustomSnackBar(recyclerView,
                        getString(R.string.dialog_msg_error_upload_answer),
                        ContextCompat.getColor(getContext(),R.color.white));
                /*UIHelper.showErrorDialog(getFragmentManager(),getResources(),
                        getString(R.string.dialog_title_error_upload_answer),
                        getString(R.string.dialog_msg_error_upload_answer));*/

            }

            @Override
            public void onNext(AnswerResponseMessage msg) {
                if (msg.isSuccess()){

                    Answer answer = msg.getAnswer();

                    listener.onSuccessPostAnswer(answer);

                    dismiss();

                }else{
                    UIHelper.showCustomSnackBar(recyclerView,
                            msg.getMessage(),
                            ContextCompat.getColor(getContext(),R.color.white));
                    /*UIHelper.showErrorDialog(getFragmentManager(),getResources(),
                            msg.getTitle(),msg.getMessage());*/

                }

            }
        });
    }

    private void rxUnSubscribe() {
        if(subscription!=null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setAdapter(mAnswerAdapter);

        String url = session.getUserProfilePictureUrl();
        if (url == null) {
            Glide.with(this).load(R.drawable.img_user_profile_default)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(userAvatarImg);
        }else
            Glide.with(this).load(url)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .error(R.drawable.img_user_profile_default)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(userAvatarImg);

        textUserName.setText(session.getUserName());



        editAnswer.requestFocus();
        editAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0){
                    buttonAnswer.setEnabled(true);
                    isTextPostIsFilled = true;
                }else {
                    buttonAnswer.setEnabled(false);
                    isTextPostIsFilled = false;
                }
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getContext(),getTheme()){
            @Override
            public void onBackPressed() {
                if(isTextPostIsFilled){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(getString(R.string.dialog_msg_discard_answer_confirmation));
                    builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {

                    });

                    builder.setPositiveButton(getString(R.string.action_yes), (dialogInterface, i) -> getDialog().dismiss());

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else getDialog().dismiss();
            }
        };
    }

   /* @Override
    public void onItemClick(View view, int position) {

    }*/

    public interface PostAnswerListener{
        void onSuccessPostAnswer(Answer answer);
        //void onErrorPostAnswer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
