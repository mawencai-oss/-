package com.infinity.codegen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代码生成配置
 */
@Component
@ConfigurationProperties(prefix = "gen")
public class GenConfig {
    /** 作者 */
    private String author;

    /** 生成包路径 */
    private String packageName;

    /** 自动去除表前缀 */
    private boolean autoRemovePre;

    /** 表前缀(类名不会包含表前缀) */
    private String tablePrefix;
    
    /** 模板路径 */
    private String templatePath;
    
    /** 输出路径 */
    private String outputDir;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isAutoRemovePre() {
        return autoRemovePre;
    }

    public void setAutoRemovePre(boolean autoRemovePre) {
        this.autoRemovePre = autoRemovePre;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }
    
    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
    
    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
} 