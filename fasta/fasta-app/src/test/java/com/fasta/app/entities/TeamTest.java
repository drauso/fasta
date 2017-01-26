package com.fasta.app.entities;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.fasta.app.enums.Role;

public class TeamTest {

    private static final BigDecimal BUDGET = new BigDecimal(100);
    private Team teamTest;
    private Player playerTest = new Player("name", "surname", Role.DEFENDER, "team");;

    @Before
    public void initPlayer() {
        teamTest = new Team("name", BUDGET, "password");
    }

    @Test
    public void shouldBuyPlayer() {
        BigDecimal price = BigDecimal.ONE;
        teamTest.buyPlayer(playerTest, price);
        Assertions.assertThat(teamTest.getBudget()).isEqualTo(BUDGET.subtract(price));
        Assertions.assertThat(teamTest.playersNumber()).isEqualTo(1);
        Assertions.assertThat(teamTest.getPlayers()).isNotNull().isNotEmpty().containsOnly(playerTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotBuyPlayer() {
        BigDecimal price = BUDGET.add(BigDecimal.ONE);
        teamTest.buyPlayer(playerTest, price);
    }

    @Test
    public void shouldReleasePlayer() {
        shouldBuyPlayer();

        BigDecimal budgetBefore = teamTest.getBudget();
        teamTest.releasePlayer(playerTest);
        Assertions.assertThat(teamTest.getBudget()).isEqualTo(budgetBefore);
        Assertions.assertThat(teamTest.playersNumber()).isEqualTo(0);
        Assertions.assertThat(teamTest.getPlayers()).isNotNull().isEmpty();
        Assertions.assertThat(teamTest.getReleasedPlayers()).isNotNull().isNotEmpty().containsOnly(playerTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotReleasePlayer() {
        teamTest.releasePlayer(playerTest);
    }

    @Test
    public void shouldGetPlayer() {
        shouldBuyPlayer();
        Assertions.assertThat(teamTest.playersNumber()).isEqualTo(1);
        Assertions.assertThat(teamTest.palyersByRole(Role.DEFENDER)).isNotNull().isNotEmpty().containsOnly(playerTest);
        Assertions.assertThat(teamTest.palyersByRole(Role.GOALKEEPER)).isNotNull().isEmpty();
    }

    @Test
    public void shouldNotGetPlayer() {
        Assertions.assertThat(teamTest.playersNumber()).isEqualTo(0);
        Assertions.assertThat(teamTest.palyersByRole(Role.DEFENDER)).isNotNull().isEmpty();
    }
}
