package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class ReptileBehavior {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String remark;

    private String url;

    private Date startTime;

    private Date endTime;

    private String status;

    private String runStatus;

    private String type;

    private String params;

    private String tableId;

    private String groovy;
}
