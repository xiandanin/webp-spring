package com.dyhdyh.webp.service;

import com.dyhdyh.webp.config.ApplicationConfig;
import com.dyhdyh.webp.model.WebPConfig;
import com.dyhdyh.webp.utils.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * author  dengyuhan
 * created 2018/6/20 14:04
 */
@Service
public class WebPService {
    @Autowired
    ApplicationConfig appConfig;

    private WebPConfig mWebpConfig;

    public WebPConfig defaultConfig() {
        if (mWebpConfig == null) {
            mWebpConfig = new WebPConfig();
            mWebpConfig.setQuality(80);

            //String currentUser = System.getProperty("user.name");
            //String output = String.format("/Users/%s/Downloads/webp", currentUser);
            String defaultOutput = String.format("%s/output", appConfig.getPackageDir());
            String output = appConfig.getOutputDir();
            mWebpConfig.setOutputDir(StringUtils.isEmpty(output) ? defaultOutput : output);
            mWebpConfig.setKeepCache(true);

            mWebpConfig.setFrameInterval(100);
        }
        return mWebpConfig;
    }


    public void setOutputDir(String outputDir) {
        appConfig.saveOutputDir(outputDir);
        mWebpConfig.setOutputDir(outputDir);
    }

    public String imageArray2webp(WebPConfig config, List<String> inputArray, String outputPath) throws IOException {
        return imageArray2webp(config.getQuality(), config.getFrameInterval(), config.getLoopCount(), inputArray, outputPath);
    }

    public String imageArray2webp(int quality, long frameInterval, int loopCount, List<String> inputArray, String outputPath) throws IOException {
        File library = getLibraryFile("webpmux");
        List<String> webpPaths = new ArrayList<>();
        for (String input : inputArray) {
            if (!input.endsWith("webp")) {
                File inputFile = new File(input);
                File tempFile = new File(inputFile.getParent(), FileUtils.getFileNameNoExByUrl(input) + "_temp.webp");
                image2webp(quality, inputFile.getAbsolutePath(), tempFile.getAbsolutePath());
                input = tempFile.getAbsolutePath();
            }
            webpPaths.add(input);
        }
        //拼接命令
        StringBuffer sb = new StringBuffer(library.getAbsolutePath());
        for (String input : webpPaths) {
            sb.append(" -frame ");
            sb.append(input);
            sb.append(" +");
            sb.append(frameInterval);
        }
        sb.append(" -loop ");
        sb.append(loopCount);
        sb.append(" -o ");
        sb.append(outputPath);
        return execute(sb.toString());
    }

    public String gif2webp(String inputPath, String outputPath) throws IOException {
        File library = getLibraryFile("gif2webp");
        return execute(String.format("%s %s -o %s", library.getAbsolutePath(), inputPath, outputPath));
    }

    public String image2webp(int quality, String inputPath, String outputPath) throws IOException {
        if (inputPath.endsWith("gif")) {
            return gif2webp(inputPath, outputPath);
        }
        File library = getLibraryFile("cwebp");
        return execute(String.format("%s -q %d %s -o %s", library.getAbsolutePath(), quality, inputPath, outputPath));
    }

    private File getLibraryFile(String libraryName) {
        File rootFile = appConfig.getPackageDir();
        File parentFile = new File(rootFile, "libs");
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        InputStream stream = getClass().getClassLoader().getResourceAsStream("webp/" + libraryName);
        File targetFile = new File(parentFile, libraryName);
        if (!targetFile.exists() && targetFile.length() <= 0) {
            System.out.println("复制库文件-->" + targetFile);
            FileUtils.inputStream2file(stream, targetFile);
            execute("chmod 777 " + targetFile.getAbsolutePath());
        }
        return targetFile;
    }

    private String execute(String command) {
        try {
            System.out.println(command);
            Process process = Runtime.getRuntime().exec(command);

            DataInputStream dataInputStream = new DataInputStream(process.getErrorStream());
            Reader inputStreamReader = new InputStreamReader(dataInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer sb = new StringBuffer();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (StringUtils.isEmpty(readLine)) {
                    break;
                }
                sb.append(readLine);
                sb.append("\n");
            }
            process.destroy();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
