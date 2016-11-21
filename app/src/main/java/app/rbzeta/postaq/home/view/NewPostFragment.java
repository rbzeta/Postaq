package app.rbzeta.postaq.home.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.custom.CircleTransform;
import app.rbzeta.postaq.helper.SessionManager;

/**
 * Created by Robyn on 11/19/2016.
 */

public class NewPostFragment extends DialogFragment {
    private ImageView imgUserAvatar,imgPost;
    private TextView textUserName;
    private EditText editPostText;
    private Button buttonSubmitPost;
    private ImageButton buttonAddImage,buttonDeleteImage;
    private View imgPostContainer;
    private NestedScrollView scrollView;
    private boolean isTextPostIsFilled = false;

    public static NewPostFragment newInstance(Bundle bundle){
        NewPostFragment fragment = new NewPostFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getContext(),getTheme()){
            @Override
            public void onBackPressed() {
                if(isTextPostIsFilled){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(getString(R.string.dialog_msg_discard_post_confirmation));
                    builder.setNegativeButton(getString(R.string.no), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.setPositiveButton(getString(R.string.action_yes), new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getDialog().dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else getDialog().dismiss();
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyDialogFullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_question,container,false);
        imgUserAvatar = (ImageView)view.findViewById(R.id.img_new_post_avatar);
        imgPost = (ImageView)view.findViewById(R.id.img_new_post_question);
        textUserName = (TextView)view.findViewById(R.id.text_new_post_user_name);
        editPostText = (EditText)view.findViewById(R.id.edit_new_post_text);
        buttonSubmitPost = (Button)view.findViewById(R.id.button_new_post_ask_question);
        buttonAddImage = (ImageButton)view.findViewById(R.id.button_new_post_image);
        buttonDeleteImage = (ImageButton)view.findViewById(R.id.button_new_post_delete_image);
        imgPostContainer = view.findViewById(R.id.container_img_new_post_question);
        scrollView = (NestedScrollView) view.findViewById(R.id.scroll_new_post_text);

        editPostText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0){
                    buttonSubmitPost.setEnabled(true);
                    isTextPostIsFilled = true;
                }else {
                    buttonSubmitPost.setEnabled(false);
                    isTextPostIsFilled = false;
                }
            }
        });
        buttonDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgPostContainer.setVisibility(View.GONE);
                //delete image
            }
        });

        editPostText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //if(view.getId() == R.id.edit_new_post_text){
                  //  view.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction()&MotionEvent.ACTION_MASK){
                        case    MotionEvent.ACTION_UP:
                            view.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                //}
                return false;
            }
        });


        buttonSubmitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Save post to server",Toast.LENGTH_SHORT).show();
            }
        });

        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Open image selector intent",Toast.LENGTH_SHORT).show();
                imgPostContainer.setVisibility(View.VISIBLE);
                View lastChild = scrollView.getChildAt(scrollView.getChildCount() - 1);
                int bottom = lastChild.getBottom() + scrollView.getPaddingBottom();
                int sy = scrollView.getScrollY();
                int sh = scrollView.getHeight();
                int delta = bottom - (sy + sh);

                scrollView.smoothScrollBy(0, delta);

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = new SessionManager(getContext(),getContext().MODE_PRIVATE);
        String url = session.getUserProfilePictureUrl();
        if (url == null) {
            Glide.with(this).load(R.drawable.img_user_profile_default)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imgUserAvatar);
        }else
            Glide.with(this).load(url)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .error(R.drawable.img_user_profile_default)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imgUserAvatar);

        textUserName.setText(session.getUserName());

    }


}
