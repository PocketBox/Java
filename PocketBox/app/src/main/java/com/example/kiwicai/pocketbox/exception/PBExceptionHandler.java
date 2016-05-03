package com.example.kiwicai.pocketbox.exception;

/**
 * Created by hongbao on 2016/3/22.
 */
public class PBExceptionHandler {

	public void fixLogin(int errno) {
		System.out.println("Error" + errno + ": Cannot start login activity.");
	}
	public void fixRegister(int errno) {
		System.out.println("Error" + errno + ": Cannot start register activity.");
	}
	public void fixReturn(int errno) {
		System.out.println("Error" + errno + ": Cannot return to main activity.");
	}
	public void fixChat(int errno) {
		System.out.println("Error" + errno + ": Cannot start chat activity.");
	}
	public void fixConnect(int errno) {
		System.out.println("Error" + errno + ": Cannot connect to server.");
	}
}
