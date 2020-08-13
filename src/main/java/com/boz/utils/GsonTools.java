package com.boz.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
/**
 * 推荐使用
 * @author boz
 * jar包依赖gson
 */
public class GsonTools {

	/**
	 * map转json
	 * @param map
	 * @return
	 */
	public static String mapToJson(Map map){
		String jsonString = new Gson().toJson(map);
		return jsonString;
	}


	public static String ListToJson(List list){
		return new Gson().toJson(list);
	}


	public static JsonObject StringToJson(String jsonStr){
		JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();
		return returnData;
	}

	public static  String beanToJSONString(Object bean){
		return new Gson().toJson(bean);
	}

	public static Object JSONStringToObject(String json,Class beanClass){
		Gson gson = new Gson();
		Object res = gson.fromJson(json,beanClass);
		return res;
	}


	public static <T>List<T> jsonToList(String json,Class beanType){
		Type type = new ParameterizedTypeImpl(beanType);
		List<T> list = new Gson().fromJson(json,type);
		return list;
	}

	private static class ParameterizedTypeImpl implements ParameterizedType {
		Class clazz;

		public ParameterizedTypeImpl(Class clz){
			clazz = clz;
		}

		@Override
		public Type[] getActualTypeArguments() {
			return new Type[]{clazz};
		}

		@Override
		public Type getRawType() {
			return List.class;
		}

		@Override
		public Type getOwnerType() {
			return null;
		}
	}

}
