package com.kretov.accountmanagement.rest;

import com.kretov.accountmanagement.controller.AccountController;
import com.kretov.accountmanagement.controller.CustomerController;
import com.kretov.accountmanagement.dto.AccountDto;
import com.kretov.accountmanagement.entity.Account;
import com.kretov.accountmanagement.entity.Customer;
import com.kretov.accountmanagement.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.kretov.accountmanagement.response.Status.ERROR;
import static com.kretov.accountmanagement.response.Status.SUCCESS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(value = "Account", description = "API для работы со счетами клиентов")
@RestController
@RequestMapping("/bank")
public class AccountRestController {

	@Autowired
	private AccountController accountController;

	@Autowired
	private CustomerController customerController;

	/**
	 * Получить все банковские счета
	 *
	 * @return список счетов
	 */
    @ApiOperation(value = "Получить информацию о всех счетах")
	@GetMapping("/accounts")
	List<String> getAllAccounts() {
		Response<Account> response = accountController.getAllAccounts();
		return response.getResult().stream().map(Account::toString).collect(Collectors.toList());
	}

	/**
	 * Получить все счета конкретного клиента
	 *
	 * @param id клиент
	 * @return все счета клиента или сообщение, что клиент некорректный
	 */
    @ApiOperation(value = "Получить все счета клиента")
	@GetMapping("/accounts/{id}")
	String getAccountsByCustomerId(@PathVariable String id) {
        Response<Account> response = accountController.getAccountsByCustomerId(id);
        if (SUCCESS.equals(response.getStatus())) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Account account : response.getResult()) {
                if (account != null) {
                    stringBuilder.append(account.toString());
                    stringBuilder.append("\n");
                }
            }
            return stringBuilder.toString();
        }
        return response.getDescription();
	}

	/**
	 * Получить конкретный счет
	 *
	 * @param id счет
	 * @return информация по счету или сообщение, что счет некорректный
	 */
    @ApiOperation(value = "Получить данные по счету")
	@GetMapping("/accountInfo/{id}")
	String getAccountByAccountId(@PathVariable String id) {
        Response<Account> response = accountController.getAccountByAccountId(id);
        if (SUCCESS.equals(response.getStatus())) {
            return response.getResult().get(0).toString();
        }
        return response.getDescription();
    }

	/**
	 * Удалить счет
	 * @param id идентификатор счета
	 * @return Статус операции
	 */
    @ApiOperation(value = "Удалить счет")
	@DeleteMapping("/accountDelete/{id}")
	String deleteAccountByAccountId(@PathVariable String id) {
        Response<Account> response = accountController.deleteAccountByAccountId(id);
        return response.getDescription();
	}

    /**
     * Создать счет
     * @param newAccount dto с id клиента и суммой на счету
     * @return Статус операции
     */
    @ApiOperation(value = "Создать новый счет")
    @PostMapping(value="/accountCreate", consumes = APPLICATION_JSON_VALUE)
    String createAccount(@RequestBody AccountDto newAccount) {
        Response<Account> response = accountController.createAccount(newAccount.getCustomerId(), newAccount.getMoney());
        return response.getDescription();
    }

    /**
     * Обновить существующий счет
     * @param id идентификатор счета
     * @param updatedAccount новые данные (id клиента и сумма)
     * @return Статус операции
     */
    @ApiOperation(value = "Изменить данные счета")
    @PutMapping(value="/accountUpdate/{id}", consumes = APPLICATION_JSON_VALUE)
    String updateAccount(@PathVariable String id, @RequestBody AccountDto updatedAccount) {
        try {
            Response<Customer> customerResponse = customerController.findById(updatedAccount.getCustomerId().toString());
            if (ERROR.equals(customerResponse.getStatus())) {
                return "Illegal customer id";
            }
            Account account = new Account();
            account.setId(Long.valueOf(id));
            account.setCustomer(customerResponse.getResult().get(0));
            account.setMoney(updatedAccount.getMoney());
            Response<Account> response = accountController.updateAccount(account);
            return response.getDescription();
        } catch (NumberFormatException e) {
            return "Illegal format of input data. Please, use number.";
        }
    }

	/**
	 * Положить деньги на счет
	 *
	 * @param id счет
	 * @param money сумма пополнения
	 * @return Статус операции и новое состояние счета
	 */
    @ApiOperation(value = "Положить деньги на счет")
	@PutMapping("/deposit/{id}/{money}")
	String depositMoney(@PathVariable String id, @PathVariable String money) {
        Response<Account> response = accountController.depositMoney(id, money);
        if(SUCCESS.equals(response.getStatus())) {
            return response.getDescription() + "\n" + response.getResult().get(0).toString();
        } else {
            return response.getDescription();
        }
    }

	/**
	 * Снять деньги со счета
	 *
	 * @param id счет
	 * @param money сумма снятия
	 * @return Статус операции (могла быть слишком большая сумма снятия) и новое состояние счета
	 */
    @ApiOperation(value = "Снять деньги со счета")
	@PutMapping("/withdraw/{id}/{money}")
	String withdrawMoney(@PathVariable String id, @PathVariable String money) {
        Response<Account> response = accountController.withdrawMoney(id, money);
        if(SUCCESS.equals(response.getStatus())) {
            return response.getDescription() + "\n" + response.getResult().get(0).toString();
        } else {
            return response.getDescription();
        }
	}

	/**
	 * Перевести со счета на счет
	 *
	 * @param sourceId счет снятия
	 * @param destinationId счет пополнения
	 * @param money сумма перевода
	 * @return Статус операции (могло быть недостаточно денег на счету) и новые состояния счетов
	 */
    @ApiOperation(value = "Перевести деньги со счета на счет")
	@PutMapping("/transfer/{sourceId}/{destinationId}/{money}")
	String transferMoney(@PathVariable String sourceId, @PathVariable String destinationId, @PathVariable String money) {
        Response<Account> response = accountController.transferMoney(sourceId, destinationId, money);
        if(SUCCESS.equals(response.getStatus())) {
            return response.getDescription() + "\n" + response.getResult().get(0).toString() + "\n" + response.getResult().get(1).toString();
        } else {
            return response.getDescription();
        }
	}

}
