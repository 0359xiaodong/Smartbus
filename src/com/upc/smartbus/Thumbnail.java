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
	//�������������������ͼ
	public Bitmap getImageThumbnail(String imagePath) throws FileNotFoundException {
		Bitmap bitmap = null;
		InputStream is=null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds =false;  
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inInputShareable=true;
		options.inPurgeable=true;//����ͼƬ���Ա�����
		// ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull  
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
		options.inPurgeable=true;//����ͼƬ���Ա�����
		// ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull  
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
		}   //����URL
		HttpURLConnection con=null;
		try {
			con = (HttpURLConnection)url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  //������		          
		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //�������󷽷�		
		//�������ӳ�ʱʱ��Ϊ5s	
		con.setConnectTimeout(5000);		
		InputStream in=null;
		try {
			in = con.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  //ȡ���ֽ�������	
		return in;
	}

}
