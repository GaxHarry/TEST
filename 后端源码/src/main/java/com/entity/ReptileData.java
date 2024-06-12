package com.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class ReptileData {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long behaviorId;

    private String data;

    private Long flag;

    private String type;

    private Date createTime;
}
