# TTMIK 엔지니어팀 기술 면접 사전 과제

### 면접 과제
- 현재 IP를 기반으로 사용자 접속 국가를 알려주는 REST API 개발해주세요.
- 옵션 : cdktf 혹은 유사한 IaC툴 을 이용하여 개발된 API 를 배포해주세요.

### 기본 기능 구현
- 접속 IP 표시
- 접속 국가 표시
- 페이로드로 현재 IP를 POST 메서드로 API 로 전달해주면 현재 국가와 HTTP상태 코드를 출력

## 과제 내용
### 배포 서버 주소 : http://3.39.182.50:8080
### IaC 툴: AWS CloudFormation 사용
- resources/template.yaml
### API URL(http://3.39.182.50:8080/ip-to-country)

#### 유효한 IP 
<img src="https://github.com/kgc0120/ipgeocheck/assets/29886664/e402de65-8cf3-4349-95f0-4dc2b47ebfd7" width="700" height="370">
                                                                                                                             
```
Request
{
    "ip" : "223.38.90.72"
}
```
```
Response
HTTP/1.1 200 OK
{
    "countryCode": "KR",
    "status": 200,
    "ip": "223.38.90.72"
}
```

#### 유효하지 않는 IP
<img src="https://github.com/kgc0120/ipgeocheck/assets/29886664/1bad836e-6bc2-4543-96eb-abd6ea1de05d" width="700" height="370">

```
Request
{
    "ip" : "10.0.0.1"
}
```
```
Response
HTTP/1.1 404 Not Found
{
    "countryCode": "UnKnown",
    "status": 404,
    "ip": "10.0.0.1"
}
```
#### 잘못된 요청
<img src="https://github.com/kgc0120/ipgeocheck/assets/29886664/e369ec83-cb60-42f1-ba76-4d5de2c9be1c" width="700" height="370">

```
Request
{
    "ip123" : "223.38.90.72"
}
```
```
Response
HTTP/1.1 400 Bad Request
{
    "countryCode": "UnKnown",
    "status": 400,
    "ip": ""
}
```
