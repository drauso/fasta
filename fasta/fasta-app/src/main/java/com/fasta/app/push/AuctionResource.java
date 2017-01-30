package com.fasta.app.push;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.primefaces.push.impl.JSONEncoder;

@PushEndpoint("/{room}")
@Singleton
public class AuctionResource {

	@OnMessage(encoders = { JSONEncoder.class })
	public PushMessage onMessage(PushMessage push) {
		return push;
	}
}
