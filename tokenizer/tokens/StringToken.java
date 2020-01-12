package json.tokenizer.tokens;

import json.tokenizer.tokens.TokenTypes;
import json.tokenizer.tokens.Token;

public class StringToken extends Token {
    private String value;

    public StringToken(String value, int line, int column) {
        super(TokenTypes.STRING, line, column);
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "STRING[" + value + "]";
    }
}