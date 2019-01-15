package com.kretov.accountmanagement.util;

import com.kretov.accountmanagement.service.AccountService;

public class CustomerUtil {
	private CustomerUtil() {
	}

	public static boolean validateCustomer(AccountService accountService, Long customerId){
		return accountService.findById(customerId) != null;
	}

}
