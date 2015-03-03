package com.peer.localDB;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
	private static final int VERSION=1;
	private static final String DBNAME="PeerDB";
	private static MySQLiteHelper instance=null;
	
	private static final String USERMESSAGE_TABLE_CREATE="CREATE TABLE "
	+UserDao.TABEL_NAME+"("
	+UserDao.COLUMN_NAME_ID+" integer primary key autoincrement,"
	+UserDao.COLUMN_NAME_EMAIL+" text unique,"
	+UserDao.COLUMN_NAME_PASSWORD+" text,"
	+UserDao.COLUMN_NAME_NIKENAME+" text,"
	+UserDao.COLUMN_NAME_IMAGE+" text,"
	+UserDao.COLUMN_NAME_CITY+" text,"
	+UserDao.COLUMN_NAME_SEX+" text,"
	+UserDao.COLUMN_NAME_LOGINED+" text,"
	+UserDao.COLUMN_NAME_BIRTHDAY+" text);";
	private static final String USERLABEL_TABLE_CREATE="CREATE TABLE "
			+LabelDao.TABEL_NAME + " ("
			+LabelDao.COLUMN_NAME_ID+" integer primary key autoincrement,"
			+LabelDao.COLUMN_NAME_EMAIL+" text,"
			+LabelDao.COLUMN_NAME_LABLENAME+" text);"
			;
	
	
	private MySQLiteHelper(Context context) {		
		super(context, DBNAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}
	
	public static MySQLiteHelper getInstance(Context context){
		if(instance==null){
			instance=new MySQLiteHelper(context.getApplicationContext());
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub		
		db.execSQL(USERMESSAGE_TABLE_CREATE);
		db.execSQL(USERLABEL_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
