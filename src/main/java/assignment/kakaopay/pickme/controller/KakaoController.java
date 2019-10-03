package assignment.kakaopay.pickme.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import assignment.kakaopay.pickme.service.KakaoService;

/**
 * 과제 호출을 위한 RestController 
 * @author 나현기
 */
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
	public List<HashMap<String, Object>> selectTopSumAmtByCustEachYear() throws Exception {
		return service.selectTopSumAmtByCustEachYear();
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
	
	/**
	 * [문제 3] 년도 별, 관리점 별 거래금액 합계 추출 : 출력 순서 [합계금액 desc] 
	 * @return List<HashMap<String, Object>> : jsonArray
	 * @throws Exception
	 */
	@GetMapping("/function3")
	public List<HashMap<String, Object>> selectRankSumAmtByBrEachYear() throws Exception {
		return service.selectRankSumAmtByBrEachYear();
	}
	
}