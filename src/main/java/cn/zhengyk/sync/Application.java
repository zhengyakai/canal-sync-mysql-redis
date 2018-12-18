package cn.zhengyk.sync;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableScheduling     //开启定时任务支持
@SpringBootApplication
@EnableTransactionManagement //开启事务支持
@MapperScan("cn.zhengyk.sync.dao")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

