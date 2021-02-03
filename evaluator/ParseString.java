package com.sacane.calc.evaluator;

import static com.sacane.calc.evaluator.Node.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public final class ParseString {
    private ParseString() {}

    /**
     * Turn the input separate by spaces.
     * @param input to tokenize
     * @return the input tokenize
     * Author of the regex : Jimmy Teillard (@notKamui)
     */
    private static List<String> tokenize(String input){
        final var sanitized = input.replaceAll("\\s", "");
        final var symbols = BaseOperator.symbols();
        return List.of(sanitized.split("(?<=[%s()])|(?=[%s()])".formatted(symbols, symbols)));
    }

    /**
     * Convert a String into an Element, test if the token is an operator
     * or a value : if the token is an operator return an new Operator, else
     * return a new Value.
     * @param token to convert into Element
     * @return the element designed by the token.
     */
    private static BaseOperator convertStringToBaseOperator(String token) throws IllegalStateException {
        final var op = BaseOperator.get(token);
        if (op == null) {
            throw new IllegalStateException("Invalid operator : %s".formatted(token));
        }
        return op;
    }

    /**
     *
     * @param token to convert
     * @return the Value designed by the token.
     */
    private static Value convertStringToValue(String token) throws IllegalStateException {
        try {
            return new Value(Double.parseDouble(token));
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid expression: invalid token %s".formatted(token));
        }
    }

    /**
     * take the two values in the top of the stack and return the node with the operator as root and the values
     * as his left and right node.
     * @param stack which contains Node. The stack has to follow the Shunting-yard algorithm.
     * @param operator of the Node to add.
     */
    private static void addNode(Stack<Node> stack, BaseOperator operator) {
        Objects.requireNonNull(operator);
        final Node rightNode = stack.pop();
        final Node leftNode = stack.pop();
        stack.push(new Operator(leftNode, operator, rightNode));
    }

    /**
     * Turn the input into an AST form, using the Shunting-yard algorithm.
     * @param input to convert into AST form. The input has to be in infix notation before calling this function.
     * @return the final Node converted.
     */
    //TODO comment the algorithm
    static Node convertInfixToAST(String input) throws IllegalStateException{
        var tokens = tokenize(input);
        final Stack<String> operatorStack = new Stack<>();
        final Stack<Node> operandStack = new Stack<>();
        mainLoop:
        for(var token: tokens) {
            String popped;
            switch(token){
                case "(":
                    operatorStack.push("(");
                    break;
                case ")":
                    while(!operatorStack.isEmpty()){
                        popped = operatorStack.pop();
                        if("(".equals(popped)){
                            continue mainLoop;
                        } else{
                            addNode(operandStack, convertStringToBaseOperator(popped));
                        }
                    }
                    throw new IllegalStateException("Expression incorrect");
                default:
                    if(containsOp(token)){
                        String op;
                        while(!operatorStack.isEmpty() && null != (op = operatorStack.peek()) && containsOp(op)){
                            var top = BaseOperator.get(op);
                            var operator = BaseOperator.get(token);
                            if(operator.isLeftAssociative && top.comparePrecedence(operator) >= 0) {
                                operatorStack.pop();
                                addNode(operandStack, convertStringToBaseOperator(op));
                            } else {
                                break;
                            }
                        }
                        operatorStack.push(token);
                    }else{
                        operandStack.push(convertStringToValue(token));
                    }
                    break;
            }
        }
        while(!operatorStack.isEmpty()){
            addNode(operandStack, convertStringToBaseOperator(operatorStack.pop()));
        }

        return operandStack.pop();
    }


    /**
     *
     * @param token to check.
     * @return true if the token is an operator, else return false.
     */
    private static boolean containsOp(String token){
        return Arrays.stream(BaseOperator.values()).anyMatch(op -> token.equals(op.symbol));
    }
}
