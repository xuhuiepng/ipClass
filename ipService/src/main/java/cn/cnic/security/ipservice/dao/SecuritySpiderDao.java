package cn.cnic.security.ipservice.dao;

import cn.cnic.security.ipservice.entity.RedqueenEntity;
import cn.cnic.security.ipservice.entity.SecuritySpiderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * security.360.cn
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-11-23 16:22:15
 */
@Mapper
@Component
public interface SecuritySpiderDao extends BaseMapper<SecuritySpiderEntity> {
    public Page<SecuritySpiderEntity> getSecuritySpiderDao(Page<SecuritySpiderEntity> page, Date startTime, Date endTime, String content, String level);
}
