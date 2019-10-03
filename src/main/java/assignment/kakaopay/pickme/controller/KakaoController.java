package assignment.kakaopay.pickme.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import assignment.kakaopay.pickme.service.KakaoService;

@RestController
public class KakaoController {

	@Autowired
	KakaoService service;
	
	@RequestMapping("/time")
	public String time() throws Exception {
		return service.getTime();
	}
	
	/**
	 * [문제 1] 년도 별 최대 합계 금액 고객 정보 추출
	 * @return List<HashMap<String, Object>> : jsonArray
	 * @throws Exception
	 */
	@GetMapping("/function1")
	public List<HashMap<String, Object>> selectTopSumAmtEachYear() throws Exception {
		return service.selectTopSumAmtEachYear();
	}
	
	/**
	 * [문제 2] 년도 별 거래가 없는 고객 정보 추출
	 * @return List<HashMap<String, Object>> : jsonArray
	 * @throws Exception
	 */
	@GetMapping("/function2")
	public List<HashMap<String, Object>> selectNoTransEachYear() throws Exception {
		return service.selectNoTransEachYear();
	}
}
