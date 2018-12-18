package cn.zhengyk.sync.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/12/18
 * @description:
 * @version: 1.0
 */
@Slf4j
@Component
public class DeleteHandler extends AbstractHandler {

    private EventType eventType = EventType.DELETE;

    @Autowired
    public void setNextHandler(UpdateHandler updateHandler) {
        this.nextHandler = updateHandler;
    }

    @Override
    public void handleMessage(Entry entry) {
        if(this.eventType == entry.getHeader().getEventType()){
            //发生删除操作的库名
            String database = entry.getHeader().getSchemaName();
            //发生删除操作的表名
            String table = entry.getHeader().getTableName();
            log.info("监听到数据库：{}，表：{} 的 DELETE 事件",database,table);
            Optional.ofNullable(super.getRowChange(entry))
                    .ifPresent(rowChange -> {
                        rowChange.getRowDatasList().forEach(rowData -> {
                            rowData.getBeforeColumnsList().forEach(column -> {
                                if("id".equals(column.getName())){
                                    //清除 redis 缓存
                                    redisUtil.del("blog:"+column.getValue());
                                    log.info("清除 Redis 缓存 key={} 成功!\r\n","blog:"+column.getValue());
                                }
                            });
                        });
                    });
        }else{
            if(nextHandler != null){
                nextHandler.handleMessage(entry);
            }
        }
    }
}
