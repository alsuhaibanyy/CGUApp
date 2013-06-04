package cgu.edu.ist380.alsuhaibanyy.alghosona;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnCGUMap = (Button) findViewById(R.id.Button_CGUMap);
		Button btnBuildingInfo = (Button) findViewById(R.id.Button_BuildingInfo);
		Button btnCGUWebsite = (Button) findViewById(R.id.Button_CGUWebsite);
		
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
	
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
