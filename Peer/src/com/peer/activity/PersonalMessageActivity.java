package com.peer.activity;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.peer.BuildConfig;
import com.peer.R;
import com.peer.activitymain.PersonalPageActivity;
import com.peer.client.User;
import com.peer.client.service.SessionListener;
import com.peer.client.ui.PeerUI;
import com.peer.constant.Constant;
import com.peer.localDB.LocalStorage;
import com.peer.localDB.UserDao;
import com.peer.localDBbean.UserBean;
import com.peer.util.Tools;
import com.peer.widgetutil.LoadImageUtil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalMessageActivity extends BasicActivity implements OnClickListener{
	private TextView birthday,address,sex,nikename,title;
	private LinearLayout set_birthday,set_city,setsex;
	private ImageView headpic_personMSG;
	private LinearLayout back,head,updatenike;
	private Button update;
	private ProgressDialog pd;
	private String[] items;
	
	private static final int CYTYSELECT=3;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	public static final int UPDATENIKENAME=4;
	Bitmap photo;
	byte[] img;
	
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	
	private int mYear;  						
	private int mMonth;
	private int mDay; 
	private static final int SHOW_DATAPICK = 0; 
	private static final int DATE_DIALOG_ID = 1;
	
	private String email;
	private UserDao userdao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personmessage);
		init();
		items = getResources().getStringArray(R.array.pictrue);
		setDateTime();	
	}

	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.personalmessage));
		
		head=(LinearLayout)findViewById(R.id.ll_headpic);
		head.setOnClickListener(this);
		
		sex=(TextView)findViewById(R.id.tv_sex);
		setsex=(LinearLayout)findViewById(R.id.ll_setsex);
		setsex.setOnClickListener(this);
		
		set_birthday=(LinearLayout)findViewById(R.id.ll_setbirthday_my);
		set_birthday.setOnClickListener(this);
		birthday=(TextView)findViewById(R.id.tv_setbirthday_my);
		
		set_city=(LinearLayout)findViewById(R.id.ll_setaddress_my);
		set_city.setOnClickListener(this);		
		address=(TextView)findViewById(R.id.tv_setaddress_my);
		
		updatenike=(LinearLayout)findViewById(R.id.ll_updatenike);
		updatenike.setOnClickListener(this);
		nikename=(TextView)findViewById(R.id.et_nikename_personMSG);
		
		headpic_personMSG=(ImageView)findViewById(R.id.iv_headpic_personMSG);
		headpic_personMSG.setDrawingCacheEnabled(true);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);		
		
		update=(Button)findViewById(R.id.bt_update);
		update.setOnClickListener(this);
		
		email=LocalStorage.getString(this,Constant.EMAIL);
		userdao=new UserDao(this);
		UserBean u=userdao.findOne(email);
		sex.setText(u.getSex());
		birthday.setText(u.getAge());
		address.setText(u.getCity());
		nikename.setText(u.getNikename());
		LoadImageUtil.imageLoader.displayImage(u.getImage(), headpic_personMSG, LoadImageUtil.options);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_setbirthday_my:
			ChangBirthday();
			break;
		case R.id.ll_setaddress_my:
			ChangAddress();
			break;
		case R.id.ll_headpic:
			showDialog();
			break;
		case R.id.ll_setsex:
			SexSelect();
			break;
		case R.id.ll_updatenike:
			Intent intent=new Intent(PersonalMessageActivity.this,UpdateNikeActivity.class);
			intent.putExtra("nikename", nikename.getText().toString());
			startActivityForResult(intent, UPDATENIKENAME);
			break;
		case R.id.bt_update:
			headpic_personMSG.getDrawingCache();
			photo=headpic_personMSG.getDrawingCache();
			img=getBitmapByte(photo);
			if(checkNetworkState()){
				CommiteToServer();
			}else{
				ShowMessage(getResources().getString(R.string.Broken_network_prompt));
			}			
			break;
		default:
			break;
		}
	}
	private void CommiteToServer() {
		// TODO Auto-generated method stub
			pd = ProgressDialog.show(PersonalMessageActivity.this,"", getResources().getString(R.string.committing));
			Thread t=new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					SessionListener callback=new SessionListener();
					try {
						User u=PeerUI.getInstance().getISessionManager().profileUpdate(nikename.getText().toString(),birthday.getText().toString(), address.getText().toString(), sex.getText().toString(),IMAGE_FILE_NAME, img, callback);										
						if(callback.getMessage().equals(Constant.CALLBACKSUCCESS)){
							pd.dismiss();
							UserBean userbean=new UserBean();
							userbean.setNikename(u.getUsername());
							userbean.setEmail(u.getEmail());
							userbean.setAge(u.getBirthday());
							userbean.setCity(u.getCity());
							userbean.setSex(u.getSex());
							userbean.setImage(u.getImage());
							userdao.updateUser(userbean);	
							runOnUiThread(new Runnable() {
								public void run() {
									deleteFilesByDirectory(getExternalCacheDir());
									ShowMessage(getResources().getString(R.string.updatemsgsuccess));
								}
							});
						}else{
							ShowMessage(getResources().getString(R.string.updatemsgfail));
						}
					
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}								
				}
			};
			t.start();				
	}
	private static void deleteFilesByDirectory(File directory) {  	
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
	private void SexSelect() {
		// TODO Auto-generated method stub
		 final String[] items = getResources().getStringArray(  
                 R.array.sex);  
         new AlertDialog.Builder(PersonalMessageActivity.this)  
                 .setTitle(getResources().getString(R.string.sex))  
                 .setItems(items, new DialogInterface.OnClickListener() {  

                     public void onClick(DialogInterface dialog,  
                             int which) {  
                         sex.setText(items[which]);  
                     }  
                 }).show();  
	}	

	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.picture))
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent(Intent.ACTION_PICK,null);
							intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");  
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							if (Tools.hasSdcard()) {
								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												IMAGE_FILE_NAME)));
							}
							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case CYTYSELECT:
				if(data.getStringExtra("city")==null){
					address.setText(data.getStringExtra("province"));
				}else{
					address.setText(data.getStringExtra("province") + "-" + data.getStringExtra("city"));
				}				
				break;
			case UPDATENIKENAME:
				nikename.setText(data.getStringExtra("newnikename"));
				break;
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (Tools.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory()
									+"/"+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					
				}
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					Bitmap bt=getImageToView(data);
					
					img=getBitmapByte(bt);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void startPhotoZoom(Uri uri) {
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	public Bitmap getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
		
			headpic_personMSG.setImageBitmap(photo);
		}
		return photo;
	}

	public byte[] getBitmapByte(Bitmap bitmap) {
		if (bitmap == null) {  
		     return null;  
		  }  
		  final ByteArrayOutputStream os = new ByteArrayOutputStream();  
		  
		  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);//����PNG���кܶೣ���ʽ����jpeg�ȡ�  
		  return os.toByteArray();
	}

	Handler dateandtimeHandler = new Handler(){
	       @Override  
	       public void handleMessage(Message msg) {  
	           switch (msg.what) {  
	           case SHOW_DATAPICK:  
	               showDialog(DATE_DIALOG_ID);  
	               break; 
	           }
	       }
	    };
	private void ChangBirthday() {
		// TODO Auto-generated method stub
		 Message msg = new Message();         
         msg.what = SHOW_DATAPICK;            
         dateandtimeHandler.sendMessage(msg);
         }
	
 private void setDateTime(){
	 final Calendar c = Calendar.getInstance();  
     mYear = 1993;  
     mMonth = 1;  
     mDay = 1; 
 }

 private void updateDateDisplay(){
       birthday.setText(new StringBuilder().append(mYear).append("-")
         .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
               .append((mDay < 10) ? "0" + mDay : mDay)); 
 }
    
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {  
       public void onDateSet(DatePicker view, int year, int monthOfYear,  
              int dayOfMonth) {  
           mYear = year;  
           mMonth = monthOfYear;  
           mDay = dayOfMonth;  
           updateDateDisplay();
       }  
    };  
    @Override  
    protected Dialog onCreateDialog(int id) {  
       switch (id) {  
       case DATE_DIALOG_ID:  
           return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,  
                  mDay);
       	}
       return null;  
    }
    @Override  
    protected void onPrepareDialog(int id, Dialog dialog) {  
       switch (id) {  
       case DATE_DIALOG_ID:  
           ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);  
           break;
       }
    }

	private void ChangAddress() {
		// TODO Auto-generated method st
		Intent intent=new Intent(PersonalMessageActivity.this, GetAddressInfoActivity.class);
		startActivityForResult(intent,CYTYSELECT);
	}
}
