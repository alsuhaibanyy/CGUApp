package cgu.edu.ist380.alsuhaibanyy.alghosona.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BuildingCoordinateDataSource {
	

	
	// Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { 
			  MySQLiteHelper.COORDINATE_COLUMN_LONGITUDE,
			  MySQLiteHelper.COORDINATE_COLUMN_LATITUDE,
			  MySQLiteHelper.COORDINATE_COLUMN_BUILDING_ID
		  };
public BuildingCoordinateDataSource(Context context) {
	  try{
dbHelper = new MySQLiteHelper(context);
	  }
	  catch (Exception e)
	  {
		    Log.e(BuildingCoordinateDataSource.class.getName(), "Error opening the db "+ e.getMessage());
	  }
}

public void open() throws SQLException {
database = dbHelper.getWritableDatabase();
}

public void close() {
dbHelper.close();
}

/*
*  This method takes an object of type BuildingCoordinateDataSource and insert it to the database
*  Note that the return type is also BuildingCoordinateDataSource, meaning that the inserted 
*  object will be returned form the database
*/
public BuildingCoordinate createBuildingCoordinate(BuildingCoordinate buildingCoordinate) {
ContentValues values = new ContentValues();

values.put(   MySQLiteHelper.COORDINATE_COLUMN_LONGITUDE,buildingCoordinate.getmLongitude());
values.put(   MySQLiteHelper.COORDINATE_COLUMN_LATITUDE,buildingCoordinate.getmLatitude());
values.put(   MySQLiteHelper.COORDINATE_COLUMN_BUILDING_ID,buildingCoordinate.getmBuildingID());

/* call the insert method on the database
 * 
 * Since the method only retuns a number of type "double", I need to downcasted to int to be able to update 
 * the id in my buildingCoordinate object.  buildingCoordinate.setId((int)insertedId);
 */
return buildingCoordinate;
}

public void deleteBuildingCoordinate(BuildingCoordinate buildingCoordinate) {
double intLongitude = buildingCoordinate.getmLongitude();
double intLatitude = buildingCoordinate.getmLatitude();
database.delete(MySQLiteHelper.TABLE_COORDINATE, MySQLiteHelper.COORDINATE_COLUMN_LONGITUDE
    + " = " + intLongitude + " AND " + MySQLiteHelper.COORDINATE_COLUMN_LATITUDE
    + " = " + intLatitude , null);
Log.i(BuildingCoordinateDataSource.class.getName(), "Record : BuildingCoordinate with Longitude:" 
		 + buildingCoordinate.getmLongitude() +" was deleted from the db.");
Log.i(BuildingCoordinateDataSource.class.getName(), "Record : BuildingCoordinate with Latitude:" 
		 + buildingCoordinate.getmLatitude() +" was deleted from the db.");

}

public List<BuildingCoordinate> getAllBuildingCoordinate() {
List<BuildingCoordinate> buildingCoordinateList = new ArrayList<BuildingCoordinate>();

Cursor cursor = database.query(MySQLiteHelper.TABLE_COORDINATE,
    allColumns, null, null, null, null, null);

cursor.moveToFirst();
while (!cursor.isAfterLast()) {
  BuildingCoordinate buildingCoordinate = cursorToBuildingCoordinate(cursor);
  buildingCoordinateList.add(buildingCoordinate);
  cursor.moveToNext();
}
// Make sure to close the cursor
cursor.close();
return buildingCoordinateList;
}

private BuildingCoordinate cursorToBuildingCoordinate(Cursor cursor) {
	BuildingCoordinate buildingCoordinate = new BuildingCoordinate ();
// get the values from the cursor 
	double lngLongtitude =  cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COORDINATE_COLUMN_LONGITUDE));
	double lngLatitude =  cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COORDINATE_COLUMN_LATITUDE));
	int intBuildingID=cursor.getInt(cursor.getColumnIndexOrThrow(MySQLiteHelper.COORDINATE_COLUMN_BUILDING_ID));
buildingCoordinate.setmLongitude(lngLongtitude);
buildingCoordinate.setmLatitude(lngLatitude);
buildingCoordinate.setmBuildingID(intBuildingID);
return buildingCoordinate;
}
}
