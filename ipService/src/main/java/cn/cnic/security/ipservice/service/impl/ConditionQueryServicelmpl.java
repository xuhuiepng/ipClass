package cn.cnic.security.ipservice.service.impl;

import cn.cnic.security.ipservice.dao.*;
import cn.cnic.security.ipservice.entity.*;
import cn.cnic.security.ipservice.service.ConditionQueryService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConditionQueryServicelmpl implements ConditionQueryService {
    @Autowired
    private CnvdSpiderDao cnvdSpiderDao ;
    @Autowired
    private RedqueenDao redqueenDao;
    @Autowired
    private ScapSpiderDao scapSpiderDao;
    @Autowired
    private SecuritySpiderDao securitySpiderDao;
    @Autowired
    private SeebugSpiderDao seebugSpiderDao;
    @Autowired
    private VxvaultDao vxvaultDao;
    @Autowired
    private ZoneTodayDao zoneTodayDao;
    @Override
    public Page<CnvdSpiderEntity> getCnvdSpider(Page<CnvdSpiderEntity> page, Date startTime, Date endTime, String content, String level) {
        return cnvdSpiderDao.getCnvdSpiderDao(page,startTime,endTime,content,level);
    }

    @Override
    public Page<RedqueenEntity> getRedqueen(Page<RedqueenEntity> page, Date startTime, Date endTime, String content, String tag) {
        return redqueenDao.getRedqueenDao(page,startTime,endTime,content,tag);
    }

    @Override
    public Page<ScapSpiderEntity> getScapSpider(Page<ScapSpiderEntity> page, Date startTime, Date endTime, String content) {
        return scapSpiderDao.getScapSpiderDao(page,startTime,endTime,content);
    }

    @Override
    public Page<SecuritySpiderEntity> getSecuritySpider(Page<SecuritySpiderEntity> page, Date startTime, Date endTime, String content, String level) {
        return securitySpiderDao.getSecuritySpiderDao(page,startTime,endTime,content,level);
    }

    @Override
    public Page<SeebugSpiderEntity> getSeebugSpider(Page<SeebugSpiderEntity> page, Date startTime, Date endTime, String content, String type) {
        return seebugSpiderDao.getSeebugSpiderDao(page,startTime,endTime,content,type);
    }

    @Override
    public Page<VxvaultEntity> getVxvault(Page<VxvaultEntity> page, Date startTime, Date endTime, String content, String ip) {
        return vxvaultDao.getVxvaultDao(page,startTime,endTime,content,ip);
    }

    @Override
    public Page<ZoneTodayEntity> getZoneToday(Page<ZoneTodayEntity> page, Date startTime, Date endTime, String content, String country, String os) {
        return zoneTodayDao.getZoneTodayDao(page,startTime,endTime,content,country,os);
    }
}
