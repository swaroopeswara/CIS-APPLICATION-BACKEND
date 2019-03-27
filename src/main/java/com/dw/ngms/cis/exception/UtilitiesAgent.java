package com.dw.ngms.cis.exception;

import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class UtilitiesAgent {
	
	
	public long getTimeStampInMillis()
	{
		return System.currentTimeMillis();
	}
	
	public String getExceptionMessage(Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	public String getCurrentTimeStamp() {
		String currnetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		currnetDate = currnetDate.replaceAll(" ", "T");
		return currnetDate;
	}
	
	public String getGUID()
	{
		return java.util.UUID.randomUUID().toString();
	}

	public static void main(String[] args) {
		String dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		dt = dt.replaceAll(" ", "T");
		String pattern = "yyyy-mm-dd hh:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = simpleDateFormat.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("Date:" + date);

	}
}
