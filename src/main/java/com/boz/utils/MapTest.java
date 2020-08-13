package com.boz.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MapTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String, String>();
		map.put("a", "aaa");
		map.put("b", "bbb");
		map.put("c", "ccc");
		
		for(Entry<String, String> item : map.entrySet()){
			System.out.println("key:"+item.getKey()+"    value:"+item.getValue());
		}
	}

}
