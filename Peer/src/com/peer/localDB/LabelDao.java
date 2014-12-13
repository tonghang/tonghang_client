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
	 * addLabel 添加一个用户标签
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
	/**
	 * 更新用户的某个标签
	 * @param email
	 * @param oldLabel
	 * @param newLabel
	 */
	public void updateLabel(String email,String oldLabel,String newLabel){
		SQLiteDatabase db=sqlhelper.getWritableDatabase();		
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_LABLENAME, newLabel);
		db.update(TABEL_NAME, values,COLUMN_NAME_EMAIL+"=?and"+ COLUMN_NAME_LABLENAME+"=?", new String[]{email,oldLabel});
		db.close();
	}
	/**
	 * 删除某个标签
	 * @param email
	 * @param targetLabel
	 */
	public void deleteLabel(String email,String targetLabel){
		SQLiteDatabase db=sqlhelper.getWritableDatabase();		
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_LABLENAME, targetLabel);
		db.delete(TABEL_NAME, COLUMN_NAME_EMAIL+"=?", new String[]{email});
		db.close();
	}

}
