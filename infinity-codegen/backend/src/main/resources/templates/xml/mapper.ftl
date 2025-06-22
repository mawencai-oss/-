<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.mapper.${className}Mapper">
    
    <resultMap type="${packageName}.entity.${className}" id="${className}Result">
<#list columns as column>
        <result property="${column.javaField}" column="${column.columnName}"/>
</#list>
    </resultMap>

    <sql id="select${className}Vo">
        select<#list columns as column> ${column.columnName}<#if column_has_next>,</#if></#list> from ${tableName}
    </sql>

    <select id="select${className}List" parameterType="${packageName}.entity.${className}" resultMap="${className}Result">
        <include refid="select${className}Vo"/>
        <where>  
<#list columns as column>
<#if column.queryType?? && column.queryType == "EQ">
            <if test="${column.javaField} != null <#if column.javaType == 'String'> and ${column.javaField} != ''</#if>"> and ${column.columnName} = #{${column.javaField}}</if>
</#if>
<#if column.queryType?? && column.queryType == "NE">
            <if test="${column.javaField} != null <#if column.javaType == 'String'> and ${column.javaField} != ''</#if>"> and ${column.columnName} != #{${column.javaField}}</if>
</#if>
<#if column.queryType?? && column.queryType == "GT">
            <if test="${column.javaField} != null <#if column.javaType == 'String'> and ${column.javaField} != ''</#if>"> and ${column.columnName} &gt; #{${column.javaField}}</if>
</#if>
<#if column.queryType?? && column.queryType == "GTE">
            <if test="${column.javaField} != null <#if column.javaType == 'String'> and ${column.javaField} != ''</#if>"> and ${column.columnName} &gt;= #{${column.javaField}}</if>
</#if>
<#if column.queryType?? && column.queryType == "LT">
            <if test="${column.javaField} != null <#if column.javaType == 'String'> and ${column.javaField} != ''</#if>"> and ${column.columnName} &lt; #{${column.javaField}}</if>
</#if>
<#if column.queryType?? && column.queryType == "LTE">
            <if test="${column.javaField} != null <#if column.javaType == 'String'> and ${column.javaField} != ''</#if>"> and ${column.columnName} &lt;= #{${column.javaField}}</if>
</#if>
<#if column.queryType?? && column.queryType == "LIKE">
            <if test="${column.javaField} != null <#if column.javaType == 'String'> and ${column.javaField} != ''</#if>"> and ${column.columnName} like concat('%', #{${column.javaField}}, '%')</if>
</#if>
<#if column.queryType?? && column.queryType == "BETWEEN">
            <if test="begin${column.javaField?cap_first} != null and end${column.javaField?cap_first} != null <#if column.javaType == 'String'> and begin${column.javaField?cap_first} != '' and end${column.javaField?cap_first} != ''</#if>"> and ${column.columnName} between #{begin${column.javaField?cap_first}} and #{end${column.javaField?cap_first}}</if>
</#if>
</#list>
        </where>
    </select>
    
    <select id="select${className}ById" parameterType="${pkColumn.javaType}" resultMap="${className}Result">
        <include refid="select${className}Vo"/>
        where ${pkColumn.columnName} = #{${pkColumn.javaField}}
    </select>
        
    <insert id="insert${className}" parameterType="${packageName}.entity.${className}"<#if pkColumn.increment?? && pkColumn.increment> useGeneratedKeys="true" keyProperty="${pkColumn.javaField}"</#if>>
        insert into ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
<#list columns as column>
<#if column.columnName != pkColumn.columnName || !pkColumn.increment>
            <if test="${column.javaField} != null<#if column.javaType == 'String'> and ${column.javaField} != ''</#if>">${column.columnName},</if>
</#if>
</#list>
         </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
<#list columns as column>
<#if column.columnName != pkColumn.columnName || !pkColumn.increment>
            <if test="${column.javaField} != null<#if column.javaType == 'String'> and ${column.javaField} != ''</#if>">#{${column.javaField}},</if>
</#if>
</#list>
         </trim>
    </insert>

    <update id="update${className}" parameterType="${packageName}.entity.${className}">
        update ${tableName}
        <trim prefix="SET" suffixOverrides=",">
<#list columns as column>
<#if column.columnName != pkColumn.columnName>
            <if test="${column.javaField} != null<#if column.javaType == 'String'> and ${column.javaField} != ''</#if>">${column.columnName} = #{${column.javaField}},</if>
</#if>
</#list>
        </trim>
        where ${pkColumn.columnName} = #{${pkColumn.javaField}}
    </update>

    <delete id="delete${className}ById" parameterType="${pkColumn.javaType}">
        delete from ${tableName} where ${pkColumn.columnName} = #{${pkColumn.javaField}}
    </delete>

    <delete id="delete${className}ByIds" parameterType="String">
        delete from ${tableName} where ${pkColumn.columnName} in 
        <foreach item="${pkColumn.javaField}" collection="array" open="(" separator="," close=")">
            #{${pkColumn.javaField}}
        </foreach>
    </delete>
    
</mapper> 