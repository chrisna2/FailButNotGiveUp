package assignment.kakaopay.pickme.exception;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 서비스가 실패할 경우 공통적으로 발생하는 
 * 작업들을 실행하는 Handler클래스
 * @author 나현기
 *
 */
@ControllerAdvice
public class ServiceFailHandler {
	
	private Logger logger = LoggerFactory.getLogger(ServiceFailHandler.class);
	
	private HashMap<String, Object> msgMap;
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(Exception.class)
	public HashMap<String, Object> handle(HttpServletRequest request, Exception ex) throws Throwable {
		msgMap = new HashMap<String, Object>();
		
		//에러 발생지점의 에러 로그 출력
		ex.printStackTrace();
		
		//사용자 정의 에러 메세지 
		if(ex instanceof KakaoException) {
			KakaoException bizExp = (KakaoException) ex;
			msgMap.put("메세지", bizExp.getErrorMsg());
			msgMap.put("code", bizExp.getErrorCode());
		}
		//MyBatis
		else if(ex instanceof MyBatisSystemException) {
			//TODO:해당 에러 타입에 대한 사용자 정의 에러 메세지 출력 설정
			msgMap.put("메세지", "mybatis에러가 발생했습니다.");
			msgMap.put("code", "error");
		} 
		//SQL
		else if(ex instanceof SQLException) {
			//TODO:해당 에러 타입에 대한 사용자 정의 에러 메세지 출력 설정
			msgMap.put("메세지", "SQL에러가 발생했습니다.");
			msgMap.put("code", "error");
		} 
		else {
			//TODO:해당 에러 타입에 대한 사용자 정의 에러 메세지 출력 설정
			msgMap.put("메세지", "서버에 장애가 발생했습니다. 잠시만 기다려주세요");
			msgMap.put("code", "error");
		}
		
		logger.info("@ComServiceFailHandler => "+msgMap.toString());
		
		return msgMap;
	}
}
