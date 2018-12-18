package cn.zhengyk.sync.handler;

import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/12/18
 * @description:
 * @version: 1.0
 */
@Slf4j
@Component
public class DeleteHandler extends AbstractHandler {

    public DeleteHandler(){
        eventType = EventType.DELETE;
    }

    @Autowired
    public void setNextHandler(UpdateHandler updateHandler) {
        this.nextHandler = updateHandler;
    }

    @Override
    public void handleRowChange(RowChange rowChange) {
        rowChange.getRowDatasList().forEach(rowData -> {
            rowData.getBeforeColumnsList().forEach(column -> {
                if("id".equals(column.getName())){
                    //清除 redis 缓存
                    log.info("清除 Redis 缓存 key={} 成功!\r\n","blog:"+column.getValue());
                    redisUtil.del("blog:"+column.getValue());
                }
            });
        });
    }
}
