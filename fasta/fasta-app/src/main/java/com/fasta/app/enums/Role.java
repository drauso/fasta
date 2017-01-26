package com.fasta.app.enums;

public enum Role {

    GOALKEEPER("P"), DEFENDER("D"), MIDFIELDER("C"), FORWARD("A");

    private final String code;

    private Role(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
