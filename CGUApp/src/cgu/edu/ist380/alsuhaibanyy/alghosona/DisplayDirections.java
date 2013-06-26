package cgu.edu.ist380.alsuhaibanyy.alghosona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class DisplayDirections extends FragmentActivity{
	
	public GoogleMap mMap;

	
	private WebView wvGoogleDirections;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_directions); 
        

        //wvGoogleDirections = (WebView) findViewById(R.id.webView_GoogleDirections);
        //wvGoogleDirections.getSettings().setJavaScriptEnabled(true);
     //   wvGoogleDirections.loadUrl("https://maps.google.com/?q=from+34.102401,-117.714343+to+34.102175,-117.712348");
        //wvGoogleDirections.loadUrl("http://www.bing.com/maps/?FORM=HDRSC4#Y3A9MzQuMTUyNjMwfi0xMTcuNTc5MTA5Jmx2bD00JnN0eT1yJnJ0cD1wb3MuMzQuMTAyNjEyXy0xMTcuNTczODM3X1JhbmNobyUyMEN1Y2Ftb25nYSUyQyUyMENBX19fZV9+cG9zLjM0LjA5NjEwMF8tMTE3LjcxNjQwMF9DbGFyZW1vbnQlMkMlMjBDQV9fX2VfJm1vZGU9RCZydG9wPTB+MH4wfg==");
        
        //Intent i = getIntent();
       
        double dblDestinationLat = 34.102999;
        double dblDestinationLong = -117.714248;
        
        double dblSourceLat = 34.102957;
        double dblSourceLong = -117.714000;
     //   double dblSourceLat= Double.parseDouble(i.getStringExtra("SourceLat"));
       // double dblSourceLong= Double.parseDouble(i.getStringExtra("SourceLong"));
        
       // double dblDestinationLat= Double.parseDouble(i.getStringExtra("DestinationLat"));
       // double dblDestinationLong= Double.parseDouble(i.getStringExtra("DestinationLong"));
        
        
        
		final LatLng SourceLatLng = new LatLng(dblSourceLat, dblSourceLong);
		final LatLng DestinationLatLng = new LatLng(dblDestinationLat, dblDestinationLong);
	    
		String url = getDirectionsUrl(SourceLatLng, DestinationLatLng);				
		DownloadTask downloadTask = new DownloadTask();
		downloadTask.execute(url);
	      
           
         SupportMapFragment m= (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map); 
 		 mMap= m.getMap();
 		 
 		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		final LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(SourceLatLng);
		builder.include(DestinationLatLng);
		Marker mrkSourceMarker = mMap.addMarker(new MarkerOptions().position(SourceLatLng).title("My Location"));
		Marker mrkDestinationMarker = mMap.addMarker(new MarkerOptions().position(DestinationLatLng).title("Destination"));
		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override 
            public void onCameraChange(CameraPosition arg0) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),150)) ;
                mMap.setOnCameraChangeListener(null);
            }
        });
         
         //map = (WebView) findViewById(R.id.dir);
         //map.getSettings().setJavaScriptEnabled(true);
 	     //map.setWebViewClient(new WebViewClient());
 	     //map.getSettings().setJavaScriptEnabled(true);
 	     //map.getSettings().setBuiltInZoomControls(true);
 	     //map.getSettings().setSupportZoom(true);
 	     //map.loadUrl("http://maps.google.com/maps?q&saddr="+lat+","+lang+"&daddr="+h.getName());
    
	}

	
private String getDirectionsUrl(LatLng origin,LatLng dest){
		
		// Origin of route
		String str_origin = "origin="+origin.latitude+","+origin.longitude;
		
		// Destination of route
		String str_dest = "destination="+dest.latitude+","+dest.longitude;		
		
					
		// Sensor enabled
		String sensor = "sensor=false";			
					
		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
					
		// Output format
		String output = "json";
		
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		
		// https://maps.googleapis.com/maps/api/directions/json?origin=34.101944,-117.714043&destination=34.101302,-117.636514&sensor=false
		
		
		return url;
	}
	
	// A method to download json data from url 
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

	// Fetches data from url passed
		private class DownloadTask extends AsyncTask<String, Void, String>{			
					
			// Downloading data in non-ui thread
			@Override
			protected String doInBackground(String... url) {
					
				// For storing data from web service
				String data = "";
						
				try{
					// Fetching the data from web service
					data = downloadUrl(url[0]);
				}catch(Exception e){
					Log.d("Background Task",e.toString());
				}
				return data;		
			}
			
			// Executes in UI thread, after the execution of
			// doInBackground()
			@Override
			protected void onPostExecute(String result) {			
				super.onPostExecute(result);			
				
				ParserTask parserTask = new ParserTask();
				
				// Invokes the thread for parsing the JSON data
				parserTask.execute(result);
					
			}		
		}
		
		// A class to parse the Google Places in JSON format 
	    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
	    	
	    	// Parsing the data in non-ui thread    	
			@Override
			protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
				
				JSONObject jObject;	
				List<List<HashMap<String, String>>> routes = null;			           
	            
	            try{
	            	jObject = new JSONObject(jsonData[0]);
	            	DirectionsJSONParser parser = new DirectionsJSONParser();
	            	
	            	// Starts parsing data
	            	routes = parser.parse(jObject);    
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
	            return routes;
			}
			
			// Executes in UI thread, after the parsing process
			@Override
			protected void onPostExecute(List<List<HashMap<String, String>>> result) {
				ArrayList<LatLng> points = null;
				PolylineOptions lineOptions = null;
				MarkerOptions markerOptions = new MarkerOptions();
				String distance = "";
				String duration = "";
				
				
				
				if(result.size()<1){
					Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
					return;
				}
					
				
				// Traversing through all the routes
				for(int i=0;i<result.size();i++){
					points = new ArrayList<LatLng>();
					lineOptions = new PolylineOptions();
					
					// Fetching i-th route
					List<HashMap<String, String>> path = result.get(i);
					
					// Fetching all the points in i-th route
					for(int j=0;j<path.size();j++){
						HashMap<String,String> point = path.get(j);	
						
						if(j==0){	// Get distance from the list
							distance = (String)point.get("distance");						
							continue;
						}else if(j==1){ // Get duration from the list
							duration = (String)point.get("duration");
							continue;
						}
						
						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);	
						
						points.add(position);						
					}
					
					// Adding all the points in the route to LineOptions
					lineOptions.addAll(points);
					lineOptions.width(2);
					lineOptions.color(Color.RED);	
					
				}
				
				//tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
				
				// Drawing polyline in the Google Map for the i-th ro
				mMap.addPolyline(lineOptions);
				
			
				
					
			}			
         
     
    }
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

}