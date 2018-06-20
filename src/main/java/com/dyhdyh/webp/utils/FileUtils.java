package com.dyhdyh.webp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * author  dengyuhan
 * created 2018/6/20 17:21
 */
public class FileUtils {

    public static String getFileNameNoExByUrl(String url) {
        return getFileNameNoEx(getFileNameByUrl(url));
    }

    public static String getFileNameByUrl(String url) {
        try {
            String result = url.substring(url.lastIndexOf("/") + 1);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String getFileNameNoEx(String filename) {
        try {
            if ((filename != null) && (filename.length() > 0)) {
                int dot = filename.lastIndexOf('.');
                if ((dot > -1) && (dot < (filename.length()))) {
                    return filename.substring(0, dot);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    public static void inputStream2file(InputStream ins, File file){
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
