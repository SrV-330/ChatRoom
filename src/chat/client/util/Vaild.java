package chat.client.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vaild {
	public static boolean vaild(String s){
		try {
			if(s.getBytes("utf-8").length>32) return false;
			Pattern patter=Pattern.compile("^[A-Za-z]{1,1}[A-Za-z0-9]{0,15}$");
			Matcher matcher=patter.matcher(s);
			return matcher.matches();
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			return false;
		}
	}

}
