package com.boz.utils;
/**
 * StringUtil.java
 * @date   2011-12-19 上午11:46:34
 * @author 张博
 * @version 
 */
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 字符串处理工具类
 *
 */
public class StringTools {
    private static final Logger log = LoggerFactory.getLogger(StringTools.class);
	//HTML转义词
    private static final char[] AMP_ENCODE = "&amp;".toCharArray();//&and符号
    private static final char[] LT_ENCODE = "&lt;".toCharArray();//左尖括号
    private static final char[] GT_ENCODE = "&gt;".toCharArray();//右尖括号
    private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();//双引号
    private static final char[] APOS_ENCODE = "&#39;".toCharArray();//英文单引号(&apos;IE不识别)
    //"&acute;"//单引号(非英文)

    
	/**
	 * 判断空方法
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str==null || str.length()<1){
			return true;
		}
		return false;
	}
	
	/**
	 * 释放 StringBuffer 内存
	 * @param sbf
	 */
	public static void deleteStringBuffer(StringBuffer sbf){
		if(sbf!=null){
			sbf.delete(0,sbf.length());
			sbf = null;
		}
	}
	
	/**
	 * 判断字符串是否为英文和数字
	 * @param str
	 * @return
	 */
	public static boolean isWordOrNumber(String str){
		return Pattern.matches("(\\d|\\w)+", str);
	}
	
	 /**
     * 对replaceAll的替换值进行处理，避免其包含$时导致错误
     * @param str
     * @return
     */
    public static String convertForReplaceAll(String str){
    	if(isEmpty(str)){
    		return str;
    	}
    	return str.replaceAll("", "\\\\\\$");
    }

	/**
	 * 处理HTML标签及其内容
	 * @param content
	 * @return
	 */
    public static String converttxt(String content) {
        if(content==null || content.equals(""))
           content="";
    	String res = content.trim();
        res = res.replaceAll("[<]([^>]*)[>]", "");
        return res;
    }
    
    /**
     * 转换HTML代码为可显示内容(转义尖括号/&/双引号)
     * @param i
     * @return
     */
    public static String convertHtml(String htmlcode) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < htmlcode.length(); i++) {
			char tempchar = htmlcode.charAt(i);
			if(tempchar == '"'){
				buf.append(QUOTE_ENCODE);
            }else if(tempchar == '\''){
            	buf.append(APOS_ENCODE);
            }else if(tempchar == '&'){
            	buf.append(AMP_ENCODE);
            }else if (tempchar == '<'){
				buf.append(LT_ENCODE);
			}else if (tempchar == '>'){
				buf.append(GT_ENCODE);
			}else{
				buf.append(tempchar);
			}
		}
		String htmlstr = buf.toString();
		deleteStringBuffer(buf);
		return htmlstr;
	}
	
    /**
     * 如果一个字符串为null，则返回空串
     * @param str
     * @return
     */
    public static String getStrFromNull(String str) {
        if (str == null || "null".equals(str) || "NULL".equals(str)) {
            str = "";
        }
        return str;
    }
    
    /**
     * 获取从index位置开始首个以start起始end结束的字符串
     * 
     * @param start
     * @param end
     * @param tempstr
     * @param index
     * @return
     */
    public static final String getTagStr(String start,String end,String tempstr,int index){
    	int istart = tempstr.indexOf(start,index);
    	int iend = tempstr.indexOf(end,index);
    	if(istart>-1 && iend >-1 && istart<iend){
    		return tempstr.substring(istart,iend);
    	}
    	return null;
    }

    public static final String escapeHTMLTags(String in) {
        if (in == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = in.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer( (int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
                continue;
            }
            else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            }
            else if (ch == '>') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(GT_ENCODE);
            }else if(ch == '"'){
            	out.append(QUOTE_ENCODE);
            }else if(ch == '\''){
            	out.append(APOS_ENCODE);
            }else if(ch == '&'){
            	out.append(AMP_ENCODE);
            }
        }
        if (last == 0) {
            return in;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }
    
    /**
     * Replaces all instances of oldString with newString in line.
     *
     * @param line the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     *
     * @return a String will all instances of oldString replaced by newString
     */
    public static final String replace(String line, String oldString, String newString) {
        if (line == null) {
            return null;
        }
        oldString = getStrFromNull(oldString);
        newString = getStrFromNull(newString);
        int i = 0;
        if ( (i = line.indexOf(oldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ( (i = line.indexOf(oldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }
    
    /**
     * 为字符串中的正则关键字加入转义符，将返回字符串对应的正则表达式
     * @param str
     * @return
     */
    public static String parseRegex(String str){
		if(StringTools.isEmpty(str)){
			return str;
		}
    	return str.replaceAll("(\\.|\\?|\\||\\(|\\)|\\[|\\]|\\{|\\}|\\^|\\$|\\\\|\\-|\\+|\\*|\\,)", "\\\\$1");
    }
    
	/**
	 * 获取json用文本(对左斜线、单引号、双引号加斜线做转义处理)
	 * @param str
	 * @return
	 */
	public static String getJsonText(String str){
		if(StringTools.isEmpty(str)){
			return "";
		}
		String restr = null;
		restr = str.replaceAll("\\r","").replaceAll("\\n","").replaceAll("\\\\","\\\\\\\\").replaceAll("\'", "\\\\\'").replaceAll("\"","\\\\\"");
		return restr;
	}
	
	/**
     * 去掉重复的标签
     * @param testStr String
     * @return String
     */
    public static String getnorepeatstr(String testStr) {
        StringBuffer returnStr = new StringBuffer(20);
        String[] tmpList = testStr.split(",");
        for (int i = 0; i < tmpList.length; i++) {
            if (testStr.indexOf(tmpList[i]) != -1) {
                if (testStr.indexOf(tmpList[i]) != testStr.lastIndexOf(tmpList[i])) {
                    testStr = replace(testStr, tmpList[i], "");
                    //testStr = testStr.replaceAll(tmpList[i], ""); 有两个重复标签的时候会java.util.regex.PatternSyntaxException报错
                }
                returnStr.append(",").append(tmpList[i]);
            }
        }
        
        return returnStr.substring(1);
    }
    
}
