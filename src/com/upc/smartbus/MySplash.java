package com.upc.smartbus;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;

public class MySplash extends Activity {

	  private final int SPLASH_DISPLAY_LENGHT = 6000; // —”≥Ÿ¡˘√Î  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//»•µÙ±ÍÃ‚¿∏
		setContentView(R.layout.activity_my_splash);
		 new Handler().postDelayed(new Runnable() {  
	            public void run() {  
	                Intent mainIntent = new Intent(MySplash.this,  
	                        MainMap.class);  
	                MySplash.this.startActivity(mainIntent);  
	                MySplash.this.finish();  
	            }  
	  
	        }, SPLASH_DISPLAY_LENGHT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_splash, menu);
		return true;
	}

}
