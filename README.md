Приложение на Java, Spring Boot и JPA.

**Основные операции**

Перевод денег с одного счёта на другой (transfer);
положить деньги на счёт (deposit);
снять деньги со счёта (withdraw).
Отрицательный баланс счета недопустим.

Используется HSQLDB, инициализация БД при старте приложения.

Таблицы в файле schema.sql, тестовые данные в data.sql (тоже заполняется при старте).
CUSTOMER - таблица клиентов
ACCOUNT - таблица счетов, связана с клиентами по полю CUSTOMER_ID

Сборка maven (mvn clean package). Формируется jar

Сделаны юнит-тесты на AccountService в части операций transfer, deposit и withdraw

REST-контроллеры AccountController и CustomerController.

**Добавлен swagger для удобной проверки API в браузере.**

**UI доступен по адресу http://localhost:9090/swagger-ui.html**

Можно проверить и через curl ( https://curl.haxx.se/download.html ).

_Примеры локальных запросов (порт 9090)_

Получить всех клиентов:
**curl localhost:9090/customers**

Получить все счета:
**curl localhost:9090/accounts**

Получить все счета клиента с id=1:
**curl localhost:9090/accounts/1**

Получить счет с id=1:
**curl localhost:9090/accountInfo/1**

Положить 100 рублей на счет 1:
**curl localhost:9090/deposit/1/100**

Снять 100 рублей со счета 1:
**curl localhost:9090/withdraw/1/100**

Перевести 100 рублей со счета 1 на счет 2:
**curl localhost:9090/transfer/1/2/100**