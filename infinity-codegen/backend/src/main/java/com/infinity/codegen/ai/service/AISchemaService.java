package com.infinity.codegen.ai.service;

import java.util.List;
import java.util.Map;

import com.infinity.codegen.entity.GenTable;

/**
 * AI表结构生成服务接口
 */
public interface AISchemaService {
    
    /**
     * 根据业务描述生成表结构
     * 
     * @param businessDescription 业务描述
     * @return 表结构元数据
     */
    GenTable generateTableSchema(String businessDescription);
    
    /**
     * 优化现有表结构
     * 
     * @param currentSchema 当前表结构
     * @param requirements 优化需求
     * @return 优化后的表结构
     */
    GenTable optimizeTableSchema(GenTable currentSchema, String requirements);
    
    /**
     * 生成示例数据
     * 
     * @param schema 表结构
     * @param count 数据条数
     * @return 示例数据
     */
    List<Map<String, Object>> generateSampleData(GenTable schema, int count);
    
    /**
     * 根据表结构生成SQL
     * 
     * @param table 表结构
     * @return 建表SQL
     */
    String generateTableSql(GenTable table);
    
    /**
     * 智能补全表注释
     * 
     * @param table 表结构
     * @return 补全后的表结构
     */
    GenTable enhanceTableComment(GenTable table);
} 