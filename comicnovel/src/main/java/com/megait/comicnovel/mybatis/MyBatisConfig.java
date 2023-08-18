package com.megait.comicnovel.mybatis;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@Configuration //Config(설정) 관련 클래스 - bean과 관련있음
@RequiredArgsConstructor 
//필요한 argument는 알아서 만든다 - final과 관련있음
@MapperScan("com.megait.board.mapper")
//에 들어있는거 scan해 주세요
public class MyBatisConfig {
	//커넥션 풀 및 MyBatis에 필요한 요소를 메모리에 할당 및 관리
	//XML과 java 연동에 필요한 경로 관리
	private final ApplicationContext applicationContext; //spring추가 - mapper에 있는 timer지우기
	//생성자 만들기 - spring container만들기
	//https://velog.io/@developerjun0615/Spring-RequiredArgsConstructor-%EC%96%B4%EB%85%B8%ED%85%8C%EC%9D%B4%EC%85%98%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%9C-%EC%83%9D%EC%84%B1%EC%9E%90-%EC%A3%BC%EC%9E%85
	
	//https://velog.io/@gillog/Spring-Bean-%EC%A0%95%EB%A6%AC
	
	//@Bean
	//@Configuration 또는 @Component가 작성된 클래스의 메서드에 사용
	//Method의 리턴 객체를 spring 컨테이너에 등록
	//객체명은 메서드의 이름으로 자동설정, 직접 설정시 @Bean(name="객체명")으로 사용
	
	//1. property 가져오기
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.hikari") //application.properties에 저장된 앞글자가 이거다
	public HikariConfig hikariConfig() {return new HikariConfig();}
	//https://taler.tistory.com/13
	//https://taler.tistory.com/12
	//db와 connection(아래까지합해서)
	
	//2. DataSource 설정
	@Bean 
	public DataSource dataSource() {//application.properties에서는 data"s"ource이지만, 여기서는 Data"S"ource
		//데이타소스가 뭘까요... -> x눌러 알려주기....
		HikariDataSource hds = new HikariDataSource(hikariConfig());
		return hds;
	}
	
	//3.SQLSessionFactory
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws IOException{
		SqlSessionFactoryBean sfb = new SqlSessionFactoryBean();
		sfb.setDataSource(dataSource());//설정끝나고 datasource를 가져오고
		
		// SQL query를 작성할 xml 경로 설정
		sfb.setMapperLocations(applicationContext.getResources("classpath*:/mappers/*.xml")); //getResource"S"
		//src/main/resources>(mappers,config폴더만들기)> 
		
		try {
			SqlSessionFactory factory = sfb.getObject(); //datasource중 하나를 꺼낸다
			factory.getConfiguration().setMapUnderscoreToCamelCase(true); 
			//ex) math_score(table, db) > mathScore바꾸는거 허락하시겠습니까? -> yes(true)
			return factory;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;//factory가 없으면 null값을 return 한다
	}//project파일을 run as로 spring boot 돌려서 실행한다 
	//mappers > xml생성 > source부분으로 가기
}