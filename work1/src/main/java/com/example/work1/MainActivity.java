package com.example.work1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //图片控件
    ImageView iv;
    //进度条控件
    TextView tv;

    //广播接收者
    MyReceiver_Upadte receiver_upadte;
    MyReceiver_Finish receiver_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //使用actionBar自定义标题
        ActionBar actionBar = getSupportActionBar();
        //设置标题
        actionBar.setTitle("DownLoad");
        //获得图片控件
        iv = (ImageView) findViewById(R.id.iv);
        //获得进度条控件
        tv = (TextView) findViewById(R.id.tv);
        //生成动态广播接收者对象，下载中
        receiver_upadte = new MyReceiver_Upadte(this, tv);
        //生成动态广播接收者对象，下载完成
        receiver_finish = new MyReceiver_Finish(this, tv, iv);

    }

    //点击按钮下载图片
    public void downloadImg(View view) {
        //联网下载，开启异步任务
        //保存图片到本地
        Intent intent = new Intent(this, DownloadService.class);
        //传入图片地址
        intent.putExtra("url", "http://img.sc115.com/uploads1/sc/jpgs/1508/apic22412_sc115.com.jpg");
        //开启服务
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将广播注销掉
        unregisterReceiver(receiver_upadte);
        unregisterReceiver(receiver_finish);
    }
}
