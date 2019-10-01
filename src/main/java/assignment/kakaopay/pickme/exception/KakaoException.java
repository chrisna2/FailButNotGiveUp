package assignment.kakaopay.pickme.exception;

/**
 * 프래임 워크 공통 메세지 전달 Exception
 * @author 나현기
 */
public class KakaoException extends RuntimeException{
	
	private static final long serialVersionUID = -5089032787769895226L;
	
	/**
	 * errorCode : 메세지 출력 형태
	 * - info
	 * - warn
	 * - error
	 */
	private String errorCode;
	/**
	 * errorMsg : 팝업창에 출력될 에러/인포 메세지
	 * ※ 추가 메세지는 '%s'로 처리
	 */
	private String errorMsg;
	
	public KakaoException(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public KakaoException(String errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}
	
	public KakaoException(String errorCode, String errorMsg, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public KakaoException(String errorCode,String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
