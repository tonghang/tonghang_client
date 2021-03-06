package com.peer.client;

import android.os.Parcel;
import android.os.Parcelable;

public class Topic implements Parcelable{
	private String label_name;
	private String create_time;
	private String huangxin_group_id;
	private String user_id;
	private String body;
	private String topicid;
	private String subject;
	private User user;
	
	public Topic(){}
	
	public  Topic(Parcel source) {
		readFromParcel(source);
	}
	
	public static final Parcelable.Creator<Topic> CREATOR = new Parcelable.Creator<Topic>() {  
		  
        @Override  
        public Topic createFromParcel(Parcel source) {  
            return new Topic(source);  
        }  
  
        @Override  
        public Topic[] newArray(int size) {  
            return new Topic[size];  
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
		dest.writeString(label_name);
		 dest.writeString(create_time);
		 dest.writeString(user_id);
	        dest.writeString(topicid);
	        dest.writeString(body);
	        dest.writeString(huangxin_group_id);
	        dest.writeString(subject);
	}
	public void readFromParcel(Parcel source) { 
		user_id = source.readString();
		create_time=source.readString();
		body = source.readString();
		label_name = source.readString();
		huangxin_group_id = source.readString();
		subject = source.readString();
		topicid = source.readString();
	}

	public String getLabel_name() {
		return label_name;
	}

	public void setLabel_name(String label_name) {
		this.label_name = label_name;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getHuangxin_group_id() {
		return huangxin_group_id;
	}

	public void setHuangxin_group_id(String huangxin_group_id) {
		this.huangxin_group_id = huangxin_group_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTopicid() {
		return topicid;
	}

	public void setTopicid(String topicid) {
		this.topicid = topicid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}  	
	
}
