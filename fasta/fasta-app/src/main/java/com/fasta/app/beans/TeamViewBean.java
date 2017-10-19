package com.fasta.app.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.LinkedBlockingDeque;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
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
import lombok.extern.slf4j.Slf4j;

@ManagedBean
@SessionScoped
@Getter
@Slf4j
public class TeamViewBean implements Serializable {

	private static final long serialVersionUID = 456082522929014854L;

	private static final String BANDITORE = "banditore";
	private static final EventBus EVENT_BUS = EventBusFactory.getDefault().eventBus();
	private static final String CHANNEL = "/asta";

	private League league;
	private boolean loggedIn;
	private boolean admin;

	@Setter
	private BigDecimal bid;
	@Setter
	private String message;
	@Setter
	private String teamName = BANDITORE;

	public void login(League league) {
		this.league = league;
		league.addTeam(teamName, "");
		if (teamName.equals(BANDITORE)) {
			admin = true;
		}
		loggedIn = true;
		EVENT_BUS.publish(CHANNEL, new Information(teamName + " e' entrato"));
	}

	public void remove() {
		league.removeTeam(teamName);
		EVENT_BUS.publish(CHANNEL, new Information(teamName + " e' stato rimosso"));
		loggedIn = false;
		teamName = null;
	}

	public void disconnect() {
		EVENT_BUS.publish(CHANNEL, new Information(teamName + " e' uscito"));
		loggedIn = false;
		teamName = null;
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
		} catch (IllegalStateException e) {
			log.debug("Impossibile cominciare l'asta");
			addMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
		}
	}

	public void endAuction() {
		try {
			Bid lastBid = league.sellPlayer();
			league.getTimer().reset();
			RequestContext.getCurrentInstance().execute("PF('myPoll').stop();");
			String sellMessage = (lastBid == null) ? "Giocatore invenduto"
					: "Il giocatore e' stato venduto a " + lastBid.getTeamName() + " per " + lastBid.getValue()
							+ " crediti";
			EVENT_BUS.publish(CHANNEL, new Information("Banditore:" + sellMessage));
		} catch (IllegalArgumentException e) {
			log.debug("Impossibile terminare l'asta");
			addMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
		}
	}

	public void sendBid() {
		try {
			Bid send = new Bid(bid, league.getName(), teamName);
			league.callBid(send);
			league.getTimer().reset();
			EVENT_BUS.publish(CHANNEL, send);
		} catch (IllegalStateException | IllegalArgumentException e) {
			log.debug("Impossibile accettare l'offerta");
			addMessage(FacesMessage.SEVERITY_ERROR, null, e.getMessage());
		}
	}

	public void increment() {
		Timer timer = league.getTimer();
		try {
			timer.increment();
			EVENT_BUS.publish(CHANNEL, (PushMessage) () -> "update");
		} catch (TimeOutException e) {
			log.debug("Tempo scaduto, asta terminata");
			endAuction();
		}
	}

	public Team getTeam() {
		if (league == null) {
			return null;
		}
		return league.getTeam(teamName).orElse(null);
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

	public LinkedBlockingDeque<Bid> getBids() {
		if (league == null || league.getPlayerToBuy() == null) {
			return new LinkedBlockingDeque<>();
		}
		return league.getHistory().get(league.getPlayerToBuy());
	}

	private void addMessage(Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

}