package com.fasta.app.push;

import com.fasta.app.enums.Role;

import lombok.Getter;

@Getter
public class Auction implements PushMessage {

	private static final long serialVersionUID = -7615006319432525288L;
	private Role role;
	private String leagueName;

	public Auction(Role role, String leagueName) {
		this.role = role;
		this.leagueName = leagueName;
	}

	public String getType() {
		return "auction";
	}
}
