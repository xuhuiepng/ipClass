package cn.cnic.security.ipservice.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author wq
 * @date 2020-07-31 22:06
 */
public class TXTUtils {
    /**
     * 读取txt文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String[] txt2String(File file) {
        String[] result = new String[88203];
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String str = null;
            int i = 0;
            while ((str = br.readLine()) != null) {//使用readLine方法，一次读一行
                result[i] = str;
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
