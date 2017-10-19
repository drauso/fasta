package com.fasta.app.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.fasta.app.entities.League;
import com.fasta.app.entities.Team;

@ManagedBean(name = "auctionsBean", eager = true)
@ApplicationScoped
public class AuctionsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, League> leagues;

	@PostConstruct
	public void init() {
		leagues = new HashMap<>();
		leagues.put("asta", new League("asta", new BigDecimal("300"), ""));
	}

	public League getLeague(String name) {
		return leagues.get(name);
	}

	public Set<String> getLeaguesName() {
		return leagues.keySet();
	}

	public Set<Team> getTeams(String leagueName) {
		return getLeague(leagueName).getTeams();
	}

}