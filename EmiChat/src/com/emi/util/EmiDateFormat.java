package com.emi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EmiDateFormat extends ThreadLocal<SimpleDateFormat>{

	public static final EmiDateFormat Date_Pattern1 = new EmiDateFormat("yyyy-MM-dd", Locale.US);
	
	public static final EmiDateFormat Date_Pattern2 = new EmiDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	
	public static final EmiDateFormat Date_Pattern3 = new EmiDateFormat("yyyyMMdd", Locale.US);
	
	private String pattern;
	
	private Locale locale;
	
	private EmiDateFormat(String pattern, Locale locale){
		this.pattern = pattern;
		this.locale = locale;
	}
	
	@Override
	protected SimpleDateFormat initialValue() 
	{
		return locale != null ? new SimpleDateFormat(pattern, locale) : new SimpleDateFormat(pattern);
	}
	
	public Date parse(String source) throws ParseException
	{
		return this.get().parse(source);
	}

	public String format(Date date)
	{
		return this.get().format(date);
	}
}
