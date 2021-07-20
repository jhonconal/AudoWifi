package com.example.autowifi;
import com.example.autowifi.WIFIConnectionManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

public class WIFIStateReceiver extends BroadcastReceiver {

    private static final String TAG = WIFIStateReceiver.class.getName();
    private Context mContext;
    List<ScanResult> scanResults;

    public WIFIStateReceiver(Context context) {
        this.mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            return;
        }
        scanResults =  WIFIConnectionManager.getInstance(mContext).getWifiManager().getScanResults();
        for (int i = 0 ; i < scanResults.size();i++) {
            Log.e(TAG,"scanResults:----"+(scanResults.get(i)).SSID);
        }
//        if (!WIFIConnectionManager.getInstance(mContext).isConnected("hhtceshi_5.1G")) {
//            WIFIConnectionManager.getInstance(mContext).connect("hhtceshi_5.1G", "hhtcszx2020");
//        }
        if (!WIFIConnectionManager.getInstance(mContext).isConnected("chinalife-device")) {
//            WIFIConnectionManager.getInstance(mContext).connect("chinalife-device", "hht123456");
            WIFIConnectionManager.getInstance(mContext).connect("chinalife-device", "");
        }
    }

}