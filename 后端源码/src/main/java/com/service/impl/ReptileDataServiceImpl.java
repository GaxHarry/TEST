package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.ReptileData;
import com.mapper.ReptileDataMapper;
import com.service.ReptileDataService;
import org.springframework.stereotype.Service;

@Service
public class ReptileDataServiceImpl extends ServiceImpl<ReptileDataMapper, ReptileData> implements ReptileDataService {
}
