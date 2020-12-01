package cn.cnic.security.ipservice.service;

import cn.cnic.security.ipcore.model.IpLocation;
import org.springframework.cache.annotation.Cacheable;

/**
 * 通过网络查询ip定位信息
 */
public interface RemoteIpSearch {

    /**
     * 查询ip接口
     * 返回json字符串
     * @param ip
     * @return
     */
    String query(String ip);

    /**
     * 格式ip的信息
     * @param line
     * @return
     */
    IpLocation formatJsonResult(String line);

    /**
     * 优先从缓存获得
     * @param ip
     * @return
     */
    IpLocation quickAccess(String ip);
}
