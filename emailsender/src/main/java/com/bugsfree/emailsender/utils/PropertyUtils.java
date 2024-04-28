package com.bugsfree.emailsender.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class PropertyUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtils.class);
    static String value = "";
    public static String getProperty(String key) {
        if(key == null || "".equals(key)) {
            throw new IllegalArgumentException();
        }

        InputStream inputStream;
        try {
            inputStream = PropertyUtils.class.getClassLoader().getResourceAsStream("emailsender.properties");
            Properties p = new Properties();
            p.load(inputStream);
            value = p.getProperty(key);
        } catch (FileNotFoundException e){
            LOGGER.error("File not found emailsender.properties", e.getMessage());
        } catch(IOException e) {
            LOGGER.error("IO Exception while reading emailsender.properties", e.getMessage());
        }
        return value;
    }
}
