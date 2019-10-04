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

프로그래밍 로직 외에 다른 이유로 시간을 굉장히 잡아 먹었던 문제였습니다. ㅜ_ㅜ; 그 문제를 해결하기 위해 개천절 오전 오후를 다 보내게 되어 일정이 하루 늦어지게 되었습니다. 

로직 부터 설명을 드리면 우선 화면상에 json 데이터를 입력받는 일을 처리해야 했습니다. 그래서 간단하게 index.html을 생성하여 jquery Ajax 를 활용해restController를 호출 할 수 있도록 처리 했습니다. json 데이터는 스크립트 상에서 생성했지만, 만약 평가자께서 POSTMAN이라는 API테스트 툴을 알고 계신다먼 그것을 활용해 주셔도 됩니다. 호출 URL은 동일하게 localhost:8080/function4 이고 통신방식은 POST입니다. (POSTMAN 활용시 Json 데이터 설정이 따로 필요합니다.) index.html 은 빌드된 jar 파일 실행 후 웹브라우저에 http://localhost:8080을 입력하면 바로 뜹니다. 여기에 문제 제공하신 모든 문제에 대한 대답을 정리해 놨습니다. 평가에 활용하시기 바랍니다. 

우선 문제에 조건가운데 분당점이 판교로 이관되었다는 것에 집중 했습니다. 신용보증기금 차세대 프로젝트를 진행하면서 이수관 업무를 맏았던 저로서는
정상적으로 이관이 완료가 되었다면 해당 하위 데이터가 모두 판교점으로 이관 되어야 한다고 생각했습니다. 즉 분당점의 데이터는 판교점으로 조회할때 같이 조회되어야 한다고 생각했습니다. 그



















