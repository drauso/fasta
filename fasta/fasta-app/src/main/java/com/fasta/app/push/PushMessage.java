package com.fasta.app.push;

import java.io.Serializable;

public interface PushMessage extends Serializable {

	String getLeagueName();

	String getType();

}
