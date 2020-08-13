package com.boz.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ReadProperties {
	private String filename;
	
    public ReadProperties(String filename) {
    	this.filename=filename;
	}
    
    //读取项目文件
    public String readPropertiesByResource(String parameter){
        Properties prop = new Properties();
        InputStream in = ReadProperties.class.getResourceAsStream(filename);
        String arg = null;
        try {
            prop.load(in);
            arg = prop.getProperty(parameter);
            if(arg!=null)
            {
            	arg=arg.trim();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arg;
    }
    //读取项目文件
    public Map<String, String> readPropertiesByResource(){
        Map<String, String> map_interface = new ConcurrentHashMap<String, String>();
        Properties prop = new Properties();
        InputStream in = ReadProperties.class.getResourceAsStream(filename);
        String arg = null;
        try {
            prop.load(in);
            writerMap(prop,map_interface);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map_interface;
    }
    //读取硬盘文件
    public String readPropertiesByFile(String parameter){
    	File f1 = new File(filename);
		Properties prop = new Properties();
		InputStream in;
		String arg = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f1));
			prop.load(in);
			arg = prop.getProperty(parameter);
			if(arg!=null)
            {
            	arg=arg.trim();
            }
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return arg;
    }
    
    
    private static void writerMap(Properties props,Map<String, String> tmpMap) {
        Set<Object> keys = props.keySet();
        for (Object key : keys) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            //对于CMS||开头的去掉:\:8080/cms/interface.jsp?readfile\=
            /*if(keyStr.startsWith("CMS||"))
            {
                value=value.replaceFirst(":8080/cms/interface.jsp\\?readfile=", "");
            }*/
            if (value != null) {
                value = value.trim();
            } else {
                value = "";
            }
            tmpMap.put(keyStr, value);
        }
    }
    
}
