package cn.cnic.security.ipservice.config;

import cn.cnic.security.ipcore.IpQuery2qqwry;
import cn.cnic.security.ipcore.IpQuery2region;
import cn.cnic.security.ipcore.IpQueryByGeoip2;
import cn.cnic.security.ipcore.ip2region.DbMakerConfigException;
import cn.cnic.security.ipservice.common.utils.IPUtils;
import cn.cnic.security.ipservice.common.utils.IpDataListener;
import cn.cnic.security.ipservice.dao.NtiHistoryDao;
import cn.cnic.security.ipservice.model.IpSegment;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * 配置本地的IP定位库
 * 主要就是ipcor里
 */
@Configuration
@Slf4j
public class LocationIpDBConfig {

    @Autowired
    private NtiHistoryDao ntiHistoryDao;

    @Bean(name = "ipQuery2region")
    public IpQuery2region getIpQuery2region() throws DbMakerConfigException, FileNotFoundException {
        String lib = "db/ip2region.db";
        String path = getPath(lib);
        if (log.isInfoEnabled()) {
            log.info("ipQuery2region 读取本地路径 {}", path);
        }
        return new IpQuery2region(path);
    }

    @Bean(name = "ipQuery2qqwry")
    public IpQuery2qqwry getIpQuery2qqwry() throws Exception {
        String lib = "db/qqwry.dat";
        String path = getPath(lib);
        if (log.isInfoEnabled()) {
            log.info("ipQuery2qqwry 读取本地路径 {}", path);
        }
        return new IpQuery2qqwry(path);
    }

    @Bean(name = "ipQueryByGeoip2")
    public IpQueryByGeoip2 getIpQueryByGeoip2() throws Exception {
        String lib = "db/GeoLite2-City.mmdb";
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(lib);
        if (log.isInfoEnabled()) {
            log.info("ipQueryByGeoip2 读取本地路径 {}", lib);
        }
        return new IpQueryByGeoip2(resourceAsStream);
    }

    /**
     * ip归属研究所读取本地文件
     *
     * @return
     */
    @Bean(name = "ipSegmentList")
    public List<IpSegment> getIpSegmentList() {
        String lib = "db/20200426institutesForm2.xlsx";
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(lib);
        //读取excel文件
        List<IpSegment> ipSegmentList = EasyExcel.read(resourceAsStream, IpSegment.class, new IpDataListener()).sheet().doReadSync();


        for (int i = 0; i < ipSegmentList.size(); i++) {
            if (ipSegmentList.get(i).getEndStr() != null
                    && ipSegmentList.get(i).getIpRange() != null
                    && IPUtils.isIPv4(ipSegmentList.get(i).getIpRange().split("/")[0])) {
                String startIp = ipSegmentList.get(i).getIpRange().split("/")[0];
                String endIp = ipSegmentList.get(i).getEndStr();
                //将ip转为long类型
                ipSegmentList.get(i).setStart(IPUtils.ip2long(startIp));
                ipSegmentList.get(i).setEnd(IPUtils.ip2long(endIp));

            } else {
                //移除不符合条件得元素
                ipSegmentList.remove(i);
                i--;
            }
        }
        //进行排序
        Collections.sort(ipSegmentList);
        if (log.isInfoEnabled()) {
            log.info("ipSegmentList 配置完成,共{}条数据", ipSegmentList.size());
        }
        return ipSegmentList;
    }


    /**
     * 将库中的信誉历史的ip都读到list中
     */
//    @Bean(name = "ntiHistoryIpSet")
//    public Set<String> getNtiHistoryIpSet(){
//        Set<String> ntiHistoryIpSet = ntiHistoryDao.selectAllDistinctIp();
//        if (log.isInfoEnabled()) {
//            log.info("ntiHistoryIpSet 配置完成共{}条",ntiHistoryIpSet.size());
//            log.info("RamUsageEstimator数据大小：{}",RamUsageEstimator.sizeOf(ntiHistoryIpSet)/(double)1024/1024 + "mb");
//        }
//        return ntiHistoryIpSet;
//    }

    // 部署时，遇到读取不到配置文件

    /**
     * 获得绝对路径
     *
     * @param path
     * @return
     */
    public String getPath(String path) {

        String projectPath = this.getClass().getResource("/").getPath() + path;
        File file = new File(projectPath);
        if (file.exists()) {
            return projectPath;
        } else {

            //创建父类文件夹
            File copyParentPath = new File(path.substring(0, path.lastIndexOf("/")));
            if (!copyParentPath.exists()) {
                copyParentPath.mkdirs();
            }

            //读写文件
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);

            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(path);
                long l = 0;
                byte[] buf = new byte[8192];
                int n;
                while ((n = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, n);
                    l += n;
                }
                log.info("复制{}文件完成 {}Mb", path, l / 1024 / 1024);
                return path;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }


}
