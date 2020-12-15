# IpService接口文档

​	为查询IPv4的定位信息、信誉信息和归属中科院院所问题。提供一系列的restful风格接口。

接口总览：

| 名称               | 路径                     | 描述                                               |
| ------------------ | ------------------------ | -------------------------------------------------- |
| 定位查询           | /location/{ip}           | GET                                                |
| 批量定位查询       | /location/group          | POST                                               |
| 综合评分定位查询   | /election/{ip}           | GET 对比全部接口返回值中的城市，选择权重最高的城市 |
| 信誉查询           | /credit/{ip}             | GET                                                |
| 信誉批量查询       | /credit/group            | POST                                               |
| 归属研究所         | /institute_inquire/{ip}  | GET                                                |
| 归属研究所批量查询 | /institute_inquire/group | POST                                               |
| 全部信息查询       | /ipInfo/{ip}             | GET                                                |

IP的定位信息主要有国家，城市，坐标，运行商。

IP 的信誉是由绿盟威胁情报科提供的对于IP安全情报的描述，分别有攻击类型，信誉评分，信誉等级。信誉评分和等级值越大威胁越大。

IP归属研究所是在（保障室付老师提供）所属IP段中查询IP是否为中科院研究所名下研究所。

注：{ip}是IP变量。



## 接口详细说明

### 公共字段说明

| 字段 | 值   | 说明         |
| :--- | :--- | :----------- |
| code | 0    | 0:成功       |
| code | 1    | 1:失败       |
| code | 5    | 5:认证失败   |
| code | 9    | 9:服务器错误 |
| msg  |      | 消息         |
| {ip} |      | ipv4         |

### 1. IP定位

返回IP定位信息字段

| 字段     | 说明         |
| :------- | ------------ |
| country  | 国家         |
| province | 省           |
| city     | 市           |
| isp      | 运行商       |
| source   | 使用的查询库 |
| lon      | 经度         |
| lat      | 纬度         |
| ip       | 查询ip       |

#### 1.1 定位查询 

- 路径：/location/{ip}      GET

- 参数

| 参数   | 是否必填                                 | 说明                                                         |
| ------ | ---------------------------------------- | ------------------------------------------------------------ |
| {ip}   | 必填                                     | 获得路径上的ipv4地址                                         |
| source | ip2region\|qqwry\|geoLite2\|aMap\|ip-api | 从指定库查询<br />ip2region开源库<默认>查询速度最快，但没有经纬度 <br />qqwry  纯真库<br />geoLite2  maxmind提供，有归宿IP段<br />aMap 高德地图sdk，不提供国外ip查询<br />ip-api  准确率最高，但每秒查询有限制 |

请求示例

1、http://159.226.16.34:8082/location/182.48.98.168

- 返回值

```json
{
  "msg": "success",
  "country": "中国",
  "code": 0,
  "province": "北京",
  "city": "北京市",
  "query": "182.48.98.168",
  "isp": "联通",
  "source": "ip2region"
}
```

2、 http://159.226.16.34:8082/location/182.48.98.168?source=qqwry

- 返回值

```json
{
  "msg": "success",
  "country": "中国",
  "code": 0,
  "city": "北京市海淀区",
  "query": "182.48.98.168",
  "isp": "北京舒华士科技有限公司",
  "source": "纯真"
}
```





#### 1.2 批量定位查询

- 路径：/location/group

- 参数

| 参数     | 是否必填             | 说明                                                         |
| -------- | -------------------- | ------------------------------------------------------------ |
| file     | 上传文件导入模式必填 | 上传模式需要上传excel文件。Content-Type:multipart/form-data  |
| json数组 | json方式必填         | Content-Type:application/json                                |
| type     | 必填                 | 本地库（local）一般建议<br />远程库（remote）限制，最高1000个ip，**注意：这个没有缓存，不要有重复ip** |

请求示例

1、上传文件导入模式：http://159.226.16.34:8082/page/batch-ipsearch.html

![image-20200907115903357](img\image-20200907115903357.png)



- 返回值：

自动下载查询后的文件

![image-20200907145711743](img\image-20200907145711743.png)



2、传入 的json字符串模式（需要指定type！）

http://159.226.16.34:8082/location/group?type=local

传入

```json
["93.91.80.6","185.152.67.14"]
```

- 返回值

```json
{
    "msg": "success",
    "ipLocations": [
        {
            "query": "93.91.80.6",
            "source": "ip2region",
            "country": "德国",
            "province": "黑森",
            "lat": 51.2993,
            "lon": 9.491,
            "network": "93.91.80.0/20"
        },
        {
            "query": "185.152.67.14",
            "source": "ip2region",
            "country": "英国",
            "lat": 34.0729,
            "lon": -118.2606,
            "network": "185.152.67.0/24"
        }
    ],
    "code": 0
}
```



- 错误：

未指定type的值

```json
{
    "msg": "查询出错",
    "code": -1
}
```



#### 1.3 综合评分定位查询 

- 路径：/election/{ip}

- 参数

| 参数 | 是否必填 |                       说明                       |
| :--: | :------: | :----------------------------------------------: |
|  ip  |   必填   | 对比全部接口返回值中的城市，选择权重最高的城市。 |

- 请求示例

http://159.226.16.34:8082/election/93.91.80.6

- 返回值

```json
{
    "msg": "success",
    "country": "德国",
    "code": 0,
    "province": "Hesse",
    "city": "法兰克福",
    "query": "93.91.80.6",
    "isp": "NYNEX satellite OHG",
    "lon": 8.68213,
    "source": "api",
    "lat": 50.1109,
    "network": "93.91.80.0/20"
}
```





### 2. 信誉查询

返回值IP今年（2020）的历史信息。

#### 2.1 信誉查询

- 路径：/credit/{ip}

- 参数

| 参数 | 是否必填 | 说明 |
| ---- | -------- | ---- |
| ip   | 必填     |      |



- 请求示例

 http://159.226.16.34:8082/credit/185.152.67.14 

- 返回值

  - 查询成功

  ```json
  {
      "msg": "success",
      "code": 0,
      "list": [
          {
              "historyDate": "2020-05-28 00:00:00",
              "ip": "185.152.67.14",
              "attackType": 4,
              "attackTypeCN": "Web攻击",
              "credit": 90,
              "creditLevel": 5,
              "source": "Open Source"
          },
          {
              "historyDate": "2020-06-01 00:00:00",
              "ip": "185.152.67.14",
              "attackType": 2,
              "attackTypeCN": "漏洞利用",
              "credit": 100,
              "creditLevel": 5,
              "source": "NSFOCUS Security Labs"
          },
          {
              "historyDate": "2020-06-07 00:00:00",
              "ip": "185.152.67.14",
              "attackType": 5,
              "attackTypeCN": "扫描探测",
              "credit": 60,
              "creditLevel": 5,
              "source": "NSFOCUS Security Labs"
          },
          {
              "historyDate": "2020-06-09 00:00:00",
              "ip": "185.152.67.14",
              "attackType": 0,
              "attackTypeCN": "其他",
              "credit": 10,
              "creditLevel": 5,
              "source": "NSFOCUS Security Labs"
          },
          {
              "historyDate": "2020-07-29 00:00:00",
              "ip": "185.152.67.14",
              "attackType": 4,
              "attackTypeCN": "Web攻击",
              "credit": 90,
              "creditLevel": 5,
              "source": "Open Source"
          },
          {
              "historyDate": "2020-08-20 00:00:00",
              "ip": "185.152.67.14",
              "attackType": 4,
              "attackTypeCN": "Web攻击",
              "credit": 90,
              "creditLevel": 5,
              "source": "NSFOCUS Security Labs"
          },
          {
              "historyDate": "2020-08-29 00:00:00",
              "ip": "185.152.67.14",
              "attackType": 4,
              "attackTypeCN": "Web攻击",
              "credit": 90,
              "creditLevel": 5,
              "source": "Open Source"
          },
          {
              "historyDate": "2020-09-01 00:00:00",
              "ip": "185.152.67.14",
              "attackType": 5,
              "attackTypeCN": "扫描探测",
              "credit": 80,
              "creditLevel": 5,
              "source": "NSFOCUS Security Labs"
          }
      ]
  }
  ```

  

  - 查询为空

  ```json
  {
      "msg": "success",
      "code": 0,
      "list": []
  }
  ```



#### 2.2 批量信誉查询

- 路径：/credit/group

- 参数

| 参数 | 参数说明 | 请求类型  |
| :--- | :------- | :-------- |
| file | 必填     | ip的excel |

- 请求示例

http://159.226.16.34:8082/page/batch-ipsearchHistory.html

![image-20200907153330714](img\image-20200907153330714.png)

- 返回值

  - 查询成功，无返回值，浏览器直接下载文件
  - 查询失败

  ![image-20200907153545709](img\image-20200907153545709.png)







### 3. IP归属

#### 3.1 归属研究所查询



- 路径：/institute_inquire/{ip}

- 参数

| 参数 | 是否必填 |      说明      |
| :--: | :------: | :------------: |
|  ip  |   必填   | 查询归属研究所 |

- 请求示例

http://159.226.16.34:8082/institute_inquire/203.196.0.1



- 返回值

成功

```json
{
    "msg": "success",
    "code": 0,
    "org": {
        "queryIp": "203.196.0.1",
        "orgName": "北京世纪互联宽带数据中心有限公司"
    }
}
```

失败

```json
{
    "msg": "库中未查到ip",
    "code": 1
}
```





#### 3.2 归属研究所批量查询

- 路径：/institute_inquire/group

- 参数

|   参数   |     是否必填     |               说明               |
| :------: | :--------------: | :------------------------------: |
|   file   |   上传模式必填   | Content-Type:multipart/form-data |
| json数组 | json查询方式必填 |  Content-Type:application/json   |

- 请求示例

http://159.226.16.34:8082/page/batch-ipsegmentSearch.html

![image-20200907153931930](img\image-20200907153931930.png)



- 返回值

成功自动下载文件

![image-20200907154156666](img\image-20200907154156666.png)



失败

![image-20200907154226682](img\image-20200907154226682.png)

### 4. 全部信息查询（不建议使用，只作为网页显示用）

- 路径：ipInfo/{ip}

- 参数

| 参数 | 是否必填 | 说明                                                         |
| :--- | :------- | :----------------------------------------------------------- |
| ip   | 必填     | 查询归属、ip2region、qqwry、geoLite2、aMap、ip-api、信誉历史 |

- 请求示例

http://159.226.16.34:8082/page/ipLocation.html

![image-20200907154452378](img\image-20200907154452378.png)



- 返回值

```json
{
    "msg": "success",
    "code": 0,
    "list": [
        {
            "query": "93.91.80.6",
            "source": "ip2region",
            "country": "德国",
            "province": "黑森",
            "lat": 0.0,
            "lon": 0.0
        },
        {
            "query": "93.91.80.6",
            "source": "纯真",
            "country": "德国",
            "city": "达姆施塔特NYNEX卫星通讯公司",
            "lat": 0.0,
            "lon": 0.0,
            "isp": "达姆施塔特NYNEX卫星通讯公司"
        },
        {
            "query": "93.91.80.6",
            "source": "geoLite2",
            "country": "德国",
            "lat": 51.2993,
            "lon": 9.491,
            "network": "93.91.80.0/20"
        },
        {
            "query": "93.91.80.6",
            "source": "高德",
            "lat": 0.0,
            "lon": 0.0
        },
        {
            "query": "93.91.80.6",
            "source": "IpApi",
            "country": "德国",
            "province": "Hesse",
            "city": "法兰克福",
            "lat": 50.1109,
            "lon": 8.68213,
            "isp": "NYNEX satellite OHG"
        }
    ],
    "ipSegmentResponses": [
        {
            "queryIp": "93.91.80.6",
            "orgName": "库中未查到ip"
        }
    ]
}
```







## 注意事项：

1.上传文件（xlsx）没有特殊说明都是从A1列开始读取。

2.上传文件的接口示例是网页端和接口路径要分开。

3.上传接口同是一个路径，用Content-Type来分辨请求是文件还是json。并只能用一种格式上传。