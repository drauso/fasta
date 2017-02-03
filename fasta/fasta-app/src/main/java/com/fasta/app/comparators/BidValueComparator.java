package com.fasta.app.comparators;

import java.util.Comparator;

import com.fasta.app.push.Bid;

public class BidValueComparator implements Comparator<Bid> {

	@Override
	public int compare(Bid b1, Bid b2) {
		return b1.getValue().compareTo(b2.getValue());
	}

}
