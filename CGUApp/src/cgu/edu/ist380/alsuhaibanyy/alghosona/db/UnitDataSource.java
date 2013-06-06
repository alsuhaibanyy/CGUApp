package cgu.edu.ist380.alsuhaibanyy.alghosona.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UnitDataSource {

	
	// Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { 
			  MySQLiteHelper.UNIT_COLUMN_UNIT_ID,
			  MySQLiteHelper.UNIT_COLUMN_UNIT_NAME,
			  MySQLiteHelper.UNIT_COLUMN_UNIT_CATEGORY
		  };
public UnitDataSource(Context context) {
	  try{
dbHelper = new MySQLiteHelper(context);
	  }
	  catch (Exception e)
	  {
		    Log.e(UnitDataSource.class.getName(), "Error opening the db "+ e.getMessage());
	  }
}

public void open() throws SQLException {
database = dbHelper.getWritableDatabase();
}

public void close() {
dbHelper.close();
}

/*
*  This method takes an object of type UnitDataSource and insert it to the database
*  Note that the return type is also UnitDataSource, meaning that the inserted 
*  object will be returned form the database
*/
public Unit createUnit(Unit unit) {
ContentValues values = new ContentValues();

values.put(   MySQLiteHelper.UNIT_COLUMN_UNIT_ID,unit.getmUnitID());
values.put(   MySQLiteHelper.UNIT_COLUMN_UNIT_NAME,unit.getmUnitName());
values.put(   MySQLiteHelper.UNIT_COLUMN_UNIT_CATEGORY,unit.getmUnitCategory());

/* call the insert method on the database
 * 
 * Since the method only retuns a number of type "long", I need to downcasted to int to be able to update 
 * the id in my unit object.  unit.setId((int)insertedId);
 */
long insertedId = database.insert(MySQLiteHelper.TABLE_UNIT,null, values);
unit.setmUnitID((int)insertedId);
Log.i(UnitDataSource.class.getName(), "Record : Unit with id:" + unit.getmUnitID() +" was inserted to the db.");
return unit;
}

public void deleteUnit(Unit unit) {
long id = unit.getmUnitID();
database.delete(MySQLiteHelper.TABLE_UNIT, MySQLiteHelper.UNIT_COLUMN_UNIT_ID
    + " = " + id, null);
Log.i(UnitDataSource.class.getName(), "Record : Unit with id:" + unit.getmUnitID() +" was deleted from the db.");

}

public List<Unit> getAllUnit() {
List<Unit> unitList = new ArrayList<Unit>();

Cursor cursor = database.query(MySQLiteHelper.TABLE_UNIT,
    allColumns, null, null, null, null, null);

cursor.moveToFirst();
while (!cursor.isAfterLast()) {
  Unit unit = cursorToUnit(cursor);
  unitList.add(unit);
  cursor.moveToNext();
}
// Make sure to close the cursor
cursor.close();
return unitList;
}

private Unit cursorToUnit(Cursor cursor) {
	Unit unit = new Unit ();
// get the values from the cursor 
int intUnitID =  cursor.getInt(cursor.getColumnIndexOrThrow(MySQLiteHelper.UNIT_COLUMN_UNIT_ID));
String strUnitName=cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.UNIT_COLUMN_UNIT_NAME));
String strUnitCategory = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.UNIT_COLUMN_UNIT_CATEGORY));
unit.setmUnitID(intUnitID);
unit.setmUnitName(strUnitName);
unit.setmUnitCategory(strUnitCategory);
return unit;
}
}