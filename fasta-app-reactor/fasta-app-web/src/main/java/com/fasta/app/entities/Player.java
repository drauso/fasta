package com.fasta.app.entities;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.fasta.app.enums.Role;

@Getter
@EqualsAndHashCode(of = "code")
@ToString
public class Player {

    private static Long generetedCode = 0L;

    private final Long code;

    private final String name;

    private final String surname;

    private final Role role;

    private final String team;

    private BigDecimal price;

    public Player(String name, String surname, Role role, String team) {
        this.code = generetedCode++;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.team = team;
    }

    public void sell(BigDecimal offer) {
        if (isSold()) {
            throw new IllegalArgumentException("Il giocatore é gia stato venduto");
        }
        this.price = offer;
    }

    public boolean isSold() {
        return price != null;
    }

}
