package cn.zhengyk.sync.handler;

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
public class InsertHandler extends AbstractHandler{

    private EventType eventType = EventType.INSERT;

    @Autowired
    public void setNextHandler(DeleteHandler deleteHandler) {
        this.nextHandler = deleteHandler;
    }

    @Override
    public void handleMessage(Entry entry) {
        if(this.eventType == entry.getHeader().getEventType()){
            //发生 insert 操作的库名
            String database = entry.getHeader().getSchemaName();
            //发生 insert 操作的表名
            String table = entry.getHeader().getTableName();
            log.info("监听到数据库：{}，表：{} 的 INSERT 事件",database,table);
            Optional.ofNullable(super.getRowChange(entry))
                    .ifPresent(rowChange -> {
                        rowChange.getRowDatasList().forEach(rowData -> {
                            //每一行的每列数据  字段名->值
                            List<Column> afterColumnsList = rowData.getAfterColumnsList();
                            Map<String, Object> map = columnsToMap(afterColumnsList);
                            String id = (String) map.get("id");
                            String jsonStr = JSONObject.toJSONString(map);
                            redisUtil.setDefault("blog:"+id, jsonStr);
                            log.info("新增的数据：{}\r\n",jsonStr);
                        });
                    });
        }else{
            if(nextHandler != null){
                nextHandler.handleMessage(entry);
            }
        }
    }
}
