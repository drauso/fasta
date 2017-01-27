package com.fasta.app.push;

import lombok.Getter;

@Getter
public class Information implements PushMessage {

	private static final long serialVersionUID = 5579261076139734487L;

	private final String text;

	public Information(final String text) {
		this.text = text;
	}

	@Override
	public String getType() {
		return "information";
	}
}
