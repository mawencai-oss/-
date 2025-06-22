package ${packageName}.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ${packageName}.mapper.${className}Mapper;
import ${packageName}.entity.${className};
import ${packageName}.service.${className}Service;

/**
 * ${functionName}Service业务层处理
 * 
 * @author ${author}
 * @date ${datetime}
 */
@Service
public class ${className}ServiceImpl implements ${className}Service {
    @Autowired
    private ${className}Mapper ${className?uncap_first}Mapper;

    /**
     * 查询${functionName}
     * 
     * @param ${pkColumn.javaField} ${functionName}ID
     * @return ${functionName}
     */
    @Override
    public ${className} select${className}ById(${pkColumn.javaType} ${pkColumn.javaField}) {
        return ${className?uncap_first}Mapper.select${className}ById(${pkColumn.javaField});
    }

    /**
     * 查询${functionName}列表
     * 
     * @param ${className?uncap_first} ${functionName}
     * @return ${functionName}
     */
    @Override
    public List<${className}> select${className}List(${className} ${className?uncap_first}) {
        return ${className?uncap_first}Mapper.select${className}List(${className?uncap_first});
    }

    /**
     * 新增${functionName}
     * 
     * @param ${className?uncap_first} ${functionName}
     * @return 结果
     */
    @Override
    public int insert${className}(${className} ${className?uncap_first}) {
        return ${className?uncap_first}Mapper.insert${className}(${className?uncap_first});
    }

    /**
     * 修改${functionName}
     * 
     * @param ${className?uncap_first} ${functionName}
     * @return 结果
     */
    @Override
    public int update${className}(${className} ${className?uncap_first}) {
        return ${className?uncap_first}Mapper.update${className}(${className?uncap_first});
    }

    /**
     * 批量删除${functionName}
     * 
     * @param ${pkColumn.javaField}s 需要删除的${functionName}ID
     * @return 结果
     */
    @Override
    public int delete${className}ByIds(${pkColumn.javaType}[] ${pkColumn.javaField}s) {
        return ${className?uncap_first}Mapper.delete${className}ByIds(${pkColumn.javaField}s);
    }

    /**
     * 删除${functionName}信息
     * 
     * @param ${pkColumn.javaField} ${functionName}ID
     * @return 结果
     */
    @Override
    public int delete${className}ById(${pkColumn.javaType} ${pkColumn.javaField}) {
        return ${className?uncap_first}Mapper.delete${className}ById(${pkColumn.javaField});
    }
} 