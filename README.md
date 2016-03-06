
# thx-ws-api
maven template project of test suite for web services testing

## how to run in commandline
1. compile  
   `mvn clean install`

2. run  
```
java -cp target/*:target/th/*:target/dependency/* \
     -Dqa.th.comm.ws.HOST=10.0.0.123 \
     -Dqa.th.comm.ws.PORT=443 \
     -Dqa.th.comm.ws.USER=admin \
     -Dqa.th.comm.ws.PASS=password \
     com.cliqr.qa.user.suite.UserManagementSuite
```
