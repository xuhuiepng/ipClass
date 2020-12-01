package cn.cnic.security.ipservice.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author wq
 * @date 2020-07-23 16:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GaodeIpApiResponseEntity implements Serializable {
    /**
     * 查询ip
     */
    @ExcelProperty(value = "query",index = 0)
    private String query;
    /**
     * 请求状态
     * 值为0或1,0表示失败；1表示成功
     */
    @ExcelIgnore
    private String status;
    /**
     * 请求说明
     * 返回状态说明，status为0时，info返回错误原因，否则返回“OK”。
     */
    @ExcelIgnore
    private String info;
    /**
     * 状态码
     * 返回状态说明,10000代表正确,详情参阅info状态表
     */
    @ExcelIgnore
    private String infocode;
    /**
     * 省份名称
     *
     *  若为直辖市则显示直辖市名称；
     * 如果在局域网 IP网段内，则返回“局域网”；
     * 非法IP以及国外IP则返回空
     */
    @ExcelProperty(value = "province",index = 1)
    private String province;
    /**
     * 城市名称
     *
     * 若为直辖市则显示直辖市名称；
     * 如果为局域网网段内IP或者非法IP或国外IP，则返回空
     */
    @ExcelProperty(value = "city",index = 2)
    private String city;
    /**
     * 城市abcd编码
     */
    @ExcelIgnore
    private String adcode;
    /**
     * 所在城市矩形区域范围,所在城市范围的左下右上对标对
     *
     */
    @ExcelIgnore
    private String rectangle;

    /**
     * 经度
     */
    @ExcelProperty(value = "lat",index = 3)
    private Double lat;
    /**
     * 纬度
     */
    @ExcelProperty(value = "lon",index = 4)
    private Double lon;

}
