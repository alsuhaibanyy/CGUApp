package cgu.edu.ist380.alsuhaibanyy.alghosona;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DisplayBuildingInfo extends Activity{
	
	private Spinner spBuildingName;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_info); {
        	
        	spBuildingName = (Spinner) findViewById(R.id.Spinner_Building_Info);
        	List<String> lBuildingName = new ArrayList<String>();
        	lBuildingName.add("SISAT");
        	lBuildingName.add("Druker");
        	lBuildingName.add("Library");
        	lBuildingName.add("Safety and Security");
        	lBuildingName.add("Education");
        	
        	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lBuildingName);
        	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       
        	spBuildingName.setAdapter(dataAdapter);
        }
    }

}
