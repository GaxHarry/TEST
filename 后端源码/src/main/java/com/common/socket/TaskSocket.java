package com.common.socket;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.entity.ReptileData;
import com.service.ReptileDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class TaskSocket {

    @Autowired
    private ReptileDataService dataService;

    //打印时间
    @Scheduled(fixedRate=1000) //1000毫秒执行一次
    public  void  printTime(){

        LambdaQueryWrapper<ReptileData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReptileData::getType, "证券快讯");

        List<ReptileData> list = dataService.list(wrapper);
        List<JSONObject> datas = list.stream().map(item -> {
            String data = item.getData();
            JSONObject entries = JSONUtil.parseObj(data);
            entries.putOpt("createTime", DateUtil.formatDateTime(item.getCreateTime()));
            return entries;
        }).collect(Collectors.toList());

        WebSocketServer.sendMessage(JSONUtil.toJsonStr(datas));
    }

}
