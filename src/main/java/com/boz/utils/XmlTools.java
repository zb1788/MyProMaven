package com.boz.utils;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.WstxDriver;




public class XmlTools {

	//读取XML
	private static void readXml() {
		SAXReader reader = new SAXReader();
		Document d = null;
		
		String path = null;
		try {
			path = XmlTools.class.getClassLoader().getResource("xml.xml").toURI().getPath();
			System.out.println(path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		try {
			d = reader.read(new File(path));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		//获取根元素
		Element root = d.getRootElement();

		List<Element> elements = root.elements();
		for(Element child : elements){
			List<Element> books = child.elements();
			for(Element book : books){
				String name = book.getName();//获取当前元素名
				String text = book.getText();//获取当前元素值
				System.out.println(name+":"+text);
			}
		}
		
		//获取第二条书籍的信息
		Element book2 = elements.get(1);
        Element author = book2.element("author");//根据元素名获取子元素
        Element title = book2.element("title");
        Element publisher = book2.element("publisher");
        System.out.println("作者：" + author.getText());//获取元素值
        System.out.println("书名：" + title.getText());
        System.out.println("出版社："+publisher.getText());

	}
	
	private static String xmlResult(String str){
		XStream xstream = new XStream(new WstxDriver());
		String data = xstream.toXML(str);//转换成 xml 格式 
		return data;
	}
	
	
	public static void main(String[] args) {
//		readXml();
		String str = "一二三四五";
		System.out.println(xmlResult(str));
	}

}
