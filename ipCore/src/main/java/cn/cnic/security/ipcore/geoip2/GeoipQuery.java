package cn.cnic.security.ipcore.geoip2;


import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;


/**
 * @author baokang
 * @date 2020/7/28 11:45 查询出错
 */
public class GeoipQuery {

    private DatabaseReader reader;


    public GeoipQuery(String dbPath) throws IOException {
        File file = new File(dbPath);
        this.reader = new DatabaseReader
                .Builder(file)
                .build();
    }

    public GeoipQuery(InputStream inputStream) throws IOException {
        this.reader = new DatabaseReader
                .Builder(inputStream)
                .build();
    }

    public CityResponse getGeoipCityResponse(String ip) throws AddressNotFoundException, GeoIp2Exception, IOException {
        InetAddress byName = InetAddress.getByName(ip);
        return reader.city(byName);
    }


}
