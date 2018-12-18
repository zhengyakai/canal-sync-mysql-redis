package cn.zhengyk.sync.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/12/18
 * @description:
 * @version: 1.0
 */
@Slf4j
@Component
public class InsertHandler extends AbstractHandler{

    public InsertHandler(){
        this.eventType = EventType.INSERT;
    }

    @Autowired
    public void setNextHandler(DeleteHandler deleteHandler) {
        this.nextHandler = deleteHandler;
    }

    @Override
    public void handleRowChange(RowChange rowChange) {
        rowChange.getRowDatasList().forEach(rowData -> {
            //每一行的每列数据  字段名->值
            List<Column> afterColumnsList = rowData.getAfterColumnsList();
            Map<String, Object> map = columnsToMap(afterColumnsList);
            String id = (String) map.get("id");
            String jsonStr = JSONObject.toJSONString(map);
            log.info("新增的数据：{}\r\n",jsonStr);
            redisUtil.setDefault("blog:"+id, jsonStr);
        });
    }


}
