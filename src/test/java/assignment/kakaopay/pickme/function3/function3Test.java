package assignment.kakaopay.pickme.function3;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import assignment.kakaopay.pickme.controller.KakaoController;
import assignment.kakaopay.pickme.service.KakaoService;

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
	
	@MockBean
	private KakaoService service;

	@Test
	public void function3() throws Exception{
		//KakaoController의  "/function3" 매핑으로 정의
		mockMvc.perform(get("/function3"))
			    //처리내용 출력
			   .andDo(print())
			    //테스트 상태의 값은 200 OK (정상)
			   .andExpect(status().isOk())
			    //출력 결과가 JSON 출력값인지 확인
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			   .andReturn();
	}
	
}
