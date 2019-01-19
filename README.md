# Приложение на Java, Spring Boot и JPA.

### **Основные операции**
_Отрицательный баланс счета недопустим._
+ _Перевести деньги с одного счёта на другой: **transfer**_
+ _Положить деньги на счёт: **deposit**_
+ _Снять деньги со счёта: **withdraw**_

### Сборка приложения

+ Сборка _maven_ (_**mvn clean package**_).
+ Формируется _jar_-файл _**account-management-0.0.1-SNAPSHOT.jar**_.

### Хранение данных

Используется _HSQLDB_, инициализация базы данных при старте приложения.

Таблицы описаны в файле _schema.sql_, тестовые данные описаны в _data.sql_ (тоже заполняется при старте).
+ **CUSTOMER** - таблица клиентов.
+ **ACCOUNT** - таблица счетов, связана с клиентами по полю _CUSTOMER_ID_. По умолчанию предусмотрено каскадное удаление.

### Техническая реализация

+ Приложение сделано на _Spring Boot_, параметры в _application.properties_.
+ Слои контроллеров (REST-контроллеры _AccountController_ и _CustomerController_), сервисов и репозитория (_JpaRepository_).
+ Сделаны юнит-тесты на _AccountService_ в части операций _transfer_, _deposit_ и _withdraw_.
+ Добавлен **_swagger_** для удобной проверки API в браузере.

### Проверка приложения

UI _swagger_ доступен по адресу **http://localhost:9090/swagger-ui.html**

Можно проверить и напрямую через _curl_ [Скачать curl](https://curl.haxx.se/download.html)

#### Примеры локальных запросов (порт 9090)

##### CustomerController

+ _Получить всех клиентов:_
**curl localhost:9090/bank/customers**

+ _Удалить клиента с id=1:_
**curl -X DELETE "http://localhost:9090/bank/customerDelete/1" -H  "accept: \*/\*"**

##### AccountController

+ _Получить все счета:_
**curl localhost:9090/bank/accounts**

+ _Получить все счета клиента с id=1:_
**curl localhost:9090/bank/accounts/1**

+ _Получить счет с id=1:_
**curl localhost:9090/bank/accountInfo/1**

+ _Удалить счет с id=1:_
**curl -X DELETE "http://localhost:9090/bank/accountDelete/1" -H  "accept: \*/\*"**

+ _Положить 100 рублей на счет 1:_
**curl -X PUT "http://localhost:9090/bank/deposit/1/100" -H  "accept: \*/\*"**

+ _Снять 100 рублей со счета 1:_
**curl -X PUT "http://localhost:9090/bank/withdraw/1/100" -H  "accept: \*/\*"**

+ _Перевести 100 рублей со счета 1 на счет 2:_
**curl -X PUT "http://localhost:9090/bank/withdraw/1/2/100" -H  "accept: \*/\*"**