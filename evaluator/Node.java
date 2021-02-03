package com.sacane.calc.evaluator;

interface Node {

    /**
     * Evaluate the Tree : if the element is an operator, evaluate the left and right nodes.
     * if the element is a value, return the value.
     * @return the result of the evaluation of the tree.
     */
    double eval();

    record Operator(Node left, BaseOperator operator, Node right) implements Node {
        @Override
        public double eval() {
            return operator.op.applyAsDouble(left.eval(), right.eval());
        }
    }

    record Value(double value) implements Node {
        @Override
        public double eval() {
            return value;
        }
    }
}
