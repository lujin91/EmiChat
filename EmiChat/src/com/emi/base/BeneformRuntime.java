package com.emi.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BeneformRuntime {

	private static ThreadLocal<Map<String, Object>> tv = new ThreadLocal<Map<String,Object>>(){

		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};
	
	public static HttpServletRequest getRequest(){
		HttpServletRequest request  = (HttpServletRequest) tv.get().get(Scope.request.toString());
		return request;
	}
	
	public static void setRequest(HttpServletRequest request){
		tv.get().put(Scope.request.toString(), request);
	}
	
	public static HttpServletResponse getResponse(){
		HttpServletResponse response  = (HttpServletResponse) tv.get().get(Scope.response.toString());
		return response;
	}
	
	public static void setResponse(HttpServletResponse response){
		tv.get().put(Scope.response.toString(), response);
	}
	
	enum Scope{
		request, response;
	}
}
