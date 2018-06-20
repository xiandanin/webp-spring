package com.dyhdyh.webp.service;

import com.dyhdyh.webp.model.WebPConfig;
import com.dyhdyh.webp.utils.FileUtils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
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
    private WebPConfig mDefaultConfig;

    public WebPConfig defaultConfig() {
        if (mDefaultConfig == null) {
            mDefaultConfig = new WebPConfig();
            mDefaultConfig.setQuality(80);

            String currentUser = System.getProperty("user.name");
            String mac = String.format("/Users/%s/Downloads/webp", currentUser);
            String windows = String.format("D:/");
            mDefaultConfig.setOutputDir(mac);

            mDefaultConfig.setFrameInterval(100);
        }
        return mDefaultConfig;
    }

    public String imageArray2webp(WebPConfig config, List<String> inputArray, String outputPath) throws IOException {
        return imageArray2webp(config.getQuality(), config.getFrameInterval(), config.getLoopCount(), inputArray, outputPath);
    }

    public String imageArray2webp(int quality, long frameInterval, int loopCount, List<String> inputArray, String outputPath) throws IOException {
        Resource resource = new ClassPathResource("webp/webpmux");
        File library = resource.getFile();
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
        Resource resource = new ClassPathResource("webp/gif2webp");
        File library = resource.getFile();
        return execute(String.format("%s %s -o %s", library.getAbsolutePath(), inputPath, outputPath));
    }

    public String image2webp(int quality, String inputPath, String outputPath) throws IOException {
        if (inputPath.endsWith("gif")) {
            return gif2webp(inputPath, outputPath);
        }
        Resource resource = new ClassPathResource("webp/cwebp");
        File library = resource.getFile();
        return execute(String.format("%s -q %d %s -o %s", library.getAbsolutePath(), quality, inputPath, outputPath));
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
