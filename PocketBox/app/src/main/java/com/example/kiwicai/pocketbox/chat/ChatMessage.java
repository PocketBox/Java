package com.example.kiwicai.pocketbox.chat;

import java.util.Date;
/*
 * Wrapper class for chatroom message
 */
public class ChatMessage {

	private String username;
	private String message;
	private Date date;
	private boolean inMessage;

	public ChatMessage() {}

	public ChatMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isIncomingMessage() {
		return inMessage;
	}

	public void setIncomingMessage(boolean inMessage) {
		this.inMessage = inMessage;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public boolean isSystemMessage(){
		return getUsername()==null;
	}

}
