package cn.zhengyk.sync.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/12/18
 * @description:
 * @version: 1.0
 */
@Data
@Accessors(chain = true)
public class Blog implements Serializable {

    private Integer id;
    private String title;
    private String subtitle;
    private String author;
    private String content;
    private Date createTime;
    private Date updateTime;
}
