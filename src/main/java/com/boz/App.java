package com.boz;

import java.io.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );
        App app = new App();
        app.test();
    }

    public void test() throws IOException {
        System.out.println(getClass().getClassLoader());
        String userPath = System.getProperty("user.dir");
        String path = userPath+"\\text.txt";
        File file = new File(path);

        InputStream inputStream = new FileInputStream(file);
        if(inputStream != null ){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (((line = bufferedReader.readLine()))!= null){
                System.out.println(line);
            }
            inputStream.close();
        }

    }
}



