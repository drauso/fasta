package com.fasta.app.push;

import lombok.Getter;

@Getter
public class Bid implements PushMessage {

	private static final long serialVersionUID = -7615006319432525288L;
	private int value;
	private String text;

	public Bid(String text, int value) {
		this.text = text;
		this.value = value;
	}

	public String getType() {
		return "bid";
	}

}
