package cn.cnic.security.ipcore;

import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipcore.qqwry.IPqqwry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wq
 * @email 1013893214@qq.com
 * @date 2020-07-17 12:37
 */
public class IpQuery2qqwry implements IpQuery {

    private IPqqwry iPqqwry = null;

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public String localSearch(String ip) throws Exception {
        IpLocation locationInfo = iPqqwry.fetchIPLocation(ip);
        return mapper.writeValueAsString(locationInfo);
    }

    public IpQuery2qqwry(String filePath) throws Exception {
        this.iPqqwry = new IPqqwry(filePath);
    }

    public IPqqwry getiPqqwry() {
        return iPqqwry;
    }

    public void setiPqqwry(IPqqwry iPqqwry) {
        this.iPqqwry = iPqqwry;
    }

    @Override
    public IpLocation formatData(String result) {

        try{
            return mapper.readValue(result,IpLocation.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IpLocation searchAndFormat(String ip) throws Exception {
        IpLocation locationInfo = iPqqwry.fetchIPLocation(ip);
        String result = mapper.writeValueAsString(locationInfo);
        try{
            return mapper.readValue(result,IpLocation.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
