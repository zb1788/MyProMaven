package com.boz.test;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author boz
 * @date 2019/11/13
 */
public class Converter {
    private static String sourceDir;
    private static String fileExtension = ",js,json,";//.java;.js;.css;.sql;.txt;.xml;.properties;
    private static int maxDepth = Integer.MAX_VALUE;

    public static void main(String[] args) {
        process("C:\\Users\\boz\\Desktop\\1111\\ceYuLiang2",fileExtension,maxDepth);
    }

    public static void process(String src, String ext, int maxDepth) {
        File _src = new File(src);
        if (!_src.exists()) {
            logger("the path \"{0}\" not exist!", src);
            return;
        }
        if (_src.isDirectory()) {
            List<File> files = ConverterUtil.listFiles(_src, ext, maxDepth);
            if (files.size() == 0) {
                logger("NO FILES TO BE PROCESSED.");
                return;
            } else {
                logger("TOTAL FOUND {0} FILES, BEGIN TO PROCESS...\n", files.size());
            }
            files.stream().forEach(f -> gbk2utf(f));
        } else {
            gbk2utf(_src);
        }
    }

    public static boolean gbk2utf(File file) {
        try {
//            String content = ConverterUtil.readString(file, "GBK");
//            ConverterUtil.writeString(file, content, "UTF-8");
            String content = ConverterUtil.readString(file, "UTF-8");
            ConverterUtil.writeString(file, content, "GBK");
        } catch (Exception e) {
            logger("process {0} - FAILURE, reason: {1}", file.getName(), e.getMessage());
            return false;
        }
        logger("process {0} - SUCCESS", file.getName());
        return true;
    }

    private static boolean usage(String[] args) {
        Map<String, String> argsMap = ConverterUtil.parseArgs(args);
        if (argsMap.get("--src") != null && !"".equals(argsMap.get("--src"))) {
            sourceDir = argsMap.get("--src");
        } else {
            return true;
        }
        if (argsMap.get("--ext") != null) {
            if (!"".equals(argsMap.get("--ext"))) {
                fileExtension = argsMap.get("--ext").toLowerCase();
            } else {
                return true;
            }
        }
        if (argsMap.get("--max-depth") != null) {
            if (!"".equals(argsMap.get("--max-depth"))) {
                maxDepth = Integer.parseInt(argsMap.get("--max-depth"));
            } else {
                return true;
            }
        }
        return false;
    }

    private static void logger(String s) {
        System.out.println(s);
    }

    private static void logger(String pattern, Object...arguments) {
        logger(MessageFormat.format(pattern, arguments));
    }
}
