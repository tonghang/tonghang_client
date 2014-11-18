package com.peer.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import com.peer.R;
import com.peer.activitymain.HomePageActivity;
import com.peer.util.Tools;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CompleteActivity extends BasicActivity{
	private TextView birthday,cityselect,sex,remind,title;
	private LinearLayout infor_Layout,setbirthday,setcity,uploadepic,setsex;
	private ImageView uploadepic_complete;
	private Button login_complete;
	private LinearLayout back;

	private String[] items;
	
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	Bitmap photo;
	byte[] img;	
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";
	int width,height;
	private ArrayList<String> tags;
	private int mYear;  						
	private int mMonth;
	private int mDay; 
	
	private static final int CYTYSELECT=3;
	private static final int SHOW_DATAPICK = 0; 
	private static final int DATE_DIALOG_ID = 1;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete);
		
		items = getResources().getStringArray(R.array.pictrue);
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);  
	    mMonth = c.get(Calendar.MONTH);  
	    mDay = c.get(Calendar.DAY_OF_MONTH);
		init();
		setDateTime();		
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
	private void init() {
		// TODO Auto-generated method stub
		title=(TextView)findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.complete));
		
		Intent intent=getIntent();
		tags=intent.getStringArrayListExtra("tags");
		infor_Layout=(LinearLayout)findViewById(R.id.ll_imfor_complete); 
		uploadepic=(LinearLayout)findViewById(R.id.ll_uploadepic);
		uploadepic.setOnClickListener(this);
		uploadepic_complete=(ImageView)findViewById(R.id.iv_uploadepic_complete);
		uploadepic_complete.setDrawingCacheEnabled(true);		
		
		setsex=(LinearLayout)findViewById(R.id.ll_setsex);
		setsex.setOnClickListener(this);
		sex=(TextView)findViewById(R.id.tv_sex);
		
		setbirthday=(LinearLayout)findViewById(R.id.ll_setbirthday);
		birthday=(TextView)findViewById(R.id.tv_setbirth);
		
		setcity=(LinearLayout)findViewById(R.id.ll_setaddress);
		cityselect=(TextView)findViewById(R.id.tv_setaddress);
		remind=(TextView)findViewById(R.id.tv_remind);
		login_complete=(Button)findViewById(R.id.bt_login_complete);
		login_complete.setOnClickListener(this);
		
		setbirthday.setOnClickListener(this);
		setcity.setOnClickListener(this);
		back=(LinearLayout)findViewById(R.id.ll_back);
		back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_uploadepic:
			showDialog();
			break;
		case R.id.ll_setbirthday:
			ChangBirthday();
			break;
		case R.id.ll_setaddress:
			ChangAddress();
			break;
		case R.id.ll_setsex:
			SexSelect();
			break;
		case R.id.bt_login_complete:
			Intent intent=new Intent(CompleteActivity.this,HomePageActivity.class);
			startActivity(intent);
//			uploadepic_complete.getDrawingCache();
//			photo=uploadepic_complete.getDrawingCache();
//			img=getBitmapByte(photo);
//			if(checkNetworkState()){
//				
//			}else{
//				ShowMessage("��ǰ����δ���ӣ�������������");
//			}			
			break;
		}
	}
	private void SexSelect() {
		// TODO Auto-generated method stub
		 final String[] items = getResources().getStringArray(  
                 R.array.sex);  
         new AlertDialog.Builder(CompleteActivity.this)  
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
					cityselect.setText(data.getStringExtra("province"));
				}else{
					cityselect.setText(data.getStringExtra("province") + "-" + data.getStringExtra("city"));
				}				
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
					ShowMessage(getResources().getString(R.string.sdcard));
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
			
			uploadepic_complete.setImageBitmap(photo);
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
	
	private void ChangBirthday() {
		// TODO Auto-generated method stub
		 Message msg = new Message(); 
         
         msg.what = SHOW_DATAPICK;  
          
         dateandtimeHandler.sendMessage(msg);
         }
	
 private void setDateTime(){
       final Calendar c = Calendar.getInstance();  
       mYear = c.get(Calendar.YEAR);  
       mMonth = c.get(Calendar.MONTH);  
       mDay = c.get(Calendar.DAY_OF_MONTH); 

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
		Intent intent=new Intent(CompleteActivity.this, GetAddressInfoActivity.class);
		startActivityForResult(intent,CYTYSELECT);
	}
}
