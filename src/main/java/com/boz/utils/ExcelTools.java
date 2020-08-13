package com.boz.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelTools {
	public static final Logger LOGGER = LoggerFactory.getLogger(ExcelTools.class);
	private static List<Map<Integer, String>> readExcel(String excelPath){
	
		try {
			File excel = new File(excelPath);
			if(excel.isFile() && excel.exists()){
				String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
				Workbook wb = null;
				//根据文件后缀（xls/xlsx）进行判断
				if("xls".equals(split[1])){
					FileInputStream fis = new FileInputStream(excel);
					wb = new HSSFWorkbook(fis);
				}else if("xlsx".equals(split[1])){
						try {
							wb = new XSSFWorkbook(excel);
						} catch (InvalidFormatException e) {
							e.printStackTrace();
						}
				}else{
					LOGGER.info("file type error");
					System.out.println("文件类型错误!");
					return null;
				}
				
				LOGGER.debug("star read excel ");
				//开始解析
				Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
				int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();
                
                LOGGER.debug("firstRowIndex: "+firstRowIndex);
                LOGGER.debug("lastRowIndex: "+lastRowIndex);
                
                
                List<Map<Integer, String>> lists = new ArrayList<Map<Integer,String>>();
                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                	Map<Integer, String> m = new HashMap<Integer, String>();
                    LOGGER.debug("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                               // System.out.println(cell.toString());
                                m.put(cIndex, cell.toString());
                            }else{
                            	m.put(cIndex, "");
                            }
                        }
                        lists.add(m);
                    }
                }
                
                return lists;
			}else{
				LOGGER.debug("file is not exists");
				return null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		
		for(int i=1;i<13;i++){
			String filePath = "D:/excel/"+i+".xlsx";
			
			DaoTools dao = new DaoTools();
			
			List<Map<Integer, String>> result = readExcel(filePath);
			for(Integer index=0;index<result.size();index++){
				Map<Integer, String> m = result.get(index);
				String arr [] = new String [12];
				for(Entry<Integer, String> item : m.entrySet()){
					if(item.getValue().equals("部门小计：")||item.getValue().equals("人数合计：")){
						break;
					}
					
					if(item.getKey()==0 ||item.getKey()==4 ||item.getKey()==8 ||item.getKey()==11 ){
						System.out.println("key:"+item.getKey()+"    value:"+item.getValue());
						arr[item.getKey()] = item.getValue();
					}
				}
				System.out.println(arr[0]+"|"+arr[4]+"|"+arr[8]+"|"+arr[11]);
				if(arr[0]!=null){
//					dao.ExecSQL("insert into kq (card,onn,offf,tstr) values ('"+arr[0]+"','"+arr[8]+"','"+arr[11]+"','"+arr[4]+"')");
					Map<String, String> re = dao.getSingleData("select * from kq where card='"+arr[0]+"';");
					if(re == null){
						if((!arr[8].equals("正常")&& !arr[8].equals("调休")&& !arr[8].equals("年休假")&& !arr[8].equals("因公外出"))|| (!arr[11].equals("正常")&& !arr[11].equals("调休")&& !arr[11].equals("年休假")&& !arr[11].equals("因公外出")) ){
							dao.ExecSQL("insert into kq (card,onn,offf,tstr) values ('"+arr[0]+"','"+arr[8]+"','"+arr[11]+"','"+arr[4]+"')");
						}
					}
					
				}
				
			}
		}
		

		
		
	}
}
