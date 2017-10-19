package com.fasta.app.entities;

import com.fasta.app.exceptions.TimeOutException;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class Timer {

	private final int expiredTime;

	public Timer(int expiredTime) {
		this.expiredTime = expiredTime;
	}

	private int number;

	public void increment() {
		number++;
		log.info("Contatore incrementato a: {}", number);
		if (number == expiredTime) {
			throw new TimeOutException();
		}
	}

	public void reset() {
		number = 0;
	}

}
