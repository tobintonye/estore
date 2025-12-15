package com.tonyeapp.estore.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.Cloudinary;

import org.springframework.beans.factory.annotation.Value;

@Configuration
public class CloudinaryConfig {
    @Value("${cloud_name}")
    private String cloudname;

    @Value("${api_key}")
    private String apiKey;

    @Value("${api_secret}")
    private String apisecret;
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudname);
        config.put("api_key", apiKey);
        config.put("api_secret", apisecret);
        return new Cloudinary(config);
    }
}
