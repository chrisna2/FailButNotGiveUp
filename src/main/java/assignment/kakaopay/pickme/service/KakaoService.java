package assignment.kakaopay.pickme.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public interface KakaoService {
	
	public String getTime() throws Exception;
	
	public List<HashMap<String, Object>> selectTopSumAmtByCustEachYear() throws Exception;
	
	public List<HashMap<String, Object>> selectNoTransEachYear() throws Exception;
	
	public List<HashMap<String, Object>> selectRankSumAmtByBrEachYear() throws Exception;

	public HashMap<String, Object> selectSumAmtByBrToBrName(HashMap<String, Object> param, HttpServletResponse response) throws Exception;

}
