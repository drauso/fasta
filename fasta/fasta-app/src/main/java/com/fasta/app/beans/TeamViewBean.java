package com.fasta.app.beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

import com.fasta.app.entities.League;
import com.fasta.app.entities.Team;
import com.fasta.app.push.Bid;
import com.fasta.app.push.Information;

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
	private int bid;
	@Setter
	private boolean loggedIn;
	@Setter
	private String teamName;

	public void login(League league) {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		this.league = league;
		try {
			team = new Team(teamName, league.getGlobalBudget(), "");
			league.addTeam(team);
			EVENT_BUS.publish(CHANNEL, new Information(teamName + " e' entrata."));
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
		league.removeTeam(team);
		RequestContext.getCurrentInstance().update("form:users");
		// push leave information
		EVENT_BUS.publish(CHANNEL, new Information(team.getName() + " e' uscita."));
		// reset state
		loggedIn = false;
		teamName = null;
	}

	public void sendBid() {
		EVENT_BUS.publish(CHANNEL, new Bid(teamName + " offre:", bid));
	}
}