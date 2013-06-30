package cgu.edu.ist380.alsuhaibanyy.alghosona;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {
ProgressBar bar;
TextView tvCurrentBuilding;
Context mContext;
double mSourceLat;
    double mSourceLong;
    
    double mDestinationLat;
    double mDestinationLong;
    Button btnShowDirections;
    
public List<String> mBuildingID = new ArrayList<String>();
public List<String> mBuildingName = new ArrayList<String>();
public List<String> mSchoolName = new ArrayList<String>();
public List<Double> mLatitude = new ArrayList<Double>();
public List<Double> mLongitude= new ArrayList<Double>();
public Spinner spBuildingName;


@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
mContext = this.getApplicationContext();
   	spBuildingName = (Spinner) findViewById(R.id.AllCGUBuildingsSpinner);


AllBuildingNetworkTask task = new AllBuildingNetworkTask(); // call service in a separate thread 
   	task.execute("http://134.173.236.80:6080/arcgis/rest/services/claremont_colleges_buildings/MapServer/1/query?where=&text=&objectIds=&time=&geometry=q&geometryType=esriGeometryPoint&inSR=4326&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=ID%2CBuilding%2CSchool%2CLat%2CLon&returnGeometry=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&f=pjson"); 


 
bar = (ProgressBar) findViewById(R.id.GPSProgressBar);
tvCurrentBuilding = (TextView) findViewById(R.id.CurrentBuildingTextView);
        bar.setVisibility(View.INVISIBLE);
        tvCurrentBuilding.setVisibility(View.INVISIBLE);
        
        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String strGPSInfo = null;
        LocationListener mlocListener = new MyLocationListener(getApplicationContext(),tvCurrentBuilding,bar, mSourceLat, mSourceLong);
  
    mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
       bar.setVisibility(View.VISIBLE);
       tvCurrentBuilding.setVisibility(View.VISIBLE);
tvCurrentBuilding.setText(strGPSInfo);
Button btnCGUMap = (Button) findViewById(R.id.Button_Location);
Button btnBuildingInfo = (Button) findViewById(R.id.Button_BuildingInfo);
Button btnCGUWebsite = (Button) findViewById(R.id.Button_CGUWebsite);
btnShowDirections = (Button) findViewById(R.id.ShowDirectionsButton);
btnShowDirections.setEnabled(false);
btnCGUMap.setOnClickListener(new OnClickListener() {
          public void onClick(View v) {
              Intent i = new Intent();
              i.setClassName("cgu.edu.ist380.alsuhaibanyy.alghosona", "cgu.edu.ist380.alsuhaibanyy.alghosona.DisplayMap");     
              startActivity(i); }});
btnBuildingInfo.setOnClickListener(new OnClickListener() {
          public void onClick(View v) {
              Intent i = new Intent();
              i.setClassName("cgu.edu.ist380.alsuhaibanyy.alghosona", "cgu.edu.ist380.alsuhaibanyy.alghosona.DisplayBuildingInfo");     
              startActivity(i); }});
btnCGUWebsite.setOnClickListener(new OnClickListener() {
          public void onClick(View v) {
              Intent i = new Intent();
              i.setClassName("cgu.edu.ist380.alsuhaibanyy.alghosona", "cgu.edu.ist380.alsuhaibanyy.alghosona.DisplayCGUWebsite");     
              startActivity(i); }});
btnShowDirections.setOnClickListener(new OnClickListener() {
          public void onClick(View v) {
              
          int p=0;
       	boolean found = false;
       	while(p < mBuildingName.size() && !found)
       	{
       	 String a = mBuildingName.get(p).trim();
           String b = spBuildingName.getSelectedItem().toString().trim();
   
           if(a.equals(b)) found = true; else p++;
           
       	}
       
       	if(found)
       	{
              Intent i = new Intent();
              i.setClassName("cgu.edu.ist380.alsuhaibanyy.alghosona", "cgu.edu.ist380.alsuhaibanyy.alghosona.DisplayDirections");

    
              mDestinationLat= mLatitude.get(p);
              mDestinationLong= mLongitude.get(p);
     
             // mSourceLat = 34.102326;
              //mSourceLong = -117.714375;
              

    i.putExtra("SourceLat", Double.toString(mSourceLat));
    i.putExtra("SourceLong", Double.toString(mSourceLong));
    i.putExtra("DestinationLat", Double.toString(mDestinationLat));
    i.putExtra("DestinationLong", Double.toString(mDestinationLong));
    i.putExtra("BuildingName", mBuildingName.get(p));
    i.putExtra("SchoolName", mSchoolName.get(p));

    startActivity(i); }}});	               
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
getMenuInflater().inflate(R.menu.main, menu);
return true;
}

//}

/* Class My Location Listener */

class MyLocationListener implements LocationListener{
    TextView mCurrentBuilding;
ProgressBar bar;
Context context;
 
 
public MyLocationListener (Context context,TextView tvCurrentBuilding,ProgressBar bar, Double dblSourceLat, Double dblSourceLong)
{
this.context = context;
mCurrentBuilding = tvCurrentBuilding;
this.bar = bar;
}
 

@Override

public void onLocationChanged(Location loc)

{

mSourceLat = loc.getLatitude();

mSourceLong = loc.getLongitude();


NetworkTask task = new NetworkTask(); // call service in a separate thread 
task.execute("http://134.173.236.80:6080/arcgis/rest/services/claremont_colleges_buildings/MapServer/1/query?where=&text=&objectIds=&time=&geometry=" + loc.getLongitude() + "%2C" + loc.getLatitude() + "&geometryType=esriGeometryPoint&inSR=4326&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=Building%2CSchool%2CLat%2CLon&returnGeometry=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&f=pjson"); 
// URL to Brian's service


//String Text = "My current location is: " +
//"\nLatitud = " + loc.getLatitude() +
//"\nLongitud = " + loc.getLongitude();

// show location to user
bar.setVisibility(View.INVISIBLE);
btnShowDirections.setEnabled(true);

//loading.setVisibility(View.INVISIBLE);

//mCurrentBuilding.setText(Text);

}


@Override

public void onProviderDisabled(String provider)

{

Toast.makeText( context,

"Gps Disabled",

Toast.LENGTH_SHORT ).show();

}


@Override

public void onProviderEnabled(String provider)

{

Toast.makeText( context,"Gps Enabled",

Toast.LENGTH_SHORT).show();

}


@Override

public void onStatusChanged(String provider, int status, Bundle extras)

{




}
} /* End of Class MyLocationListener */

private class AllBuildingNetworkTask extends AsyncTask<String, Integer, String[]> {

    @Override
    protected void onPostExecute(String result[]) {
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, result);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spBuildingName.setAdapter(dataAdapter); // link the result to the list view
        
       // Log.d("Result",result[0]); 
    
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
 


private class NetworkTask extends AsyncTask<String, Integer, String[]> {

    @Override
    protected void onPostExecute(String result[]) {
         
    //Display 
    if(result.length > 0)
    tvCurrentBuilding.setText(result[0]);
    else
    tvCurrentBuilding.setText("You are not at any of CGU buildings");
   // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.feature,result);
       // lv.setAdapter(adapter); // link the result to the list view
        
       // Log.d("Result",result[0]); 
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
result= new String [n];
 
for(int i=0;i<n ; i++)
{ 
Log.d("JSON", fields.getJSONObject(i).getJSONObject("attributes").toString());
// format list view as  Name + wait + distance
String strBuildingName = fields.getJSONObject(i).getJSONObject("attributes").getString("Building") +"\n";  // get the Building name

result[i] = strBuildingName;
}
       }
       }
       catch(Exception e)
       {
       	e.printStackTrace();
       }
return result;
       
       
}
 
}

}/* End of UseGps Activity */
