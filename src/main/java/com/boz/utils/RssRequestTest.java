package com.boz.utils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RssRequestTest {
	private final static Logger log = LoggerFactory.getLogger(RssRequestTest.class);
	
	public static String sendPlayServlet(String tmpUrl, final String gbk, int i) {
		/**
		String responseBody = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			HttpGet httpget = new HttpGet(tmpUrl);

			RequestConfig config = RequestConfig.custom().setConnectTimeout(i).build();
			httpget.setConfig(config);
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity, gbk) : null;
					} else {
						return null;
					}
				}

			};
			responseBody = httpclient.execute(httpget, responseHandler);

		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(MyHttpClient.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		} finally {
			try {
				httpclient.close();
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(MyHttpClient.class.getName()).log(Level.SEVERE, null, ex);
				return null;
			}
		}
		return responseBody;
		*/
		
		
		
		
		return null;
	}
	
	
	public static void main(String[] args)
	{
		final StringBuffer sb = new StringBuffer();
		for(int i=0;i<20;i++)
		{
			int len=500;
			 final CountDownLatch countDownLatch = new CountDownLatch(len);
			for(int j=0;j<len;j++)
			{
			 new Thread() {
		           public void run() {
		        	   String str = UUID.randomUUID().toString().replaceAll("-", "") +"-"+ new Date().getTime();
		        	  String url ="http://rss2henan.czbanbantong.com:9000/crossdomain.xml";
		        	  String tmpUrl="http://125.46.30.54:9000/material/20170727/20170727152410921360213231098/小蝌蚪.jpg?rcode=127edaa1b940f61d3105bcf4c6aaff5185f024349696f85339c0fd8179e00531&validate_code=5783d36072be9ccd3216200663e27da2&username=4101018888880152&flashtype=郑馥&ad=410101888888015220181025154014_0&st=vod&ct=0&CLIENTIP=218.28.20.138&abc=1&qqqqq="+str;
		        	  //tmpUrl = "http://192.168.161.4/index.html";
		         // String tmpUrl="http://rss1henan.czbanbantong.com:9000/crossdomain.xml";
		        	   //String tmpUrl="https://plshenan.czbanbantong.com/youjiao/baceContent.do?section=0001&grade=0002&volume=0001&subject=0001&versions=0001&listType=1&classHour=1&t="+new Date().getTime();
		        	   Date start=new Date();
					    try {
					    	String result=HttpRequestTools.sendGet(tmpUrl, "", "utf-8");
						} catch (Exception e) {
							e.printStackTrace();
						}
					 
					   long s=(new Date().getTime()-start.getTime())/1000L;
					   log.info("s="+s+"xx="+currentThread().getName()+"-"+str);
					   sb.append("s="+s+"xx="+currentThread().getName()+"-"+str+"\r\n");
					   countDownLatch.countDown();
		           }
		     }.start();
			}
		    
		    try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
        //写入文件
        String targetPath = "D:\\logs\\a.txt";//目标文件路径
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
	}
}