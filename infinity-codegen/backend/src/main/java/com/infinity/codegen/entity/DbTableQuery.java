package com.infinity.codegen.entity;

import lombok.Data;

/**
 * 数据库表查询参数
 */
@Data
public class DbTableQuery {
    /** 表名称 */
    private String tableName;

    /** 表描述 */
    private String tableComment;

    /** 数据源ID */
    private String datasourceId;

    /** 开始条数 */
    private Integer pageNum;

    /** 每页显示条数 */
    private Integer pageSize;
} 