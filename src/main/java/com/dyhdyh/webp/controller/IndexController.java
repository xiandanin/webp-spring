package com.dyhdyh.webp.controller;

import com.dyhdyh.webp.model.ResultModel;
import com.dyhdyh.webp.model.WebPConfig;
import com.dyhdyh.webp.service.WebPService;
import com.dyhdyh.webp.utils.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * author  dengyuhan
 * created 2018/6/20 14:04
 */
@Controller
public class IndexController {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    WebPService service;

    @RequestMapping("/")
    String index(Model model) {
        WebPConfig config = service.defaultConfig();
        model.addAttribute("config", config);
        return "index";
    }

    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    @ResponseBody
    List<ResultModel> handle(HttpServletRequest request, @RequestPart(value = "files") List<MultipartFile> files, WebPConfig config) {
        //File rootDir = new File(config.getOutputDir(),"webp");
        File rootDir = new File(config.getOutputDir());
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }

        File tempDir = new File(rootDir, "cache");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        List<ResultModel> result = new ArrayList<>();

        //本地临时文件
        List<String> cacheFiles = new ArrayList<>();
        try {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile itemFile = files.get(i);
                File uploadFile = new File(tempDir, itemFile.getOriginalFilename());
                itemFile.transferTo(uploadFile);
                cacheFiles.add(uploadFile.getAbsolutePath());
            }

            //转换
            String outputTime = format.format(System.currentTimeMillis());
            if (config.isAnim()) {
                //序列帧转动态
                File outputFile = new File(rootDir, FileUtils.getFileNameNoExByUrl(cacheFiles.get(0)) + "_all.webp");
                String resultLog = service.imageArray2webp(config, cacheFiles, outputFile.getAbsolutePath());
                result.add(new ResultModel(outputFile.exists(), transformResultLog(resultLog), outputFile.getAbsolutePath(), outputTime));
            } else {
                //批量转单张
                for (int i = 0; i < cacheFiles.size(); i++) {
                    String cachePath = cacheFiles.get(i);
                    File outputFile = new File(rootDir, FileUtils.getFileNameNoExByUrl(cachePath) + ".webp");
                    String resultLog = service.image2webp(config.getQuality(), cachePath, outputFile.getAbsolutePath());

                    result.add(new ResultModel(outputFile.exists(), transformResultLog(resultLog), outputFile.getAbsolutePath(), outputTime));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        //删除临时文件
        for (String cache : cacheFiles) {
            new File(cache).delete();
        }
        //如果临时文件夹是空的 文件夹也删除
        if (tempDir.listFiles().length <= 0) {
            tempDir.delete();
        }
        */
        return result;
    }

    private String transformResultLog(String resultLog) {
        StringBuffer sb = new StringBuffer();
        String[] newResult = resultLog.split("\n");
        for (String line : newResult) {
            sb.append(line);
            sb.append("<br/>");
        }
        return sb.toString();
    }


    @RequestMapping("/open-folder")
    @ResponseBody
    boolean openFolder(HttpServletRequest request, String path) {
        try {
            File file = new File(path);
            if (!file.isDirectory()) {
                file = file.getParentFile();
            }
            String userAgent = request.getHeader("user-agent");
            if (userAgent.contains("windows")) {
                Desktop.getDesktop().open(file);
            } else {
                Runtime.getRuntime().exec("open " + file.getAbsolutePath());
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
