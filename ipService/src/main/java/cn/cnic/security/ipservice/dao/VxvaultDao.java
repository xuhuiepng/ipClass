package cn.cnic.security.ipservice.dao;

import cn.cnic.security.ipservice.common.utils.IntelligenceEntity;
import cn.cnic.security.ipservice.entity.RedqueenEntity;
import cn.cnic.security.ipservice.entity.VxvaultEntity;
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
public interface VxvaultDao extends BaseMapper<VxvaultEntity> {
    public Page<VxvaultEntity> getVxvaultDao(Page<VxvaultEntity> page, Date startTime, Date endTime, String content, String ip);
    /**
     *查询下标限定在start和end之间的数据
     */
    public List<IntelligenceEntity> getPublicData(Integer start, Integer size);
    /**
     *查询数据条数
     */
    public int count();
}
