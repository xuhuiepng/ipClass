package cn.cnic.security.ipservice;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author baokang
 * @date 2020/9/22 10:21
 */

public class ThreatTest {

    @Test
    public void testClient() throws IOException, InterruptedException {
        //此处读取txt文件
        List<String> ips = txt2IpList("E:\\project_20190509\\arpvpnip20201014.txt");
        RestTemplate template = new RestTemplate();
        //结果写入地址
        File res = new File("res.txt");
        //创建的线程总数
        final int threatCount = 1;
        //判断是否存在该文件，如果不存在就进行创建
        if(!res.isFile()){
            res.createNewFile();
        }
        //创建输入流
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(res),"utf-8"));
        //创建主线程阻塞对象，保证所有子线程能够成功结束
        final CountDownLatch latch = new CountDownLatch(threatCount);
        //循环创建子线程
        for (int i = 0; i < threatCount; i++) {
            System.out.println(" 创建线程 "+i);
            Thread t = new Thread(() -> {
                //用于存放查询的结果
                List<String> ipLocations = new ArrayList<>();
                Thread tt = Thread.currentThread();
                String name = tt.getName();
                //循环进行查询
                for(String ip : ips){
                    String url = "http://localhost:8082/location/"+ip+"?source=ip2region";
                    ResponseEntity<String> response = template.getForEntity(url, String.class);
                    String ipLocation = response.getBody();
                    ipLocations.add(ipLocation);
                }
                System.out.println("线程 " + name +" 查询结束");
                //此处写入txt文件，对输入流进行加锁
                synchronized(bw){
                    System.out.println("线程 " + name +" 正在写数据");
                    for (String ipLocation : ipLocations) {
                        try {
                            bw.write(ipLocation + "\r\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        bw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                latch.countDown();
            });
            t.start();
        }
        latch.await();
        //最后关闭输入流
        bw.close();
    }

    //将txt文件转化为List对象
    public static List<String> txt2IpList(String path){
        List<String> ips = new ArrayList<>();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                ips.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ips;
    }
}
