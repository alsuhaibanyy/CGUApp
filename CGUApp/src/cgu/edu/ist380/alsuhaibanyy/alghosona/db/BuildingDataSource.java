package cgu.edu.ist380.alsuhaibanyy.alghosona.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BuildingDataSource {
	
	// Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { 
			  MySQLiteHelper.BUILDING_COLUMN_BUILDING_ID,
			  MySQLiteHelper.BUILDING_COLUMN_BUILDING_NAME,
			  MySQLiteHelper.BUILDING_COLUMN_BUILDING_ADDRESS
		  };
public BuildingDataSource(Context context) {
	  try{
dbHelper = new MySQLiteHelper(context);
	  }
	  catch (Exception e)
	  {
		    Log.e(BuildingDataSource.class.getName(), "Error opening the db "+ e.getMessage());
	  }
}

public void open() throws SQLException {
database = dbHelper.getWritableDatabase();
}

public void close() {
dbHelper.close();
}

/*
*  This method takes an object of type BuildingDataSource and insert it to the database
*  Note that the return type is also BuildingDataSource, meaning that the inserted 
*  object will be returned form the database
*/
public Building createBuilding(Building building) {
ContentValues values = new ContentValues();

values.put(   MySQLiteHelper.BUILDING_COLUMN_BUILDING_ID,building.getmBuildingID());
values.put(   MySQLiteHelper.BUILDING_COLUMN_BUILDING_NAME,building.getmBuildingName());
values.put(   MySQLiteHelper.BUILDING_COLUMN_BUILDING_ADDRESS,building.getmBuildingAddress());

/* call the insert method on the database
 * 
 * Since the method only retuns a number of type "long", I need to downcasted to int to be able to update 
 * the id in my meds object.  meds.setId((int)insertedId);
 */
long insertedId = database.insert(MySQLiteHelper.TABLE_BUILDING,null, values);
building.setmBuildingID((int)insertedId);
Log.i(BuildingDataSource.class.getName(), "Record : Med with id:" + building.getmBuildingID() +" was inserted to the db.");
return building;
}

public void deleteBuilding(Building building) {
long id = building.getmBuildingID();
database.delete(MySQLiteHelper.TABLE_BUILDING, MySQLiteHelper.BUILDING_COLUMN_BUILDING_ID
    + " = " + id, null);
Log.i(BuildingDataSource.class.getName(), "Record : Med with id:" + building.getmBuildingID() +" was deleted from the db.");

}

public List<Building> getAllBuilding() {
List<Building> buildingList = new ArrayList<Building>();

Cursor cursor = database.query(MySQLiteHelper.TABLE_BUILDING,
    allColumns, null, null, null, null, null);

cursor.moveToFirst();
while (!cursor.isAfterLast()) {
  Building building = cursorToBuilding(cursor);
  buildingList.add(building);
  cursor.moveToNext();
}
// Make sure to close the cursor
cursor.close();
return buildingList;
}

private Building cursorToBuilding(Cursor cursor) {
	Building building = new Building ();
// get the values from the cursor 
int intBuildingID =  cursor.getInt(cursor.getColumnIndexOrThrow(MySQLiteHelper.BUILDING_COLUMN_BUILDING_ID));
String strBuildingName=cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.BUILDING_COLUMN_BUILDING_NAME));
String strBuildingAddress = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.BUILDING_COLUMN_BUILDING_ADDRESS));
building.setmBuildingID(intBuildingID);
building.setmBuildingName(strBuildingName);
building.setmBuildingAddress(strBuildingAddress);
return building;
}

}
