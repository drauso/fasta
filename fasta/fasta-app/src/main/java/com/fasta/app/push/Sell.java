package com.fasta.app.push;

import lombok.Getter;

@Getter
public class Sell implements PushMessage {

	private static final long serialVersionUID = -7615006319432525288L;
	private String leagueName;

	public Sell(String leagueName) {
		this.leagueName = leagueName;
	}

	public String getType() {
		return "sell";
	}
}
