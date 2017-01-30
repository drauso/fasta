package com.fasta.app.push;

import lombok.Getter;

@Getter
public class Information implements PushMessage {

	private static final long serialVersionUID = 5579261076139734487L;

	private String text;

	public Information(String text) {
		this.text = text;
	}

	@Override
	public String getType() {
		return "information";
	}
}
