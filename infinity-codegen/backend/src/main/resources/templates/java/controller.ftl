package ${packageName}.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
<#if swagger?? && swagger>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>
import ${packageName}.entity.${className};
import ${packageName}.service.${className}Service;

/**
 * ${functionName}Controller
 * 
 * @author ${author}
 * @date ${datetime}
 */
@RestController
@RequestMapping("/${moduleName}/${businessName}")
<#if swagger?? && swagger>
@Api(tags = "${functionName}管理")
</#if>
public class ${className}Controller {
    @Autowired
    private ${className}Service ${className?uncap_first}Service;

    /**
     * 查询${functionName}列表
     */
    @GetMapping("/list")
    <#if swagger?? && swagger>
    @ApiOperation("获取${functionName}列表")
    </#if>
    public List<${className}> list(${className} ${className?uncap_first}) {
        return ${className?uncap_first}Service.select${className}List(${className?uncap_first});
    }

    /**
     * 获取${functionName}详细信息
     */
    @GetMapping(value = "/{${pkColumn.javaField}}")
    <#if swagger?? && swagger>
    @ApiOperation("获取${functionName}详细信息")
    </#if>
    public ${className} get(@PathVariable("${pkColumn.javaField}") ${pkColumn.javaType} ${pkColumn.javaField}) {
        return ${className?uncap_first}Service.select${className}ById(${pkColumn.javaField});
    }

    /**
     * 新增${functionName}
     */
    @PostMapping
    <#if swagger?? && swagger>
    @ApiOperation("新增${functionName}")
    </#if>
    public int add(@RequestBody ${className} ${className?uncap_first}) {
        return ${className?uncap_first}Service.insert${className}(${className?uncap_first});
    }

    /**
     * 修改${functionName}
     */
    @PutMapping
    <#if swagger?? && swagger>
    @ApiOperation("修改${functionName}")
    </#if>
    public int edit(@RequestBody ${className} ${className?uncap_first}) {
        return ${className?uncap_first}Service.update${className}(${className?uncap_first});
    }

    /**
     * 删除${functionName}
     */
    @DeleteMapping("/{${pkColumn.javaField}s}")
    <#if swagger?? && swagger>
    @ApiOperation("删除${functionName}")
    </#if>
    public int remove(@PathVariable ${pkColumn.javaType}[] ${pkColumn.javaField}s) {
        return ${className?uncap_first}Service.delete${className}ByIds(${pkColumn.javaField}s);
    }
} 