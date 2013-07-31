package es.rczone.simonsays.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tools {
	
	public static String dateToString(Date date){
		String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return dateString;
	}

}
