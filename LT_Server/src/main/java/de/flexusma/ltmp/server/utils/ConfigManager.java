package de.flexusma.ltmp.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ConfigManager {
    ObjectMapper mapper;

    public ConfigManager(){
        initConfig();
    }

    public void initConfig(){
        mapper = new ObjectMapper(new YAMLFactory());
        //mapper.findAndRegisterModules();
    }

    public Config readConfig(String path) throws IOException {
        Config c = mapper.readValue(new File(path),Config.class);
        return c;
    }

    public void createDefault() throws IOException {
        Config c = new Config();
        mapper.writeValue(new File("config.yml"),c);
    }

}
