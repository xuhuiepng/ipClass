package cn.cnic.security.ipservice.service.impl;

import cn.cnic.security.ipservice.common.utils.IntelligenceEntity;
import cn.cnic.security.ipservice.dao.VxvaultDao;
import cn.cnic.security.ipservice.service.IntegratedIntelligenceService;
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
    public List<IntelligenceEntity> getAllintelligence(Integer pagenum, Integer size) {

        //获取数据总数目
        int count = vxvaultDao.count();
        //若当前页数超过最大页数时的处理
        if(count%size==0&&(count/size)<pagenum)
            pagenum = count/size;
        else if(count%size!=0&&((count/size)+1)<pagenum)
            pagenum = (count/size)+1;

        int start = (pagenum-1)*size;
        //存储最终获取的当前分页的所有数据
        return vxvaultDao.getPublicData(start,size);
    }

}
