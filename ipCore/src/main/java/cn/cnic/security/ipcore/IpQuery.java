package cn.cnic.security.ipcore;


import cn.cnic.security.ipcore.model.IpLocation;

/**
 * 所有ipcore的实现必须继承
 * 便于管理
 */
public interface IpQuery {


    /**
     * 从查询本地库检索
     * @param ip 信息
     * @return
     */

    String localSearch(String ip) throws Exception;

    /**
     * 格式ip的信息
     * @param result
     * @return
     */
    IpLocation formatData(String result);

    /**
     * 本地检索ip信息，并格式化
     * @param ip
     * @return IpLocation
     */
    IpLocation searchAndFormat(String ip) throws Exception;
}
