package com.boz.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;




public class HttpRequestTools {

	private static String getPostUrl(String weburl, String para, int mothed,String encoding) {
	     weburl = weburl.replace(" ", "%20");
	     if (weburl == null || weburl.trim().length() <= 0) {
	         return null;
	     }
	     if (mothed < 0 && mothed > 1) {
	         mothed = 0;
	     }
		 if(encoding==null || encoding.trim().length()==0){
		 	encoding="UTF-8";
		 }
	     URL url = null;
	     HttpURLConnection urlcon = null;
	     java.io.BufferedReader breader = null;
	     java.io.InputStream in = null;
	     StringBuffer sb = new StringBuffer();
	     sb.append("");
	     try {	         
	         if (mothed == 1) {
	             // 以post方式请求
		         url = new URL(weburl);
		         urlcon = (HttpURLConnection) url.openConnection();
		         urlcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
		         urlcon.setRequestProperty("Connection", "keep-alive");
		         urlcon.setDoOutput(true);
	             urlcon.setRequestMethod("POST");
	             // 装置参数
	             if (para != null && para.trim().length() > 0) {
	                 urlcon.getOutputStream().write(para.getBytes());
	                 urlcon.getOutputStream().flush();
	                 urlcon.getOutputStream().close();
	             }
	         } else {
	             // 以wget方式请求
		         url = new URL(weburl+"?"+para);
		         urlcon = (HttpURLConnection) url.openConnection();
		         urlcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
		         urlcon.setRequestProperty("Connection", "keep-alive");
		         urlcon.setDoOutput(true);
	             urlcon.setRequestMethod("GET");
	         }
	         //urlcon.setConnectTimeout(timeout * 1000);
	         //urlcon.setReadTimeout(timeout * 1000);
	         urlcon.setFollowRedirects(true);

	         //String aen = urlcon.getContentEncoding();

	         //String text_e = urlcon.getHeaderField("Content-Type");
	         
	         // 获取响应代码
	         int code = urlcon.getResponseCode();
	         
	         // 获取页面内容
	         in = urlcon.getInputStream();
	         
	         // gzip压缩传输格式支持
	         //breader = new BufferedReader(new InputStreamReader(new GZIPInputStream(in), encoding));
	         
			 breader = new BufferedReader(new InputStreamReader(in, encoding));
	         String str = breader.readLine();

	         while (str != null) {
	             sb.append(str + "\n");
	             str = breader.readLine();
	         }
	     }catch (ConnectException e) {
	    	 e.printStackTrace();
	         return null;
	     } catch (Exception e) {
	    	 e.printStackTrace();
	         return null;

	     } finally {
	         if (urlcon != null) {
	             urlcon.disconnect();
	             urlcon = null;
	         }
	         if (breader != null) {
	             try {
	                 breader.close();
	             } catch (IOException ex) {
	                 ex.printStackTrace();
	             }
	             breader = null;
	         }
	         if (in != null) {
	             try {
	                 in.close();
	             } catch (IOException ex) {
	                 ex.printStackTrace();
	             }
	             in = null;
	         }
	     }
	     return sb.toString();
	 }	
	
	
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param,String encoding) {
    	if(encoding==null || encoding.trim().length()==0){
		 	encoding="UTF-8";
		 }else if(encoding.toLowerCase()=="utf-8"){
			 encoding="UTF-8"; 
		 }else if(encoding.toLowerCase()=="gbk"){
			 encoding="GBK"; 
		 }else{
			 encoding="UTF-8";
		 }
        String result = "";
        BufferedReader in = null;
        try {
        	String urlNameString = "";
        	if(!"".equals(param)){
        		urlNameString = url + "?" + param;
        	}else{
        		urlNameString = url;
        	}
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
            
            
            connection.setConnectTimeout(50000);  
            connection.setReadTimeout(50000);  
            // 建立实际的连接
            connection.connect();
            /**
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            */
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),encoding));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }	
    
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param,String encoding) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
    	if(encoding==null || encoding.trim().length()==0){
		 	encoding="UTF-8";
		 }else if(encoding.toLowerCase()=="utf-8"){
			 encoding="UTF-8"; 
		 }else if(encoding.toLowerCase()=="gbk"){
			 encoding="GBK"; 
		 }else{
			 encoding="UTF-8";
		 }
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            conn.setConnectTimeout(50000);  
            conn.setReadTimeout(50000);  
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),encoding));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
    
    public static void main(String[] args) {
		String weburl = "http://192.168.151.126:8042/login/login";
		String param = "username=admin&pwd=1111";
		
		//GET
		String response = HttpRequestTools.sendGet(weburl,param,"UTF-8");
		System.out.println(response);
		
		
		//String res = getPostUrl(weburl,param,0,"UTF-8");
		//System.out.println(res);
		
		//POST
//		String responsePost = HttpRequestTools.sendPost(weburl,param,"UTF-8");
//		System.out.println(responsePost);
//		JsonObject o = GsonTools.StringToJson(responsePost);
//		System.out.println(o.get("status").getAsString());
//		System.out.println(o.get("msg").getAsString());
	}

}
