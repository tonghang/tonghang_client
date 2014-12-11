package com.peer.localDB;

import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class LabelDao {
	public static String TABEL_NAME="userlabel";
	public static String COLUMN_NAME_ID="labelid";
	public static String COLUMN_NAME_EMAIL="email";
	public static String COLUMN_NAME_LABLENAME="labelname";
	
	private MySQLiteHelper sqlhelper;
	public LabelDao(Context context){
		sqlhelper=MySQLiteHelper.getInstance(context);
	}
	/**
	 * adduser 方法添加一个用户 参数为要添加用户的信息
	 * 该方法用于注册时使用
	 */
	public void addLabel(String email,List<String> labels){ 
		SQLiteDatabase db=sqlhelper.getWritableDatabase();
		for(int i=0;i<labels.size();i++){
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_EMAIL, email);
			values.put(COLUMN_NAME_LABLENAME, labels.get(i));
			db.insert(TABEL_NAME, null, values);
		}
		
		db.close();
	}

}
