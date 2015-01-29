package com.peer.client.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * 携带head token
 * */
public class DataUtilHeader {
	
	private final static RestTemplate formTemplate=new RestTemplate();
	
	static{			
		List<HttpMessageConverter<?>> formPartConverters = new ArrayList<HttpMessageConverter<?>>();
		formPartConverters.add(new ByteArrayHttpMessageConverter());
		StringHttpMessageConverter formStringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		formStringHttpMessageConverter.setWriteAcceptCharset(false);
		formPartConverters.add(formStringHttpMessageConverter);
		formPartConverters.add(new ResourceHttpMessageConverter());
		
		List<HttpMessageConverter<?>> partConverters = new ArrayList<HttpMessageConverter<?>>();
		partConverters.addAll(formPartConverters);
		
		FormHttpMessageConverter formConverter = new FormHttpMessageConverter();
		formConverter.setCharset(Charset.forName("UTF-8"));
	    formConverter.setPartConverters(formPartConverters);
	    
	    partConverters.add(formConverter);
	    partConverters.add(new GsonHttpMessageConverter());
	    
	    formTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	    formTemplate.setMessageConverters(partConverters);
	}
	public static <T> ResponseEntity<T> putJson(String url,HttpEntity requestEntity,Class<T> type){
		return formTemplate.exchange(url, HttpMethod.PUT, requestEntity, type);
	}

}
