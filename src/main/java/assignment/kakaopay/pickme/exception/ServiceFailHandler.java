package assignment.kakaopay.pickme.exception;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

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
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public HashMap<String, Object> handle(HttpServletRequest request, Exception ex) throws Throwable {
		msgMap = new HashMap<String, Object>();
		
		//에러 발생지점의 에러 로그 출력
		ex.printStackTrace();
		
		//해당 프로젝트 패키지 안에서 발생하는 에러만 추출해서 메세지 출력
		String stackTrace = "";
		for(int i = 0; i < ex.getStackTrace().length; i++) {
			if(ex.getStackTrace()[i].toString().matches(".*assignment.kakaopay.pickme.*")) {
				stackTrace += ex.getStackTrace()[i].toString()+"\n";
			}
		}
		
		//사용자 정의 에러 메세지 
		if(ex instanceof KakaoException) {
			KakaoException bizExp = (KakaoException) ex;
			msgMap.put("errorMsg", bizExp.getErrorMsg());
			msgMap.put("errorCode", bizExp.getErrorCode());
		}
		
		//MyBatis
		else if(ex instanceof MyBatisSystemException) {
			//TODO:해당 에러 타입에 대한 사용자 정의 에러 메세지 출력 설정
			msgMap.put("errorMsg", "mybatis에러가 발생했습니다.");
			msgMap.put("errorCode", "error");
		} 
		
		//SQL
		else if(ex instanceof SQLException) {
			//TODO:해당 에러 타입에 대한 사용자 정의 에러 메세지 출력 설정
			msgMap.put("errorMsg", "SQL에러가 발생했습니다.");
			msgMap.put("errorCode", "error");
		} 
		
		else {
			//TODO:해당 에러 타입에 대한 사용자 정의 에러 메세지 출력 설정
			msgMap.put("errorMsg", "서버에 장애가 발생했습니다. 잠시만 기다려주세요");
			msgMap.put("errorCode", "error");
		}
		
		//공통 메세지 부분
		msgMap.put("exception", ex.getMessage());
		msgMap.put("stackTrace", stackTrace);
		
		logger.info("@ComServiceFailHandler =>"+msgMap.toString());
		
		return msgMap;
	}
}
