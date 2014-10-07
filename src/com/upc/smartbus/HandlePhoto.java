package com.upc.smartbus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;

public class HandlePhoto {
	/*
	 * ������еķ���������������Ƭ
	 */
	private ConnectService cs;//����webservice,��ȡ��Ƭ���ƣ�������Ƭ
	private Thumbnail tb;//��ȡ��Ƭ����ͼ��
	private Bitmap bitmap;
	private String bigphotourl="http://192.168.191.1:8080/axis2/images/";
	private String SmallphotoPath="mnt/sdcard/Smartbus/small/";


	//�ж�sd�����Ƿ��Ѿ�������Ƭ�������������ء�
	public boolean fileExist(String file_absolutePath){
		try{
			File myfile=new File(file_absolutePath);
			if(!myfile.exists())//��Ƭ�����ڷ���false�����ڷ���true
				return false;
		}catch(Exception e){
			return false;
		}
		return true;
	}
	/*******************��ȡ��Ƭ���ƣ������ַ���������ʽ����*********************/
	public String[] handle_photoname(String cityName){
		cs=new ConnectService();
		return namesplit(cs.getPhotoName(cityName));
	}
	public String[] namesplit(String name){//�����ַ���Ϊ�ַ�������
		int count=0;
		//ͳ����Ƭ���Ƹ���
		for(int k=0;k<name.length();k++){
			if(name.charAt(k)==',')
				count++;
		}
		String []ss=new String[count];
		ss=name.split(",");
		return ss;
	}
	/********************������Ƭ��sd��***************************************/
	//��������ͼ�ķ���.
	public boolean DownLoad2SDCard(String []photoName){
		cs=new ConnectService();
		FileOutputStream fos=null;
		String []image=new String[photoName.length];
		System.out.println(photoName.length);
		for (int i=0;i<photoName.length;i++){
			if(!fileExist(SmallphotoPath+photoName[i])){
				image[i]=cs.downloadphoto(photoName[i],"downloadSmallphoto");
				try{		
					byte [] buffer=android.util.Base64.decode(image[i], android.util.Base64.DEFAULT);
					File destdir=new File(SmallphotoPath);
					if(!destdir.exists())destdir.mkdir();
					fos = new FileOutputStream(new File(destdir,photoName[i]));//����ͼƬ
					fos.write(buffer);
					image[i]=null;
					System.gc();//�����ڴ����
					fos.flush();
					fos.close();
				}
				catch(Exception e){
					e.printStackTrace();
					return false;
				}
			}
			else{
				System.out.println("error___error");
			}
		}
		return true;
	}
	/****************��ȡsd������Ƭ������ͼ*******************************************/
	//��ȡ����ͼƬ��
	public Bitmap getBigPhoto(String photoName) throws FileNotFoundException{
		tb=new Thumbnail();
		bitmap=null;
		bitmap=tb.getImageThumbnail(bigphotourl+photoName);
		return bitmap;
	}
}
