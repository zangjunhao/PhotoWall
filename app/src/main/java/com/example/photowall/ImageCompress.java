package com.example.photowall;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 67698 on 2018/4/27.
 */

    //图片压缩
    //分别从资源 FileDescriptor进行加载
    //这里要明白的options.inJustDecodeBounds的ture代表option只测不加载而false就代表要加载
public class ImageCompress {
    private static final String Tag="ImageCompress";
    public ImageCompress(){
    }
    public Bitmap decodeSampledBitmapFromResource(Resources res,int resId,int reqWidth,int reqHeight)
    {
        final BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize=CalculateInsampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(res,resId,options);//转换成位图
    }

    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight)
    {
        final  BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
        options.inSampleSize=CalculateInsampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
    }
    public Bitmap decodeSampledBitmapFromStream(InputStream inputStream, int reqWidth, int reqHeight)
    {
        if(inputStream==null)return null;
        byte[]date;
        Log.d("难受啊卢员外", "decodeSampledBitmapFromStream: "+inputStream);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
             date=inputStream2ByteArr(inputStream);
            BitmapFactory.decodeStream(inputStream,null,options);

            options.inSampleSize = CalculateInsampleSize(options,reqWidth,reqHeight);
            Log.d("难受呀卢员外", "decodeSampledBitmapFromStream: "+inputStream);
            options.inJustDecodeBounds = false;
            Log.d("难受呀卢员外", "decodeSampledBitmapFromStream_decode: "+BitmapFactory.decodeByteArray(date,0,date.length,options));
            return BitmapFactory.decodeByteArray(date,0,date.length,options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //计算采样率
    public int CalculateInsampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight)
    {   int insamplesize=1;
        if(reqHeight==0||reqWidth==0)return 1;
        final int outWidth=options.outWidth;
        final int outHeight=options.outHeight;
        if(outHeight>reqHeight||outWidth>reqWidth){
            final int halfHeight=outHeight/2;
            final int halfWidth=outWidth/2;
            while((halfWidth/insamplesize)>=reqWidth&&(halfHeight/insamplesize)>=reqHeight){
                insamplesize*=2;
            }
        }
        Log.d(Tag,"simsize"+insamplesize);
        return insamplesize;
    }
    private byte[] inputStream2ByteArr(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ( (len = inputStream.read(buff)) != -1) {
            outputStream.write(buff, 0, len);
        }
        inputStream.close();
        outputStream.close();
        return outputStream.toByteArray();
    }

}
