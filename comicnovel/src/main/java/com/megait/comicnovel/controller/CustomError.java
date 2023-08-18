package com.megait.comicnovel.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CustomError implements ErrorController{//interface상속
	@GetMapping("/error")
	public String handleError(HttpServletRequest req) {//error코드가 담겨져 있다
		Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		//ERROR_STATUS_CODE: 상수, 오류코드를 주세요
		log.info("Error but not serious");
		if(status!=null) {//오류가 발생하면
			int statusCode = Integer.valueOf(status.toString());//object클래스에 toString매서드가 잇으니까
			if(statusCode==HttpStatus.NOT_FOUND.value()) {//notFound와 code가 같다면, 페이지가 없다는 오류이면
				return "error/404";//localhost:10002/json.html(없는페이지)를 치면 404.html로 가게됨
			}	
		}
		return "error/500";
		// localhost:10001/example/test10 -> test10 controller에 int a = 100/0; 으로 오류냄
		// -> 500.html로 감
	}
}
