package com.fasta.app.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasta.app.exceptions.TimeOutException;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Timer {

	private static final Logger logger = LogManager.getLogger(Timer.class);

	private final int expiredTime;

	public Timer(int expiredTime) {
		this.expiredTime = expiredTime;
	}

	private int number;

	public void increment() {
		number++;
		logger.info("Contatore incrementato a: {}", number);
		if (number == expiredTime) {
			throw new TimeOutException();
		}
	}

	public void reset() {
		number = 0;
	}

}
