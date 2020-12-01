package cn.cnic.security.ipcore.qqwry;

import cn.cnic.security.ipcore.model.IpLocation;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 纯真ip库
 * @Description:ip定位(使用byte数据方式读取)
 * @author:difeng
 * @date:2016年12月13日
 */
public class IPqqwry {
	
	private  byte[] data;
	
	private  long firstIndexOffset;
	
	private  long lastIndexOffset;
	
	private  long totalIndexCount;
	
	private static final byte REDIRECT_MODE_1 = 0x01;
	
	private static final byte REDIRECT_MODE_2 = 0x02;
	
	static   final long IP_RECORD_LENGTH = 7;
	
	private static ReentrantLock lock = new ReentrantLock();
	
	private static Long lastModifyTime = 0L;

	public static boolean enableFileWatch = false;
	
	private File qqwryFile;
	
	public IPqqwry(String  filePath) throws Exception {
		this.qqwryFile = new File(filePath);
		load();
		if(enableFileWatch){
			watch();
		}
	}
	
    private void watch() {
    	Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				long time = qqwryFile.lastModified();
				if (time > lastModifyTime) {
					lastModifyTime = time;
					try {
						load();
						//System.out.println("reload");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, 1000L, 5000L, TimeUnit.MILLISECONDS);
    }
    
	private void load() throws Exception {
		lastModifyTime = qqwryFile.lastModified();
		ByteArrayOutputStream out = null;
		FileInputStream in = null;
		lock.lock();
		try {
			out = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			in = new FileInputStream(qqwryFile);
			while(in.read(b) != -1){
				out.write(b);
			}
			data = out.toByteArray();
			firstIndexOffset = read4ByteAsLong(0);
			lastIndexOffset = read4ByteAsLong(4);
			totalIndexCount = (lastIndexOffset - firstIndexOffset) / IP_RECORD_LENGTH + 1;
			in.close();
			out.close();
		} finally {
			try {
				if(out != null) {
					out.close();
				}
				if(in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			lock.unlock();
		}
	}
	
	private long read4ByteAsLong(final int  offset) {
		long val = data[offset] & 0xFF;
		val |= (data[offset + 1] << 8L) & 0xFF00L;
		val |= (data[offset + 2] << 16L) & 0xFF0000L;
		val |= (data[offset + 3] << 24L) & 0xFF000000L;
		return val;
	}

	private long read3ByteAsLong(final int offset) {
		long val = data[offset] & 0xFF;
		val |= (data[offset + 1] << 8) & 0xFF00;
		val |= (data[offset + 2] << 16) & 0xFF0000;
		return val;
	}
    
	private long search(long ip) {
		long low = 0;
		long high = totalIndexCount;
		long mid = 0;
		while(low <= high){
			mid = (low + high) >>> 1 ;
		    long indexIP = read4ByteAsLong((int)(firstIndexOffset + (mid - 1) * IP_RECORD_LENGTH));
	        long indexIPNext = read4ByteAsLong((int)(firstIndexOffset + mid * IP_RECORD_LENGTH));
		    if(indexIP <= ip && ip < indexIPNext) {
		    	return read3ByteAsLong((int)(firstIndexOffset + (mid - 1) * IP_RECORD_LENGTH + 4));
		    } else {
		    	if(ip > indexIP) {
				    low = mid + 1;
				} else if (ip < indexIP) {
				    high = mid - 1;
				}
		    }
		}
		return -1;
	}
	
	public IpLocation fetchIPLocation(String ip) {
		long numericIp = inet_pton(ip);
		lock.lock();
		long offset = search(numericIp);
		try{
			if(offset != -1) {
				IpLocation ipLoc = readIPLocation((int) offset);
				ipLoc.setQuery(ip);
				return ipLoc;
			}
		} finally {
		    lock.unlock();
		}
		return null;
	}
	
	private IpLocation readIPLocation(final int offset) {
		final IpLocation ipLoc = new IpLocation();
		try {
			byte redirectMode = data[offset + 4];
			if (redirectMode == REDIRECT_MODE_1) {
				long countryOffset = read3ByteAsLong((int)offset + 5);
				redirectMode = data[(int)countryOffset];
				if (redirectMode == REDIRECT_MODE_2) {
					final QQwryString country = readString((int)read3ByteAsLong((int)countryOffset + 1));
					ipLoc.setCountry(country.string);
					countryOffset = countryOffset + 4;
				} else {
					final QQwryString country = readString((int)countryOffset);
					ipLoc.setCountry(country.string);
					countryOffset += country.byteCountWithEnd;
				}
				ipLoc.setCity(readArea((int)countryOffset));
			} else if (redirectMode == REDIRECT_MODE_2) {
				ipLoc.setCountry(readString((int)read3ByteAsLong((int)offset + 5)).string);
				ipLoc.setCity(readArea((int)offset + 8));
			} else {
				final QQwryString country = readString((int)offset + 4);
				ipLoc.setCountry(country.string);
				ipLoc.setCity(readArea((int)offset + 4 + country.byteCountWithEnd));
			}
			//查询国外ip的city就是运营商
			ipLoc.setIsp(ipLoc.getCity());
			if(!isInCountries(ipLoc.getCountry())){
				ipLoc.setCity(ipLoc.getCountry());
				ipLoc.setCountry("中国");
			}
			return ipLoc;
		} catch (Exception e) {
			return null;
		}
	}



	private String readArea(final int offset) {
		byte redirectMode = data[offset];
		if (redirectMode == REDIRECT_MODE_1 || redirectMode == REDIRECT_MODE_2) {
			long areaOffset = read3ByteAsLong((int)offset + 1);
			if (areaOffset == 0) {
				return "";
			} else {
				return readString((int)areaOffset).string;
			}
		} else {
			return readString(offset).string;
		}
	}
	
	private QQwryString readString(int offset) {
		int pos = offset;
		final byte[] b = new byte[128];
		int i;
		for (i = 0, b[i] = data[pos++]; b[i] != 0; b[++i] = data[pos++]);
		try{
			   return new QQwryString(new String(b,0,i,"GBK"),i + 1);
		} catch(UnsupportedEncodingException e) {
			return new QQwryString("",0);
		}
	}
	
	 /**
     * @Description:“.”号分隔的字符串转换为long类型的数字
     * @param ipStr 
     * @return:long
     */
	private static long inet_pton(String ipStr) {
		if(ipStr == null){
			throw new NullPointerException("ip不能为空");
		}
		String [] arr = ipStr.split("\\.");
		long ip = (Long.parseLong(arr[0])  & 0xFFL) << 24 & 0xFF000000L;
		ip |=  (Long.parseLong(arr[1])  & 0xFFL) << 16 & 0xFF0000L;
		ip |=  (Long.parseLong(arr[2])  & 0xFFL) << 8 & 0xFF00L;
		ip |=  (Long.parseLong(arr[3])  & 0xFFL);
		return ip;
	}

	private boolean isInCountries(String country) {
		for (int i = 0; i < countries.length; i++) {
			if(countries[i].equals(country)){
				return true;
			}
		}
		return false;
	}

	public static final String[] countries = {
			"美国","日本","德国","英国","韩国","巴西","法国","加拿大","澳大利亚",
			"意大利","荷兰","俄罗斯","印度","西班牙","瑞典","墨西哥","南非","比利时",
			"埃及","波兰","瑞士","阿根廷","印尼","哥伦比亚","土耳其","越南","挪威",
			"新加坡","芬兰","伊朗","丹麦","摩洛哥","奥地利","乌克兰","沙特阿拉伯","智利",
			"捷克","泰国","罗马尼亚","塞舌尔","突尼斯","以色列","新西兰","委内瑞拉","爱尔兰",
			"葡萄牙","马来西亚","肯尼亚","匈牙利","希腊","菲律宾","巴基斯坦","阿尔及利亚","保加利亚",
			"阿联酋","秘鲁","尼日利亚","哈萨克斯坦","斯洛伐克","厄瓜多尔","斯洛文尼亚","毛里求斯","哥斯达黎加","立陶宛",
			"乌拉圭","克罗地亚","塞尔维亚","加纳","科威特","苏丹","拉脱维亚","白俄罗斯","孟加拉","巴拿马",
			"科特迪瓦","赞比亚","多米尼加","卢森堡","乌干达","爱沙尼亚","摩尔多瓦","格鲁吉亚","安哥拉","叙利亚",
			"玻利维亚","巴拉圭","坦桑尼亚","阿曼","波多黎各","塞浦路斯","冰岛","卡塔尔","波黑","阿塞拜疆",
			"巴勒斯坦","马其顿","伊拉克","喀麦隆","萨尔瓦多","约旦","马耳他","亚美尼亚","危地马拉","黎巴嫩",
			"马达加斯加","斯里兰卡","尼泊尔","洪都拉斯","马拉维","特立尼达和多巴哥","纳米比亚","巴林","莫桑比克","利比亚",
			"加蓬","阿尔巴尼亚","尼加拉瓜","塞内加尔","柬埔寨","留尼汪","埃塞俄比亚","多哥","布基纳法索","卢旺达",
			"吉尔吉斯斯坦","冈比亚","古巴","蒙古国","乌兹别克斯坦","黑山","关岛","文莱","牙买加","也门",
			"缅甸","库拉索","直布罗陀","巴巴多斯","贝宁","刚果","伯利兹","阿富汗","海地","新喀里多尼亚",
			"博茨瓦纳","斐济群岛","巴哈马","津巴布韦","莱索托","美属维尔京群岛","列支敦士登","百慕大","利比里亚","阿鲁巴",
			"塞拉利昂","马里","马尔代夫","老挝","苏里南","开曼群岛","塔吉克斯坦","摩纳哥","圭亚那","英属维尔京群岛",
			"泽西岛","巴布亚新几内亚","波利尼西亚","安提瓜和巴布达","安道尔","斯威士兰","吉布提","索马里","法罗群岛","毛里塔尼亚",
			"尼日尔","布隆迪","圣马力诺","格陵兰","荷属圣马丁","不丹","几内亚","马恩岛","佛得角","乍得",
			"根西岛","荷兰加勒比区","法属圭亚那","南苏丹","圣卢西亚","瓜德罗普","赤道几内亚","萨摩亚","瓦努阿图","东帝汶",
			"北马里亚纳群岛","土库曼斯坦","圣基茨和尼维斯","马提尼克","所罗门群岛","梵蒂冈","多米尼克","汤加","特克斯和凯科斯群岛","瑙鲁",
			"圣马丁","格林纳达","圣多美和普林西比","圣文森特和格林纳丁斯","安圭拉","密克罗尼西亚联邦","库克群岛","图瓦卢",
			"帕劳","几内亚比绍","科摩罗","萨摩亚","中非","基里巴斯","圣皮埃尔和密克隆","马绍尔群岛","厄立特里亚",
			"福克兰","瓦利斯和富图纳","英属印度洋领地","马约特","纽埃","诺福克岛","托克劳","圣巴泰勒米岛","蒙塞拉特岛","奥兰群岛",
			"朝鲜","南极洲"
	};
	
	private class QQwryString{
		
		public final String string;
		
		public final int byteCountWithEnd;
		
		public QQwryString(final String string,final int byteCountWithEnd) {
			this.string = string;
			this.byteCountWithEnd = byteCountWithEnd;
		}
		
		@Override
		public String toString() {
			return string;
		}


		
	}
}

