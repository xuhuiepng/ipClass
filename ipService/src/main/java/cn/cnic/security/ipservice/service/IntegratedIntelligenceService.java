package cn.cnic.security.ipservice.service;

import cn.cnic.security.ipservice.common.utils.IntelligenceEntity;
import cn.cnic.security.ipservice.entity.CnvdSpiderEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


public interface IntegratedIntelligenceService {

    /**
     *@param pagenum 要查询的页序号
     * @return 返回所有表的公共字段值
     *分页查询7个表的所有情报数据
     */
    public Page<IntelligenceEntity> getAllintelligence(Page<IntelligenceEntity> page);

}
