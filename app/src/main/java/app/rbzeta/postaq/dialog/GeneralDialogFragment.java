package app.rbzeta.postaq.dialog;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import app.rbzeta.postaq.R;

/**
 * Created by Robyn on 01/10/2016.
 */

public class GeneralDialogFragment extends DialogFragment {

    private TextView mTextTitle;
    private TextView mTextSubTitle;
    private Button mButtonOk;
    private String mTitle;
    private String mSubTitle;

    public GeneralDialogFragment() {
        super();
    }

    public static GeneralDialogFragment newInstance(String title, String subTitle){
        GeneralDialogFragment frag = new GeneralDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("subtitle",subTitle);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mTitle = args.getString("title");
        mSubTitle = args.getString("subtitle");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        return inflater.inflate(R.layout.fragment_dialog_general_,container);
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        window.setLayout((int)(size.x * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextTitle = (TextView) view.findViewById(R.id.textTitleErrorDialogFragment);
        mTextSubTitle = (TextView) view.findViewById(R.id.textSubTitleErrorDialogFragment);

        mTextTitle.setText(mTitle);
        mTextSubTitle.setText(mSubTitle);

        mButtonOk = (Button) view.findViewById(R.id.ok_fragment_error_button);

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //getActivity().finish();
            }
        });



    }
}
