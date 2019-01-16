package com.kretov.accountmanagement.util;

import com.kretov.accountmanagement.service.CustomerService;

public class CustomerUtil {
	private CustomerUtil() {
	}

	public static boolean validateCustomer(CustomerService customerService, Long customerId){
		return customerService.findById(customerId) != null;
	}

}
