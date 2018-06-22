package com.dyhdyh.webp.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * author  dengyuhan
 * created 2018/6/22 10:15
 */
@Component
public class ApplicationConfig implements InitializingBean {
    public static final String PROFILE_DEV = "dev";
    public static final String PROFILE_PROD = "prod";

    @Value("${spring.profiles.active}")
    private String profile;

    private File configFile;
    private Properties properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        properties = new Properties();
        configFile = new File(getPackageDir(), "config.properties");
        System.out.println("项目根目录------->"+configFile.getAbsolutePath());
        try {
            if (configFile.exists()) {
                properties.load(new FileInputStream(configFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getPackageDir() {
        if (PROFILE_PROD.equals(getProfile())) {
            return new File(System.getProperty("java.class.path")).getParentFile();
        } else {
            String parentPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            return new File(new File(parentPath).getParentFile().getParentFile(),"target");
        }
    }

    public void saveOutputDir(String outputDir) {
        if (properties != null && !StringUtils.isEmpty(outputDir)) {
            properties.setProperty("output_dir", outputDir);
            store();
        }
    }

    public String getOutputDir() {
        if (properties != null) {
            return properties.getProperty("output_dir");
        }
        return "";
    }

    public void store() {
        try {
            if (properties != null) {
                properties.store(new FileOutputStream(configFile), "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getProfile() {
        return profile;
    }

}
