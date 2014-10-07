package com.upc.smartbus;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Thumbnail {
	//下面这个方法返回缩略图
	public Bitmap getImageThumbnail(String imagePath) throws FileNotFoundException {
		Bitmap bitmap = null;
		InputStream is=null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds =false;  
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inInputShareable=true;
		options.inPurgeable=true;//设置图片可以被回收
		// 获取这个图片的宽和高，注意此处的bitmap为null  
		options.inSampleSize =4;
		is =getInputStream(imagePath);
		bitmap = BitmapFactory.decodeStream(is, null, options); 
		return bitmap;
	}
	public Bitmap getImage(String image_path){
		Bitmap bitmap = null;
		InputStream is=null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds =false;  
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inInputShareable=true;
		options.inPurgeable=true;//设置图片可以被回收
		// 获取这个图片的宽和高，注意此处的bitmap为null  
		options.inSampleSize =4;
		try {
			is =new FileInputStream(image_path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bitmap = BitmapFactory.decodeStream(is, null, options); 
		return bitmap;
	}
	public InputStream getInputStream(String path){
		URL url=null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   //设置URL
		HttpURLConnection con=null;
		try {
			con = (HttpURLConnection)url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  //打开连接		          
		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //设置请求方法		
		//设置连接超时时间为5s	
		con.setConnectTimeout(5000);		
		InputStream in=null;
		try {
			in = con.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  //取得字节输入流	
		return in;
	}

}
