package com.example.work1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/25 0025.
 */
public class MyReceiver_Finish extends BroadcastReceiver {
    TextView tv;
    ImageView iv;

    public MyReceiver_Finish(MainActivity mainActivity, TextView tv, ImageView iv) {
        IntentFilter filter = new IntentFilter();
        //过滤的条件
        filter.addAction("cww.download.finish");
        //注册广播
        mainActivity.registerReceiver(this, filter);
        this.tv = tv;
        this.iv = iv;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //表示下载完成
        Log.i("TAG", "下载完成，显示图片");
        //得到图片保存在本地的地址
        String filePath = intent.getStringExtra("filePath");
//        //获取图片流
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//        //进行图片的二次采样
//        iv.setImageBitmap(bitmap);
        //创建options
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //解析btimap的边缘
        opt.inJustDecodeBounds = true;//设置只解析边缘
        //一次采样，指解析边缘，得到图片的宽度和高度
        BitmapFactory.decodeFile(filePath, opt);
        //得到图片的宽度和高度，并把它们设置为原来的一半
        //int width=opt.outWidth/2;
        //int height=opt.outHeight/2;
        //直接可以得到采样比为1/2
        int scaleX = 1 / 2;
        int scaleY = 1 / 2;
        //设置图片质量及采样比
        opt.inPreferredConfig = Bitmap.Config.ALPHA_8;//8位图片
        opt.inSampleSize = Math.max(scaleX, scaleY);
        //真正的解析
        opt.inJustDecodeBounds = false;
        //获得二次采样的bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, opt);
        iv.setImageBitmap(bitmap);
    }
}




