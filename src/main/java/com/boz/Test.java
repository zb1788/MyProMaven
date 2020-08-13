package com.boz;

import com.boz.utils.GsonTools;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static String getRan(String u){
		System.out.println(u);
		return "abc";
	}
	
	
	public static void check(String str){
		System.out.println("check:"+str);
		check2(str);
	}
	
	
	private static void check2(String str) {
		System.out.println("check2:"+str);
		check3(str);
	}


	private static void check3(String str) {
		System.out.println("check3:"+str);
		String aa = getRan(str);
		System.out.println("aa:"+aa);
	}


	public static void main(String[] args) {
		System.out.println("aabc");
		System.out.println(new Date().getTime());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String dstr = sdf.format(date);
		System.out.println(dstr);


		String jsonStr = "{\"name\":\"abc\",\"age\":23}";

		JsonObject json =  GsonTools.StringToJson(jsonStr);

		System.out.println(json);

		json.addProperty("area","xxx");

		System.out.println(json);

		System.out.println(json.toString());

	}

}
