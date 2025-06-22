package com.infinity.codegen.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 代码生成业务表
 */
@Data
public class GenTable {
    /** 编号 */
    private Long id;
    
    /** 表名称 */
    private String tableName;
    
    /** 表描述 */
    private String tableComment;
    
    /** 实体类名称 */
    private String className;
    
    /** 使用的模板（crud单表操作 tree树表操作） */
    private String tplCategory;
    
    /** 生成包路径 */
    private String packageName;
    
    /** 生成模块名 */
    private String moduleName;
    
    /** 生成业务名 */
    private String businessName;
    
    /** 生成功能名 */
    private String functionName;
    
    /** 生成功能作者 */
    private String functionAuthor;
    
    /** 生成代码方式（0zip压缩包 1自定义路径） */
    private String genType;
    
    /** 生成路径（不填默认项目路径） */
    private String genPath;
    
    /** 其它生成选项 */
    private String options;
    
    /** 创建者 */
    private String createBy;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 更新者 */
    private String updateBy;
    
    /** 更新时间 */
    private Date updateTime;
    
    /** 备注 */
    private String remark;
    
    /** 表列信息 */
    private List<GenTableColumn> columns;
    
    /** 树编码字段 */
    private String treeCode;
    
    /** 树父编码字段 */
    private String treeParentCode;
    
    /** 树名称字段 */
    private String treeName;
} 