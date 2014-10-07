package com.upc.smartbus;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
public class PoiSearch extends Activity {
	private MapView mMapView = null; 
	//private MapController mMapController = null; 
	private MKSearch mMKSearch=null;
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private String str_city;
    private int load_Index;
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
        Intent it=getIntent();
        str_city=it.getStringExtra("cityName");
        //System.out.println(str_city);
		setContentView(R.layout.activity_poi_search);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.setTraffic(true);
		initMapView();
		mMKSearch = new MKSearch();
		mMKSearch.init(app.mBMapManager, new MKSearchListener(){

			@Override
			public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
				// TODO Auto-generated method stub
				if (error != 0) {
                    Toast.makeText(PoiSearch.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PoiSearch.this, "成功，查看详情页面", Toast.LENGTH_SHORT).show();
                }
			}

			@Override
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// TODO Auto-generated method stub
				if (error != 0 || res == null) {
                    Toast.makeText(PoiSearch.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
                    return;
                }
                // 将地图移动到第一个POI中心点
                if (res.getCurrentNumPois() > 0) {
                    // 将poi结果显示到地图上
                    MyPoiOverlay poiOverlay = new MyPoiOverlay(PoiSearch.this, mMapView, mMKSearch);
                    poiOverlay.setData(res.getAllPoi());
                    mMapView.getOverlays().clear();
                    mMapView.getOverlays().add(poiOverlay);
                    mMapView.refresh();
                    //当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
                    for( MKPoiInfo info : res.getAllPoi() ){
                    	if ( info.pt != null ){
                    		mMapView.getController().animateTo(info.pt);
                    		break;
                    	}
                    }
                } else if (res.getCityListNum() > 0) {
                	//当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                    String strInfo = "在";
                    for (int i = 0; i < res.getCityListNum(); i++) {
                        strInfo += res.getCityListInfo(i).city;
                        strInfo += ",";
                    }
                    strInfo += "找到结果";
                    Toast.makeText(PoiSearch.this, strInfo, Toast.LENGTH_LONG).show();
                }
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
					int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				// TODO Auto-generated method stub
				if ( res == null || res.getAllSuggestions() == null){
            		return ;
            	}
            	sugAdapter.clear();
            	for ( MKSuggestionInfo info : res.getAllSuggestions()){
            		if ( info.key != null)
            		    sugAdapter.add(info.key);
            	}
            	sugAdapter.notifyDataSetChanged();
			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub
				
			}
			
		});
		 keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
	     sugAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
	     keyWorldsView.setAdapter(sugAdapter);
	     keyWorldsView.addTextChangedListener(new TextWatcher(){

				@Override
				public void afterTextChanged(Editable arg0) {
					
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					
				}
				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2,
						int arg3) {
					 if ( cs.length() <=0 ){
						 return ;
					 }
					 String city =  ((EditText)findViewById(R.id.city)).getText().toString();
					 /**
					  * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
					  */
	                 mMKSearch.suggestionSearch(cs.toString(), city);				
				}
	        });
		
		
	}
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		mMKSearch.destory();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}
	@Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
    
    private void initMapView() {
        mMapView.setLongClickable(true);
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);
    }
    /**
     * 影响搜索按钮点击事件
     * @param v
     */
    public void searchButtonProcess(View v) {
          EditText editCity = (EditText)findViewById(R.id.city);
          EditText editSearchKey = (EditText)findViewById(R.id.searchkey);
          editCity.setText(str_city);
          mMKSearch.poiSearchInCity(editCity.getText().toString(), 
                  editSearchKey.getText().toString());
    }
   public void goToNextPage(View v) {
        //搜索下一组poi
        int flag = mMKSearch.goToPoiPage(++load_Index);
        if (flag != 0) {
            Toast.makeText(PoiSearch.this, "先搜索开始，然后再搜索下一组数据", Toast.LENGTH_SHORT).show();
        }
    }

	

}
