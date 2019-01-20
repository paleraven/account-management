# Приложение на Java, Vaadin, Spring Boot и JPA.

### **Основные операции**
- _Операции доступны по REST API_
- _Отрицательный баланс счета недопустим._
+ _Перевести деньги с одного счёта на другой: **transfer**_
+ _Положить деньги на счёт: **deposit**_
+ _Снять деньги со счёта: **withdraw**_
+ Стандартные операции (создать, удалить, изменить счет и клиента; получить всех клиентов, все счета, все счета по конкретному клиенту, конкретный счет)

### Сборка приложения

+ Сборка _maven_ (_**mvn clean package**_).
+ Формируется _jar_-файл _**account-management-0.0.1-SNAPSHOT.jar**_.

### Хранение данных

Используется _HSQLDB_, инициализация базы данных при старте приложения.

Таблицы описаны в файле _schema.sql_, тестовые данные описаны в _data.sql_ (тоже заполняется при старте).
+ **CUSTOMER** - таблица клиентов.
+ **ACCOUNT** - таблица счетов, связана с клиентами по полю _CUSTOMER_ID_. Предусмотрено каскадное удаление.

### Техническая реализация

+ Приложение сделано на _Spring Boot_, параметры в _application.properties_ (порт _9090_).
+ Слои контроллеров (REST-контроллеры _AccountRestController_ и _CustomerRestController_), сервисов и репозитория (_JpaRepository_).
+ Сделаны юнит-тесты на _AccountService_ в части операций _transfer_, _deposit_ и _withdraw_.
+ Добавлен **_swagger_** для удобной проверки API в браузере.
+ Добавлен UI на **Vaadin**

### Проверка приложения

UI _Vaadin_ доступен по адресу **http://localhost:9090/ui**

UI _swagger_ доступен по адресу **http://localhost:9090/swagger-ui.html**

Можно проверить и напрямую через _curl_ [Скачать curl](https://curl.haxx.se/download.html)

#### Описание UI Vaadin
* Верхняя часть формы - список клиентов, с возможностью фильтрации по фамилии. Можно добавить нового клиента.
* По нажатию на конкретного клиента доступны следующие возможности:
    * Редактирование и удаление клиента
    * В нижней части формы отображаются счета с фильтрацией по выбранному клиенту, доступны следуюшие операции:
        * Создание счета у выбранного клиента
        * Редактирование и удаление выбранного счета
        * Положить деньги на счет, снять деньги со счета, перевести деньги со счета на счет

#### Примеры локальных запросов (порт 9090)

##### CustomerRestController

+ _Получить всех клиентов:_
**curl localhost:9090/bank/customers**

+ _Удалить клиента с id=1:_
**curl -X DELETE "http://localhost:9090/bank/customerDelete/1" -H  "accept: \*/\*"**

+ _Создать клиента Sergey Sidorov:_
**curl -X POST "http://localhost:9090/bank/customerCreate" -H  "accept: \*/\*" -H  "Content-Type: application/json" -d "{  \\"firstName\\": \\"Sergey\\",  \\"lastName\\": \\"Sidorov\\"}"**

+ _Переименовать клиента с id=1 как Sergey Sidorov:_
**curl -X PUT "http://localhost:9090/bank/customerUpdate/1" -H  "accept: */*" -H  "Content-Type: application/json" -d "{  \\"firstName\\": \\"Sergey\\",  \\"lastName\\": \\"Sidorov\\"}"**

##### AccountRestController

+ _Получить все счета:_
**curl localhost:9090/bank/accounts**

+ _Получить все счета клиента с id=1:_
**curl localhost:9090/bank/accounts/1**

+ _Получить счет с id=1:_
**curl localhost:9090/bank/accountInfo/1**

+ _Удалить счет с id=1:_
**curl -X DELETE "http://localhost:9090/bank/accountDelete/1" -H  "accept: \*/\*"**

+ _Создать счет с id клиента 2 на 200 рублей:_
**curl -X POST "http://localhost:9090/bank/accountCreate" -H  "accept: \*/\*" -H  "Content-Type: application/json" -d "{  \\"customerId\\": 2,  \\"money\\": 200}"**

+ _У счета с id=1 установить клиента с id=2 и установить сумму 200 рублей:_
**curl -X PUT "http://localhost:9090/bank/accountUpdate/1" -H  "accept: \*/\*" -H  "Content-Type: application/json" -d "{  \\"customerId\\": 1,  \\"money\\": 200}"**

+ _Положить 100 рублей на счет 1:_
**curl -X PUT "http://localhost:9090/bank/deposit/1/100" -H  "accept: \*/\*"**

+ _Снять 100 рублей со счета 1:_
**curl -X PUT "http://localhost:9090/bank/withdraw/1/100" -H  "accept: \*/\*"**

+ _Перевести 100 рублей со счета 1 на счет 2:_
**curl -X PUT "http://localhost:9090/bank/withdraw/1/2/100" -H  "accept: \*/\*"**