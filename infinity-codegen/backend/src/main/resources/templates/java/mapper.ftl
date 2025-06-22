package ${packageName}.mapper;

<#if mybatisPlus?? && mybatisPlus>
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
</#if>
import java.util.List;
import ${packageName}.entity.${className};

/**
 * ${functionName}Mapper接口
 * 
 * @author ${author}
 * @date ${datetime}
 */
<#if mybatisPlus?? && mybatisPlus>
public interface ${className}Mapper extends BaseMapper<${className}> {

}
<#else>
public interface ${className}Mapper {
    /**
     * 查询${functionName}
     * 
     * @param ${pkColumn.javaField} ${functionName}ID
     * @return ${functionName}
     */
    ${className} select${className}ById(${pkColumn.javaType} ${pkColumn.javaField});

    /**
     * 查询${functionName}列表
     * 
     * @param ${className?uncap_first} ${functionName}
     * @return ${functionName}集合
     */
    List<${className}> select${className}List(${className} ${className?uncap_first});

    /**
     * 新增${functionName}
     * 
     * @param ${className?uncap_first} ${functionName}
     * @return 结果
     */
    int insert${className}(${className} ${className?uncap_first});

    /**
     * 修改${functionName}
     * 
     * @param ${className?uncap_first} ${functionName}
     * @return 结果
     */
    int update${className}(${className} ${className?uncap_first});

    /**
     * 删除${functionName}
     * 
     * @param ${pkColumn.javaField} ${functionName}ID
     * @return 结果
     */
    int delete${className}ById(${pkColumn.javaType} ${pkColumn.javaField});

    /**
     * 批量删除${functionName}
     * 
     * @param ${pkColumn.javaField}s 需要删除的数据ID
     * @return 结果
     */
    int delete${className}ByIds(${pkColumn.javaType}[] ${pkColumn.javaField}s);
}
</#if> 