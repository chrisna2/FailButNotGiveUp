package assignment.kakaopay.pickme.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 컨트롤러에서 받아오는 입력 출력 로그 출력 AOP 클래스 
 * @author 나현기
 *
 */
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class LogAspect {
	
	private Logger logger = LoggerFactory.getLogger(LogAspect.class);
	
	@Before("execution(* *..controller.*Controller.*(..))")
	public void before(JoinPoint joinPoint) throws Throwable {
		String paramInfo = "";
		if(joinPoint.getArgs().length == 0 || joinPoint.getArgs()[0] == null) {
			paramInfo = "";
		}
		else {
			paramInfo = "\n\t@Parameter : " + joinPoint.getArgs()[0].toString();
		}
		logger.info("\n\t@Controller Call : "
					+ joinPoint.getSignature().getDeclaringTypeName() 
					+ "." + joinPoint.getSignature().getName()
					+ paramInfo);
	}
	
	@AfterThrowing(pointcut = "execution(* *..controller.*Controller.*(..))", throwing = "ex")
	public void afterThrowing(Exception ex) throws Throwable {
		logger.info("@AfterThrowing:" + ex.getCause());
	}
}
