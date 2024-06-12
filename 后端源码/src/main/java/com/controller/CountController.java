package com.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.utils.Func;
import com.common.utils.Query;
import com.entity.ReptileData;
import com.service.ReptileDataService;
import com.sun.scenario.effect.Crop;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CountController {

    @Autowired
    private ReptileDataService dataService;

    @GetMapping("/count")
    public IPage count(@RequestParam("type") String type, Query query) {
        LambdaQueryWrapper<ReptileData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReptileData::getType, type);
        wrapper.orderByDesc(ReptileData::getCreateTime);

        IPage<ReptileData> page = new Page<>(query.getCurrent(), query.getSize());
        IPage<JSONObject> resultPage = new Page<>(query.getCurrent(), query.getSize());

        page = dataService.page(page, wrapper);
        List<JSONObject> datas = page.getRecords().stream().map(item -> {
            String data = item.getData();
            JSONObject entries = JSONUtil.parseObj(data);
            entries.putOpt("createTime", DateUtil.formatDateTime(item.getCreateTime()));
            return entries;
        }).collect(Collectors.toList());

        resultPage.setRecords(datas);
        resultPage.setTotal(page.getTotal());
        return resultPage;
    }

    @GetMapping("/count2")
    public List<JSONObject> count2(@RequestParam("type") String type) {
        LambdaQueryWrapper<ReptileData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReptileData::getType, type);

        List<ReptileData> list = dataService.list(wrapper);
        List<JSONObject> datas = list.stream().map(item -> {
            String data = item.getData();
            JSONObject entries = JSONUtil.parseObj(data);
            entries.putOpt("createTime", DateUtil.formatDateTime(item.getCreateTime()));
            return entries;
        }).collect(Collectors.toList());

        return datas;
    }

}
