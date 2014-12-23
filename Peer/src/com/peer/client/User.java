package com.peer.client;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	private String email;
	private String username;
	private String id;
	private String password;
	private String image;
	private String sex;
	private String birthday;
	private String city;
	
	private List<String> labels=new ArrayList<String>();
	
	
	public User() {
	}
    
	public User(Parcel source) {
		// TODO Auto-generated constructor stub
		readFromParcel(source);
	}    
    
	public void readFromParcel(Parcel source) { 
		id = source.readString();
		email=source.readString();
		username = source.readString();
		image = source.readString();
		sex = source.readString();
		birthday = source.readString();
		city = source.readString();
		password=source.readString();
		labels=new ArrayList<String>();
		source.readList(labels, getClass().getClassLoader());
	}  	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {  
		  
        @Override  
        public User createFromParcel(Parcel source) {  
            return new User(source);  
        }  
  
        @Override  
        public User[] newArray(int size) {  
            return new User[size];  
        }  
    };  	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(email);
		 dest.writeString(id);
		 dest.writeString(password);
	        dest.writeString(username);
	        dest.writeString(image);
	        dest.writeString(sex);
	        dest.writeString(birthday);
	        dest.writeString(city);
	        dest.writeList(labels);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
