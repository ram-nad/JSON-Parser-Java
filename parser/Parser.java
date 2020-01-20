package json.parser;

import java.util.ArrayList;
import json.tokenizer.tokens.*;

import json.value.Value;
import json.value.JSONArray;
import json.value.JSONObject;
import json.value.Types;
import json.parser.ParserError;

public class Parser {
    private ArrayList<Token> tokens;

    // Next token to be read
    private int current;

    // Last read token
    private Token lastToken;

    // final parsed Value
    private Value parsedValue;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
        this.lastToken = null;
        this.parsedValue = parseInitial();
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

    private JSONObject parseObject() throws ParserError {
        consume(); // Consume the first token '{'
        JSONObject object = new JSONObject();
        if (match(TokenTypes.CURLY_BRACKET_CLOSE)) { // If empty object
            return object;
        }
        while (true) {
            if (!match(TokenTypes.STRING)) {
                throw new ParserError(expectedError("string", peek()));
            }
            StringToken key = (StringToken) this.lastToken;
            if (!match(TokenTypes.COLON)) {
                throw new ParserError(expectedError(":", peek()));
            }
            object.insertValue(key.getValue(), parseValue());
            if (!match(TokenTypes.CURLY_BRACKET_CLOSE)) {
                if (!match(TokenTypes.COMMA)) {
                    throw new ParserError(expectedError(",", peek()));
                }
            } else {
                break;
            }
        }
        return object;
    }

    private JSONArray parseArray() throws ParserError {
        consume(); // Consume the first token '['
        JSONArray array = new JSONArray();
        if (match(TokenTypes.SQAURE_BRACKET_CLOSE)) { // If empty array
            return array;
        }
        while (true) {
            array.addValue(parseValue());
            if (!match(TokenTypes.SQAURE_BRACKET_CLOSE)) {
                if (!match(TokenTypes.COMMA)) {
                    throw new ParserError(expectedError(",", peek()));
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

    private String expectedError(String type, Token token) {
        if (token == null) {
            return "Unexpected end of tokens. Expected \''" + type + "\'.";
        } else {
            return "Expected \'" + type + "\', but got " + token + " at Line: " + token.getLine() + " Column: "
                    + token.getColumn() + ".";
        }
    }

    private Value parseValue() throws ParserError {
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

    private Value parseInitial() throws ParserError {
        Value value = parseValue();
        if (!match(TokenTypes.END_OF_FILE)) {
            throw new ParserError(expectedError("End of File", peek()));
        } else {
            return value;
        }
    }

    public JSONObject getObject() throws ParserError {
        if (this.parsedValue.getType() == Types.OBJECT) {
            return (JSONObject) this.parsedValue;
        } else {
            throw new ParserError("Cannot parse a JSON Object. Found " + this.parsedValue.getType().name() + ".");
        }
    }

    public JSONArray getArray() throws ParserError {
        if (this.parsedValue.getType() == Types.ARRAY) {
            return (JSONArray) this.parsedValue;
        } else {
            throw new ParserError("Cannot parse a JSON Array. Found " + this.parsedValue.getType().name() + ".");
        }
    }
}