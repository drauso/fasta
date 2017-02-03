package com.fasta.app.push;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = { "value" })
public class Bid implements PushBid {

	private static final long serialVersionUID = -7615006319432525288L;
	private BigDecimal value;
	private String leagueName;
	private String teamName;

	public Bid(BigDecimal value, String leagueName, String teamName) {
		this.value = value;
		this.leagueName = leagueName;
		this.teamName = teamName;
	}

	@Override
	public String getType() {
		return "bid";
	}
}
