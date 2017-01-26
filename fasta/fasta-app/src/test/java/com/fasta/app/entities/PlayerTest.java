package com.fasta.app.entities;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.fasta.app.enums.Role;

public class PlayerTest {

    private Player playerTest;

    @Before
    public void initPlayer() {
        playerTest = new Player("name", "surname", Role.DEFENDER, "team");
    }

    @Test
    public void shouldSell() {
        BigDecimal price = BigDecimal.ONE;
        playerTest.sell(price);
        Assertions.assertThat(playerTest.getPrice()).isEqualTo(price);
        Assertions.assertThat(playerTest.isSold()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSell() {
        shouldSell();
        playerTest.sell(BigDecimal.ONE);
    }
}
