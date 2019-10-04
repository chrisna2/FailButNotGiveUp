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

2. AWS EC2 linux + MySQL DBMS
> * 접속 정보 IP : 52.79.240.193:3306 


## 문제 해결 방법
### 서비스 개발 사전 작업 : Databasea및 프레임워크 구축
1. 데이터를 어디에서도 확인이 가능 하도록 Database를 구축해야 했습니다.
AWS EC2 리눅스(프리티어) 서버를 구축하고 MySql DB를 구축하여 해당 접속 정보를 
properties.yml에 등록했습니다. 현재 이 해당 깃 허브를 통해 소스를 다운 받으시거나
빌드된 JAR파일을 보유하고 있다면 언제든 해당 DB에 접속이 가능합니다.
하지만 로컬의 DB를 구축한 상황을 가정해서 제가 구축한 DB의 schema.sql도 프로젝트에 함께 동봉했습니다. (src/main/resource/)


2. 기본적으로 개발을 진행하기 위해서 프로젝트 내에 일을 구성할 수 있는 프래임워크가 구축이 되어야 합니다. 가장 일반적으로 필요한 것은 로그처리와 예외처리, 그에 대한 메세징처리, DataSource 설정, 프로젝트 디자인이 대표 적일 것입니다.

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
	where
		a.acct_no = b.acct_no
		and a.can_yn = 'N'
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


