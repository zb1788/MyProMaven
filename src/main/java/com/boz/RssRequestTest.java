package com.boz;

import com.boz.utils.HttpRequestTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;


public class RssRequestTest {
	private static final Logger log = LoggerFactory.getLogger(RssRequestTest.class);

	private static InputStream getClassConfigFile(String configfilename){
		return RssRequestTest.class.getResourceAsStream("/"+configfilename);
	}
	
	public static void main(String[] args)
	{
		String key = "url";
		Properties properties = new Properties();
		String targetUrl = "";
		String count;
		String thread;

//		BufferedReader bufferedReader = null;
//		try {
//			bufferedReader = new BufferedReader(new FileReader("/home/root.adminssh/test.properties"));
//			properties.load(bufferedReader);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}


		InputStream filein;
		filein = getClassConfigFile("test.properties");
		try {
			properties.load(filein);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			filein.close();
		} catch (IOException e) {
			e.printStackTrace();
		}




		targetUrl = properties.getProperty(key);



		if(key == null){
			System.out.println("url参数不存在！");
			System.exit(0);
		}

		count = properties.getProperty("count");
		if(count == null){
			System.out.println("count参数不存在！");
			System.exit(0);
		}
		thread = properties.getProperty("thread");
		if(thread == null){
			System.out.println("thread参数不存在！");
			System.exit(0);
		}

		System.out.println(count);
//		System.exit(0);


		//final StringBuffer sb = new StringBuffer();
		for(int i=0;i<Integer.parseInt(count);i++)
		{
			int len= Integer.parseInt(thread);
			 final CountDownLatch countDownLatch = new CountDownLatch(len);
			for(int j=0;j<len;j++)
			{
				final String finalTargetUrl = targetUrl;
				new Thread() {
		           public void run() {
		        	   String str = UUID.randomUUID().toString().replaceAll("-", "") +"-"+ new Date().getTime();
		        	  String url ="http://rss2henan.czbanbantong.com:9000/crossdomain.xml";
		        	  String tmpUrl="http://125.46.30.54:9000/material/20170831/20170831133055369454759841743/data.xml?rcode=f4c09c2fd72cbefee2d37536e297befaa597c67d9add52d39d08c289816bfd68&validate_code=b0efdda174adc9ed407aff967bc7da1b&username=4101018888880003&flashtype=%CB%EF%F6%CE%88%90&ad=410101888888000320190218172808_0&st=vod&ct=0&CLIENTIP=218.28.20.138"+str;
		        	  String ahurl = "http://rss1hn.czbanbantong.com:9000/material/20170831/20170831133331228393860009287/data.xml?rcode=f4c09c2fd72cbefe3e090a370e1f528d625b75ae3cb8fe6ba23a2502f3f06c8f&validate_code=195e1813a75f50eebd51d65172bd626f&username=4301010000010048&flashtype=%C9%EA%B0%D8%CF%CD&ad=430101000001004820190218101132_0&st=vod&ct=0&CLIENTIP=218.28.20.138"+str;
		        	  //tmpUrl = "http://192.168.161.4/index.html";
		         // String tmpUrl="http://rss1henan.czbanbantong.com:9000/crossdomain.xml";
		        	   //String tmpUrl="https://plshenan.czbanbantong.com/youjiao/baceContent.do?section=0001&grade=0002&volume=0001&subject=0001&versions=0001&listType=1&classHour=1&t="+new Date().getTime();
		        	   Date start=new Date();
					    try {
					    	String result= HttpRequestTools.sendGet(finalTargetUrl, "", "utf-8");
						} catch (Exception e) {
							e.printStackTrace();
						}
					 
					   long s=(new Date().getTime()-start.getTime())/1000L;
					   log.info("s="+s+"xx="+currentThread().getName()+"-"+str);
					   System.out.println("s="+s+"xx="+currentThread().getName()+"-"+str);

					   //sb.append("s="+s+"xx="+currentThread().getName()+"-"+str+"\r\n");
					   countDownLatch.countDown();
		           }
		     }.start();
			}
		    
		    try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		/**
        //写入文件
//        String targetPath = "D:\\logs\\a.txt";//目标文件路径
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = sdf.format(new Date());
		String targetPath = "/home/root.adminssh/"+dateStr+".log";
		String targetPath2 = String.valueOf(RssRequestTest.class.getResource("/"));
        File f = new File(targetPath);//新建文件
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(sb.toString());
            bw.flush();
            bw.close();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
		 */
	}
}