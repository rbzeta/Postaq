package app.rbzeta.postaq.helper;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.dialog.GeneralDialogFragment;
import app.rbzeta.postaq.dialog.ResetPasswordDialogFragment;

/**
 * Created by Robyn on 28/09/2016.
 */

public class UIHelper {

    public static void showToastLong(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showCustomSnackBar(View view, String msg, int color){

        Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView =
                (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();

    }

    public static void requestFocus(Window window, View view) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void showErrorDialog(FragmentManager fm, Resources res,String title, String subTitle) {
        DialogFragment dialog = new GeneralDialogFragment()
                .newInstance(title,subTitle);
        dialog.show(fm, res.getString(R.string.err_dialog_fragment_tag));
    }

    public static void showGeneralErrorDialog(FragmentManager fm, Resources res) {
        DialogFragment dialog = new GeneralDialogFragment()
                .newInstance(res.getString(R.string.err_msg_title_general),
                        res.getString(R.string.err_msg_subtitle_general));
        dialog.show(fm,
                res.getString(R.string.err_dialog_fragment_tag));
    }

    public static void showTestDialog(FragmentManager fm, Resources res,String title, String subTitle) {
        DialogFragment dialog = new ResetPasswordDialogFragment()
                .newInstance(title,subTitle);
        dialog.show(fm, res.getString(R.string.err_dialog_fragment_tag));
    }
}