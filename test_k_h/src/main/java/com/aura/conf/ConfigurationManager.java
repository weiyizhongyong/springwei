package com.aura.conf;




import com.aura.constans.Constants;
import com.aura.constans.DeployMode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 单例 主要用作配置文件的全局管理
 */
public class ConfigurationManager {

    private ConfigurationManager(){}

    public static DeployMode dMode;
    private static Properties properties;
    static {
        try {
            properties = new Properties();
            InputStream in = ConfigurationManager.class.getClassLoader().getResourceAsStream("conf.properties");
            properties.load(in);

            dMode = DeployMode.valueOf(properties.getProperty(Constants.SPARK_JOB_RUN_MODE).toUpperCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static Long getLongProperty(String key) {
        return Long.valueOf(properties.getProperty(key));
    }

    public static int getIntProperty(String key) {
        return Integer.valueOf(properties.getProperty(key));
    }
    public static Double getDoubleProperty(String key) {
        return Double.valueOf(properties.getProperty(key));
    }

    public static Boolean getBooleanProperty(String key) {
        return Boolean.valueOf(properties.getProperty(key));
    }
}
