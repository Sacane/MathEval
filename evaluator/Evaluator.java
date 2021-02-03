package com.sacane.calc.evaluator;

import java.util.EmptyStackException;
import java.util.Objects;

public final class Evaluator {
    private Evaluator() {}

    public static double eval(String input) throws IllegalStateException, EmptyStackException {
        Objects.requireNonNull(input);
        var ast = ParseString.convertInfixToAST(input);
        return ast.eval();
    }

}
