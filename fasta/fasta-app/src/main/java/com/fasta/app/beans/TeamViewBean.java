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
import com.fasta.app.entities.Timer;
import com.fasta.app.enums.Role;
import com.fasta.app.exceptions.TimeOutException;
import com.fasta.app.push.Bid;
import com.fasta.app.push.Information;
import com.fasta.app.push.PushMessage;

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
	private boolean loggedIn;
	private boolean admin;

	@Setter
	private BigDecimal bid;
	@Setter
	private String message;
	@Setter
	private String teamName = "banditore";

	public void login(League league) {
		this.league = league;
		try {
			team = league.addTeam(teamName, "");
			if (teamName.equals("banditore")) {
				admin = true;
			}
			loggedIn = true;
			EVENT_BUS.publish(CHANNEL, new Information(teamName + " e' entrato"));
		} catch (IllegalArgumentException e) {
			loggedIn = false;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Squadra presente", "Prova con un altro nome."));
		}
	}

	public void disconnect() {
		// remove user and update ui
		league.removeTeam(teamName); 
		loggedIn = false;
		teamName = null;
		EVENT_BUS.publish(CHANNEL, new Information(teamName + " e' uscito"));
	}

	public void sendMessage() {
		EVENT_BUS.publish(CHANNEL, new Information(teamName + ":" + message));
		message = null;
	}

	public void startAuction() {
		try {
			league.callAuction(Role.DEFENDER);
			league.getTimer().reset();
			RequestContext.getCurrentInstance().execute("PF('myPoll').start();");
			EVENT_BUS.publish(CHANNEL, new Bid(BigDecimal.ZERO, league.getName(), null));
		} catch (IllegalArgumentException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", e.getMessage()));
		}
	}

	public void endAuction() {
		try {
			Bid lastBid = league.sellPlayer();
			league.getTimer().reset();
			RequestContext.getCurrentInstance().execute("PF('myPoll').stop();");
			String sellMessage = (lastBid == null) ? "Giocatore invenduto"
					: "Il giocatore +  e' stato venduto a " + lastBid.getTeamName() + " per " + lastBid.getValue()
							+ " crediti";
			EVENT_BUS.publish(CHANNEL, new Information("Banditore:" + sellMessage));
		} catch (IllegalArgumentException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", e.getMessage()));
		}
	}

	public void sendBid() {
		try {
			Bid send = new Bid(bid, league.getName(), teamName);
			league.callBid(send);
			league.getTimer().reset();
			EVENT_BUS.publish(CHANNEL, send);
		} catch (IllegalArgumentException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore", e.getMessage()));
		}
	}

	public void increment() {
		Timer timer = league.getTimer();
		try {
			timer.increment();
			EVENT_BUS.publish(CHANNEL, (PushMessage) () -> "update");
		} catch (TimeOutException e) {
			endAuction();
		}
	}

	public Timer getTimer() {
		return league.getTimer();
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