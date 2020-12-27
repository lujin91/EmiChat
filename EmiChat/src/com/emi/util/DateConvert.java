package com.emi.util;

import java.text.ParseException;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class DateConvert implements Converter<String, Date> {

	
	@Override
	public Date convert(String source) {
		
		Date result = null;
		
		try {
			result = EmiDateFormat.Date_Pattern1.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
