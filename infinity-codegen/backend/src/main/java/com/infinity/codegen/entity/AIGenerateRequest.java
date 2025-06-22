package com.infinity.codegen.entity;

import lombok.Data;

/**
 * AI表结构生成请求
 */
@Data
public class AIGenerateRequest {
    /** 业务描述 */
    private String description;
    
    /** 模块名称 */
    private String moduleName;
    
    /** 业务名称 */
    private String businessName;
    
    /** 功能描述 */
    private String functionName;
    
    /** 作者名称 */
    private String author;
    
    /** AI模型 */
    private String model;
} 