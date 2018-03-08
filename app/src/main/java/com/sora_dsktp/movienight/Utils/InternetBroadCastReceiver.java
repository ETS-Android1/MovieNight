package com.sora_dsktp.movienight.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by SoRa-DSKTP on 8/3/2018.
 */

public class InternetBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(activeNetwork != null)
        {
            if(activeNetwork.isConnected())
            {
                Toast.makeText(context,"Yayyy we have internet",Toast.LENGTH_LONG).show();
            }
        }
        else Toast.makeText(context,"Ooopsy we lost internet",Toast.LENGTH_LONG).show();


    }
}
