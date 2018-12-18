package cn.zhengyk.sync.handler;

import cn.zhengyk.sync.utils.RedisUtil;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/12/18
 * @description:   使用责任链模式
 * @version: 1.0
 */
@Slf4j
public abstract class AbstractHandler {

    protected AbstractHandler nextHandler;

    protected EventType eventType;

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

    private RowChange getRowChange(Entry entry){
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
    public void handleMessage(Entry entry){
        if(this.eventType == entry.getHeader().getEventType()){
            //发生 update 操作的库名
            String database = entry.getHeader().getSchemaName();
            //发生 update 操作的表名
            String table = entry.getHeader().getTableName();
            log.info("监听到数据库：{}，表：{} 的 {} 事件",database,table, eventType.toString());
            Optional.ofNullable(getRowChange(entry))
                    .ifPresent(this::handleRowChange);
        }else{
            if(nextHandler != null){
                nextHandler.handleMessage(entry);
            }
        }
    }

    public abstract void handleRowChange(RowChange rowChange);

}
