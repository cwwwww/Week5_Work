package com.example.work1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/25 0025.
 */
public class MyReceiver_Upadte extends BroadcastReceiver {
    TextView tv;

    public MyReceiver_Upadte(Context mainActivity, TextView tv) {
        //过滤器
        IntentFilter filter = new IntentFilter();
        //过滤器条件
        filter.addAction("cww.download.update");
        //注册广播接收器
        mainActivity.registerReceiver(this, filter);
        //获得进度条对象
        this.tv = tv;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //获取图片下载的进度值
        int progress = intent.getIntExtra("progress", 0);
        //设置进度值
        tv.setText("downloading..." + progress + "%");
    }
}
