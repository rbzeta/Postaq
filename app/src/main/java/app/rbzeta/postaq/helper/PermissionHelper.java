package app.rbzeta.postaq.helper;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import app.rbzeta.postaq.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Robyn on 15/10/2016.
 */

public class PermissionHelper{
    private AppCompatActivity mContext;
    public static int REQUEST_READ_CONTACTS = 0;
    public static int REQUEST_USE_CAMERA = 1;
    public static int REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    public PermissionHelper(AppCompatActivity context){
        this.mContext = context;
    }

    public  boolean mayRequestContacts(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (mContext.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (mContext.shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(view, R.string.permission_rationale_contact, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            mContext.requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    }).setActionTextColor(Color.WHITE).show();
        } else {
            mContext.requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    public boolean mayRequestCamera(View view){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (mContext.checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (mContext.shouldShowRequestPermissionRationale(CAMERA)) {
            Snackbar.make(view, R.string.permission_rationale_camera, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            mContext.requestPermissions(new String[]{CAMERA},
                                    REQUEST_USE_CAMERA);
                        }
                    }).setActionTextColor(Color.WHITE).show();
        } else {
            mContext.requestPermissions(new String[]{CAMERA}, REQUEST_USE_CAMERA);
        }
        return false;
    }

    public boolean mayRequestWriteExternalStorage(View view){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (mContext.checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (mContext.shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(view, R.string.permission_rationale_storage, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            mContext.requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    }).setActionTextColor(Color.WHITE).show();
        } else {
            mContext.requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        return false;
    }
}
