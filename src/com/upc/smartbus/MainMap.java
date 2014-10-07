package com.upc.smartbus;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;  
import com.baidu.location.BDLocationListener;  
import com.baidu.location.LocationClient;  
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;  
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;  
import com.baidu.mapapi.map.MapController;  
import com.baidu.mapapi.map.MyLocationOverlay;  
import com.baidu.mapapi.map.MapPoi;  
import com.baidu.mapapi.map.MapView;  
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint; 
public class MainMap extends Activity {

	private Toast mToast;
	private String cityName;
	private String address;
	//private BMapManager mBMapManager; 
	private MapView mMapView = null;  
	/** 
	 * ��MapController��ɵ�ͼ���� 
	 */  
	private MapController mMapController = null;  
	/** 
	 * MKMapViewListener ���ڴ����ͼ�¼��ص� 
	 */  
	MKMapViewListener mMapListener = null;
	 /** 
     * ��λSDK�ĺ����� 
     */  
    private LocationClient mLocClient;  
    /** 
     * �û�λ����Ϣ  
     */  
    private LocationData mLocData;  
    /** 
     * �ҵ�λ��ͼ�� 
     */  
    private LocationOverlay myLocationOverlay = null;  
    /** 
     * ��������ͼ�� 
     */  
    private PopupOverlay mPopupOverlay  = null;  
      
    private boolean isRequest = false;//�Ƿ��ֶ���������λ  
    private boolean isFirstLoc = true;//�Ƿ��״ζ�λ  
      
    /** 
     * ��������ͼ���View 
     */  
    private View viewCache;  
    private BDLocation location; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MyApplication app = (MyApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(getApplicationContext());
            /**
             * ���BMapManagerû�г�ʼ�����ʼ��BMapManager
             */
            app.mBMapManager.init(new MyApplication.MyGeneralListener());
        }  
        requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		setContentView(R.layout.activity_main_map);
		mMapView = (MapView) findViewById(R.id.bmapView);  

		/** 
		 * ��ȡ��ͼ������ 
		 */  
		mMapController = mMapView.getController();  
		/** 
		 *  ���õ�ͼ�Ƿ���Ӧ����¼�  . 
		 */  
		mMapController.enableClick(true);  
		/** 
		 * ���õ�ͼ���ż��� 
		 */  
		mMapController.setZoom(14);  

		/** 
		 * ��ʾ�������ſؼ� 
		 */  
		mMapView.setBuiltInZoomControls(true);  

		/** 
		 * ���澫�Ⱥ�γ�ȵ���, 
		 */  
		GeoPoint p = new GeoPoint((int)(36.06667 * 1E6), (int)(120.33333 * 1E6));  //�ൺ��γ��
		//����p�ط�Ϊ���ĵ�  
		mMapController.setCenter(p);
		mMapView.regMapViewListener(app.mBMapManager, new MKMapViewListener() {  

			/** 
			 * ��ͼ�ƶ����ʱ��ص��˽ӿ� ���� 
			 */  
			@Override  
			public void onMapMoveFinish() {  
				showToast("��ͼ�ƶ���ϣ�");  
			}  

			/** 
			 * ��ͼ������ϻص��˽ӿڷ��� 
			 */  
			@Override  
			public void onMapLoadFinish() {  
				showToast("��ͼ������ϣ�");  
			}  

			/** 
			 *  ��ͼ��ɴ������Ĳ�������: animationTo()���󣬴˻ص������� 
			 */  
			@Override  
			public void onMapAnimationFinish() {  

			}  

			/** 
			 *  �����ù� mMapView.getCurrentMap()�󣬴˻ص��ᱻ���� 
			 *  ���ڴ˱����ͼ���洢�豸 
			 */  
			@Override  
			public void onGetCurrentMap(Bitmap arg0) {  

			}  

			/** 
			 * �����ͼ�ϱ���ǵĵ�ص��˷��� 
			 *  
			 */  
			@Override  
			public void onClickMapPoi(MapPoi arg0) {  
				if (arg0 != null){  
					showToast(arg0.strText);  
				}  
			}  
		}); 
		((Button) findViewById(R.id.request)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestLocation();
			}
		});
		viewCache = LayoutInflater.from(this).inflate(R.layout.pop_layout, null);
        mPopupOverlay = new PopupOverlay(mMapView ,new PopupClickListener() {
			
			@Override
			public void onClickedPopup(int arg0) {
				mPopupOverlay.hidePop();
			}
		});
        mLocData = new LocationData();
        
        
        //ʵ������λ����LocationClient����������߳�������
        mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(new BDLocationListenerImpl());//ע�ᶨλ�����ӿ�
		
		/**
		 * /���ö�λ����
		 */
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); //��GPRS
		option.setAddrType("all");//���صĶ�λ���������ַ��Ϣ
		option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		option.setScanSpan(5000); //���÷���λ����ļ��ʱ��Ϊ5000ms
		option.disableCache(false);//��ֹ���û��涨λ
		//option.setPoiNumber(5);    //��෵��POI����   
		//option.setPoiDistance(1000); //poi��ѯ����        
		//option.setPoiExtraInfo(true);  //�Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ        
		
		mLocClient.setLocOption(option);
		mLocClient.start();  //	���ô˷�����ʼ��λ
		
		//��λͼ���ʼ��
		myLocationOverlay = new LocationOverlay(mMapView);
		//���ö�λ����
	    myLocationOverlay.setData(mLocData);
	    
	    myLocationOverlay.setMarker(getResources().getDrawable(R.drawable.location_arrows));
	    
	    //��Ӷ�λͼ��
	    mMapView.getOverlays().add(myLocationOverlay);
	    myLocationOverlay.enableCompass();
	    
	    //�޸Ķ�λ���ݺ�ˢ��ͼ����Ч
	    mMapView.refresh();
        
	}
	
	 private class LocationOverlay extends MyLocationOverlay{  
		  
	        public LocationOverlay(MapView arg0) {  
	            super(arg0);  
	        }  
	  
	          
	        /** 
	         * �ڡ��ҵ�λ�á������ϴ������¼��� 
	         */  
	        @Override  
	        protected boolean dispatchTap() {  
	            //����ҵ�λ����ʾPopupOverlay  
	            showPopupOverlay(location);  
	            return super.dispatchTap();  
	        }  
	          
	    }  
	 private void showPopupOverlay(BDLocation location){
		 TextView popText = ((TextView)viewCache.findViewById(R.id.location_tips));
		 cityName=location.getCity();
		 address=location.getAddrStr();
		 popText.setText("[�ҵ�λ��]\n" + location.getAddrStr());
		 mPopupOverlay.showPopup(getBitmapFromView(popText),
					new GeoPoint((int)(location.getLatitude()*1e6), (int)(location.getLongitude()*1e6)),
					10);
	}
	 public class BDLocationListenerImpl implements BDLocationListener {

			/**
			 * �����첽���صĶ�λ�����������BDLocation���Ͳ���
			 */
			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null) {
					return;
				}
				
				MainMap.this.location = location;
				
				mLocData.latitude = location.getLatitude();
				mLocData.longitude = location.getLongitude();
				//�������ʾ��λ����Ȧ����accuracy��ֵΪ0����
				mLocData.accuracy = location.getRadius();
				mLocData.direction = location.getDerect();
				
				//����λ�������õ���λͼ����
	            myLocationOverlay.setData(mLocData);
	            //����ͼ������ִ��ˢ�º���Ч
	            mMapView.refresh();
	            if(isFirstLoc || isRequest){
					mMapController.animateTo(new GeoPoint(
							(int) (location.getLatitude() * 1e6), (int) (location
									.getLongitude() * 1e6)));
					
					showPopupOverlay(location);
					
					isRequest = false;
	            }
	            
	            isFirstLoc = false;
			}

			/**
			 * �����첽���ص�POI��ѯ�����������BDLocation���Ͳ���
			 */
			@Override
			public void onReceivePoi(BDLocation poiLocation) {
				
			}

		}
	@Override  
	protected void onResume() {  
		//MapView������������Activityͬ������activity����ʱ�����MapView.onPause()  
		mMapView.onResume();  
		super.onResume();  
	}  



	@Override  
	protected void onPause() {  
		//MapView������������Activityͬ������activity����ʱ�����MapView.onPause()  
		mMapView.onPause();  
		super.onPause();  
	}  

	@Override  
	protected void onDestroy() {  
		//MapView������������Activityͬ������activity����ʱ�����MapView.destroy()  
		mMapView.destroy();
		super.onDestroy();  
	}  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, 1, 0, "�ܱ�Ѱ��").setIcon(R.drawable.neighbor);
		menu.add(0, 4, 0, "Whats����").setIcon(R.drawable.weather1);
		menu.add(0,5,0,"Tom���").setIcon(R.drawable.photo1);
		menu.add(0,6,0,"��Ҳ����").setIcon(R.drawable.cermare1);
		menu.add(0,7,0,"�˳�").setIcon(R.drawable.exit1);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		case 1:
			Intent it=new Intent();
			it.setClass(MainMap.this,PoiSearch.class);
			it.putExtra("cityName",cityName);
			startActivity(it);
			break;
		
		case 4:
			Intent it2=new Intent();
			it2.setClass(MainMap.this, MyWeather.class);
			startActivity(it2);
			break;
		case 5:
			Intent it1=new Intent();
			it1.setClass(MainMap.this, ViewRealTimeScenery.class);
			it1.putExtra("city", cityName);
			startActivity(it1);
			break;
		case 6:
			Intent it21=new Intent();
			it21.setClass(MainMap.this, UpLoadingPhoto.class);
			it21.putExtra("address",address);
			startActivity(it21);

			break;
		case 7:
			finish();
			break;
		}
		return true;
	}
	public void requestLocation() {
		isRequest = true;
		
		if(mLocClient != null && mLocClient.isStarted()){
			showToast("���ڶ�λ......");
			mLocClient.requestLocation();
		}else{
			Log.d("LocSDK4", "locClient is null or not started");
		}
		
	}
	private void showToast(String msg){    
		if(mToast == null){    
			mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);    
		}else{    
			mToast.setText(msg);    
			mToast.setDuration(Toast.LENGTH_SHORT);  
		}    
		mToast.show();    
	} 
	public static Bitmap getBitmapFromView(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
	}

}
