package com.fasta.app.push;

import java.io.Serializable;

public interface PushMessage extends Serializable {

	default String getType() {
		return "bid";
	}
}
