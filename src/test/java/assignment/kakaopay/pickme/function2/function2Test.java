package assignment.kakaopay.pickme.function2;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
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
 * [문제2] 단위 테스트 WebMvcTest 클래스 구현 
 * @author 나현기
 */
@RunWith(SpringRunner.class)
@WebMvcTest(KakaoController.class)
@AutoConfigureMybatis
public class function2Test {

	@Autowired
	private MockMvc mockMvc;
	
	//Mybatis와 실제 연결 하기 위해서 KakaoServiceImpl 스파이빈 설정
	@SpyBean
	private KakaoServiceImpl testServiceImpl;
	
	ObjectMapper mapper = new ObjectMapper();
	private Logger logger = LoggerFactory.getLogger(function2Test.class);

	@Test
	public void function2() throws Exception{
		//KakaoController의  "/function2" 매핑으로 정의
		MvcResult result = mockMvc.perform(get("/function2"))
								   //처리내용 출력
								  .andDo(print())
								   //테스트 상태의 값은 200 OK (정상)
								  .andExpect(status().isOk())
								   //출력 결과가 JSON 출력값인지 확인
								  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
								   //JSON 출력결과 확인
								  .andExpect(jsonPath("$", Matchers.iterableWithSize(4)))//배열의 크기 4건
								   //KEY 존재 유무 확인
								  .andExpect(jsonPath("$[0]", Matchers.hasKey("year"))) 
								  .andExpect(jsonPath("$[0]", Matchers.hasKey("acctNo")))
								  .andExpect(jsonPath("$[0]", Matchers.hasKey("name")))
								  .andReturn();
		
		String content = result.getResponse().getContentAsString();
		List<HashMap<String,Object>> data = mapper.readValue(content, List.class);
		logger.info("@Test Result : "+mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
	}
	
}
