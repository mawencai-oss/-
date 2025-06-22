package com.infinity.codegen.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.infinity.codegen.common.AjaxResult;
import com.infinity.codegen.common.TableDataInfo;
import com.infinity.codegen.entity.AIGenerateRequest;
import com.infinity.codegen.entity.GenTable;
import com.infinity.codegen.entity.DbTableQuery;
import com.infinity.codegen.service.GeneratorService;

/**
 * 代码生成 操作处理
 */
@RestController
@RequestMapping("/codegen")
public class GenController {
    
    @Autowired
    private GeneratorService generatorService;

    /**
     * 查询数据库列表
     */
    @GetMapping("/db/list")
    public AjaxResult queryDbTableList(DbTableQuery query) {
        List<GenTable> list = generatorService.queryDbTableList(query);
        int count = generatorService.countDbTable(query);
        Map<String, Object> data = new HashMap<>();
        data.put("rows", list);
        data.put("total", count);
        return AjaxResult.success(data);
    }

    /**
     * 查询数据表列表
     */
    @GetMapping("/list")
    public TableDataInfo genList(GenTable genTable) {
        List<GenTable> list = generatorService.selectGenTableList(genTable);
        TableDataInfo dataInfo = new TableDataInfo();
        dataInfo.setRows(list);
        dataInfo.setTotal(list.size());
        return dataInfo;
    }
    
    /**
     * 查询数据表详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        GenTable table = generatorService.selectGenTableById(id);
        return AjaxResult.success(table);
    }

    /**
     * 导入表结构
     */
    @PostMapping("/importTable")
    public AjaxResult importTableSave(String tables) {
        String[] tableNames = tables.split(",");
        generatorService.importTable(tableNames);
        return AjaxResult.success();
    }

    /**
     * 修改保存代码生成业务
     */
    @PutMapping
    public AjaxResult editSave(@RequestBody GenTable genTable) {
        generatorService.updateGenTable(genTable);
        return AjaxResult.success();
    }

    /**
     * 删除代码生成
     */
    @DeleteMapping("/{tableIds}")
    public AjaxResult remove(@PathVariable Long[] tableIds) {
        generatorService.deleteGenTableByIds(tableIds);
        return AjaxResult.success();
    }

    /**
     * 预览代码
     */
    @GetMapping("/preview/{tableId}")
    public AjaxResult preview(@PathVariable("tableId") Long tableId) {
        Map<String, String> dataMap = generatorService.previewCode(tableId);
        return AjaxResult.success(dataMap);
    }

    /**
     * 生成代码（下载方式）
     */
    @GetMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = generatorService.downloadCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     */
    @GetMapping("/genCode/{tableName}")
    public AjaxResult genCode(@PathVariable("tableName") String tableName) {
        generatorService.generatorCode(tableName);
        return AjaxResult.success();
    }

    /**
     * 批量生成代码
     */
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = tables.split(",");
        byte[] data = generatorService.downloadCode(tableNames);
        genCode(response, data);
    }

    /**
     * AI生成表结构
     */
    @PostMapping("/ai/generate")
    public AjaxResult generateWithAI(@RequestBody AIGenerateRequest request) {
        GenTable table = generatorService.generateTableWithAI(request.getDescription());
        return AjaxResult.success(table);
    }

    /**
     * 从SQL创建表
     */
    @PostMapping("/fromSql")
    public AjaxResult createFromSql(@RequestBody String sql) {
        GenTable table = generatorService.createTableFromSql(sql);
        return AjaxResult.success(table);
    }

    /**
     * 从Excel导入表结构
     */
    @PostMapping("/importExcel")
    public AjaxResult importFromExcel(MultipartFile file) {
        GenTable table = generatorService.importFromExcel(file);
        return AjaxResult.success(table);
    }

    /**
     * 下载代码
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"codegen.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        response.getOutputStream().write(data);
    }
} 