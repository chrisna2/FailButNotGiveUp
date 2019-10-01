package assignment.kakaopay.pickme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


/**
 * 프레임워크 실행 클래스
 * @author 나현기
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class KakaoPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(KakaoPayApplication.class, args);
	}

}
