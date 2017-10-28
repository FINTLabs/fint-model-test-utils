package no.fint.model.test.utils.testmodel;

import lombok.Data;
import no.fint.model.test.utils.testmodel.nested.TestModelNested;

@Data
public class TestModel {
    public enum Relasjonsnavn {
        TESTREL,
        TESTREL123
    }

    private String test;
    private int value;
    private TestModelNested nestedObj;
}
