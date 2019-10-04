package assignment.kakaopay.pickme.function3;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import assignment.kakaopay.pickme.controller.KakaoController;
import assignment.kakaopay.pickme.function4.function4Test;
import assignment.kakaopay.pickme.service.KakaoService;
import assignment.kakaopay.pickme.service.impl.KakaoServiceImpl;

/**
 * 문제 3 단위 테스트 WebMvcTest 클래스 구현 
 * @author 나현기
 */
@RunWith(SpringRunner.class)
@WebMvcTest(KakaoController.class)
@AutoConfigureMybatis
public class function3Test {

	@Autowired
	private MockMvc mockMvc;
	
	//Mybatis와 실제 연결 하기 위해서 KakaoServiceImpl 스파이빈 설정
	@SpyBean
	private KakaoServiceImpl testServiceImpl;
	
	ObjectMapper mapper = new ObjectMapper();
	private Logger logger = LoggerFactory.getLogger(function3Test.class);

	@Test
	public void function3() throws Exception{
		//KakaoController의  "/function3" 매핑으로 정의
		MvcResult result = mockMvc.perform(get("/function3"))
								   //처리내용 출력
								  .andDo(print())
								   //테스트 상태의 값은 200 OK (정상)
								  .andExpect(status().isOk())
								   //출력 결과가 JSON 출력값인지 확인
								  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
								  .andReturn();
		
		String content = result.getResponse().getContentAsString();
		List<HashMap<String,Object>> data = mapper.readValue(content, List.class);
		logger.info("@Test Result : "+mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
	}
	
}
