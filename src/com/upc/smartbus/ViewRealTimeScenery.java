package com.upc.smartbus;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class ViewRealTimeScenery extends Activity {

	private HandlePhoto hp;
	private Thumbnail Tb;
	private GridView gd;
	private String cityName="";//
	private String[] photoName;
	private String SmallphotoPath="mnt/sdcard/Smartbus/small/";
	private ConnectService cs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		setContentView(R.layout.activity_view_real_time_scenery);
		cs=new ConnectService();
		initView();
		setPhotoName(cityName);
		getSmallphFromServer(cityName);
	}
	public void initView(){
		gd=(GridView)findViewById(R.id.gridview);//������ʾ����ͼ
		Tb=new Thumbnail();
	}
	public void getSmallphFromServer(String name){
		GetPhotoTask gtph=new GetPhotoTask();
		gtph.execute();
	}
	/*
	 * �ӷ�������ȡ��Ƭ�����ơ�
	 */
	public void setPhotoName(String addr){		
		photoName=getName(addr);
	}

	public void setViews() throws FileNotFoundException{
		ImageSimpleAdapter saImageItems = new ImageSimpleAdapter(this, //ûʲô����  
				getDatas(),//������Դ   
				R.layout.night_item,//night_item��XMLʵ��  

				//��̬������ImageItem��Ӧ������          
				new String[] {"ItemImage","ItemText"},   

				//ImageItem��XML�ļ������һ��ImageView,����TextView ID  
				new int[] {R.id.ItemImage,R.id.ItemText});  
		//��Ӳ�����ʾ  
		gd.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
			{ 
				System.out.println(position);
				Intent showImage=new Intent();
				showImage.putExtra("photoName", photoName[position]);
				showImage.setClass(ViewRealTimeScenery.this, ShowImage.class);
				startActivity(showImage);
			}
		});
		gd.setAdapter(saImageItems); 
	}
	public String [] getName(String addr){//���ݵص����ƻ�ȡ��Ƭ����
		hp=new HandlePhoto();
		return hp.handle_photoname(addr);
	}

	private List<Map<String,Object>> getDatas() throws FileNotFoundException {
		ArrayList<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i=0;i<photoName.length;i++){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ItemImage",Tb.getImage(SmallphotoPath+photoName[i]));
			map.put("ItemText", cs.getLcation(photoName[i]));
			list.add(map);
		}
		return list;  
	} 
	private class GetPhotoTask extends AsyncTask<Void, Void, String> {
		protected String doInBackground(Void... arg0){     // �ں�̨��������Ƭ
			String result="";
			hp=new HandlePhoto();
			try{
				if(hp.DownLoad2SDCard(photoName)){
					result="���سɹ�";	
					System.out.println(result);
				}
				else{
					result="����ʧ��";	
					System.out.println(result);
				}
				return result;
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}  

		protected void onProgressUpdate(Void... arg0) {    // ������UI�ؼ�����  
			// ��ȡ publishProgress((int)ratio)��values  
			Toast.makeText(ViewRealTimeScenery.this, "LOADING......",Toast.LENGTH_LONG).show(); 
		}  

		protected void onPostExecute(String result) {  // ������UI�ؼ�����  
			try {
				setViews();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Toast.makeText(ViewRealTimeScenery.this, result,Toast.LENGTH_LONG).show();
		}
	}

}
