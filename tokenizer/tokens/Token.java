package json.tokenizer.tokens;

import json.tokenizer.tokens.TokenTypes;

public class Token {

    private TokenTypes type;
    private int line;
    private int column;

    public Token(TokenTypes type, int line, int column) {
        this.type = type;
        this.line = line;
        this.column = column;
    }

    public TokenTypes getType() {
        return this.type;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}