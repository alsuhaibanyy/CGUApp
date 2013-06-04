package cgu.edu.ist380.alsuhaibanyy.alghosona;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class DisplayCGUWebsite extends Activity{
	
	private WebView wvCGUWebsite;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgu_website); 
        
        wvCGUWebsite = (WebView) findViewById(R.id.webView_CGUWebsite);
        wvCGUWebsite.getSettings().setJavaScriptEnabled(true);
        wvCGUWebsite.loadUrl("http://www.cgu.edu");
        
    }
    
    

}
