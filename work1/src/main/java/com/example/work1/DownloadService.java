package com.example.work1;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Administrator on 2016/9/25 0025.
 */
public class DownloadService extends Service {

    //创建一个handler对象，用来接收图片下载的进度
    //因为服务是在主线程运行的，所以可以直接创建handler对象
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpUtils.UPDATE_PROGRESS: {
                    //接收图片下载的进度
                    //动态注册广播接收者，将图片下载进度发送到主线程中
                    int progress = msg.arg1;
                    //创建发送广播的意图
                    Intent intent = new Intent();
                    //设置发送广播的接收条件
                    intent.setAction("cww.download.update");
                    intent.putExtra("progress", progress);
                    //发送广播
                    sendBroadcast(intent);
                    //在这里发送通知，通知用户下载进度
                    //通过系统服务获得通知管理者
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    //创建通知构建者对象
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(DownloadService.this);
                    //设置通知的小图片，标题，内容，可点击，
                    //一定要设置滚动字符串，否则会报错
                    builder.setTicker("图片开始下载");
                    builder.setSmallIcon(android.R.drawable.stat_sys_download);
                    //设置标题和内容
                    builder.setContentTitle("正在下载:" + fileName).setContentText(progress + "%");
                    //设置进度
                    builder.setProgress(100, progress, false);
                    //发送通知
                    nm.notify(1, builder.build());
                }
                break;
                case HttpUtils.DOWNLOAD_FINISH: {
                    //图片下载完成，把它显示到imgeview
                    //要对图片进行二次采样处理
                    //也只能通过发广播的形式，来实现
                    //传值过去，图片的位流
                    //Log.i("TAG", "下载完成");
                    Intent intent = new Intent();
                    intent.setAction("cww.download.finish");
                    String filePath = (String) msg.obj;
                    intent.putExtra("filePath", filePath);
                    sendBroadcast(intent);
                }
                break;
            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    String fileName = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //获得url地址
        final String url = intent.getStringExtra("url");
        //Log.i("TAG", "图片的地址" + url);
        //用文件取名字
        fileName = url.substring(url.lastIndexOf("/") + 1);
        //Log.i("TAG", "图片的名字" + fileName);
        //开启下载任务
        //因为服务是在主线中运行，所以需要用子线程来下载
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtils.download(url, fileName, null, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        try {
//            // Log.i("TAG", "开启下载任务");
//            //
//            HttpUtils.saveFile(url, fileName, null, handler);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
        return super.onStartCommand(intent, flags, startId);
    }
}
