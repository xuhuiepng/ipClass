package cn.cnic.security.ipservice.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

@Slf4j
public class JacksonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
        // Include.Include.ALWAYS 默认
        // Include.NON_DEFAULT 属性为默认值不序列化
        // Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。这样对移动端会更省流量
        // Include.NON_NULL 属性为NULL 不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        // 所有日期格式统一为 yyyy:MM:dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy:MM:dd HH:mm:ss"));
        // 忽略在Json字符串中存在，但在Java对象中不存在的对应属性的状况，防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    }



    public static ObjectMapper getObjectMapper(){
        return objectMapper;
    }

    /** 将对象转为string */
    public static <T> String obj2String(T obj){

        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error",e);
            return null;
        }
    }

    /**
     * 将对象转为json，并格式化的输出
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error",e);
            return null;
        }
    }

    /**
     * Json字符串转成对象
     * @param str
     * @param clazz
     * @param <T>
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {

            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error", e);
            return null;
        }

    }




}
