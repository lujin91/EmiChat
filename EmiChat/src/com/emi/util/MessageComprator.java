package com.emi.util;

import java.util.Comparator;

import com.emi.entity.Message;

public class MessageComprator implements Comparator<Message> {

	private boolean asc;
	
	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public MessageComprator(boolean asc){
		this.asc = asc;
	}
	
	@Override
	public int compare(Message current, Message target) {
		if(current.getId() > target.getId())
		{
			return asc ? 1 : -1;
		}
		else if(current.getId() < target.getId())
		{
			return asc ? -1 : 1;
		}
		else
		{
			return 0;
		}
	}

}
