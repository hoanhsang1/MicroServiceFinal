@echo off

echo Starting EurekaServer...
start "EurekaServer" cmd /k "cd /d D:\User\downloads\Assignment_VTi\javacore\DTN2601\MicroServiceFinal\EurekaServer && mvn spring-boot:run"

echo Waiting for EurekaServer...

:WAIT_EUREKA
timeout /t 5 /nobreak >nul
curl -s http://localhost:8761/ >nul

if errorlevel 1 (
    echo EurekaServer is not ready...
    goto WAIT_EUREKA
)

echo EurekaServer is ready!
echo Starting ConfigServer...

start "ConfigServer" cmd /k "cd /d D:\User\downloads\Assignment_VTi\javacore\DTN2601\MicroServiceFinal\ConfigServer && mvn spring-boot:run"

echo Waiting for ConfigServer...
timeout /t 10 /nobreak >nul

echo Starting UserService...
start "UserService" cmd /k "cd /d D:\User\downloads\Assignment_VTi\javacore\DTN2601\MicroServiceFinal\UserService && mvn spring-boot:run"

echo Starting ProductService...
start "ProductService" cmd /k "cd /d D:\User\downloads\Assignment_VTi\javacore\DTN2601\MicroServiceFinal\ProductService && mvn spring-boot:run"

echo Starting OrderService...
start "OrderService" cmd /k "cd /d D:\User\downloads\Assignment_VTi\javacore\DTN2601\MicroServiceFinal\OrderService && mvn spring-boot:run"

echo Starting PaymentService...
start "PaymentService" cmd /k "cd /d D:\User\downloads\Assignment_VTi\javacore\DTN2601\MicroServiceFinal\PaymentService && mvn spring-boot:run"

echo Starting AuthService...
start "AuthService" cmd /k "cd /d D:\User\downloads\Assignment_VTi\javacore\DTN2601\MicroServiceFinal\AuthService && mvn spring-boot:run"

echo Starting NotifyService...
start "NotifyService" cmd /k "cd /d D:\User\downloads\Assignment_VTi\javacore\DTN2601\MicroServiceFinal\NotifyService && mvn spring-boot:run"

echo Starting APIGateway...
start "APIGateway" cmd /k "cd /d D:\User\downloads\Assignment_VTi\javacore\DTN2601\MicroServiceFinal\APIGateway && mvn spring-boot:run"

echo All services started.