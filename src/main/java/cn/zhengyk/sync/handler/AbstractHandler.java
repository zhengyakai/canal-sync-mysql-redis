package cn.zhengyk.sync.handler;

import cn.zhengyk.sync.utils.RedisUtil;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/12/18
 * @description:   使用责任链模式
 * @version: 1.0
 */
@Slf4j
public abstract class AbstractHandler {

    protected AbstractHandler nextHandler;

    @Autowired
    protected RedisUtil redisUtil;

    protected Map<String,Object> columnsToMap(List<Column> columns) {
        Map<String,Object> map = new HashMap<>();
        columns.forEach(column -> {
            if (column == null) {
                return;
            }
            map.put(column.getName(), column.getValue());
        });
        return map;
    }

    protected RowChange getRowChange(Entry entry){
        RowChange rowChange = null;
        try {
            rowChange = RowChange.parseFrom(entry.getStoreValue());
        } catch (InvalidProtocolBufferException e) {
            log.error("根据CanalEntry获取RowChange异常:", e);
        }
        return rowChange;
    }

    /**
     * 传递处理的事件
     */
    public abstract void handleMessage(Entry entry);
}
