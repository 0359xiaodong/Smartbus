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
	 * 用MapController完成地图控制 
	 */  
	private MapController mMapController = null;  
	/** 
	 * MKMapViewListener 用于处理地图事件回调 
	 */  
	MKMapViewListener mMapListener = null;
	 /** 
     * 定位SDK的核心类 
     */  
    private LocationClient mLocClient;  
    /** 
     * 用户位置信息  
     */  
    private LocationData mLocData;  
    /** 
     * 我的位置图层 
     */  
    private LocationOverlay myLocationOverlay = null;  
    /** 
     * 弹出窗口图层 
     */  
    private PopupOverlay mPopupOverlay  = null;  
      
    private boolean isRequest = false;//是否手动触发请求定位  
    private boolean isFirstLoc = true;//是否首次定位  
      
    /** 
     * 弹出窗口图层的View 
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
             * 如果BMapManager没有初始化则初始化BMapManager
             */
            app.mBMapManager.init(new MyApplication.MyGeneralListener());
        }  
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.activity_main_map);
		mMapView = (MapView) findViewById(R.id.bmapView);  

		/** 
		 * 获取地图控制器 
		 */  
		mMapController = mMapView.getController();  
		/** 
		 *  设置地图是否响应点击事件  . 
		 */  
		mMapController.enableClick(true);  
		/** 
		 * 设置地图缩放级别 
		 */  
		mMapController.setZoom(14);  

		/** 
		 * 显示内置缩放控件 
		 */  
		mMapView.setBuiltInZoomControls(true);  

		/** 
		 * 保存精度和纬度的类, 
		 */  
		GeoPoint p = new GeoPoint((int)(36.06667 * 1E6), (int)(120.33333 * 1E6));  //青岛经纬度
		//设置p地方为中心点  
		mMapController.setCenter(p);
		mMapView.regMapViewListener(app.mBMapManager, new MKMapViewListener() {  

			/** 
			 * 地图移动完成时会回调此接口 方法 
			 */  
			@Override  
			public void onMapMoveFinish() {  
				showToast("地图移动完毕！");  
			}  

			/** 
			 * 地图加载完毕回调此接口方法 
			 */  
			@Override  
			public void onMapLoadFinish() {  
				showToast("地图载入完毕！");  
			}  

			/** 
			 *  地图完成带动画的操作（如: animationTo()）后，此回调被触发 
			 */  
			@Override  
			public void onMapAnimationFinish() {  

			}  

			/** 
			 *  当调用过 mMapView.getCurrentMap()后，此回调会被触发 
			 *  可在此保存截图至存储设备 
			 */  
			@Override  
			public void onGetCurrentMap(Bitmap arg0) {  

			}  

			/** 
			 * 点击地图上被标记的点回调此方法 
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
        
        
        //实例化定位服务，LocationClient类必须在主线程中声明
        mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(new BDLocationListenerImpl());//注册定位监听接口
		
		/**
		 * /设置定位参数
		 */
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); //打开GPRS
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000); //设置发起定位请求的间隔时间为5000ms
		option.disableCache(false);//禁止启用缓存定位
		//option.setPoiNumber(5);    //最多返回POI个数   
		//option.setPoiDistance(1000); //poi查询距离        
		//option.setPoiExtraInfo(true);  //是否需要POI的电话和地址等详细信息        
		
		mLocClient.setLocOption(option);
		mLocClient.start();  //	调用此方法开始定位
		
		//定位图层初始化
		myLocationOverlay = new LocationOverlay(mMapView);
		//设置定位数据
	    myLocationOverlay.setData(mLocData);
	    
	    myLocationOverlay.setMarker(getResources().getDrawable(R.drawable.location_arrows));
	    
	    //添加定位图层
	    mMapView.getOverlays().add(myLocationOverlay);
	    myLocationOverlay.enableCompass();
	    
	    //修改定位数据后刷新图层生效
	    mMapView.refresh();
        
	}
	
	 private class LocationOverlay extends MyLocationOverlay{  
		  
	        public LocationOverlay(MapView arg0) {  
	            super(arg0);  
	        }  
	  
	          
	        /** 
	         * 在“我的位置”坐标上处理点击事件。 
	         */  
	        @Override  
	        protected boolean dispatchTap() {  
	            //点击我的位置显示PopupOverlay  
	            showPopupOverlay(location);  
	            return super.dispatchTap();  
	        }  
	          
	    }  
	 private void showPopupOverlay(BDLocation location){
		 TextView popText = ((TextView)viewCache.findViewById(R.id.location_tips));
		 cityName=location.getCity();
		 address=location.getAddrStr();
		 popText.setText("[我的位置]\n" + location.getAddrStr());
		 mPopupOverlay.showPopup(getBitmapFromView(popText),
					new GeoPoint((int)(location.getLatitude()*1e6), (int)(location.getLongitude()*1e6)),
					10);
	}
	 public class BDLocationListenerImpl implements BDLocationListener {

			/**
			 * 接收异步返回的定位结果，参数是BDLocation类型参数
			 */
			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null) {
					return;
				}
				
				MainMap.this.location = location;
				
				mLocData.latitude = location.getLatitude();
				mLocData.longitude = location.getLongitude();
				//如果不显示定位精度圈，将accuracy赋值为0即可
				mLocData.accuracy = location.getRadius();
				mLocData.direction = location.getDerect();
				
				//将定位数据设置到定位图层里
	            myLocationOverlay.setData(mLocData);
	            //更新图层数据执行刷新后生效
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
			 * 接收异步返回的POI查询结果，参数是BDLocation类型参数
			 */
			@Override
			public void onReceivePoi(BDLocation poiLocation) {
				
			}

		}
	@Override  
	protected void onResume() {  
		//MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()  
		mMapView.onResume();  
		super.onResume();  
	}  



	@Override  
	protected void onPause() {  
		//MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()  
		mMapView.onPause();  
		super.onPause();  
	}  

	@Override  
	protected void onDestroy() {  
		//MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()  
		mMapView.destroy();
		super.onDestroy();  
	}  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, 1, 0, "周边寻宝").setIcon(R.drawable.neighbor);
		menu.add(0, 4, 0, "Whats天气").setIcon(R.drawable.weather1);
		menu.add(0,5,0,"Tom相册").setIcon(R.drawable.photo1);
		menu.add(0,6,0,"我也拍照").setIcon(R.drawable.cermare1);
		menu.add(0,7,0,"退出").setIcon(R.drawable.exit1);
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
			showToast("正在定位......");
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
