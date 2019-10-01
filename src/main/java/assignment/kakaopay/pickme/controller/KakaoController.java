package assignment.kakaopay.pickme.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@RequestMapping("/function1")
	public List<HashMap<String, Object>> selectTopSumAmtEachYear() throws Exception {
		return service.selectTopSumAmtEachYear();
	}
	
}
