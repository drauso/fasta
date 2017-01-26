package com.fasta.app.entities;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.fasta.app.enums.Order;
import com.fasta.app.enums.Role;

public class LeagueTest {

    private static final BigDecimal BUDGET = new BigDecimal(100);
    private League leagueTest;
    private Player playerTest = new Player("name", "surname", Role.DEFENDER, "team");
    private Team teamTest = new Team("name", BUDGET, "password");

    @Before
    public void initLeague() {
        leagueTest = new League("name", BUDGET, "password", Order.ALPHA, Order.RANDOM, Order.CHECK, Order.ALPHA);
        populatePlayers();
    }

    @Test
    public void shouldAddTeam() {
        leagueTest.addTeam(teamTest);
        Assertions.assertThat(leagueTest.getTeams()).isNotNull().isNotEmpty().containsOnly(teamTest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAddTeam() {
        shouldAddTeam();
        leagueTest.addTeam(teamTest);
    }

    @Test
    public void shouldAddPlayer() {
        leagueTest.addPlayers(Collections.singleton(playerTest));
        Assertions.assertThat(leagueTest.getPlayer(playerTest)).isNotNull().isEqualTo(Optional.ofNullable(playerTest));
    }

    @Test
    public void shouldNotAddPlayer() {
        shouldAddPlayer();
        leagueTest.addPlayers(Collections.singleton(playerTest));
        Assertions.assertThat(leagueTest.getPlayer(playerTest)).isNotNull().isEqualTo(Optional.ofNullable(playerTest));
    }

    @Test
    public void getNextPlayer() {
        Optional<Player> playerGk = leagueTest.getNextPlayer(Role.GOALKEEPER);
        Assertions.assertThat(playerGk.get()).isNotNull();
        Assertions.assertThat(playerGk.get().getSurname()).isEqualTo("Buffon");
        Assertions.assertThat(playerGk.get().getName()).isEqualTo("Gigi");
        Assertions.assertThat(playerGk.get().getRole()).isEqualTo(Role.GOALKEEPER);
        Assertions.assertThat(playerGk.get().getTeam()).isEqualTo("Juventus");

        Optional<Player> playerDef = leagueTest.getNextPlayer(Role.DEFENDER);
        Assertions.assertThat(playerDef.get()).isNotNull();

        Optional<Player> playerMid = leagueTest.getNextPlayer(Role.MIDFIELDER);
        Assertions.assertThat(playerMid).isNull();

        Optional<Player> playerFor = leagueTest.getNextPlayer(Role.FORWARD);
        Assertions.assertThat(playerFor.get()).isNotNull();
        Assertions.assertThat(playerFor.get().getSurname()).isEqualTo("Immobile");
        Assertions.assertThat(playerFor.get().getName()).isEqualTo("Ciro");
        Assertions.assertThat(playerFor.get().getRole()).isEqualTo(Role.FORWARD);
        Assertions.assertThat(playerFor.get().getTeam()).isEqualTo("Lazio");

    }

    @Test
    public void getRandomPlayer() {
        Optional<Player> player = leagueTest.getRandomPlayer();
        Assertions.assertThat(player).isNotNull();
    }

    private void populatePlayers() {
        leagueTest.addPlayers(Collections.singleton(new Player("Gigi", "Buffon", Role.GOALKEEPER, "Juventus")));
        leagueTest.addPlayers(Collections.singleton(new Player("Paolo", "Rossi", Role.GOALKEEPER, "Milan")));

        leagueTest.addPlayers(Collections.singleton(new Player("Daniele", "Rauso", Role.DEFENDER, "Juventus")));
        leagueTest.addPlayers(Collections.singleton(new Player("Giorgio", "Chiellini", Role.DEFENDER, "Juventus")));
        leagueTest.addPlayers(Collections.singleton(new Player("Paolo", "Maldini", Role.DEFENDER, "Milan")));

        leagueTest.addPlayers(Collections.singleton(new Player("Marek", "Hamsik", Role.MIDFIELDER, "Napoli")));
        leagueTest.addPlayers(Collections.singleton(new Player("Daniele", "De Rossi", Role.MIDFIELDER, "Roma")));

        leagueTest.addPlayers(Collections.singleton(new Player("Ciro", "Immobile", Role.FORWARD, "Lazio")));
        leagueTest.addPlayers(Collections.singleton(new Player("Nicola", "Kalinic", Role.FORWARD, "Fiorentina")));
    }

}
