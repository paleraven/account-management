package com.kretov.accountmanagement.util;

import com.kretov.accountmanagement.service.AccountService;

public class AccountUtil {
	private AccountUtil() {
	}


	public static boolean validateAccount(AccountService accountService, Long accountId){
		return accountService.findById(accountId) != null;
	}
}
