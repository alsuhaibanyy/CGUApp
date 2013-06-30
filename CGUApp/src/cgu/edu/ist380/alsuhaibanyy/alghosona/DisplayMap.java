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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;



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

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class DisplayMap extends FragmentActivity {
	public GoogleMap mMap;
	public List<String> mBuildingID = new ArrayList<String>();
	public List<String> mBuildingName = new ArrayList<String>();
	public List<String> mSchoolName = new ArrayList<String>();
	public List<Double> mLatitude = new ArrayList<Double>();
	public List<Double> mLongitude= new ArrayList<Double>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_cgu_map);
        
        
        SupportMapFragment m= (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.cgumap); 

     	 mMap= m.getMap();

     	
    	 mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


       AllBuildingNetworkTask task = new AllBuildingNetworkTask(); // call service in a separate thread 
     	task.execute("http://134.173.236.80:6080/arcgis/rest/services/claremont_colleges_buildings/MapServer/1/query?where=&text=&objectIds=&time=&geometry=q&geometryType=esriGeometryPoint&inSR=4326&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=ID%2CBuilding%2CSchool%2CLat%2CLon&returnGeometry=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&f=pjson"); 


       
    }

    private class AllBuildingNetworkTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected void onPostExecute(String result[]) {
        	
         	final LatLngBounds.Builder builder = new LatLngBounds.Builder();


         	for(int i=0; i<mBuildingID.size();i++)
         	{
         	 final LatLng BuildingLatLng = new LatLng(mLatitude.get(i), mLongitude.get(i));
             
                       
         	  

              builder.include(BuildingLatLng);
              Marker mrkDestinationMarker = mMap.addMarker(new MarkerOptions().position(BuildingLatLng).title(mBuildingName.get(i)).snippet(mSchoolName.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.cgulogo)));
             // Marker mrkDestinationMarker = mMap.addMarker(new MarkerOptions().position(BuildingLatLng).title("A").snippet("B").icon(BitmapDescriptorFactory.fromResource(R.drawable.cgulogo)));

         	}

       	 mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),50)) ;

  
              mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
              @Override 
                          public void onCameraChange(CameraPosition arg0) {
                              mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),100)) ;
                              mMap.setOnCameraChangeListener(null);
                          }
                      });
         

        }

    @Override
    protected String[] doInBackground(String... urls) {
    // TODO Auto-generated method stub
    String  result [] = null;
           HttpGet request = new HttpGet(urls[0]); // create an http get method to the service URL
           HttpClient httpclient = new DefaultHttpClient();  // http client
           HttpResponse httpResponse; // response from the service
           StringBuilder builder = new StringBuilder();
           try 
           {
           
            // Call 
           	httpResponse = (HttpResponse)httpclient.execute(request); // execute get method
           	Log.d("Status", httpResponse.getStatusLine().getStatusCode()+""); // get the status if 200 then OK 
           	if(httpResponse.getStatusLine().getStatusCode() == 200)
           	{
           	 HttpEntity entity = httpResponse.getEntity();
    InputStream content = entity.getContent();
    BufferedReader reader = new BufferedReader(
    new InputStreamReader(content));    // get response content from the service
    String line;
    while ((line = reader.readLine()) != null) {
    builder.append(line);
    }
    // Parse
    JSONObject  json= new JSONObject(builder.toString());   // parse content to JSON object 
    JSONArray fields = json.getJSONArray("features");   // get the incident array from the JSON object 
    int n = fields.length();
    for(int i=0;i<n ; i++)
    { 
    Log.d("JSON", fields.getJSONObject(i).getJSONObject("attributes").toString());
    // format list view as  Name + wait + distance
    String strBuildingName = fields.getJSONObject(i).getJSONObject("attributes").getString("Building") +"\n";
    if(strBuildingName.trim().length() != 0)
    {
    mBuildingID.add(fields.getJSONObject(i).getJSONObject("attributes").getString("ID").trim());  // get the Building name    
    mBuildingName.add(fields.getJSONObject(i).getJSONObject("attributes").getString("Building").trim());  // get the Building name    
    mSchoolName.add(fields.getJSONObject(i).getJSONObject("attributes").getString("School").trim());  // get the Building name    
    mLatitude.add(Double.parseDouble(fields.getJSONObject(i).getJSONObject("attributes").getString("Lat")));  // get the Building name    
    mLongitude.add(Double.parseDouble(fields.getJSONObject(i).getJSONObject("attributes").getString("Lon")));  // get the Building name    
    }
    }
    result= new String [mBuildingName.size()];
    for(int i = 0; i < mBuildingName.size(); i++) result[i]=mBuildingName.get(i);
           }
           }
           catch(Exception e)
           {
           	e.printStackTrace();
           }
           //result=(String[]) mBuildingName.toArray();
           
    return result;
           
           
    }
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
