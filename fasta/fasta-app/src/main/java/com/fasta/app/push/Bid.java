package com.fasta.app.push;

import lombok.Getter;

@Getter
public class Bid implements PushMessage {

	private static final long serialVersionUID = -7615006319432525288L;
	private final int value;

	public Bid(final int value) {
		this.value = value;
	}

}
