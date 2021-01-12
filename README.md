# ipClass
ip查询服务器

### 一、ipCore

>  ipCore不是web项目，不要按照MVC模式包命名。它是一个组件，帮助解决本地查询ip归属地问题。

必须实现的接口 cn.cnic.security.ipcore.IpQuery

例子：

```列如: 
class IpQueryByGeoip2 extends abstractXX implements IpQuery 
```

abstractXX只个预留，方便以后重构时，提取重复代码。

#### geoip更新本地数据库的方法如下：（2021/1/6更新）

1.注册maxmind账号

- 进入maxmind官网：https://www.maxmind.com/en/home

- 选择geoip

  <img src="img\download_db3.png" alt="Image text" style="zoom: 33%;" />

- 进入后选择free trial account 

  ![Image text](img\download_db4.png)

- 进入后进行账号注册

  ![Image text](img\download_db5.png)

2.注册成功后，会有免费的5美元额度用于网络请求，这里我们使用下载数据库的方式，暂时不采用网络请求。

进入个人账户，进行数据库下载

补充说明：有可能没有开通GeoIP2 / GeoLite2 服务，需要注册一下。已注册的账号无法截图了。

![Image text](img\download_db1.png)

3.选择需要的版本进行下载，这里我们下载geolite2 city（.mmdb）文件

![Image text](img\download_db2.png)

#### 纯真IP更新时间
更新时间：2020-12-19
地址：https://github.com/out0fmemory/qqwry.dat
http://update.cz88.net/ip/qqwry.rar
http://update.cz88.net/ip/copywrite.rar

#### ip2region更新时间
更新时间：2020-6-13
地址：https://gitee.com/lionsoul/ip2region/tree/v2.2.0-release/data



### 二、ipService

提供网络的查询的服务

#### 前端框架 layuimini

https://gitee.com/zhongshaofa/layuimini

要用单网页版

#### 注意：

* 远程接口的Service 都要实现RemoteIpSearch



#### 网页路径：

单个ip查询：/page/ipLocation.html

ip定位批量查询：/page/batch-ipsearch.html

IP的研究所归属批量：/page/batch-ipsegmentSearch.html

IP的信誉信息批量：/page/batch-ipsearchHistory.html





#### 1.提供单个ip查询功能

​	显示多个源的查询结果

路径：/page/ipLocation.html

#### 2.提供批量ip查询功能

##### 2.1 网页模式

路径：/page/batch-ipsearch.html

##### 2.2 上传模式

1) 功能介绍

上传有固定模板的ip.xlsx文件，根据文件中的ip进行批量查询，同时生成带有查询结果的xlsx文件，用户进行下载

**batchSearch前端页面如下：**

<img src="img\batchSearch-html.png" alt="Image text" style="zoom:80%;" />

（注：前端中，由于上传文件与下载文件为一个接口，所以采用XMLHttpRequest进行请求发送，不应采用ajax）

**ip.xlsx文件模板如下：**

![Image text](img\ips.png)

**查询结果文件样式如下：**

![Image text](img\ips-res.png)



该功能提供**远程库查询**以及**本地库查询**两种方式，其中本地库查询不做**单位时间请求次数**以及**单次查询数量限制**，而远程库设置**2分钟查询一次，一次查询1000条ip**的限制。

2) 核心类实现逻辑

**核心类图如下：**

<img src="img\batchSearchUML.png" alt="Image text" style="zoom: 80%;" />

该功能涉及到的主要类为BatchIpLocationController，BatchIpLocationService,主要业务逻辑如下：

- 本地库查询

  (1) 读取用户上传的excel文件，利用ipExcelService读取excel文件，获取ip查询列表

  (2) 利用本地库单点查询，循环查询，返回所有查询结果

  - 由于不同本地数据库的数据各有优缺点，所以本地库查询采用了多库混合查询，比较查询结果，进行结果判断，取较为准确的查询结果作为最终查询结果

  (3) 将所有查询结果写入到excel中，通过HttpServletRespose返回给前端进行下载

- 远程库查询

  (1) 读取用户上传的excel文件，利用ipExcelService读取excel文件，获取ip查询列表

  (2) 利用IPAPIHttpClient发送批量查询请求，但由于ip-api远程接口请求限制，即**每分钟只能发送15次批量查询请求**，**一次请求只能查询100条ip**,所以此处做出以下优化：

  - 取用户上传文件中前1000条数据进行查询（若总数不满1000条，则全部查询）
  - 将1000条数据分成10次查询，每次查询100条
  - 查询过程中，会由于远程接口限制，从而RestTemplate抛出RestClientException的异常，需要在Controller捕获该异常，同时返回接口被限制的提示
  - 为了最大化使用远程接口，对上传的文件中ip进行了去重处理，将去重后的ip进行查询，查询后，将查询结果复原，即包括重复的ip
  - 防止用户经常请求远程库接口，前端中，控制用户只能2分钟请求一次远程库接口

  (3) 将所有查询结果写入到excel中，通过HttpServletRespose返回给前端进行下载





#### 3.接口

##### **公共字段**

code:0 成功 1失败 5认证失败 9服务器异常

1失败：包括各种已知的失败，比如参数错误，等

msg: “success” 消息，失败的具体原因等。

**返回值的统一字段** 

​    "country": "国家",
​    "province": "省",
​    "city": "市",
​    "isp": "运行商",
​	"source": "使用点查询源 可选",
​	"lon":"经度",
​	"lat":"纬度",
​	"ip":"查询ip 可选" 

##### 3.1 ip查询(GET)

* url

> ipService/{ip}

* 参数
  source 查询ip所需源

  | 可选参数  | 描述            |      |
  | --------- | --------------- | ---- |
  | ip2region | 本地 java开源库 |      |
  | qqwry     | 纯真库          |      |
  | geoLite2  | geoip库         |      |
  | aMap      | 高德地图api查询 |      |
  | ip-api    | ip-api.com接口  |      |

* 返回值

```json
{
    "msg": "success",
    "country": "国家",
    "code": 0,
    "province": "省",
    "city": "市",
    "isp": "运行商",
	"source": "使用点查询源",
	"lon":"经度",
	"lat":"纬度",
	"ip":"查询ip"
}
```

##### 3.2 ip所属研究所查询(GET)
说明：excel文件在resource下
，并且第一行作为表头不会被读取
，所以实际cidr 从第二行开始

* url

> queryIp/{ip}

* 参数

* 返回值

```
{
    "msg": "success",
    "code": 0,
    "ipRangeEntity": {
        "ipRange": "ipv4号码段(cidr)",
        "orgName": "组织名称"
    }
}

{
    "msg": "库中未查到ip",
    "code": 9
}


```

##### 3.3 高德ip查询(GET)

* url

> /remote/amap-api

* 参数

ip

* 返回值

```
{
    "msg": "success",
    "code": 0,
    "GaodeIpApiResponse": {
        "status": "1",
        "info": "OK",
        "infocode": "10000",
        "province": "北京市",
        "city": "北京市",
        "adcode": "110000",
        "rectangle": "116.0119343,39.66127144;116.7829835,40.2164962"
    }
}


```

##### 3.4 批量查询-上传模式（POST）

- url

> /batchIpService

- 参数

| 参数名称 |           参数说明            | 请求类型 | 数据类型 |
| :------: | :---------------------------: | :------: | :------: |
|   type   |  数据库选择（remote/local）   |  query   |  string  |
|   file   | 查询的excel文件（.xls/.xlsx） | formData |   file   |

- 返回值

  - 查询成功，无返回值，浏览器直接下载文件

  - 查询失败

    ```jso
    {
    	"msg":"错误信息"
    	"code":1
    }
    ```
##### 3.5 综合评分查询

根据权重返回最大的城市信息。

- url
  GET: /queryAndSelect/{ip}

- 参数
无
- 返回值
```json
{
    "msg": "success",
    "country": "国家",
    "code": 0,
    "province": "省",
    "city": "市",
    "isp": "运行商",
	"source": "使用点查询源",
	"lon":"经度",
	"lat":"纬度",
	"ip":"查询ip"
}
```

##### 3.6 ip批量查询（POST）

IP批量查询，每次查询限100条，返回json数据，提供远程库以及本地库查询

- url

  > /batchIpService/json

- 参数

| 参数名称 |          参数说明          |     请求类型     | 数据类型 |
| :------: | :------------------------: | :--------------: | :------: |
|   ips    |        查询的ip数组        | application/json | string[] |
|   type   | 数据库选择（remote/local） |      query       |  string  |

- 发送示例

  <img src="img\batchsearch.png" alt="Image text" style="zoom:60%;"/>

- 返回值

```json
{
    "msg": "success",
    "ipLocations": [
        {
            "query": "159.226.16.183",
            "source": "ip2region",
            "country": "中国",
            "province": "北京",
            "city": "北京市",
            "lat": 39.9288,
            "lon": 116.3889,
            "isp": "科技网",
            "network": "159.226.0.0/17"
        },
        {
            "query": "210.72.7.200",
            "source": "ip2region",
            "country": "中国",
            "province": "广东省",
            "city": "广州市",
            "lat": 34.7725,
            "lon": 113.7266,
            "isp": "科技网",
            "network": "210.72.0.0/21"
        }
    ],
    "code": 0
}
```

##### 3.7 ip归属研究所（GET）

url

/queryIpBeLong/{ip}

参数

必填 路径参数,ip

返回值

```json
{"msg":"success","code":0,"org":{"queryIp":"210.72.7.200","orgName":"广州市电子政务中心"}}
```

##### 3.8 ip归属研究所批量查询（POST）

- url

  > /queryIpBelongByList

- 参数

  | 参数名称 |   参数说明   |     请求类型     | 数据类型 |
  | :------: | :----------: | :--------------: | :------: |
  |   ips    | 查询的ip数组 | application/json |   list   |

- 发送示例

  <img src="img\ipsegmentbatchsearch.png" alt="Image text" style="zoom:60%;"/>

- 返回结果

```json
{
    "msg": "success",
    "code": 0,
    "ipSegmentResponseList": [
        {
            "queryIp": "210.72.7.255",
            "orgName": "广州市电子政务中心"
        },
        {
            "queryIp": "159.226.130.255",
            "orgName": "中国科学院上海光学精密机械研究所"
        },
        {
            "queryIp": "210.72.71.255",
            "orgName": "中国科学院上海光学精密机械研究所"
        },
        {
            "queryIp": "1.1.223.14",
            "orgName": "库中未查到ip"
        }
    ]
}
```

##### 3.9 ip归属研究所批量查询-上传模式（POST）

- url

  > /queryIpBelongByList

- 参数

| 参数名称 |           参数说明            | 请求类型 | 数据类型 |
| :------: | :---------------------------: | :------: | :------: |
|   file   | 查询的excel文件（.xls/.xlsx） | formData |   file   |

- 返回值

  - 查询成功，无返回值，浏览器直接下载文件
  
  - 查询失败，返回json
  
    ```json
    {
    	"msg":"错误信息"
    	"code":1
    }
    ```
  
    

 