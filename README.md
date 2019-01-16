Используется HSQLDB, инициализация БД при старте приложения.
Таблицы в файле schema.sql, тестовые данные в data.sql (тоже заполняется при старте).

REST-контроллер AccountController.
Запросы проверяются через curl ( https://curl.haxx.se/download.html ).

_Примеры локальных запросов (порт 9090)_

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