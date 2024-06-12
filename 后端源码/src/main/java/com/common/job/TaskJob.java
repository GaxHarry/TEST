package com.common.job;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.common.utils.Func;
import com.entity.ReptileBehavior;
import com.service.ReptileBehaviorService;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Component
public class TaskJob extends QuartzJobBean {

    private static final String BASE_URL = System.getProperty("user.dir") + File.separator + "groovy" + File.separator;

    @Autowired
    private ReptileBehaviorService behaviorService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        LambdaUpdateWrapper<ReptileBehavior> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ReptileBehavior::getStatus, "停用");
        updateWrapper.eq(ReptileBehavior::getRunStatus, "运行中");
        updateWrapper.set(ReptileBehavior::getRunStatus, "已停止");
        behaviorService.update(updateWrapper);

        LambdaQueryWrapper<ReptileBehavior> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ReptileBehavior::getStatus, "启用");
        List<ReptileBehavior> behaviors = behaviorService.list(wrapper);

        for (ReptileBehavior behavior : behaviors) {
            handler(behavior);
        }

    }

    private void handler(ReptileBehavior behavior) {
        if (Func.isExistKey(behavior.getUrl())) {
            return;
        }

        GroovyScriptEngine engine = null;
        try {
            Func.putKey(behavior.getUrl());
            ReptileBehavior updateBefore = new ReptileBehavior();
            updateBefore.setId(behavior.getId());
            updateBefore.setStartTime(new Date());
            updateBefore.setRunStatus("运行中");
            behaviorService.updateById(updateBefore);


            engine = new GroovyScriptEngine(BASE_URL);
            Binding binding = new Binding();
            binding.setProperty("id", behavior.getId());
            binding.setProperty("tableId", behavior.getTableId());
            binding.setProperty("url", behavior.getUrl());
            binding.setProperty("type", behavior.getType());

            JSONObject jsons = JSONUtil.parseObj(behavior.getParams());
            binding.setProperty("jsons", jsons);


            engine.run(behavior.getGroovy() + ".groovy", binding);

            ReptileBehavior updateAfter = new ReptileBehavior();
            updateAfter.setId(behavior.getId());
            updateAfter.setEndTime(new Date());
            updateAfter.setRunStatus("运行结束");
            behaviorService.updateById(updateAfter);

            Func.removeKey(behavior.getUrl());
        } catch (Exception e) {
            e.printStackTrace();

            Func.removeKey(behavior.getUrl());
            ReptileBehavior updateAfter = new ReptileBehavior();
            updateAfter.setId(behavior.getId());
            updateAfter.setEndTime(new Date());
            updateAfter.setRunStatus("异常停止");
            behaviorService.updateById(updateAfter);
        }
    }
}
