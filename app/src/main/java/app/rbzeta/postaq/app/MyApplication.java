package app.rbzeta.postaq.app;

import android.app.Application;

import app.rbzeta.postaq.bc.ConnectivityReceiver;

/**
 * Created by Robyn on 30/09/2016.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate(){
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance(){return mInstance;}

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
