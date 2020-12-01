package cn.cnic.security.ipservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.cnic.security.ipservice.common.utils.PageUtils;
import cn.cnic.security.ipservice.entity.NtiHistoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 绿盟历史信息表
 *
 * @author xuhuipeng
 * @email xuhuipeng@cnic.com
 * @date 2020-08-13 17:26:33
 */
public interface NtiHistoryService extends IService<NtiHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     *  批量查询信誉历史
     *
     * @param ips
     * @return
     */
    List<NtiHistoryEntity> ntihistoryBatchIpQuery(List<String> ips);


    /**
     * 用配置文件读取库中百万条ip的方式优化批量查询
     * 缺点：拖慢程序启动速度
     */
    //List<NtiHistoryEntity> ntihistoryBatchIpQuery2(List<String> ips);
}

