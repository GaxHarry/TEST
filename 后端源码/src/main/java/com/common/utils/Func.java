package com.common.utils;


import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.entity.ReptileData;
import com.service.ReptileDataService;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class Func {

    private static ReptileDataService reptileDataService;

    @Autowired
    public void setReptileDataService(ReptileDataService reptileDataService) {
        Func.reptileDataService = reptileDataService;
    }

    private static final TimedCache<String, String> timedCache = CacheUtil.newTimedCache(1000 * 30);

    static {
        timedCache.schedulePrune(5);
    }

    public static void putKey(String key) {
        timedCache.put(key, "value");
    }

    public static void removeKey(String key) {
        timedCache.remove(key);
    }

    public static boolean isExistKey(String key) {
        String value = timedCache.get(key);
        return StrUtil.isNotBlank(value);
    }

    public static void save(Long id, JSONObject object, String type) {
        Long flag = generateId(object, type);

        LambdaQueryWrapper<ReptileData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReptileData::getFlag, flag);
        if (reptileDataService.count(wrapper) > 0) {
            return;
        }

        ReptileData data = new ReptileData();
        data.setBehaviorId(id);
        data.setData(JSONUtil.toJsonStr(object));
        data.setType(type);
        data.setFlag(flag);
        data.setCreateTime(new Date());
        reptileDataService.save(data);
    }

    private static Long generateId(JSONObject object, String type) {
        String s = JSONUtil.toJsonStr(object);
        s = s + "," + type;


        return Long.parseLong(s.hashCode() + "");
    }

    public static JSON get(String url) {
        return JSONUtil.parse(HttpUtil.get(url));
    }

    public static Document loadUrl(String url, Integer page) {
        String pageWaitJS = getPageWaitJS(url, page);
        return Jsoup.parse(pageWaitJS);
    }

    private static String getPageWaitJS(String url, Integer pageIndex){
       WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
        webClient.getOptions().setCssEnabled(false); //禁用css支持
        webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常

        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Object> byXPath = page.getByXPath("//*[@id=\"main-table_paginate\"]/span[1]/a[" + pageIndex + "]");
        if (byXPath == null || byXPath.size() == 0) {
            return page.asXml();
        }
        HtmlElement htmlElement = (HtmlElement) byXPath.get(0);
        try {
            page = htmlElement.click();
        } catch (IOException e) {
            e.printStackTrace();
        }
        webClient.waitForBackgroundJavaScript(3*1000);
        String pageXml = page.asXml(); //以xml的形式获取响应文本
        return pageXml;
    }
}
