package com.upc.smartbus;

import java.io.FileNotFoundException;

import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class ShowImage extends Activity {


	private ImageView image;
	private HandlePhoto hp;
	private String phinfo;
	private TextView tx_phinfo;
	private ConnectService cs;
	Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		Intent it = getIntent();// ������ˣ���дΪnew Intent(),���³�������
		phinfo = it.getStringExtra("photoName");
		setContentView(R.layout.activity_show_image);
		initView();

		try {
			setImage(phinfo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cs = new ConnectService();
		setTextView();
	}

	/*
	 * private Button.OnClickListener btnload=new Button.OnClickListener(){
	 * public void onClick(View v){
	 * 
	 * } };
	 */

	public void setTextView() {
		String[] ss = new String[4];
		String text = "";
		ss = strsplit(cs.getPhotoInfo(phinfo));
		text = "�ϴ�ʱ��:" + ss[0] + "\n" + "����ص�:" + ss[1] + "\n" + "��Ƭ����:"
				+ ss[2];
		tx_phinfo.setText(text);
	}

	public void initView() {
		image = (ImageView) findViewById(R.id.downloadphoto);
		tx_phinfo = (TextView) findViewById(R.id.txview_phinfo);
	}

	public String[] strsplit(String str) {// �����ַ���Ϊ�ַ�������
		String[] ss = new String[100];
		ss = str.split(",");
		return ss;
	}

	// ��ʾ��Ƭ
	public void setImage(String phname) throws FileNotFoundException {
		hp = new HandlePhoto();
		bitmap = hp.getBigPhoto(phname);
		if (null != bitmap) {
			image.setVisibility(View.VISIBLE);
			image.setImageBitmap(bitmap);
			image.setScaleType(ScaleType.FIT_CENTER);
		} else {
			Toast.makeText(ShowImage.this, "��Ƭ��ȡ����", Toast.LENGTH_LONG).show();
		}
	}
}
