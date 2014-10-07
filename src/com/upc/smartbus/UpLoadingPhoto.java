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
 * 这个activity用拍照并显示于界面，在用户填写相关信息后就上传，上传过程用AsyncTask处理。
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
	private String mylocation="";//获取定位后的具体定位信息，某某省/某某市/某某区/某某县/某某街道or某某路
	private TextView locCity;
	public final static int CAMERA_RESULT=8888;
	public final static String TAG="xx";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent it=getIntent();
		mylocation=it.getStringExtra("address");
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.activity_up_loading_photo);
		initView();
		locCity.setText(mylocation);
	}

	//初始化工作
	public void initView(){
		locCity=(TextView)findViewById(R.id.location_city);//用来显示所在城市
		myview=(ImageView)findViewById(R.id.myphoto);
		btn_loadphoto=(Button)findViewById(R.id.uploadphoto);//上传按钮
		TakePhoto=(Button)findViewById(R.id.startTake);//拍照动作按钮
		user_des=(EditText)findViewById(R.id.user_des);//用户描述编辑框
		btn_loadphoto.setOnClickListener(btnload);//上传响应事件
		TakePhoto.setOnClickListener(btntake);//拍照响应事件
	}
	//定位实现
	


	//上传照片
	private Button.OnClickListener btnload=new Button.OnClickListener(){
		public void onClick(View v){
			uploadphoto();//将拍的照片从sd卡读出，编码后调用服务器接口上传照片。
			Toast.makeText(UpLoadingPhoto.this, load_result, Toast.LENGTH_SHORT).show();
		}
	};


	//拍摄照片
	private Button.OnClickListener btntake=new Button.OnClickListener(){
		public void onClick(View v){
			mPhotoPath="mnt/sdcard/Smartbus/"+getPhotoFileName();
			TakeMyPhoto();//载入拍照界面,进行拍照，并存入sd卡
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//拍照完成后显示在界面上
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
	public void uploadphoto(){//将拍的照片通过Base64编码上传到服务器
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
					ByteArrayOutputStream baos=new ByteArrayOutputStream();//字节数组输出流作为目的地。
					byte [] buffer=new byte[8196];
					int count=0;
					int len=0;
					long totalsize=fis.available();//文件总的大小
					while((count=fis.read(buffer))>=0){
						baos.write(buffer,0,count);
						len+=count;
						if(len > 0) {  
							float ratio = len/(float)totalsize * 100;   // 计算下载百分比  
							publishProgress((int)ratio);    // android.os.AsyncTask.publishProgress(Integer... values)  
						}  
						Thread.sleep(100);
					}
					String uploadBuffer=new String(Base64.encodeBase64(baos.toByteArray()));
					load_result=cs.uploadPhoto(getPhotoFileName(), uploadBuffer,mylocation,user_des.getText().toString());//调用webservice方法，上传照片
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
				return "还未拍摄照片!";
			}
			return null;
		}  
		@Override  
		protected void onProgressUpdate(Integer... values) {    // 可以与UI控件交互  
			// 获取 publishProgress((int)ratio)的values  
			proDialog.setProgress(values[0]);  
		}  

		@Override  
		protected void onPostExecute(String result) { // 可以与UI控件交互  
			Toast.makeText(UpLoadingPhoto.this, result, Toast.LENGTH_LONG).show();
			if(bitmap!=null||bitmap.isRecycled()){
				bitmap.recycle();//上传后回收。
			}
			proDialog.dismiss();  
		}  
	}
}
