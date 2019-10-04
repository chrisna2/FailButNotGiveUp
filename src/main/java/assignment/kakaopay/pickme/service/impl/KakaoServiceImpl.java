package assignment.kakaopay.pickme.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import assignment.kakaopay.pickme.mapper.KakaoMapper;
import assignment.kakaopay.pickme.service.KakaoService;

@Service
@Transactional
public class KakaoServiceImpl implements KakaoService {
	
	private Logger logger = LoggerFactory.getLogger(KakaoServiceImpl.class);

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

	@Override
	public HashMap<String, Object> selectSumAmtByBrToBrName(HashMap<String, Object> param, HttpServletResponse response) throws Exception{
		
		//입력 파라미터 체크
		String br_name = (String)param.get("brName");
		//영업점이 4개 이상일 수 있으므로 DB에서 조회
		List<String> brNameList = mapper.selectBrName();
		
		//[테스트를 전제 하에 하드 코딩. 시작] : 이관된 분당점 삭제
		if(brNameList.contains("분당점")) {
			brNameList.remove("분당점");
		}
		/* 원래는 업무적으로 이관이 이루어 지면 분당점은 지워지고
		 * 이관과 관련되어 모든 데이터가 바뀌어 있어야 정상입니다.
		 * 현재는 테스트 상에 과제를 이행 하는 차원으로 가정하고
		 * 위와 같이 하드코딩을 사용하였습니다.
		 * [테스트를 전제 하에 하드 코딩. 끝]
		 */
		
		//mapper 입력 파라미터 수정
		HashMap<String, Object> paramForTest = new HashMap<String, Object>();
		List<String> listIn = new ArrayList<String>(); //mybatis in에 사용될 리스트 구성
		
		if(!brNameList.contains(br_name)) {
			HashMap<String, Object> errorMap = new HashMap<String, Object>();
			errorMap.put("메세지", "br code not found error");
			errorMap.put("code", "404");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return errorMap;
		}
		
		//[테스트를 전제 하에 하드 코딩. 시작] : 분당점으로 이관된 데이터를 판교점에서 같이 조회 할 수 있도록 처리
		else {
			if("판교점".equals(br_name)) {
				listIn.add(br_name);
				listIn.add("분당점");
			}
			else {
				listIn.add(br_name);
			}
		}
		paramForTest.put("list_in", listIn);
		//원래는 정상적으로 이관이 이루어 졌다면, 해당 로직 또한 원래 필요 없어야 되는 로직입니다.
		//[테스트를 전제 하에 하드 코딩. 끝] 
		
		return mapper.selectSumAmtByBrToBrName(paramForTest);
	}

}
