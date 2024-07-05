1. .env.example 파일 복사 후 .env 를 만든다. smtp 는 설정하지 않아도 된다. (단, smtp 설정하지 않으면 회원가입 안됨)
2. docker-compose.yml 을 실행한다. `docker-compose up -d` 로 디태치 모드로 실행한다. 실행이 안되면 port 문제일 확률이 높으므로 포트를 확인하자.
3. 서버 실행 시 시드 데이터 주입됨.


