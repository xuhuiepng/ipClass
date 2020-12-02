package cn.cnic.security.ipservice.dao;

import cn.cnic.security.ipservice.entity.RedqueenEntity;
import cn.cnic.security.ipservice.entity.SeebugSpiderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-11-23 16:22:15
 */
@Mapper
@Component
public interface SeebugSpiderDao extends BaseMapper<SeebugSpiderEntity> {
    public Page<SeebugSpiderEntity> getSeebugSpiderDao(Page<SeebugSpiderEntity> page, Date startTime, Date endTime, String content, String type);
}
