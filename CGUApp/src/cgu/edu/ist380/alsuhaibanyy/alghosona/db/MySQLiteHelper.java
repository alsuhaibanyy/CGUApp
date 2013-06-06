package cgu.edu.ist380.alsuhaibanyy.alghosona.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	
	private static final String DATABASE_NAME = "cgu.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_BUILDING = "building";
	public static final String BUILDING_COLUMN_BUILDING_ID = "building_id";
	public static final String BUILDING_COLUMN_BUILDING_NAME = "building_name";
	public static final String BUILDING_COLUMN_BUILDING_ADDRESS = "building_address";

	public static final String TABLE_UNIT = "unit";
	public static final String UNIT_COLUMN_UNIT_ID = "unit_id";
	public static final String UNIT_COLUMN_UNIT_NAME = "unit_name";
	public static final String UNIT_COLUMN_UNIT_CATEGORY = "unit_category";

	
	public static final String TABLE_COORDINATE = "coordinate";
	public static final String COORDINATE_COLUMN_LONGITUDE = "coordinate_longitude";
	public static final String COORDINATE_COLUMN_LATITUDE = "coordinate_latitude";
	public static final String COORDINATE_COLUMN_BUILDING_ID = "building_id";
	
	private static final String DATABASE_CREATE_TABLE_BUILDING = "create table "+ TABLE_BUILDING
			+ "(" 
			+ BUILDING_COLUMN_BUILDING_ID + " integer primary key autoincrement, "
			+ BUILDING_COLUMN_BUILDING_NAME + " text not null," 
			+ BUILDING_COLUMN_BUILDING_ADDRESS + " text  null" 
	 		 // no comma after last column
			+ ")";
	
	private static final String DATABASE_CREATE_TABLE_UNIT = "create table "+ TABLE_UNIT
			+ "(" 
			+ UNIT_COLUMN_UNIT_ID + " integer primary key autoincrement, "
			+ UNIT_COLUMN_UNIT_NAME + " text not null," 
			+ UNIT_COLUMN_UNIT_CATEGORY + " text  null" 
	 		 // no comma after last column
			+ ")";


	private static final String DATABASE_CREATE_TABLE_COORDINATE = "create table "+ TABLE_COORDINATE
			+ "(" 
			+ COORDINATE_COLUMN_LONGITUDE + " integer primary key, "
			+ COORDINATE_COLUMN_LATITUDE + " integer primary key," 
			+ COORDINATE_COLUMN_BUILDING_ID + " integer not null" 
	 		 // no comma after last column
			+ ")";


	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL(DATABASE_CREATE_TABLE_BUILDING);
		arg0.execSQL(DATABASE_CREATE_TABLE_UNIT);
		arg0.execSQL(DATABASE_CREATE_TABLE_COORDINATE);
	}


	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
