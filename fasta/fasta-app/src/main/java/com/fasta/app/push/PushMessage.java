package com.fasta.app.push;

import java.io.Serializable;

@FunctionalInterface
public interface PushMessage extends Serializable {
	String getType();
}
