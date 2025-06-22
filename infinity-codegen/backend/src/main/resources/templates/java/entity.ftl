package ${packageName}.entity;

import java.io.Serializable;
import java.util.Date;
<#if hasDecimal>
import java.math.BigDecimal;
</#if>
<#if hasBigInteger>
import java.math.BigInteger;
</#if>
<#if swaggerAnnotation?? && swaggerAnnotation>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
import lombok.Data;
<#if activateRecord??>
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * ${functionName}实体
 *
 * @author ${author}
 * @date ${datetime}
 */
@Data
@ApiModel("${functionName}")
@TableName("${tableName}")
public class ${className} extends Model<${className}> implements Serializable {
    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columns as field>
    <#if swaggerAnnotation?? && swaggerAnnotation>
    @ApiModelProperty("${field.columnComment}")
    <#else>
    /** ${field.columnComment} */
    </#if>
    <#if field.isPk == "1">
    @TableId
    </#if>
    private ${field.javaType} ${field.javaField};

</#list>
<#-- ----------  END 字段循环遍历  ---------->
}
<#else>
/**
 * ${functionName}实体
 *
 * @author ${author}
 * @date ${datetime}
 */
@Data
<#if swaggerAnnotation?? && swaggerAnnotation>
@ApiModel("${functionName}")
</#if>
public class ${className} implements Serializable {
    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list columns as field>
    <#if swaggerAnnotation?? && swaggerAnnotation>
    @ApiModelProperty("${field.columnComment}")
    <#else>
    /** ${field.columnComment} */
    </#if>
    private ${field.javaType} ${field.javaField};

</#list>
<#-- ----------  END 字段循环遍历  ---------->
}
</#if> 