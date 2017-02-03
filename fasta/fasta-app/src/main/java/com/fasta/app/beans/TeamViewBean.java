package com.fasta.app.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import com.fasta.app.entities.League;
import com.fasta.app.entities.Player;
import com.fasta.app.entities.Team;
import com.fasta.app.enums.Role;
import com.fasta.app.push.Auction;
import com.fasta.app.push.Bid;
import com.fasta.app.push.Sell;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
@SessionScoped
@Getter
public class TeamViewBean implements Serializable {

	private static final EventBus EVENT_BUS = EventBusFactory.getDefault().eventBus();

	private static final String CHANNEL = "/asta";

	private League league;
	private Team team;

	@Setter
	private BigDecimal bid;
	@Setter
	private boolean loggedIn;
	@Setter
	private String teamName;

	public void login(League league) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		this.league = league;
		try {
			team = league.addTeam(teamName, "");
			loggedIn = true;
		} catch (IllegalArgumentException e) {
			loggedIn = false;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Squadra presente", "Prova con un altro nome."));
			requestContext.update("growl");
		}
	}

	public void disconnect() {
		// remove user and update ui
		league.removeTeam(teamName);
		RequestContext.getCurrentInstance().update("form:users");
		// push leave information
		// reset state
		loggedIn = false;
		teamName = null;
	}

	public void sendBid() {
		EVENT_BUS.publish(CHANNEL, new Bid(bid, league.getName(), teamName));
	}

	public void startAuction() {
		EVENT_BUS.publish(CHANNEL, new Auction(Role.DEFENDER, league.getName()));
	}

	public void endAuction() {
		EVENT_BUS.publish(CHANNEL, new Sell(league.getName()));
	}

	public Player getPlayer() {
		if (league == null) {
			return null;
		}
		return league.getPlayerToBuy();
	}

	public List<Bid> getBids() {
		if (league == null || league.getPlayerToBuy() == null) {
			return Collections.emptyList();
		}
		return league.getHistory().get(league.getPlayerToBuy());
	}
}