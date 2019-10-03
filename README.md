# 특정 고객 거래내역 조회 서비스 개발 명세
### For Kakao Stock! 

## 사용한 개발 프레임워크

1. Spring Boot
> * Mybatis + MySql JDBC
> * Spring boot starter aop
> * Spring boot starter web
> * Spring boot starter test
> * Jquery

2. AWS EC2 linux + MySQL 데이터베이스 환경 구성 
> * 접속 정보 IP : 52.79.240.193:3306 
> * properties.yml에 설정되어 있어서 jar 파일 실행시 언제든 접속 가능

## 문제 해결 방법


## windows docker -> linux docker [branch : mylinux]
> 윈도우 도커로는 실제 웹 배포 환경을 구축하기 대단히 어려워서 가상화 서버를 통해 배포 시스템을 구축해 보기로 했다

1. vmware로 centos 가상화 구축
2. 리눅스 docker 설치
  ```
  1. Install => yum install docker
  2. 서비스 실행 => service docker start
  3. 부팅시 자동 실행 설정 => chkconfig docker on
  ```
3. 필요한 프로그램 설치 : git, maven (지금까진 이정도면 충분하다.)
4. git clone https://github.com/chrisna2/msa_in_docker.git
5. mvn clean package docker:build
6. [수정 지점] : docker-compose 파일의 볼륨을 설정할 경우 
> window-docker에는 host volume을 외부 볼륨(external)으로 따로 경로설정없이 이름으로 구성


# 추후 추가로 진행해야 할 것들
1. ~~7장과 8장을 같이 융합~~ 그냥 하니까 잘된다. 책을 다시 확인해보니 검증 서비스 영역과 비검증 서비스 영역으로 나눠애 도리것 같다. 그와 같이 서비스 게이트 웨이도 따로 만들어 줘야 할 것 같다.
2. ~~로그인을 검증정보 DB를 따로 만들야 될것 같다.~~ 
(오라클로 설치함, 그러나 로그인 정보는 이미 mariadb로 구축이 완료 되어서 불필요함 이건 패스)
3. 현재 MSA 환경에 파이썬을 끼얺어 봐야 할것 같다.
4. ~~9장 분산 로그 환경 구성~~ (완료)
5. 10장 AWS 배포 까지.
6. 리눅스 서버에 docker를 구축하고 실제 MSA CICD 구성
7. ui msa 서비스를 구성해 본다. msa-sa06-bnkuisvc
8. ~~리눅스로 옮기고 나서 집킨에 서비스가 제대로 등록되지 않고 있다.~~ (확인:서비스가 제대로 안올라간 것이었다.)
9. logo

