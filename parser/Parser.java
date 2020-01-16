package json.parser;

import java.util.ArrayList;
import json.tokenizer.tokens.*;

import json.value.Value;
import json.value.JSONArray;
import json.value.JSONObject;

import json.parser.ParserError;

public class Parser {
    private ArrayList<Token> tokens;

    // Next token to be read
    private int current;

    // Last read token
    private Token lastToken;

    private boolean parsed;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
        this.lastToken = null;
        this.parsed = false;
    }

    private Token consume() {
        this.lastToken = this.tokens.get(this.current);
        this.current++;
        return this.lastToken;
    }

    private Token peek() {
        if (this.current == tokens.size()) {
            return null;
        }
        return tokens.get(this.current);
    }

    private boolean match(TokenTypes type) {
        if (this.current < this.tokens.size() && this.tokens.get(this.current).getType() == type) {
            consume();
            return true;
        } else {
            return false;
        }
    }

    private JSONObject parseObject() {
        consume(); // Consume the first token '{'
        JSONObject object = new JSONObject();
        while (true) {
            if (!match(TokenTypes.STRING)) {
                throw new ParserError(expectedError("String", this.lastToken.getLine(), this.lastToken.getColumn()));
            }
            StringToken key = (StringToken) this.lastToken;
            if (!match(TokenTypes.COLON)) {
                throw new ParserError(expectedError(":", this.lastToken.getLine(), this.lastToken.getColumn()));
            }
            object.insertValue(key.getValue(), parseValue());
            if (!match(TokenTypes.CURLY_BRACKET_CLOSE)) {
                if (!match(TokenTypes.COMMA)) {
                    throw new ParserError(expectedError(",", this.lastToken.getLine(), this.lastToken.getColumn()));
                }
            } else {
                break;
            }
        }
        return object;
    }

    private JSONArray parseArray() {
        consume(); // Consume the first token '['
        JSONArray array = new JSONArray();
        while (true) {
            array.addValue(parseValue());
            if (!match(TokenTypes.SQAURE_BRACKET_CLOSE)) {
                if (!match(TokenTypes.COMMA)) {
                    throw new ParserError(expectedError(",", this.lastToken.getLine(), this.lastToken.getColumn()));
                }
            } else {
                break;
            }
        }
        return array;
    }

    private String unexpectedError(Token token) {
        return "Unexpected Token " + token + " at Line: " + token.getLine() + " Column: " + token.getColumn() + ".";
    }

    private String expectedError(String type, int line, int column) {
        return "Expected " + type + " after Line: " + line + " Column: " + column + ".";
    }

    private Value parseValue() {
        Token token = peek();
        if (token == null) {
            throw new ParserError("Unexpected end of tokens.");
        } else if (token.getType() == TokenTypes.SQAURE_BRACKET_OPEN) {
            return parseArray();
        } else if (token.getType() == TokenTypes.CURLY_BRACKET_OPEN) {
            return parseObject();
        } else if (match(TokenTypes.TRUE) || match(TokenTypes.FALSE)) {
            return new Value.JSONBoolean(this.lastToken.getType() == TokenTypes.TRUE);
        } else if (match(TokenTypes.NULL)) {
            return new Value.JSONNull();
        } else if (match(TokenTypes.NUMBER)) {
            return new Value.JSONNumber(((NumberToken) this.lastToken).getValue());
        } else if (match(TokenTypes.STRING)) {
            return new Value.JSONString(((StringToken) this.lastToken).getValue());
        } else {
            throw new ParserError(unexpectedError(token));
        }
    }

    public JSONObject getObject() {
        if (this.parsed) {
            throw new ParserError("Already Parsed. Cannot parse again.");
        }
        this.parsed = true;
        if (peek().getType() != TokenTypes.CURLY_BRACKET_OPEN) {
            throw new ParserError("Expected '{' at the beginning.");
        }
        JSONObject object = parseObject();
        if (!match(TokenTypes.END_OF_FILE)) {
            throw new ParserError(unexpectedError(peek()));
        } else {
            return object;
        }
    }

    public JSONArray getArray() {
        if (this.parsed) {
            throw new ParserError("Already Parsed. Cannot parse again.");
        }
        this.parsed = true;
        if (peek().getType() != TokenTypes.SQAURE_BRACKET_OPEN) {
            throw new ParserError("Expected '[' at the beginning.");
        }
        JSONArray array = parseArray();
        if (!match(TokenTypes.END_OF_FILE)) {
            throw new ParserError(unexpectedError(peek()));
        } else {
            return array;
        }
    }
}