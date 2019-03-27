package com.dw.ngms.cis.exception;

public interface ExceptionConstants {

	String SUCCESS = "Success";
	String ERROR = "Error";
	int RESPONSE_ERROR_CODE = 500;
    int UNAUTHORIZED_ERROR_CODE = 401;
    String BAD_CREDENTIALS = "Invalid credentials";
	int RESPONSE_SUCCESS_CODE = 200;
	String NO_ERROR = "No Error";
	String NO_EXCEPTION = "No Exception";
	int CONTROLLER_REQ_RECEIVED = 1;
	int CONTROLLER_RES_SEND = 2;
	int SERVICE_REQ_RECEIVED = 3;
	int SERVICE_RES_SEND = 4;
	String TRUE = "true";
	String FALSE = "false";
	String NO_PRODUCT_FOUND = "No Product found";
	String CART = "CART";
	String ADDORDERSTOCART = "addOrderToCart";
	String GETOPENCARTORDERS = "getOpenCartOrders";
	String REMOVEORDERSFROMCART = "removeOrderFromCart";
	String CONFIRMORDER = "confirmOrder";
	String SENDPAYMENTSTATUS = "sendPaymentStatus";
}
