package assignment.kakaopay.pickme.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KakaoMapper {
	
	public String getTime();
	public List<HashMap<String, Object>> selectTopSumAmtEachYear();
	public List<HashMap<String, Object>> selectNoTransEachYear();
	
}
