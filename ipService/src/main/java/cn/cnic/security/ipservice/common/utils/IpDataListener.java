package cn.cnic.security.ipservice.common.utils;

import cn.cnic.security.ipservice.model.IpSegment;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于查询ip所属
 * <p>
 * 读取Excel监听类
 */

public class IpDataListener extends AnalysisEventListener<IpSegment> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpDataListener.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    //private static final int BATCH_COUNT = 3000;

    List<IpSegment> list = new ArrayList<>();

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(IpSegment data, AnalysisContext context) {
        //LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));


        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
//        if (list.size() >= BATCH_COUNT) {
//            //saveData();
//            // 存储完成清理 list
//            //list.clear();
//        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        //saveData();
        //LOGGER.info("所有数据解析完成！共{}条数据", list.size());
    }


    /**
     * 加上存储数据库
     */
    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", list.size());
        LOGGER.info("存储数据库成功！");
    }
}