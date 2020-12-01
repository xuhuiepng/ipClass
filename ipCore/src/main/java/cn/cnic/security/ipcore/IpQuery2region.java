package cn.cnic.security.ipcore;

import cn.cnic.security.ipcore.IpQuery;
import cn.cnic.security.ipcore.ip2region.DataBlock;
import cn.cnic.security.ipcore.ip2region.DbConfig;
import cn.cnic.security.ipcore.ip2region.DbMakerConfigException;
import cn.cnic.security.ipcore.ip2region.DbSearcher;
import cn.cnic.security.ipcore.model.IpLocation;
import org.apache.commons.codec.binary.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

/*
package cn.cnic.security.ipcore;

import cn.cnic.security.ipcore.ip2region.DataBlock;
import cn.cnic.security.ipcore.ip2region.DbConfig;
import cn.cnic.security.ipcore.ip2region.DbMakerConfigException;
import cn.cnic.security.ipcore.ip2region.DbSearcher;
import cn.cnic.security.ipcore.model.IpLocation;
import org.apache.commons.codec.binary.StringUtils;

import java.io.FileNotFoundException;


/**
 * 根据ip2region本地库查询
 */
public class IpQuery2region implements IpQuery {

    private DbSearcher _searcher;

    private final String Zero = "0";


/**
     * 默认使用 resources
     *
     * @throws DbMakerConfigException
     * @throws FileNotFoundException
     */
//    public IpQuery2region() throws DbMakerConfigException, FileNotFoundException {
//        this(IpQuery2region.class.getResource("/").getPath() + "ip2region.db");
//    }


/**
     * 读取本地db
     *
     * @param dbPath
     */

    public IpQuery2region(String dbPath) throws DbMakerConfigException, FileNotFoundException {
        _searcher = new DbSearcher(new DbConfig(), dbPath);
    }

    /**
     * 本地库查询ip
     * @param ip 信息
     * @return
     * @throws Exception
     */
    @Override
    public String localSearch(String ip) throws Exception{
        DataBlock  dataBlock = _searcher.memorySearch(ip);
        return dataBlock.getRegion();
    }

    /**
     * 格式返回点ip信息
     * @param line
     * @return
     */
    @Override
    public IpLocation formatData(String line) {
        if (line == null || line.length() == 0) return null;
        //切分数据    国家 |区域|省份 |城市|ISP_

        String[] split = line.split("\\|");
        if (split.length == 5) {
            IpLocation ipLocation = new IpLocation();
            ipLocation.setCountry(split[0]);
            if(!StringUtils.equals(Zero,split[1])) {
                ipLocation.setProvince(split[1]);
            }
            if(!(ipLocation.getProvince() == null && StringUtils.equals(Zero,split[2]))){
                ipLocation.setProvince(split[2]);
            }
            if(!StringUtils.equals(Zero,split[3])){
                ipLocation.setCity(split[3]);
            }
            if(!StringUtils.equals(Zero,split[4])){
                ipLocation.setIsp(split[4]);
            }

            return ipLocation;
        }
        return null;
    }

    @Override
    public IpLocation searchAndFormat(String ip) {
        try {
            String s = localSearch(ip);
            return formatData(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

