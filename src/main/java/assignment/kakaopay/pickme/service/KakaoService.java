package assignment.kakaopay.pickme.service;

import java.util.HashMap;
import java.util.List;

public interface KakaoService {
	
	public String getTime() throws Exception;
	public List<HashMap<String, Object>> selectTopSumAmtEachYear() throws Exception;
	public List<HashMap<String, Object>> selectNoTransEachYear() throws Exception;

}
