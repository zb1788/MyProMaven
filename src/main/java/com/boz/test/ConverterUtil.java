package com.boz.test;

import java.io.*;
import java.util.*;

/**
 * @author boz
 * @date 2019/11/13
 */
public class ConverterUtil {
    private static String lineSepator;

    static {
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            lineSepator = "\r\n";
        } else {
            lineSepator = "\n";
        }
    }

    public static Map<String, String> parseArgs(String[] args) {
        Map<String, String> argsMap = new HashMap<String, String>();
        for (int i = 0; i < args.length; i++) {
            String key = args[i];
            if (++i >= args.length) {
                argsMap.put(key, "");
            } else {
                argsMap.put(key, args[i]);
            }
        }
        return argsMap;
    }

    public static void writeString(File file, String content, String encoding) throws Exception {
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), encoding);
        osw.write(content);
        osw.close();
    }

    public static String readString(File file, String encoding) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
        StringBuilder buff = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            if (buff.length() > 0) {
                buff.append(lineSepator);
            }
            buff.append(line);
        }
        br.close();
        return buff.toString();
    }

    public static List<File> listFiles(File dir, String ext, int maxDepth) {
        List<File> files = new ArrayList<File>();
        seekFiles(dir, ext, maxDepth, files);
        return files;
    }

    private static void seekFiles(File dir, String ext, int maxDepth, List<File> files) {
        if (maxDepth-- <= 0) {
            return;
        }
        files.addAll(Arrays.asList(dir.listFiles(f -> {
            //return f.isFile() && "*".equals(ext) ? true : f.getName().toLowerCase().endsWith("." + ext);
            return checkFile(f,ext);
        })));
        for (File _dir : dir.listFiles(f -> f.isDirectory())) {
            seekFiles(_dir, ext, maxDepth, files);
        }
    }

    private static boolean checkFile(File f, String ext){
        if(f.isDirectory()){
            return false;
        }
        if(f.isFile() && "*".equals(ext)){
            return true;
        }else{
            String fileName = f.getName().toLowerCase();
            String fext = fileName.substring(fileName.lastIndexOf(".") + 1);
            String[] split = ext.split(",");
            String fextp = ","+ ext + ",";
            return fextp.indexOf("," + fext + ",") >-1;
        }
    }

}
