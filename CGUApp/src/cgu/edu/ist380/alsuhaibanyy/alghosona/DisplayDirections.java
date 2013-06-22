package cgu.edu.ist380.alsuhaibanyy.alghosona;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class DisplayDirections extends Activity{
	

	
	private WebView wvGoogleDirections;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions); 
        
        wvGoogleDirections = (WebView) findViewById(R.id.webView_GoogleDirections);
        wvGoogleDirections.getSettings().setJavaScriptEnabled(true);
        wvGoogleDirections.loadUrl("https://maps.google.com/maps?q=from+34.102401,-117.714343+to+34.102175,-117.712348");
         
       
    }

}