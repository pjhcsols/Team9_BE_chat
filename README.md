# Team9_BE
9조 백엔드

## 백엔드 역할 분담

1. 검색 - 심규민
2. 로그인 및 회원 가입 + 작가 + 팔로우 - 윤재용
3. 채팅 + 파일 - 박한솔
4. 상품(피드), 좋아요, 감상평 - 주보경
5. 인프라(ci/cd, https, 모니터링) - 김동현

## CI/CD 및 인프라 구축
![CI/CD 과정 - GitHub Actions를 활용한 배포 프로세스](https://velog.velcdn.com/images/hyunn/post/986c6af9-b694-44c2-a554-dd51e091fde0/image.png)
### CI/CD 과정 - GitHub Actions를 활용한 배포 프로세스

1. **GitHub Actions 트리거**
    - Master 브랜치에 코드가 푸시되면 GitHub Action이 자동으로 동작합니다.
2. **도커 이미지 생성 및 업로드**
    - 프로젝트를 빌드하고, 도커 이미지를 생성한 후 도커 허브에 이미지를 업로드합니다.
3. **배포 스크립트 실행**
    - EC2-1 서버에 접속하여 배포 스크립트(`deploy.sh`)를 실행합니다.
      https://github.com/donghyuun/katecam-infra/blob/main/ec2-dual-zero-downtime-lb/deploy.sh

#### (deploy.sh 스크립트 동작)

1. **백엔드 서버의 포트 검사**
    - EC2-1, EC2-2에서 현재 동작 중인 백엔드 서버(도커 컨테이너)가 사용 중인 포트(8080 또는 8081)를 검사합니다.
    - 사용하지 않는 포트(8081 또는 8080)를 확인합니다.
2. **새 서버 컨테이너 배포**
    - 도커 허브에서 최신 이미지를 PULL하여, EC2-1, EC2-2의 사용 가능한 포트에 새 백엔드 서버(도커 컨테이너)를 실행합니다.
3. **헬스 체크 수행**
    - 새로운 서버의 정상 동작을 확인하기 위해 `/actuator/health` 엔드포인트로 헬스 체크를 수행합니다.
    - 이 과정에서 DB 연결 상태도 함께 확인합니다.
4. **트래픽 분산 대상 변경 (Blue-Green 배포)**
    - 헬스 체크가 성공하면 NGINX 설정을 업데이트하여 트래픽을 새 서버로 분산합니다.
    - BLUE-GREEN 방식으로 무중단 배포를 수행합니다.
5. **NGINX 리로드**
    - 변경된 NGINX 설정을 적용하기 위해 NGINX를 리로드합니다.
6. **기존 서버 종료 및 삭제**
    - 기존에 EC2-1, EC2-2에서 실행 중이던 컨테이너를 종료하고 삭제하여 이전 서버를 정리합니다.

### 인프라 구성 - EC2 서버 구성 및 모니터링

#### EC2 - 1

- **백엔드 서버1** (도커 컨테이너)
- **Nginx** - 80 포트로 들어오는 요청을 백엔드 서버 1, 2에 대해 로드밸런싱
- **MySQL** - 백엔드 서버 1, 2가 참조하는 DB
- **Prometheus** - 백엔드 서버 1, 2의 메트릭 수집
- **Grafana** - Prometheus가 수집한 메트릭 시각화 (URL: http://golden-ratio.duckdns.org:3000/) (admin/admin)
- **Dozzle** - EC2-1, EC2-2의 도커 컨테이너 로그 시각화 (URL: http://golden-ratio.duckdns.org:7070/)

#### EC2 - 2

- **백엔드 서버2** (도커 컨테이너)
- **Dozzle** - (Agent) EC2-2의 도커 컨테이너 로그를 EC2-1의 Dozzle에게 전달

#### 서버 1개일 때 vs 2개일 때(LB 적용) TPS 간단 비교 (Swagger GET 기준)

- TPS 테스트 결과: 두 개의 서버로 로드밸런싱 적용 시 성능이 향상됨

### EC2 인스턴스 생성 - Terraform 이용

1. **Terraform을 이용한 EC2 인스턴스 생성**
    - Terraform 스크립트를 사용해 AWS에서 EC2 인스턴스를 생성하고, 관련 리소스(보안 그룹, VPC, 서브넷, 키 페어 등)를 설정하였습니다.

2. **생성 절차**
    1. **AWS CLI**를 사용해 AWS 계정과 로컬 환경 연결 후 인증 설정
    2. 테라폼 스크립트 작성 후 실행(ec2.tf)
       https://github.com/donghyuun/terraform-study/blob/main/ec2.tf
    - 리소스 설정이 완료된 EC2 인스턴스가 생성됩니다.
