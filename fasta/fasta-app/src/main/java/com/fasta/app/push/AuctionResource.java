package com.fasta.app.push;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.primefaces.push.impl.JSONEncoder;

import com.fasta.app.beans.AuctionsBean;
import com.fasta.app.entities.League;

@PushEndpoint("/{room}")
@Singleton
public class AuctionResource {

	@Inject
	private ServletContext ctx;

	private AuctionsBean auctionsBean;

	@PostConstruct
	private void init() {
		auctionsBean = (AuctionsBean) ctx.getAttribute("auctionsBean");
	}

	@OnMessage(encoders = { JSONEncoder.class })
	public PushMessage onMessage(PushMessage push) {
		League league = auctionsBean.getLeague(push.getLeagueName());
		switch (push.getType()) {
		case "bid":
			Bid bid = (Bid) push;
			league.callBid(bid);
			break;
		case "auction":
			Auction auction = (Auction) push;
			league.callAuction(auction.getRole());
			break;
		case "sell":
			league.sellPlayer();
			break;
		default:
			break;
		}
		return push;
	}

}
