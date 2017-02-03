package com.fasta.app.push;

import lombok.Getter;

@Getter
public class Information implements PushMessage {

	private static final long serialVersionUID = 5579261076139734487L;

	private String text;
	private String leagueName;

	public Information(String text, String leagueName) {
		this.text = text;
		this.leagueName = leagueName;
	}

	@Override
	public String getType() {
		return "information";
	}
}
