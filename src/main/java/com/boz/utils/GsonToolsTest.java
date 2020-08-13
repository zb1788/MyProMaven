package com.boz.utils;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GsonToolsTest {

	private static String testMapToJson() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "gson");
		map.put("age", 28);
		map.put("sex", "male");
		map.put("arr", new String []{"a","b"});
		
		
		Map [] arr = new Map[2];
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("a", 1);
		map2.put("b", 2);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("a", 3);
		map3.put("b", 4);
		arr[0] = map2;
		arr[1] = map3;
		
		map.put("arrJson", arr);
		
		
		String jsonString = GsonTools.mapToJson(map);
		return jsonString;
	}
	
	private static void testStringToJsonObject(){
		String str = testMapToJson();
		JsonObject o = GsonTools.StringToJson(str);
		System.out.println(o.get("sex").getAsString());
		System.out.println(o.get("age").getAsInt());
		JsonArray arr = o.get("arr").getAsJsonArray();
		for(int i=0;i<arr.size();i++){
			System.out.println(arr.get(i));
		}
		
		JsonArray arr2 = o.get("arrJson").getAsJsonArray();
		for(int i=0;i<arr2.size();i++){
			JsonElement json = arr2.get(i);
			JsonObject oTmp = GsonTools.StringToJson(json.toString());
			System.out.println(oTmp.get("a"));
			System.out.println(oTmp.get("b"));
		}
	}
	
	public static void main(String[] args) {
		System.out.println(testMapToJson());
		testStringToJsonObject();
	}

}
