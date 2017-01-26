package com.fasta.app.comparators;

import java.util.Comparator;

import com.fasta.app.entities.Player;

public class PlayerComparator implements Comparator<Player> {

    @Override
    public int compare(Player pl1, Player pl2) {

        int surname = pl1.getSurname().compareTo(pl2.getSurname());
        if (surname != 0) {
            return surname;
        }

        int name = pl1.getName().compareTo(pl2.getName());
        if (name != 0) {
            return name;
        }

        int team = pl1.getTeam().compareTo(pl2.getTeam());
        if (team != 0) {
            return team;
        }

        return pl1.getCode().compareTo(pl2.getCode());
    }

}
