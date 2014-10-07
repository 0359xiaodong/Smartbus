package com.upc.smartbus;

import android.app.Activity;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKSearch;

public class MyPoiOverlay extends PoiOverlay {
    
    MKSearch mSearch;

    public MyPoiOverlay(Activity activity, MapView mapView, MKSearch search) {
        super(activity, mapView);
        mSearch = search;
    }

    @Override
    protected boolean onTap(int i) {
        super.onTap(i);
        MKPoiInfo info = getPoi(i);
        if (info.hasCaterDetails) {
            mSearch.poiDetailSearch(info.uid);
        }
        return true;
    }

    
}
