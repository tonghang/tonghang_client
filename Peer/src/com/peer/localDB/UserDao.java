package com.peer.localDB;

import com.peer.localDBbean.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
	public static String TABEL_NAME="usermessage";
	public static String COLUMN_NAME_ID="userid";
	public static String COLUMN_NAME_EMAIL="emial";
	public static String COLUMN_NAME_SEX="sex";
	public static String COLUMN_NAME_CITY="city";
	public static String COLUMN_NAME_IMAGE="image";
	public static String COLUMN_NAME_PASSWORD="password";
	public static String COLUMN_NAME_NIKENAME="nike";
	public static String COLUMN_NAME_BIRTHDAY="birthday";
	
	private MySQLiteHelper sqlhelper;
	public UserDao(Context context){
		sqlhelper=MySQLiteHelper.getInstance(context);
	}
	/**
	 * adduser 方法添加一个用户 参数为要添加用户的信息
	 * 该方法用于注册时使用
	 */
	public void addUser(User u){
		String email=u.getEmail();
		String password=u.getPassword();
		String nikename=u.getNikename(); 
		SQLiteDatabase db=sqlhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_EMAIL, email);
		values.put(COLUMN_NAME_PASSWORD, password);
		values.put(COLUMN_NAME_NIKENAME, nikename);		
		db.insert(TABEL_NAME, null, values);
		db.close();
	}
	/**
	 * 用于PersonMessageActivity界面的更新,nikename=null时说明用户不是在这个客户端注册的
	 * @param u
	 * @return
	 */
	public boolean updateUser(User u){
		String email=u.getEmail();
		String sex=u.getSex();
		String birthday=u.getAge();
		String city=u.getCity();
		String nikename=u.getNikename();
		SQLiteDatabase db=sqlhelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_SEX, sex);
		values.put(COLUMN_NAME_BIRTHDAY, birthday);
		values.put(COLUMN_NAME_CITY, city);
		if(nikename==null){			
			db.update(TABEL_NAME, values,COLUMN_NAME_EMAIL+"=?" , new String[]{email});
		}else{
			values.put(COLUMN_NAME_NIKENAME, nikename);
			db.update(TABEL_NAME, values,COLUMN_NAME_EMAIL+"=?" , new String[]{email});
		}		
		db.close();
		return true;		
	}
	/*存储图片路径*/
	public boolean updateImage(String email,String imgUrl){
		SQLiteDatabase db=sqlhelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_IMAGE, imgUrl);		
		db.update(TABEL_NAME, values,COLUMN_NAME_EMAIL+"=?" , new String[]{email});
		db.close();
		return true;
	}
	/**
	 * 取得用户对应的密码
	 * @param email
	 * @return
	 */
	public String getPassord(String email){
		SQLiteDatabase db=sqlhelper.getReadableDatabase();		
		Cursor cursor=db.query(TABEL_NAME, new String[]{COLUMN_NAME_PASSWORD}, COLUMN_NAME_EMAIL+"=?" , new String[]{email}, null, null, null, null);		
		String password=null;
		while(cursor.moveToNext()){
			password=cursor.getString(cursor.getColumnIndex("password"));
		}
		return password;
	}
	/**
	 * 更新本地数据库密码
	 * @param password
	 * @param email
	 * @return
	 */
	public boolean UpdatePassword(String password,String email){		
		SQLiteDatabase db=sqlhelper.getWritableDatabase();		
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_PASSWORD, password);
		db.update(TABEL_NAME, values,COLUMN_NAME_EMAIL+"=?" , new String[]{email});
		db.close();
		return true;
	}

}