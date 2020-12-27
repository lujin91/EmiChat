package com.emi.util;

import java.util.UUID;

public class UUIDUtils {

	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}
	
	public static void main(String[] args) {
		System.out.println(UUIDUtils.getUUID());
	}
}
