package cn.cnic.security.ipservice.dao;

import cn.cnic.security.ipservice.entity.NtiHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 绿盟历史信息表
 * 
 * @author xuhuipeng
 * @email xuhuipeng@cnic.com
 * @date 2020-08-13 17:26:33
 */
@Mapper
@Repository
public interface NtiHistoryDao extends BaseMapper<NtiHistoryEntity> {
    @Select("SELECT DISTINCT nti_history.ip FROM nti_history")
    Set<String> selectAllDistinctIp();

    List<NtiHistoryEntity> selectAll(@Param("ipsList") List<String> ips);

}
