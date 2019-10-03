package assignment.kakaopay.pickme.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assignment.kakaopay.pickme.mapper.KakaoMapper;
import assignment.kakaopay.pickme.service.KakaoService;

@Service
public class KakaoServiceImpl implements KakaoService {

	@Autowired
	KakaoMapper mapper;
	
	@Override
	public String getTime() throws Exception {
		return mapper.getTime();
	}

	@Override
	public List<HashMap<String, Object>> selectTopSumAmtEachYear() throws Exception {
		return mapper.selectTopSumAmtEachYear();
	}

	@Override
	public List<HashMap<String, Object>> selectNoTransEachYear() throws Exception {
		return mapper.selectNoTransEachYear();
	}

}
