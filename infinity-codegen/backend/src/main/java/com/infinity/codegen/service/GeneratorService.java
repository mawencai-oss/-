package com.infinity.codegen.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.infinity.codegen.entity.GenTable;
import com.infinity.codegen.entity.DbTableQuery;

/**
 * 代码生成器服务接口
 */
public interface GeneratorService {
    
    /**
     * 获取数据库表列表
     * 
     * @param query 查询参数
     * @return 数据库表集合
     */
    List<GenTable> queryDbTableList(DbTableQuery query);
    
    /**
     * 查询数据库表总数
     * 
     * @param query 查询参数
     * @return 数据库表总数
     */
    int countDbTable(DbTableQuery query);
    
    /**
     * 导入表结构
     * 
     * @param tableNames 表名称组
     */
    void importTable(String[] tableNames);
    
    /**
     * 查询业务表列表
     * 
     * @param genTable 业务表信息
     * @return 业务表集合
     */
    List<GenTable> selectGenTableList(GenTable genTable);
    
    /**
     * 获取表信息并填充表属性
     * 
     * @param tableName 表名称
     * @return 业务表信息
     */
    GenTable selectGenTableByName(String tableName);
    
    /**
     * 根据表ID查询表信息
     * 
     * @param id 表ID
     * @return 业务表信息
     */
    GenTable selectGenTableById(Long id);
    
    /**
     * 修改业务表信息
     * 
     * @param genTable 业务表信息
     */
    void updateGenTable(GenTable genTable);
    
    /**
     * 删除业务表信息
     * 
     * @param tableIds 需要删除的表数据ID
     */
    void deleteGenTableByIds(Long[] tableIds);
    
    /**
     * 同步数据库表结构
     * 
     * @param tableName 表名称
     */
    void syncDbTable(String tableName);
    
    /**
     * 预览代码
     * 
     * @param tableId 表ID
     * @return 预览数据
     */
    Map<String, String> previewCode(Long tableId);
    
    /**
     * 生成代码（下载方式）
     * 
     * @param tableName 表名称
     * @return 数据
     */
    byte[] downloadCode(String tableName);
    
    /**
     * 生成代码（自定义路径）
     * 
     * @param tableName 表名称
     */
    void generatorCode(String tableName);
    
    /**
     * 批量生成代码（下载方式）
     * 
     * @param tableNames 表数组
     * @return 数据
     */
    byte[] downloadCode(String[] tableNames);
    
    /**
     * AI生成表结构
     * 
     * @param description 业务描述
     * @return 表结构信息
     */
    GenTable generateTableWithAI(String description);
    
    /**
     * 从SQL创建表
     * 
     * @param sql SQL语句
     * @return 表结构信息
     */
    GenTable createTableFromSql(String sql);
    
    /**
     * 从Excel导入表结构
     * 
     * @param file Excel文件
     * @return 表结构信息
     */
    GenTable importFromExcel(MultipartFile file);
} 