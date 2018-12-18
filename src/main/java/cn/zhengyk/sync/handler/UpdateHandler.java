package cn.zhengyk.sync.handler;

import cn.zhengyk.sync.utils.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/12/18
 * @description:
 * @version: 1.0
 */
@Slf4j
@Component
public class UpdateHandler extends AbstractHandler {

    private EventType eventType = EventType.UPDATE;

    @Override
    public void handleMessage(Entry entry) {
        if(this.eventType == entry.getHeader().getEventType()){
            //发生 update 操作的库名
            String database = entry.getHeader().getSchemaName();
            //发生 update 操作的表名
            String table = entry.getHeader().getTableName();
            log.info("监听到数据库：{}，表：{} 的 UPDATE 事件",database,table);
            Optional.ofNullable(super.getRowChange(entry))
                    .ifPresent(rowChange -> {
                        rowChange.getRowDatasList().forEach(rowData -> {
                            //每一行的每列数据  字段名->值
                            List<Column> beforeColumnsList = rowData.getBeforeColumnsList();
                            Map<String, Object> beforeMap = columnsToMap(beforeColumnsList);
                            log.info("更新前数据：{}",JSONObject.toJSONString(beforeMap));

                            List<Column> afterColumnsList = rowData.getAfterColumnsList();
                            Map<String, Object> afterMap = columnsToMap(afterColumnsList);
                            String id = (String) afterMap.get("id");
                            String afterJsonStr = JSONObject.toJSONString(afterMap);
                            log.info("更新后数据：{}\r\n",afterJsonStr);
                            /**
                             *  高并发下，为保证数据一致性，当数据库更新后，不建议去更新缓存，
                             *  而是建议直接删除缓存，由查询时再设置到缓存。
                             *  这里为了演示，对缓存作更新操作，具体看业务需求。
                             */
                            redisUtil.setDefault("blog:"+id,afterJsonStr);

                        });

                    });
        }else{
            if(nextHandler != null){
                nextHandler.handleMessage(entry);
            }
        }

    }
}
