package no.fint.model.test.utils;

import lombok.Data;

@Data
public class TestModel {
    public enum Relasjonsnavn {
        TESTREL,
        TESTREL123
    }

    private String test;
    private TestModelNested nestedObj;
}
