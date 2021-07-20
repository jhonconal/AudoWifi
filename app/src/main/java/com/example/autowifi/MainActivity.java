package com.example.autowifi;

import com.example.autowifi.WIFIAutoConnectionService;
import com.example.autowifi.WifiUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();
    private EditText mEtAccount;
    private EditText mEtPwd;
    private WIFIStateReceiver mWIFIStateReceiver;
    private Button mBtnAuto;
    private Button mBtnDisconnect,mBtnHconnect,mBtnconnect;
    private WifiReceiver wifiReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        registerReceiver(wifiReceiver, intentFilter);
    }

    private void initEvent() {
        mBtnAuto.setOnClickListener(this);
        mBtnDisconnect.setOnClickListener(this);
        mBtnHconnect.setOnClickListener(this);
        mBtnconnect.setOnClickListener(this);
    }

    private void initData() {

    }

    private void initView() {
        mEtAccount = findViewById(R.id.et_account);
        mEtPwd = findViewById(R.id.et_pwd);
        mBtnAuto = findViewById(R.id.btn_auto);
        mBtnDisconnect = findViewById(R.id.btn_disconnect);
        mBtnconnect = findViewById(R.id.btn_connect);
        mBtnHconnect = findViewById(R.id.btn_hconnect);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWIFIStateReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auto: {
                if (mWIFIStateReceiver == null) {
                    mWIFIStateReceiver = new WIFIStateReceiver(MainActivity.this);
                    registerReceiver(mWIFIStateReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
                    WIFIAutoConnectionService.start(this,mEtAccount.getText().toString().trim(),mEtPwd.getText().toString().trim());
                }
            }
            break;
            case R.id.btn_disconnect: {
                WIFIConnectionManager.getInstance(this).disconnect();
                WIFIConnectionManager.getInstance(this).closeWifi();
            }
            break;
            case R.id.btn_connect: {
                WIFIConnectionManager.getInstance(this).disconnect();
                WIFIConnectionManager.getInstance(this).openWifi();
            }
            break;
            case R.id.btn_hconnect: {
                //ssid+psk =>OK
                //ssid+null =>OK
                //隐藏ssid+psk =OK
                //隐藏ssid+null => Yes
                WifiUtils.getInstance(this).connectWifiNoPws("chinalife-device");
//                WifiUtils.getInstance(this).connectWifiPws("chinalife-device","hht123456");
            }
            break;
        }
    }

}

class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            // Do your work.

            // e.g. To check the Network Name or other info:
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            Toast.makeText(context, "SSID: "+wifiManager.getConnectionInfo().getSSID(), Toast.LENGTH_SHORT).show();
        }
    }
}