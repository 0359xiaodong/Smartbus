package com.upc.smartbus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

/*
 * ���activity�����ղ���ʾ�ڽ��棬���û���д�����Ϣ����ϴ����ϴ�������AsyncTask����
 */
public class UpLoadingPhoto extends Activity {
	//private ProgressBar progressBar;
	private String mPhotoPath;
	private ImageView myview;
	private File mPhotoFile;
	private Button btn_loadphoto;
	private Button TakePhoto;
	private Bitmap bitmap;
	private String load_result;
	private EditText user_des;
	private String mylocation="";//��ȡ��λ��ľ��嶨λ��Ϣ��ĳĳʡ/ĳĳ��/ĳĳ��/ĳĳ��/ĳĳ�ֵ�orĳĳ·
	private TextView locCity;
	public final static int CAMERA_RESULT=8888;
	public final static String TAG="xx";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent it=getIntent();
		mylocation=it.getStringExtra("address");
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		setContentView(R.layout.activity_up_loading_photo);
		initView();
		locCity.setText(mylocation);
	}

	//��ʼ������
	public void initView(){
		locCity=(TextView)findViewById(R.id.location_city);//������ʾ���ڳ���
		myview=(ImageView)findViewById(R.id.myphoto);
		btn_loadphoto=(Button)findViewById(R.id.uploadphoto);//�ϴ���ť
		TakePhoto=(Button)findViewById(R.id.startTake);//���ն�����ť
		user_des=(EditText)findViewById(R.id.user_des);//�û������༭��
		btn_loadphoto.setOnClickListener(btnload);//�ϴ���Ӧ�¼�
		TakePhoto.setOnClickListener(btntake);//������Ӧ�¼�
	}
	//��λʵ��
	


	//�ϴ���Ƭ
	private Button.OnClickListener btnload=new Button.OnClickListener(){
		public void onClick(View v){
			uploadphoto();//���ĵ���Ƭ��sd���������������÷������ӿ��ϴ���Ƭ��
			Toast.makeText(UpLoadingPhoto.this, load_result, Toast.LENGTH_SHORT).show();
		}
	};


	//������Ƭ
	private Button.OnClickListener btntake=new Button.OnClickListener(){
		public void onClick(View v){
			mPhotoPath="mnt/sdcard/Smartbus/"+getPhotoFileName();
			TakeMyPhoto();//�������ս���,�������գ�������sd��
		}
	};
	public void TakeMyPhoto(){
		try {  
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); 
			mPhotoFile = new File(mPhotoPath);  
			if (!mPhotoFile.exists()) {  
				mPhotoFile.createNewFile(); 
				Toast.makeText(UpLoadingPhoto.this, mPhotoPath, Toast.LENGTH_LONG).show();
			}  
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));  
			startActivityForResult(intent,CAMERA_RESULT);
		} catch (Exception e) { 
			Toast.makeText(UpLoadingPhoto.this, "xianshi cuowu", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	@SuppressLint("SimpleDateFormat")
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");  
		return dateFormat.format(date) + ".jpg"; 
	} 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//������ɺ���ʾ�ڽ�����
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==CAMERA_RESULT&&resultCode==Activity.RESULT_OK) {
			bitmap = BitmapFactory.decodeFile(mPhotoPath);
			try{
				if(null != bitmap ){  
					myview.setVisibility(View.VISIBLE);
					myview.setImageBitmap(bitmap);
					myview.setScaleType(ScaleType.FIT_CENTER);
				}
			}
			catch ( Exception e ){
				e.printStackTrace();
			}
		}

	}
	public void uploadphoto(){//���ĵ���Ƭͨ��Base64�����ϴ���������
		UploadTask ut= new UploadTask(this);
		ut.execute();
	}
	public  class UploadTask extends AsyncTask<Void, Integer, String> {  
		private static final int BUTTON_NEGATIVE =-2;
		ProgressDialog proDialog;
		public UploadTask(Context context) {  
			proDialog = new ProgressDialog(context, 0);  
			proDialog.setButton(BUTTON_NEGATIVE,"cancel", new DialogInterface.OnClickListener() {  
				@Override  
				public void onClick(DialogInterface dialog, int which) {  
					dialog.cancel();  
				}  
			});  
			proDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {  
				@Override  
				public void onCancel(DialogInterface dialog) {  
					finish();  
				}  
			});  
			proDialog.setCancelable(true);  
			proDialog.setMax(100);  
			proDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
			proDialog.show();  
		} 

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			//System.out.println(mPhotoPath);
			if(mPhotoPath!=null||mPhotoPath.length()>0){
				ConnectService cs=new ConnectService();
				try{
					FileInputStream fis=new FileInputStream(mPhotoPath);
					System.out.println(mPhotoPath);
					ByteArrayOutputStream baos=new ByteArrayOutputStream();//�ֽ������������ΪĿ�ĵء�
					byte [] buffer=new byte[8196];
					int count=0;
					int len=0;
					long totalsize=fis.available();//�ļ��ܵĴ�С
					while((count=fis.read(buffer))>=0){
						baos.write(buffer,0,count);
						len+=count;
						if(len > 0) {  
							float ratio = len/(float)totalsize * 100;   // �������ذٷֱ�  
							publishProgress((int)ratio);    // android.os.AsyncTask.publishProgress(Integer... values)  
						}  
						Thread.sleep(100);
					}
					String uploadBuffer=new String(Base64.encodeBase64(baos.toByteArray()));
					load_result=cs.uploadPhoto(getPhotoFileName(), uploadBuffer,mylocation,user_des.getText().toString());//����webservice�������ϴ���Ƭ
					fis.close();
					return load_result;
				}
				catch(FileNotFoundException e){
					e.printStackTrace();
				}
				catch(IOException e){
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				return "��δ������Ƭ!";
			}
			return null;
		}  
		@Override  
		protected void onProgressUpdate(Integer... values) {    // ������UI�ؼ�����  
			// ��ȡ publishProgress((int)ratio)��values  
			proDialog.setProgress(values[0]);  
		}  

		@Override  
		protected void onPostExecute(String result) { // ������UI�ؼ�����  
			Toast.makeText(UpLoadingPhoto.this, result, Toast.LENGTH_LONG).show();
			if(bitmap!=null||bitmap.isRecycled()){
				bitmap.recycle();//�ϴ�����ա�
			}
			proDialog.dismiss();  
		}  
	}
}
