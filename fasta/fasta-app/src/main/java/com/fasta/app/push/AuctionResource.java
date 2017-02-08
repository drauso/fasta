package com.fasta.app.push;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.primefaces.push.impl.JSONEncoder;

import com.fasta.app.beans.AuctionsBean;

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
		return push;
	}

}
