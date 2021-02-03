package com.sacane.calc.evaluator;
import java.util.Arrays;
import java.util.function.DoubleBinaryOperator;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * TODO : Continue to inserts/correct comments
 */
public enum BaseOperator {
    SUB("-", 2, true, (a, b) -> a - b),
    ADD("+", 2, true, Double::sum),
    DIV("/", 3, true, (a, b) -> a / b),
    MUL("*", 3, true, (a, b) -> a * b),
    EXP("^", 4, false, Math::pow),
    MOD("%", 3, true, (a, b) -> a % b);

    final String symbol;
    final int precedence;
    final boolean isLeftAssociative;
    final DoubleBinaryOperator op;

    BaseOperator(String symbol, int precedence, boolean isLeftAssociative, DoubleBinaryOperator op) {
        Objects.requireNonNull(symbol);
        Objects.requireNonNull(op);
        this.symbol = symbol;
        this.precedence = precedence;
        this.isLeftAssociative = isLeftAssociative;
        this.op = op;
    }

    /**
     * Get the BaseOperator corresponding of the symbol.
     * @param symbol of the BaseOperator to get.
     * @return the BaseOperator of the symbol taken as parameter.
     */
    static BaseOperator get(String symbol) {
        return Arrays.stream(BaseOperator.values()).filter(it -> it.symbol.equals(symbol)).findFirst().orElse(null);
    }

    public String getSymbol(){
        return symbol;
    }

    static String symbols() {
        return Arrays.stream(BaseOperator.values()).map(s -> s.symbol).collect(Collectors.joining(""));
    }

    /**
     *
     * @param other to compare.
     * @return 0 if the precedences are equals, -1 if this precedences is
     */
    int comparePrecedence(BaseOperator other){
        return this.precedence - other.precedence;
    }

}
