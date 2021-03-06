package assignment.kakaopay.pickme.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KakaoMapper {
	
	//DB connection test
	public String getTime();
	
	/**
	 * [문제1] 년도 별 최대 합계 금액 고객 정보 추출 mapper.class
	 * @return List<HashMap<String, Object>> : 년도 별 최대 합계 금액 고객 정보 List
	 */
	public List<HashMap<String, Object>> selectTopSumAmtByCustEachYear();
	
	/**
	 * [문제 2] 년도 별 거래가 없는 고객 정보 추출 mapper.class
	 * @return List<HashMap<String, Object>> : 년도 별 거래가 없는 고객 정보 List
	 */
	public List<HashMap<String, Object>> selectNoTransEachYear();
	
	/**
	 * [문제 3] 년도 별, 관리점 별 거래금액 합계 추출 : 출력 순서 [합계금액 desc] 
	 * @return List<HashMap<String, Object>> : 년도 별, 관리점 별 거래금액 합계 추출 List
	 */
	public List<HashMap<String, Object>> selectRankSumAmtByBrEachYear(String year);
	/**
	 * 거래정보 실행 년도 추출
	 * @return List<String> 연도 추출
	 */
	public List<String> selectTransGroupYear();
	
	/**
	 * [문제 4] 지점명 입력시 해당지점의 거래금액 합계 출력
	 * @param paramForTest 
	 * @return List<HashMap<String, Object>> : 년도 별, 관리점 별 거래금액 합계 추출 List
	 */
	public HashMap<String, Object> selectSumAmtByBrToBrName(HashMap<String, Object> param);
	/**
	 * 관리점명 목록 조회
	 * @return List<String> 관리점명 목록
	 */
	public List<String> selectBrName();
	
}
