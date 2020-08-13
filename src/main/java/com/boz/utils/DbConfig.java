package com.boz.utils;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DbConfig {
	 private static final Logger log = LoggerFactory.getLogger(DbConfig.class);
	
	private static Properties DBPropertyTemp = null;
	//数据库配置文件名称
//    public static final String DBFILE_NAME = "jdbc.properties";
    public static final String DBFILE_NAME = "db.properties";
    
    
	/**
	 * (私有)获取应用根目录下配置文件(对于web及独立运行程序差异)
	 * @param configfilename
	 * @return
	 */
	private static InputStream getClassConfigFile(String configfilename){
		log.info(" Load Properties : ( "+configfilename+" ) ");
		return DbConfig.class.getResourceAsStream("/"+configfilename);
	}
	
	
	/**
	 * 获得当前数据库配置属性
	 * @return
	 */
	public static Properties getDBProperty(){
		if(DBPropertyTemp==null){
			DBPropertyTemp = new Properties();
			InputStream filein;
	        try{
				log.debug(" Load DB Info From "+DBFILE_NAME);
				filein = getClassConfigFile(DBFILE_NAME);
				DBPropertyTemp.load(filein);
				try{
					filein.close();
				}catch(Exception e){
					e.printStackTrace();
				}
				filein = null;
			}catch (Exception e){
				//e.printStackTrace();
				log.warn("GET DBTYPE LOAD DB CONFIG FILE ERROR!"+e.getMessage(),e);
			}
		}
		if(DBPropertyTemp!=null){
			String url=DBPropertyTemp.getProperty("url");
			if(url.indexOf("}}")>url.indexOf("{{") && url.indexOf("{{")>-1){
				String syskey=url.substring(url.indexOf("{{")+2,url.indexOf("}}"));
				log.debug("DBProperty URL  include {{"+syskey+"}} KEY .");
				url = url.replace("{{"+syskey+"}}", "192.168.151.126");
				log.debug("DBProperty URL  replace {{ORACLE_IP}} TO "+"192.168.151.126"+" !!! ");
				log.debug("DBProperty URL  CHANGE TO : "+url);
				DBPropertyTemp.setProperty("url",url);

			}
			return DBPropertyTemp;
		}
		log.warn("DBPropertyTemp is NULL!!! ");
		return null;
	}

}
