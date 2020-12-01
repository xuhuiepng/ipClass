package cn.cnic.security.ipservice.service.impl;

import cn.cnic.security.ipservice.common.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cnic.security.ipservice.common.utils.PageUtils;
import cn.cnic.security.ipservice.common.utils.Query;

import cn.cnic.security.ipservice.dao.NtiHistoryDao;
import cn.cnic.security.ipservice.entity.NtiHistoryEntity;
import cn.cnic.security.ipservice.service.NtiHistoryService;


@Service("ntiHistoryService")
@Slf4j
public class NtiHistoryServiceImpl extends ServiceImpl<NtiHistoryDao, NtiHistoryEntity> implements NtiHistoryService {

//    @Autowired
//    @Qualifier("ntiHistoryIpSet")
//    private Set<String> ntiHistoryIpSet;
    @Autowired
    private NtiHistoryDao ntiHistoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<NtiHistoryEntity> page = this.page(
                new Query<NtiHistoryEntity>().getPage(params),
                new QueryWrapper<NtiHistoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 批量查询信誉历史
     *
     * @param ips
     * @return
     */
//    public List<NtiHistoryEntity> ntihistoryBatchIpQuery(List<String> ips) {
//        List<NtiHistoryEntity> ntiHistoryEntities = new ArrayList<>();
//        List<String> ips_temp =new ArrayList<>();
//        for (int i = 0; i < ips.size(); i++) {
//            ips_temp.add(ips.get(i));
//            //添加到临时ips，达到100次或者达到ips的最后一条进行一次查询
//            if(ips_temp.size()==100 || i+1==ips.size()){
//                //执行查询
//                List<NtiHistoryEntity> nti_temp = this.ntiHistoryDao.selectAll(ips_temp);
//                for (NtiHistoryEntity ntiHistoryEntity: nti_temp) {
//                    ntiHistoryEntity = attackCode2CN(ntiHistoryEntity);
//                    ntiHistoryEntity = formatDate(ntiHistoryEntity);
//                    ntiHistoryEntities.add(ntiHistoryEntity);
//                }
//                //清空ips_temp
//                ips_temp.clear();
//            }
//        }
//
//        //把有数据的ip获取出来=》用于后面插入重复数据比对ip
//        List<String> hasDataIpList = new ArrayList<>();
//        Map<String,NtiHistoryEntity> cacheList = new HashMap<>();
//        for (int i = 0; i < ntiHistoryEntities.size(); i++) {
//            hasDataIpList.add(ntiHistoryEntities.get(i).getIp());
//        }
//
//        //添加空行和重复ip的数据
//        for (int i = 0; i < ips.size(); i++) {
//            //如果查到的nitEntitys没数据 || 要在尾部插入空记录
//            if(ntiHistoryEntities.size()==0 || i==ntiHistoryEntities.size()){
//                //如果有重复的，单独查出来添加进去
//                if(hasDataIpList.contains(ips.get(i))){
//                    NtiHistoryEntity repeatNtiEntity = ntiHistoryDao.selectRepeat(ips.get(i));
//                    repeatNtiEntity = attackCode2CN(repeatNtiEntity);
//                    repeatNtiEntity = formatDate(repeatNtiEntity);
//                    ntiHistoryEntities.add(i, repeatNtiEntity);
//                }else{
//                    ntiHistoryEntities.add(new NtiHistoryEntity(ips.get(i)));
//                }
//                continue;
//            }
//
//            //如果当前ntiEntity的ip与ips中的ip不等则插入
//            if(!ntiHistoryEntities.get(i).getIp().equals(ips.get(i))){
//                //如果有重复的，单独查出来添加进去
//                if(hasDataIpList.contains(ips.get(i))){
//                    NtiHistoryEntity repeatNtiEntity = ntiHistoryDao.selectRepeat(ips.get(i));
//                    repeatNtiEntity = attackCode2CN(repeatNtiEntity);
//                    repeatNtiEntity = formatDate(repeatNtiEntity);
//                    ntiHistoryEntities.add(i, repeatNtiEntity);
//                    continue;
//                }
//                ntiHistoryEntities.add(i, new NtiHistoryEntity(ips.get(i)));
//            }
//        }
//        return ntiHistoryEntities;
//    }

    /**
     * map改
     *
     * @param ips
     * @return
     */
    public List<NtiHistoryEntity> ntihistoryBatchIpQuery(List<String> ips) {

        List<NtiHistoryEntity> ntiHistoryEntities = new ArrayList<>();
        Map<String,NtiHistoryEntity> map = new HashMap<>();
        //读取ips  100个查询一次 然后清空
        List<String> readList = new ArrayList<>();

        for (int i = 0; i < ips.size(); i++) {
            readList.add(ips.get(i));
            //进行一次查询
            if(readList.size()==100 || i+1 == ips.size()){
                List<NtiHistoryEntity> ntiList = ntiHistoryDao.selectAll(readList);
                for (NtiHistoryEntity ntiEntity : ntiList) {
                    ntiEntity = attackCode2CN(ntiEntity);
                    ntiEntity = formatDate(ntiEntity);
                    //加入map
                    map.put(ntiEntity.getIp(), ntiEntity);
                }
                //清空
                readList.clear();
            }
        }

        //遍历ips 1、ip重复并能查到  2、没查到的添加空
        for (int i = 0; i < ips.size(); i++) {
            String ip = ips.get(i);
            //1.ip重复并能查到
            if(map.containsKey(ip)){
                ntiHistoryEntities.add(i,map.get(ip));
            }else {//不重复 也说明没查到数据（因为查到的都放到map里了）
                map.put(ip, new NtiHistoryEntity(ip));
                ntiHistoryEntities.add(i, map.get(ip));
            }
        }
        return ntiHistoryEntities;
    }



    @Deprecated
    @Cacheable(cacheNames = "ntiHistoryQuickAccess",key = "#ip")
    public List<NtiHistoryEntity> queryCacheFirst(String ip) {
        List<NtiHistoryEntity> ntiHistoryEntities = new ArrayList<>();

        QueryWrapper<NtiHistoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ip", ip)
                .select(NtiHistoryEntity.class,i->!i.getProperty().equals("attackTypeCN"));
        List<NtiHistoryEntity> temp_list = this.list(queryWrapper);

        //log.info("ip = {} {}",ip,temp_list);
        if(temp_list.size()!=0){
            for (NtiHistoryEntity ntiEntity : temp_list) {
                NtiHistoryEntity ntiHistoryEntity = attackCode2CN(ntiEntity);
                NtiHistoryEntity ntiHistoryEntityFormatDate = formatDate(ntiHistoryEntity);
                ntiHistoryEntities.add(ntiHistoryEntityFormatDate);
            }
        }
        return ntiHistoryEntities;
    }

    /**
     * 将信誉历史攻击码转换中文解释
     * @param resEntity
     * @return
     */
    public NtiHistoryEntity attackCode2CN(NtiHistoryEntity resEntity){
        switch (resEntity.getAttackType()){
            case 0:
                resEntity.setAttackTypeCN("其他");
                break;
            case 1:
                resEntity.setAttackTypeCN("拒绝服务");
                break;
            case 2:
                resEntity.setAttackTypeCN("漏洞利用");
                break;
            case 3:
                resEntity.setAttackTypeCN("垃圾邮件");
                break;
            case 4:
                resEntity.setAttackTypeCN("Web攻击");
                break;
            case 5:
                resEntity.setAttackTypeCN("扫描探测");
                break;
            case 6:
                resEntity.setAttackTypeCN("僵尸主机");
                break;
            case 7:
                resEntity.setAttackTypeCN("恶意软件");
                break;
            case 8:
                resEntity.setAttackTypeCN("钓鱼与欺诈");
                break;
            case 9:
                resEntity.setAttackTypeCN("代理");
                break;
            case 10:
                resEntity.setAttackTypeCN("C2主机");
                break;
            case 11:
                resEntity.setAttackTypeCN("矿池");
                break;
            case 12:
                resEntity.setAttackTypeCN("矿机");
                break;
            case 13:
                resEntity.setAttackTypeCN("撞库");
                break;
            case 14:
                resEntity.setAttackTypeCN("暴力破解");
                break;
            case 101:
                resEntity.setAttackTypeCN("白名单");
                break;
            default:
                resEntity.setAttackTypeCN("不在分类中");
                break;
        }
        return resEntity;
    }

    /**
     * 将日期格式化(去除具体时分秒，只保留年月日)
     * @param ntiEntity
     * @return
     */
    public NtiHistoryEntity formatDate(NtiHistoryEntity ntiEntity) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date historyDate = ntiEntity.getHistoryDate();
        String dateStr = sdf.format(historyDate);
        ntiEntity.setHistoryDateStr(dateStr);
        return ntiEntity;
    }



//        public List<NtiHistoryEntity> ntihistoryBatchIpQuery2(List<String> ips) {
//
//        long start = System.currentTimeMillis();
//        List<NtiHistoryEntity> ntiHistoryEntities = new ArrayList<>();
//        for (String ip : ips) {
//            if(ntiHistoryIpSet.contains(ip)){
//                List<NtiHistoryEntity> queryCacheList = queryCacheFirst(ip);
//                ntiHistoryEntities.addAll(queryCacheList);
//            }else{
//                NtiHistoryEntity ntiHistoryEntity = new NtiHistoryEntity();
//                ntiHistoryEntity.setIp(ip);
//                ntiHistoryEntity.setAttackTypeCN("未查询到该ip");
//                ntiHistoryEntities.add(ntiHistoryEntity);
//            }
//
//        }
//        long end = System.currentTimeMillis();
//        log.info("批量查询信誉历史耗时{}ms",end-start);
//        return ntiHistoryEntities;
//    }
}