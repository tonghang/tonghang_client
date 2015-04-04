package com.peer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.peer.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;

public class SdCardUtil {
	private String newPath;
	private Context mContext;	
	private static SdCardUtil sdcarutil;
	private SdCardUtil(Context mContext){
		this.mContext=mContext;
	}
	public static SdCardUtil getInstance(Context mContext){
		if(sdcarutil==null){
			sdcarutil=new SdCardUtil(mContext);
			return sdcarutil;
		}
		return sdcarutil;
	}
	private  String getSDPath(){
        File SDdir=null;
        boolean sdCardExist=
        		Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(sdCardExist){
                SDdir=Environment.getExternalStorageDirectory();
        }
        if(SDdir!=null){
                return SDdir.toString();
        }
        else{
                return null;
        }
	}
	private void createSDCardDir(){
        if(getSDPath()==null){
  //              Toast.makeText(mContext, "未找到SD卡", 1000).show();
        }else{
                    if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                        // 创建一个文件夹对象，赋值为外部存储器的目录
                         File sdcardDir =Environment.getExternalStorageDirectory();
                       //得到一个路径，内容是sdcard的文件夹路径和名字
                         newPath=sdcardDir.getPath()+"/tempImages/";//newPath在程序中要声明
                         File path1 = new File(newPath);
                        if (!path1.exists()) {
                         //若不存在，创建目录，可以在应用启动的时候创建
                         path1.mkdirs();
                         System.out.println("paht ok,path:"+newPath);
                       }
                 }
                 else{
                  System.out.println("false");
                }
        }
    }
	private void saveMyBitmap() throws IOException {  
		createSDCardDir();
        Bitmap bmp = drawable2Bitmap(mContext.getResources().getDrawable(R.drawable.logo));
        File f = new File(newPath + "logo" + ".png");
        if(!f.exists()){
        	f.createNewFile();
        }        
	    FileOutputStream fOut = null;    
	    try {
	    	fOut = new FileOutputStream(f); 
	        bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
	        fOut.flush();
	        fOut.close();
	    } catch (IOException e) {
	            e.printStackTrace();
	    }
	   
	}
	//附加drawable2Bitmap方法
	private static Bitmap drawable2Bitmap(Drawable d){
	    int width=d.getIntrinsicWidth();
	    int height=d.getIntrinsicHeight();
	    Bitmap bitmap=Bitmap.createBitmap(width,height,d.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
	            : Bitmap.Config.RGB_565);
	    Canvas canvas=new Canvas(bitmap);
	    d.setBounds(0, 0, width, height);
	    d.draw(canvas);
	    return bitmap;
	}
	public String getLogoPath(){
		try {
			saveMyBitmap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newPath+"logo.png";
	}
}
