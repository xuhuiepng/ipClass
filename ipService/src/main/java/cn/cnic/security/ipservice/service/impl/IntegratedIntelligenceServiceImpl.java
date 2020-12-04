package cn.cnic.security.ipservice.service.impl;

import cn.cnic.security.ipservice.common.utils.IntelligenceEntity;
import cn.cnic.security.ipservice.dao.VxvaultDao;
import cn.cnic.security.ipservice.service.IntegratedIntelligenceService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IntegratedIntelligenceServiceImpl implements IntegratedIntelligenceService {

    /**
     *分页查询所有情报数据
     */
    @Autowired
    private VxvaultDao vxvaultDao;

    @Override
    public Page<IntelligenceEntity> getAllintelligence(Page<IntelligenceEntity> page) {

        return vxvaultDao.getPublicData(page);

    }

}
