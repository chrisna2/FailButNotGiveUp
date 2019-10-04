# 특정 고객 거래내역 조회 서비스 개발 명세
### For Kakao Stock! 

## 사용한 개발 프레임워크

1. Spring Boot
> * Mybatis + MySql JDBC
> * Spring boot starter aop
> * Spring boot starter web
> * Spring boot starter test + hamcrest
> * html + Jquery
> * logback
> * Maven 
> * GIT + GutHub

2. AWS EC2 linux + MySQL DBMS
> * 접속 정보 IP : 52.79.240.193:3306 

## 문제 해결 방법
### 서비스 개발 사전 작업 : Databasea및 프레임워크 구축
1. 데이터를 어디에서도 확인이 가능 하도록 Database를 구축해야 했습니다.
AWS EC2 리눅스(프리티어) 서버를 구축하고 MySql DB를 구축하여 해당 접속 정보를 
properties.yml에 등록했습니다. 현재 이 해당 깃 허브를 통해 소스를 다운 받으시거나
빌드된 JAR파일을 보유하고 있다면 언제든 해당 DB에 접속이 가능합니다.
하지만 로컬의 DB를 구축한 상황을 가정해서 제가 구축한 DB의 schema.sql도 프로젝트에 함께 동봉했습니다. (src/main/resource/)

2. 기본적으로 개발을 진행하기 위해서 프로젝트 내에 일을 구성할 수 있는 프래임워크가 구축이 되어야 합니다. 가장 일반적으로 필요한 것은 로그처리와 예외처리, 그에 대한 메세징처리, DataSource 설정, 플러그인 관리, 형상관리 그리고 프로젝트 디자인이 대표 적일 것입니다.

> * 로그 처리 : 기본적인 플러그인은 logback을 사용 하였습니다. AOP 클래스를 활용하여 컨트롤러 호출시 마다. 호출된 컨트롤러 클래스와
메서드를 로그에 띄우고 컨트롤러 호출이 완료되어 화면에 리턴하는 데이터를 로그에 출력하도록 했습니다. 
SQL 출력로그는 제가 주로 사용하는 인터셉트 클래스를 활용하였고 현재 프로젝트에 맞게 간소화 시켜 사용했습니다. 현재 실행되는 쿼리의
내용과 그 SQL 처리 결과를 로그로 출력하는 기능을 가지고 있습니다.
(해당 인터셉트 클래스에 경우 제가 아는 고급개발자 한 분과 함께 배우고 개발하면서 만들어낸 소스로서 저에게는 아주 뜻 깊은 소스이기도 합니다. ^_^)

> * 예외 처리 : kakaoException 이라는 이름으로 예외 객체를 구성했으며 RuntimeException을 상속받았습니다. 원래는 문제 4번을 생각하고 만들어낸건데
httpStatus의 값을 바꾸는 부분을 고려하지 않아서 현재 프로젝트에서는 사용되지 않고 있는 상태 입니다.

> * 메세징 처리 : kakaoException에 경우 단순히 예외처리 뿐 아니라 화면에 에러 메세지를 출력하는 기능도 고려 했기 때문에 예외가 발생시 
해당 예외 내용에 대한 메세지를 출력하는 Handler클래스가 따로 구성했습니다. @ControllerAdvice REST API로서 에러가 발생시 그에 대한 메세지를
출력 하도록 설정 했습니다. kakaoException외에 MyBatisSystemException, SQLException에 대한 예외 발생시 해당 예외에 대한 메세징을 뿌려주는 
기능을 담당합니다. 만약 메세지 테이블을 구성했다면 메세지코드로 해당하는 메세지를 뿌려주는 역활을 수행해야 합니다만 현재는 아주 간단하게 
구성해 봤습니다.

> * DataSource 설정 : Spring Boot 프로젝트에서 JPA가 아닌 Mybatis를 사용한 이유는 저에게 가장 익숙해서입니다. 
최대한 짧은 시간에 과제를 제출하고 싶었고 그걸 위해서 저에게 가장 익숙한 Mybatis를 활용했습니다. 
JPA의 활용성을 모르지는 않지만 문제를 풀기위해 활용될 JAVA 람다식 프로그래밍보다 쿼리로 처리하는 것이 저에게는 익숙하고 편했기 때문입니다. 
MySql을 사용했기 때문에 mysql jdbc를 설정했습니다.

> * 플러그인 관리 & 빌드 툴 : Apache Maven을 사용했습니다. Gradle이 더 좋다고는 해서 고려를 해봤지만, 위와 같은 이유로 아직 익숙하지 않아서 사용하지 않았습니다. 

> * 형상관리 : 저는 개발 커리어를 관리하기위 github을 애용하고 있습니다. 재직자 교육과정에서 수행한 프로젝트들 이나 개인적인 공부의 내용들을 github repository를 생성 저장하여 관리합니다. 지금 현업에서 SVN에 비해 잘 사용되고 있지 않아도 저에겐 SVN보다 GIT이 친근하게 다가옵니다. AWS EC2 서버를 따로 구성했기 때문에 SVN도 고려하기는 했지만, 평가하시기 불편할 것 같아서 그 생각은 정리했습니다.

> * 프로젝트 디자인 : 가장 일반적인 MVC 패턴을 활용하여 구축하였습니다. 그리고 추가적으로 저는 DTO(VO)형 클래스가 남발되는 것을 별로 좋아하지 않습니다. 데이터의 형태가 항상 늘 클래스에 고정되지 않기 때문입니다. 따라서 현재 제 프로젝트에는 DTO 클래스가 없습니다. 대부분의 파라미터는 HashMap 형태입니다.

### 문제 해결 방법

1. [문제 1] 년도 별 최대 합계 금액 고객 정보 추출

입력데이터 없이 년도별 합계 금액이 가장 높른 고객을 한명 출력 하는 로직입니다. 현재 데이터에서는 2018년과 2019년의 거래정보밖에 없어서 두개로 한정지어 조회 할 수도 있었으나 2020년 앞으로 계속 추가될 일자의 거래정보도 고려하였습니다. 대부분의 로직은 쿼리에 녹아져 있습니다. 우선 거래정보와 계좌정보의 테이블을 계좌번호로 조인하고 그룹핑을 거래년도와 계좌명으로 해준뒤 년도는 작은순으로 합계는 큰 순으로 정렬해 줍니다. 그리고 그 조회 쿼리를 
테이블로 삼아 다시 조회 합니다. 이번에는 거래년도만 그룹핑하고 그 중에 max() 함수로 합계금액의 최대값을 가지는 열을 출력 했습니다.

```sql
select
	year,
	max(sum) as sumAmt,
	name,
	acctNo
from
(
	select
		year(a.tr_date) as year,
		sum(a.tr_amount - a.tr_fee) as sum,
		b.acct_name as name,거래내역
		a.acct_no as acctNo
	from
		tbl_transaction a,
		tbl_account b
	where a.acct_no = b.acct_no
	and   a.can_yn = 'N'
	group by
		year(a.tr_date),
		b.acct_name
	order by
		year(a.tr_date) asc,
	        sum(a.tr_amount - a.tr_fee) desc
) c
group by
	c.year asc;
```

2. [문제 2] 년도 별 거래가 없는 고객 정보 추출

1번 문제와 동일하게 입력데이터 없이 년도 별 거래가 없는 고객 정보 추출하는 로직입니다. 이번에도 서브쿼리를 활용했습니다. 성능을 고려 하면 NOT IN 과 같은 조선식에 부정 연산자의 사용은 지양되어야 하나 테스트를 한정하여 사용하는 거라 인덱스도 따로 설정 하지 않았기 때문에 현재 테스트에서는 NOT IN을 사용했습니다. 

```sql
select
	b.year,
	a.acct_name as name,
	a.acct_no as acctNo
from 
	tbl_account a,
	(select distinct year(tr_date) as year from tbl_transaction) b
where
	a.acct_no not in (
				select 
					acct_no
				from
					tbl_transaction
				where can_yn = 'N'
				and   year(tr_date) = b.year
				group by
					year(tr_date),
					acct_no
			    )
order by
	b.year asc
```

3. [문제 3] 년도 별, 관리점 별 거래금액 합계 추출 : 출력 순서 [합계금액 desc]

해당 문제에 경우 데이터의 출력형이 json배열 안에 json배열이 있는 경우라서 쿼리를 두번 타게 설정했습니다.

가장 먼저 거래내역에서 기록이 있는 연도를 추출합니다. 1번 문제와 마찬가지로 2018년과 2019년의 거래정보밖에 없어서 하드코딩으로 두 개로 한정지어 조회 할 수도 있었으나 앞으로 계속 거래내역이 있을 수 있으므로 DB에서 따로 년도를 조회하는 로직을 추가 했습니다.

```sql
select 
	distinct year(tr_date) as year 
from 
	tbl_transaction
order by
	year asc
```

그 결과를 List<String>으로 받아서 for문 조건식으로 활용하고 그 안에서 추가 조회를 합니다. 추가 조회는 dataList의 목록을 조회 합니다.
추가 조회하는 쿼리는 다음과 같습니다. 
	
```sql
select
	b.br_name as brName,
	b.br_code as brCode,
	sum(c.sum_amt) as sumAmt
from
	tbl_account a,
	tbl_branch b,
	(
		select 
			acct_no,
			sum(tr_amount) as sum_amt
		from
			tbl_transaction
		where year(tr_date) = #{value}
		and   can_yn = 'N'
		group by
			acct_no    
	) c    
where a.acct_no = c.acct_no
and   a.br_code = b.br_code
group by
	a.br_code
order by 
	sum(c.sum_amt) desc
```
이렇게 조회된 데이터를 List<HashMap>에 저장하고 해당 데이터는 dataList의 키밸류로 Object에 저장됩니다. 조회에 활용된 JAVA소스의 일부입니다.
```java
@Service
@Transactional
public class KakaoServiceImpl implements KakaoService {
	
	/** ...중략... **/
	
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
	
	/** ...후략... **/
	
}
	
```

4. [문제 4] 지점명 입력시 해당지점의 거래금액 합계 출력

프로그래밍 로직 외에 다른 이유로 시간을 굉장히 잡아 먹었던 문제였습니다. ㅜ_ㅜ; 그 문제를 해결하기 위해 개천절 오전 오후를 다 보내게 되어 제출 일정이 오늘까지 하루 늦어지게 되었습니다. (확인은 월요일날 하시겠죠.. ㅜ_ㅜ)

우선 문제에 조건가운데 분당점이 판교로 이관되었다는 것에 집중 했습니다. ***신용보증기금 차세대 프로젝트를 진행하면서 이수관 업무를 맏았던 저로서는 정상적으로 이관이 완료가 되었다면 해당 하위 데이터가 모두 판교점으로 이관 되어야 한다고 생각했습니다.*** 즉 분당점의 데이터는 판교점으로 조회할때 같이 조회가 되어야 됩니다. 하지만 어디까지나 4번 문제 한정의 상황에서 그 모든 것을 반영하기 위해서 DB의 내용을 바꿀수는 없었습니다. 상황을 반영하기위해 일부 하드코딩이 필요했습니다.

JAVA 서비스 로직 부분입니다.

```JAVA
@Service
@Transactional
public class KakaoServiceImpl implements KakaoService {
	
	/** ...중략... **/
	

	@Override
	public HashMap<String, Object> selectSumAmtByBrToBrName(HashMap<String, Object> param, HttpServletResponse response) 
	throws Exception{
		
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
		
		// 분단 조회시 또는 없는 영업점의 경우..
		if(!brNameList.contains(br_name)) {
			HashMap<String, Object> errorMap = new HashMap<String, Object>();
			errorMap.put("메세지", "br code not found error");
			errorMap.put("code", "404");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//http status 변경
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
	/** ...후략... **/
}
```
소스에 주석으로 설명을 달아 놨지만 해당 로직은 문제 4번의 상황을 특정하여 추가로직 부분이 들어간 상황입니다. 그리고 판교점의 특수 상황을 조회 하기 위해 쿼리에 IN 연산을 사용했습니다. 위에 보시면 분당점과 같은 없는 영업점을 조회하는 경우 errorMap을 리턴하도록 처리 했습니다. 원래 구상은 kakaoException처리를 하려고 했으나 그렇게 에러를 처리하는 경우 400 에러가 발생해서 문제의 정답과 틀려지게 되었습니다. exception안에 response를 받게 처리 할까도 생각했지만, 상태코드를 어거지로 몇번씩 바꾸는게 모양이 좋지 않아 그냥 서비스 안에서 한번만 바꾸도록 처리 했습니다.

위에 주석처럼 조회시 IN연산을 사용했기 때문에 Mybatis dynamic 쿼리를 사용했습니다. xml 파일소스로 같이 올립니다.

```XML
	<select id="selectSumAmtByBrToBrName" resultType="hashmap" parameterType="hashmap">
		<![CDATA[
			select
				b.br_name as brName,
			    b.br_code as brCode,
			    sum(c.tr_amount) as sumAmt
			from
				tbl_account a,
			    tbl_branch b,
			    tbl_transaction c
			where 1 = 1
			and	a.acct_no = c.acct_no
			and a.br_code = b.br_code
			and c.can_yn = 'N'
		    and b.br_name in 
		]]>
		<foreach collection="list_in" item="br_name" index="index" separator="," open="(" close=")">
        		#{br_name}
    		</foreach>
	</select>
```

이제 앞서 말씀  프로그래밍 로직 외에 다른 이유로 시간을 굉장히 잡아 먹었던 문제에 대해 말씀드리도록 하겠습니다. 저 해당 쿼리가 문제였습니다. 테스트를 위해 먼저 MySQL Workbench에서 작성할 때는 정말 잘 조회가 되던 쿼리었는데 이상하게 mybatis 를 통해서 조회하게 되면 조회 값이 null로 나오는 것이었습니다. 다이나믹 쿼리에 이상이 있나 싶었고 구글링을 통해 문제를 찾기위해 개천절, 거의 하루 절반을 보냈습니다. 너무 답답해서 스텍오버플로어에 부족한 영어실력을 보태며 질문을 올려보기도 했지만 답이 바로 오지는 않았습니다. resultType과 parameterType이 같으면 그럴 수 있다고 해서 paramterType을 String으로 바꿔 보기도 했지만 답이 아니었습니다. IN 입력 조건을 지우고 or로 변경해도 마찬가지였고 그냥 = 로 처리해도 마찬가지였습니다. 그러다 입력조건을 지우고 조회를 해봤더니 조회가 되는 것을 확인 했습니다. 이 문제는 입력을 받는 유일힌 문제였고, 그 유일한 입력값이라는 것도 **한글** 이었습니다. 10시가 넘긴 시간이었습니다. 순간 문득 **인코딩** 이라는 글자가 머리를 스쳐지나갔습니다.

평가자께서 어떻게 그런 초보적인 실수에 시간을 그렇게 허비하느냐고 실망하실 것 같습니다. 하지만 테이블 생성시 UTF-8로 설정했고 처음부터 프레임워크를 짤때 JAVA, html, xml, js등등 모든 워크스페이스의 인코딩 환경을 UTF-8로 맞춰 놓고 시작했고 위에 3문제에서 조회 할때는 정상적으로 한글이 조회되는 것을 확인 했기 때문에 인코딩이 틀어져 한글이 깨지리라고는 생각하지 못했습니다. 무엇보다 원격으로 AWS와 접속한 MySQL Workbench에서 조회 할때는 정상 조회가 되는것을 두눈으로 확인했기에 설마 그 문제일 거라고는 차마 생각하지 못했습니다.  **단, AWS EC2 리눅스 콘솔에 접속하지 전까지 말입니다.(putty,xshall)** 인코딩의 문제라는 것으로 범위를 좁혀나가고 그 어디에도 UTF-8 타입이 아닌 곳을 찾을 수 없었지만 단 한곳, AWS EC2 리눅스에서의 MySql dbms는 확인하지 못했다는 것을 11시가 넘어가는 시간에 깨닫게 되었습니다. 서버에 접속해서 mysql에 들어가 해당 쿼리를 조회해보니 애초부터 한글이 입력되지 않았고 그로인해 데이터 조회 결과가 null로 나오는 것이었습니다. 아니나 다를까 AWS MySql dbms의 입력 인코딩 설정은 **'ISO Latin-1(SIO-8859-1)'** 이었습니다. 

원격으로 AWS에 연결해서 MySQL Workbench를 이용하면 한글이 입력/조회가 되었고 csv 파일에 임포트가 되었었기에 거기에 문제가 있었을 거라고 생각하지 못했던 것이었습니다. AWS에서 다운 받은 MySqlSever을 설치를 할 경우 windows에서 처럼 친절하게 한글 인코딩 설정을 묻지도 않습니다. AWS는 기본적으로 US-us 설정이기 때문에 설치할 때도 가장 기본값의 Latin-1 인코딩 설정을 따릅니다. 이걸 깨닫고 my.cnf를 찾아가 인코딩 설정을 수정했습니다. 그제서야 한글이 입력되었고 정상적으로 조회가 되었습니다. mybatis는 데이터소스에 직접 접근하기 때문인지 Workbench와는 다르게 중간에 인코딩 변환을 하지 않았고 서버에 db는 알지도 못하는 쿼리의 한글값을 인식하지 못했던 것이었습니다. 그렇게 허탈한 마음을 다스리며 문제를 모두 해결했을때 시간을 돌아보니 벌써 자정을 넘기게 되었습니다. 처음 Database 서버를 구축할때 세심하게 인코딩 설정을 신경쓰지 못한 문제였고 정말 많은 것을 배우게되는 순간이었습니다. 


# 빌드 및 실행 방법

우선 위에 4번 문제를 풀기 위해 화면상에 json 데이터를 입력받는 일을 처리해야 했습니다. 그래서 간단하게 index.html을 생성하여 jquery Ajax 를 활용해restController를 호출 할 수 있도록 처리 했습니다. json 데이터는 스크립트 상에서 생성했지만, 만약 평가자께서 POSTMAN이라는 API테스트 툴을 알고 계신다먼 그것을 활용해 주셔도 됩니다. (POSTMAN 활용시 Json 데이터 설정이 따로 필요합니다.) index.html 은 빌드된 jar 파일 실행 후 웹브라우저에 http://localhost:8080 을 입력하시면 바로 뜹니다. 여기에 문제 제공하신 모든 문제에 대한 대답을 정리해 놨습니다. 평가에 활용하시기 바랍니다. 

1. 빌드 방법


조회 url입니다.












