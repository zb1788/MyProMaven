package com.boz.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteLog {

    public static void write(String s,String filepath) {
        //使用相对路径，日志文件在项目内
//		String filepath="log.txt";

        //获得当前系统时间
        Date date=new Date();

        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String time=df.format(date);

        File file=new File(filepath);

        Writer out=null;

        try {
            //使用字符流
            out=new FileWriter(file, true);

            //注意反斜杠的方向，/r是回车，/n是换行
            out.write(time+": "+s+";"+System.getProperty("line.separator"));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        write("abc","log.txt");
    }
}