package cn.cnic.security.ipservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wq
 * @date 2020-09-26 14:57
 */
@Data
@NoArgsConstructor
public class NewIpLocationEntity {

    /**
     * 地区编码
     */
    private String area_code;
    /**
     * 城市
     */
    private String city;
    /**
     * 国家代码
     */
    private String countryCode;
    /**
     * 国家名称
     */
    private String countryName;
    /**
     *
     */
    private String dma_code;

    /**
     * 城市全称
     */
    private String fullCity;

    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 地铁编号
     */
    private String metro_code;
    /**
     * 组织id
     */
    private String orgId;
    /**
     * 邮政编码
     */
    private String postalCode;
    /**
     * 地区
     */
    private String region;

}
