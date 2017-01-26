package com.fasta.app.entities;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.fasta.app.comparators.PlayerComparator;
import com.fasta.app.enums.Role;

@Getter
@EqualsAndHashCode(of = "name")
@ToString
public class Team {

    private final String name;
    private final String password;
    private BigDecimal budget;

    private Set<Player> players;
    private Set<Player> releasedPlayers;

    public Team(final String name, BigDecimal budget, final String password) {
        this.name = name;
        this.budget = budget;
        this.password = password;
        PlayerComparator playerComparator = new PlayerComparator();
        players = new TreeSet<Player>(playerComparator);
        releasedPlayers = new TreeSet<Player>(playerComparator);
    }

    public void buyPlayer(Player player, BigDecimal offert) {
        if (budget.compareTo(offert) < 0) {
            throw new IllegalArgumentException("Saldo non disponibile");
        }
        players.add(player);
        budget = budget.subtract(offert);
    }

    public void releasePlayer(Player player) {
        if (!players.contains(player)) {
            throw new IllegalArgumentException("Giocatore non disponibile");
        }

        players.remove(player);
        releasedPlayers.add(player);
    }

    public int playersNumber() {
        return players.size();
    }

    public Set<Player> palyersByRole(Role role) {
        return players.stream().parallel().filter(p -> p.getRole().equals(role)).sorted(
                (p1, p2) -> p1.getSurname().compareTo(p2.getSurname())).collect(Collectors.toSet());
    }

}
