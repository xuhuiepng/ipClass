package cn.cnic.security.ipcore;

import cn.cnic.security.ipcore.geoip2.GeoipQuery;
import cn.cnic.security.ipcore.model.IpLocation;
import cn.cnic.security.ipcore.parser.AbstractJsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;

import java.io.IOException;
import java.io.InputStream;


public class IpQueryByGeoip2 extends AbstractJsonParser implements IpQuery {


    private GeoipQuery geoipQuery;

    //中文：zh-CN，英语：en
    private String language = "zh-CN";

    private static ObjectMapper mapper = new ObjectMapper();


    /**
     * 构造本地查询
     * @param dbPath (geoip数据库本地文件地址)
     * @throws IOException
     */
    public IpQueryByGeoip2(String dbPath) throws IOException {
       this.geoipQuery = new GeoipQuery(dbPath);
    }

    public IpQueryByGeoip2 (InputStream inputStream) throws IOException {
        this.geoipQuery = new GeoipQuery(inputStream);
    }

    /**
     * 提供无参构造函数
     */
    public IpQueryByGeoip2(){}


    @Override
    public String localSearch(String ip) throws Exception {
        return search(ip).toJson();
    }

    @Override
    public IpLocation formatData(String result) {
        try{
            return mapper.readValue(result,IpLocation.class);
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public IpLocation searchAndFormat(String ip) throws IOException, GeoIp2Exception {
        CityResponse response = null;
        IpLocation ipLocation = new IpLocation();
//        try {
//
//          response = search(ip);
//        } catch (IOException | GeoIp2Exception e) {
//            //e.printStackTrace();
//            throw e;
//        }

        response = search(ip);

        Country country = response.getCountry();
        Subdivision subdivision = response.getMostSpecificSubdivision();
        City city = response.getCity();
        Location location = response.getLocation();

        ipLocation.setCountry(country.getNames().get(language));
        ipLocation.setProvince(subdivision.getNames().get(language));
        ipLocation.setCity(city.getNames().get(language));
        ipLocation.setLat(location.getLatitude());
        ipLocation.setLon(location.getLongitude());
        ipLocation.setNetwork(response.getTraits().getNetwork().toString());
        return ipLocation;
    }

    public CityResponse search(String ip) throws IOException, GeoIp2Exception {
        return  geoipQuery.getGeoipCityResponse(ip);
    }

    /**
     * 设置语言
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    public void setGeoipQuery(GeoipQuery geoipQuery) {
        this.geoipQuery = geoipQuery;
    }
}
