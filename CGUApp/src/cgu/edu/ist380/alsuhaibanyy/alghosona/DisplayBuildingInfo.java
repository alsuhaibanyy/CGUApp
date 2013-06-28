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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayBuildingInfo extends Activity implements OnItemSelectedListener{
	public TextView tvBuildingID;
	public Spinner spBuildingName;
	public List<String> mBuildingID = new ArrayList<String>();
	public List<String> mBuildingName = new ArrayList<String>();
	public List<String> mSchoolName = new ArrayList<String>();
	public List<Double> mLatitude = new ArrayList<Double>();
	public List<Double> mLongitude= new ArrayList<Double>();
	public TextView tvSchoolName;
	
	
	private String[] items={"Android","Bluetooth","Chrome","Docs","Email",
        "Facebook","Google","Hungary","Iphone","Korea","Machintosh",
        "Nokia","Orkut","Picasa","Singapore","Turkey","Windows","Youtube"};
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_info); 
        	NetworkTask task = new NetworkTask(); // call service in a separate thread 
        	task.execute("http://134.173.236.80:6080/arcgis/rest/services/claremont_colleges_buildings/MapServer/1/query?where=&text=&objectIds=&time=&geometry=q&geometryType=esriGeometryPoint&inSR=4326&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=ID%2CBuilding%2CSchool%2CLat%2CLon&returnGeometry=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&f=pjson"); 

        	spBuildingName = (Spinner) findViewById(R.id.CGUBuildingsSpinner);
			 tvBuildingID = (TextView) findViewById(R.id.BuildingIDTextView);
         	 tvSchoolName = (TextView) findViewById(R.id.SchoolNameTextView);

			 spBuildingName.setOnItemSelectedListener(this);
//tvBuildingID.setText(mBuildingName.size());
     String [] x = new String[mBuildingName.size()];
     int y = mBuildingName.size();
     
     
     //tvSchoolName.setText(mBuildingName.get(0).toString());
 	Log.d("Status", String.valueOf(y)); // get the status if 200 then OK 
 	tvSchoolName.setText(Integer.toString(y));
 	
       

        }
        @Override
    	public void onItemSelected(AdapterView arg0, View arg1, int pos,
    			long arg3) {     
        		       /* int index = arg0.getSelectedItemPosition();
        		        Toast.makeText(getBaseContext(),
        		            "You have selected item : " + mBuildingName.get(index),
        		            Toast.LENGTH_SHORT).show();
        		      
*/
        			//Toast.makeText(getBaseContext(), list.get(position), Toast.LENGTH_SHORT).show(); 	
                

        	String res="";
        	int i=0;
        	boolean found = false;
        	while(i < mBuildingName.size() && !found)	
        	{	
        		String a = mBuildingName.get(i).trim();
            String b = spBuildingName.getSelectedItem().toString().trim();
     					
            if(a.equals(b)) found = true; else i++;
            
        	}
        	
        	if(found)
        	{				
            	tvBuildingID.setText("School ID: " + mBuildingID.get(i));				
            	tvSchoolName.setText("School Name: " + mSchoolName.get(i));
        	}			
        

     				
     			}

        @Override
    	public void onNothingSelected(AdapterView arg0) {
        	// TODO Auto-generated method stub
        }
  
        
    private class NetworkTask extends AsyncTask<String, Integer, String[]> {

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
    				result= new String [n];
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

}
