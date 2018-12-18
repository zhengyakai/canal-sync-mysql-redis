package cn.zhengyk.sync.dao;

import cn.zhengyk.sync.model.Blog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

/**
 * @author: Yakai Zheng（zhengyk@cloud-young.com）
 * @date: Created on 2018/12/18
 * @description:
 * @version: 1.0
 */
public interface BlogDao {

    @Insert("INSERT INTO `test`.`blog` (\n" +
            "`author`,\n" +
            "`title`,\n" +
            "`subtitle`,\n" +
            "`content`\n" +
            ")\n" +
            "VALUES\n" +
            "\t(\n" +
            "\t#{blog.author},\n" +
            "\t#{blog.title},\n" +
            "\t#{blog.subtitle},\n" +
            "\t#{blog.content}\n" +
            "\t);")
    int insert(@Param("blog")Blog blog);


    @Update("update blog set author=#{blog.author}")
    int update(@Param("blog")Blog blog);
}
