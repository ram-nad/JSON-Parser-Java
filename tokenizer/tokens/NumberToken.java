package json.tokenizer.tokens;

import json.tokenizer.tokens.TokenTypes;
import json.tokenizer.tokens.Token;

public class NumberToken extends Token {
    private double value;

    public NumberToken(double value, int line, int column) {
        super(TokenTypes.NUMBER, line, column);
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "NUMBER[" + value + "]";
    }
}