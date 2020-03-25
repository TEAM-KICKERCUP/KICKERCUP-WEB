package org.kickercup.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class FileServeConfig implements WebMvcConfigurer {

    private static final Logger LOGGER = Logger.getLogger(FileServeConfig.class.getName());

    public static final String TEMP_URL = "/public/temp/";

    private static String tempPath;

    public static String getTempPath() {
        return tempPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        tempPath = System.getProperty("java.io.tmpdir").replace("\\", "/") + "kickercup/";
        File folder = new File(tempPath);
        boolean success = folder.mkdirs();
        if (!success && !folder.exists()) {
            LOGGER.log(Level.WARNING, "Could not create dedicated temp folder for application (path: '" + tempPath + "')");
        }

        String tempDir = "file:///" + tempPath;
        LOGGER.log(Level.INFO, "Serving static content from '" + tempDir + "'");

        registry.addResourceHandler(TEMP_URL + "**").addResourceLocations(tempDir);
    }
}