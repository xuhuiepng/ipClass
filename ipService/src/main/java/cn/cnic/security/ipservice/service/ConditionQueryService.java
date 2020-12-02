package cn.cnic.security.ipservice.service;

import cn.cnic.security.ipservice.entity.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;


import java.util.Date;
public interface ConditionQueryService {
    public Page<CnvdSpiderEntity> getCnvdSpider(Page<CnvdSpiderEntity> page, Date startTime, Date endTime, String content, String level);
    public Page<RedqueenEntity> getRedqueen(Page<RedqueenEntity> page, Date startTime, Date endTime, String content, String tag);
    public Page<ScapSpiderEntity> getScapSpider(Page<ScapSpiderEntity> page, Date startTime, Date endTime, String content);
    public Page<SecuritySpiderEntity> getSecuritySpider(Page<SecuritySpiderEntity> page, Date startTime, Date endTime, String content, String level);
    public Page<SeebugSpiderEntity> getSeebugSpider(Page<SeebugSpiderEntity> page, Date startTime, Date endTime, String content, String type);
    public Page<VxvaultEntity> getVxvault(Page<VxvaultEntity> page,Date startTime, Date endTime, String content, String ip);
    public Page<ZoneTodayEntity> getZoneToday(Page<ZoneTodayEntity> page,Date startTime, Date endTime, String content, String country,String os);
//      PageUtils queryPage(Map<String, Object> params);

}
