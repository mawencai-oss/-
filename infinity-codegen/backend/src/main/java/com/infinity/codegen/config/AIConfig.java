package com.infinity.codegen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AI配置
 */
@Component
@ConfigurationProperties(prefix = "ai")
public class AIConfig {
    /** AI提供商 */
    private String provider;
    
    /** API密钥 */
    private String apiKey;
    
    /** 模型名称 */
    private String model;
    
    /** API URL */
    private String url;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
} 