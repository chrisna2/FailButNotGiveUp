package assignment.kakaopay.pickme.service.impl;

import java.util.ArrayList;
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
	public List<HashMap<String, Object>> selectTopSumAmtByCustEachYear() throws Exception {
		return mapper.selectTopSumAmtByCustEachYear();
	}

	@Override
	public List<HashMap<String, Object>> selectNoTransEachYear() throws Exception {
		return mapper.selectNoTransEachYear();
	}

	@Override
	public List<HashMap<String, Object>> selectRankSumAmtByBrEachYear() throws Exception {
		
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> dataEachYear;
		List<HashMap<String, Object>> dataList;
		
		//각 거래 연도 그룹 조회
		List<String> eachYear = mapper.selectTransGroupYear();
		
		for(String year:eachYear) {
			//초기화
			dataEachYear = new HashMap<String, Object>();
			dataList = new ArrayList<HashMap<String,Object>>();
			
			//데이터 조회
			dataList = mapper.selectRankSumAmtByBrEachYear(year);
			
			//데이터 구성
			dataEachYear.put("year", year);
			dataEachYear.put("dataList", dataList);
			result.add(dataEachYear);
		}
		
		return result;
	}

}
