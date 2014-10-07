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
             * ���BMapManagerû�г�ʼ�����ʼ��BMapManager
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
                    Toast.makeText(PoiSearch.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PoiSearch.this, "�ɹ����鿴����ҳ��", Toast.LENGTH_SHORT).show();
                }
			}

			@Override
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// TODO Auto-generated method stub
				if (error != 0 || res == null) {
                    Toast.makeText(PoiSearch.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
                    return;
                }
                // ����ͼ�ƶ�����һ��POI���ĵ�
                if (res.getCurrentNumPois() > 0) {
                    // ��poi�����ʾ����ͼ��
                    MyPoiOverlay poiOverlay = new MyPoiOverlay(PoiSearch.this, mMapView, mMKSearch);
                    poiOverlay.setData(res.getAllPoi());
                    mMapView.getOverlays().clear();
                    mMapView.getOverlays().add(poiOverlay);
                    mMapView.refresh();
                    //��ePoiTypeΪ2��������·����4��������·��ʱ�� poi����Ϊ��
                    for( MKPoiInfo info : res.getAllPoi() ){
                    	if ( info.pt != null ){
                    		mMapView.getController().animateTo(info.pt);
                    		break;
                    	}
                    }
                } else if (res.getCityListNum() > 0) {
                	//������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
                    String strInfo = "��";
                    for (int i = 0; i < res.getCityListNum(); i++) {
                        strInfo += res.getCityListInfo(i).city;
                        strInfo += ",";
                    }
                    strInfo += "�ҵ����";
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
					  * ʹ�ý������������ȡ�����б������onSuggestionResult()�и���
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
     * Ӱ��������ť����¼�
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
        //������һ��poi
        int flag = mMKSearch.goToPoiPage(++load_Index);
        if (flag != 0) {
            Toast.makeText(PoiSearch.this, "��������ʼ��Ȼ����������һ������", Toast.LENGTH_SHORT).show();
        }
    }

	

}
