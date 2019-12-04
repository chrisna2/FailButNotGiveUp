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
### 서비스 개발 사전 작업 : Database 서버 및 프레임워크 구축
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
		b.acct_name as name,
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

그 결과를 List<String>으로 받아서 for문 조건식으로 활용하고 그 안에서 추가 조회를 합니다. 추가 조회는 dataList의 목록을 조회 합니다.v추가 조회하는 쿼리는 다음과 같습니다. 
	
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

## 빌드 방법

### 1. 압축 파일 버전
1. 현재 프로젝트를 내려 받습니다. (download zip)
2. 압축파일을 받으신 후 압축을 풉니다.
3. pom.xml이 있는 위치에서 shift + 오른쪽 마우스하여 PowerShall 창을 엽니다.
(cmd에서 해당 경로로 이동하셔도 됩니다.)
4. PowerShall 명령창에 'mvn clean install' 명령을 입력합니다. (maven 설치하고 환경변수 설정 필수!)
5. 테스트가 완료되고 Build Success가 뜰때까지 기다린다. (테스트 결과 확인 가능) 
6. 그러면 target 폴더가 생성되면서 그 안에 'KakaoPay-0.0.1-SNAPSHOT.jar' 파일이 생성됩니다.

### 2. STS4(이클립스) 사용시
1. 현재 프로젝트 레포지토리 주소를 복사합니다. (clone uri)
2. 이클리스 상에서 File > import 누르시고 Git > Project from Git을 클릭 
3. Clone URI를 누르시고 복사했던 URI를 붙여넣기 합니다.
4. Next > ... > import exsiting Eclipse projects 클릭 (이전까지 설정은 사용자 상황에 맞게 설정해주세요)
5. finish 클릭후 프로젝트가 완전히 생성될때 까지 대기
6. 프로젝트 오른쪽 마우스 클릭 > Run As > Maven install 클릭
7. 테스트가 완료되고 Build Success가 뜰때까지 기다린다. (테스트 결과 확인 가능)
8. 그러면 target 폴더가 생성되면서 그 안에 'KakaoPay-0.0.1-SNAPSHOT.jar' 파일이 생성됩니다.


## 실행 방법

```
1. 생성된 jar 파일을 클릭한다.
2. http://localhost:8080 접속한다.

or

1. 명령 프롬프트(cmd, powerShall)를 연다.
2. java -jar  /{...jar파일 생성경로}/KakaoPay-0.0.1-SNAPSHOT.jar 입력 (java 환경변수 설정 필수)
3. http://localhost:8080 접속한다.

or

(빌드 안하신 경우)
1. STS4에 프로젝트 import
2. 프로젝트 오른쪽 마우스 > Run As > 9. Spring Boot App 
```

우선 위에 4번 문제를 풀기 위해 화면상에 json 데이터를 입력받는 일을 처리해야 했습니다. 그래서 간단하게 index.html을 생성하여 jquery Ajax 를 활용해restController를 호출 할 수 있도록 처리 했습니다. json 데이터는 스크립트 상에서 생성했지만, 만약 평가자께서 POSTMAN이라는 API테스트 툴을 알고 계신다먼 그것을 활용해 주셔도 됩니다. (POSTMAN 활용시 Json 데이터 설정이 따로 필요합니다.) index.html 은 빌드된 jar 파일 실행 후 웹브라우저에 http://localhost:8080 을 입력하시면 바로 뜹니다. 여기에 제공하신 모든 문제에 대한 답을 확인하실수 있습니다. 평가에 활용하시기 바랍니다. 

### 조회 url입니다.
[문제 1] 년도 별 최대 합계 금액 고객 정보 추출

URL : localhost:8080/function1 [get]

[문제 2] 년도 별 거래가 없는 고객 정보 추출

URL : localhost:8080/function2 [get]

[문제 3] 년도 별, 관리점 별 거래금액 합계 추출 : 출력 순서 [합계금액 desc]

URL : localhost:8080/function2 [get]

[문제 4] 지점명 입력시 해당지점의 거래금액 합계 출력

URL : localhost:8080/function4 [post]
[localhost:8080 접속시 인덱스에서 확인 가능]


### Unit Test 코드

가장 중요한 부분인 Unit Test 코드 작성은 각각의 RestApi Cotroller Url 갯수만큼 클래스를 따로 만들어 생성했습니다.
src/test/java/assignment/kakaopay/pickme 경로에 각각의 url 이름으로 된 패키지에 단위 테스트 클래스를 작성했습니다.
@WebMvcTest 형식이고 MockMvc로 Mvc구조안에서 테스트를 구성했습니다.
mybatis테스트를 위해 @AutoConfigureMybatis 통해서 실제 DB에 접속해 데이터까지 확인합니다. 
실제 한번 전체 로직을 통과하고 입출력 데이터 형태를 체크하고 http status를 확인하는 구조로 생성했습니다. 

실제 데이터 값을 상정하고 값이 맞는지 아닌지를 테스트하지는 않았습니다. 필요한 입출력의 형태 구조를 정확히 출력하는가에 집중했습니다.

Spring boot starter test 외에 hamcrest 의존성을 추가해서 사용했습니다. json 이나 map으로 구성된 데이터의 형태를 점검하는데
아주 이상적이 라이브러리였습니다.

문제 1 부터 3까지는 조회로서 @Test 메소드를 1개씩만 구성 하였고

문제 4 의 경우 에러를 내뱉는 상황을 상정 했기 때문에 정상출력과 에러출력을 확인하기위해  @Test 메소드를 2개 구성했습니다.

자세한 내용은 코드를 참조해 주시길 부탁드립니다.


[문제 1] 년도 별 최대 합계 금액 고객 정보 추출
/function1/function1Test.java

[문제 2] 년도 별 거래가 없는 고객 정보 추출
/function2/function2Test.java

[문제 3] 년도 별, 관리점 별 거래금액 합계 추출 : 출력 순서 [합계금액 desc]
/function3/function3Test.java

[문제 4] 지점명 입력시 해당지점의 거래금액 합계 출력
/function4/function4Test.java

# 왜 탈락 했을까?

탈락했다. 이유가 무엇인지 궁급했다. 알수 없었다. 알려주지 않았기 때문이다.
답답했다. 온 정신을 다 쏟았기에 더욱 그랬다.
문제가 쉬웠다. 천하의 카카오가 이런 문제를 낼 이유가 없었다. 비록 자회사라고 해도 말이었다.
문제가 쉬워서 핵심은 문제 풀이에 있지 않을 것이라고 생각했다.

빠르게 결과를 낼수록 채용절차가 빨라진다는 시험 요건이 있었다.
나는 간단한 문제였던 만큼 문제풀이 시간에 핵심이 있다고 생각했다.
일주일 기간을 주었는데 나는 위와 같은 생각으로 4일만에 처리해서 보내주었다.

결과는 탈락이었다.

일단 분노에 휩싸여 한동안 그로기 상태에 놓여 있었다. 4일 동안 온힘을 다했기 때문에 더욱 그러한 기분이 깊게 빠졌었다. 카카오에 대한 분노가 생겼다. 사람가지고 장난치는 것 같은 기분이었다.

분노와 좌절의 기간이 지나고 
스스로에 대한 객관적인 상태 평가가 이루어지게 되었다.
나의 개발을 보며 평가를 내리는 상대가 내 결과를 보고 매력응 느끼지 못한 이유가 았을 것이었다. 한 마디로 무언가 놓치고 과제를 수행한 것이다.
지금의 이글은 과연 내가 무엇을 놓친 것인가에 대한 스스로의 반성문이다.
무언가 내가 놓친것이 있었을 것이며 그 놓친 것이 나에게 있어서 중요한 지점인것 같다.

열심히 한게 중요한게 아니었다.
잘하는 것이 중요했다. 스프링 부트 답게 처리하는 것이 중요해 보였다.

## 탈락원인(내 생각)
1. JPA를 사용하지 않았다.
-> 스프립 부트에서 이전과 다른 부분 중 가장 큰 부분을 차지 하는 것은 JPA의 존재 유무라고 생각한다. 자바 1.8버전 이상에 java stream을 적극적으로 이용하고 람다식을 활용하는 것은 이제는 일상이 되어야 했다. 특히나 새로운 시스템을 만들려하는 신생기업에서 가장 최신의 프로그래밍 기법이 적용될 것이 분명했다. JPA의 경우 가장 최근의 기법이라고 해도 나온지는 꽤 오래된 기법이었다. 내가 학원에서 공부하면서 배웠던 전자정부 시절부터 JPA는 세상에 등장했고 이미 꽤나 많은 사이트에서 쿼리를 대체하여 사용되고 있는 데이터 시스템이었다. 스프링 부트를 접한지 이제 막 6개월이 안된 시점에서 mybatis와 쿼리의 익숙하다고 혹은 부산은행 과거에 그렇게 사용햇다는 이유로 Mybatis를 사용했다는 점이 아마 평가 요소 자체를 날려버린 것일지도 모른다슨 생각이 문득 들었다. 시간을 좀 들였더라도 JPA를 활용하여 결과를 출력했어야 했다고 생각했다. 

2. TDD를 제대로 이해하지 못했다.
-> TDD, 테스트 주도형 개발이라는 것은 결국 문화이자 습관이다. 모든 기능을 개발하기 앞서 테스트를 먼저 구상하고 나중에 개밥진행한다는 어렴풋한 지식이 코드에 녹아 내렸다는 생각이 전혀 들지 않았다. 솔직히 일단 먼저 개발 부터 하고 보여주기식 테스트 코드를 만들었다. 개발주도형이있다. 일반적인 코딩을 하고 테스트 한 것 처럼 보였을 것이다. 

3. 과연 DB 서버가 필요했을까?
-> 1번의 내용괴 곂치는 것이다. 이미 데이터를 JSON으로 재공 받았으면 그 제공 받은 데이터는 고대로 데이터로서 안에 목업 파일의 형식으로 사용할수 있었다고 생각한다. 어쩌면 스프링 부트의 내용보다도 스크립트의 구현 내용도 중요햇을 것 같다.

4. 문제를 너무넘겨 짚고 생각했었는지도 모르겠댜.
-> 굳이 내가 실수했던 것을 솔직하게 밝혀야 했을지, 지난 신용보증기금에서의 이수관 경력을 들먹이며 굳이 안해도되는 부분을 나서서했던게 아니었을까? 말그대로 택스트 그대로 문제를 읽었어야 했는지도 모르겠다. 1번문제에 국한된다는 총액에서 수수료를 빼는것이 나머지 문제에서도 영향을 주었던것이 아니었을지. 너무나 자의적인 판단으로 인해서 문제자체에 답을 잘못 낸것인지도 모른다는 생각이 들었다.

5. 긍융 경력 그중에서도 증권사의 경력이 있는가 자문해 보고 싶다.
-> 카카오 이후 증권사 3곳을 도전해 보았지만 다 탈락이었다. 키움증권도 탈락이었고 이전에 미래에셋도 탈락이었다. 어느정도 이름있는 기업에게 아직 내 경력은 그렇게 매력적으로 다가 오지 않는 것이 분명했다. 아버지의 말대로 아직은 좀더 기어야 하는 건지도 모르겠다. 

6. 정말 정말 빠르게 제출했어야 했는지도 모른다.
-> 문제가 정말 말도 안되게 쉬웠다. 이걸 문제라고 내는건지 의심스러울 만큼 내용에 대해 의구심이 들었던 것이다. 사실 일정상에 중간에 개천절이 끼어있어서 개천절까지가 마지노선이 아니었을까 생각한다. 이 정도 뭔가 큰것을 바라고 만들라는 것이 아니라 테스트를 해보라는 것이니 3일 이면 충분하다고 생각했을 것이다.

7. 아니면 남은 기간을 충분히 활용하지 못했거나. 
-> 남은 3일을 기다리며 내가 짠 코드를 복기 하거나 수정하거나 테스트 해보는 과정을 생략햇는지도 모르겠다. 위에 1번과 같은 경우는 테스트 기간동안 충분히 인지하고 있었다. 남은 3일 동안 JPA 버전으로 따로 프로그램을 만들까 하는 고민이 들었지만 그러지 못했다. 지금 생각해보니 정말 최선을 다했는가 의심스럽다. 4일째 끝내고 나서 기운이 빠졌는지 충분히 되돌아 보지 못했던것 같다. 절박한 심정이 있었던 것인지 스스로에게 물어 보고 싶다. 제출한 이후 계속 코드를 수정할 수 있지 않았을까. 짧은 시간안에 제출해야한다는 조급함이 나를 몰고 간것 같다. 

8. 과거에 했던 것을 자랑 하려고 했던것이 오히려 문제를 낳은 건지 모르겠다.
-> 인터셉트를 가져와 사용했다. 쿼리의 로그를 보여주고 싶었고 개발에 용의하기 때문이었다. 그 점이 문제가 아니었을까 생각한다. 붙여넣기하여 사용하는 안일함 또는 그 코드를 자산의 일부라고 생각했는지 그걸 여기에 버젓이 사용하는 것이 윤리적으로 문제가 있다고 생각한 것일지도 모른다. 전체 간단한 패턴 디자인에 갑자기 복잡한 프로그램이 나온것, 그리고 결국에는 예외처리로는 처리하지 못해서 모델에 메세지를 보내 처리하게 했음에 필요없었던 예외 처리 클래스도 남겨두어 봐야 필요없는 부분임에도 자랑하고 싶어서 남겨 두었던 것이 거북하게 느껴졌을 것 같다. 

9. Rest API 를 만들었는가?
-> 컨트롤러에 @RestController를 선언 했다고 Rest API 서버가 아니다. Rest API의 기능을 하기 위해서 갖추어야 하는 부분이 있는가. 인프런에 스프링 교육을 다시 신청하게 된것도 바로 이러한 의문점 때문이었다. 내가 알고있는 것이 아직 많은 것이 부족하다는 반증이었다. 


### 총평 
테스트 결과에 대해 한마디로 다시 평하자면 
```
스프링 부트로 프로그램을 짰음에도 스프링 부트 답지 못했다.
```
이렇게 표현을 하고 싶다. 프레임워크에 담긴 방식을 100% 활용하지 못한것이 문제일지도 몰랐다.
결국 모든 것이 원점으로 돌아간다. 
아직은 많이 부족하고 좀 더 공부해야 한다. 내가 일부 알고 있는 지식이 모든 것을 반영하지 못한다.
급하게 생각하고 안일하게 대응했다. 
꾸준하게 해야 결과가 나오고 그 결과에 대한 반영도 서서히 들어나게 된다.

프로그래머로서 알고리즘이 부족하고 프래임워크에 대한 지식도 많이 부족했다.
업무 개발자로서 업무에 대해 많아 아는가? 그렇지도 않았다.

모든 것이 부족한 상태에서 욕망으로 도전했다. 
그것이 실패의 원인이었다.
