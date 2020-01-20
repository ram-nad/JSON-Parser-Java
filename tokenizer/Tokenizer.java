package json.tokenizer;

import json.tokenizer.TokenError;
import static json.tokenizer.Helper.*;

import java.util.ArrayList;

import json.tokenizer.tokens.*;

public class Tokenizer {

    public Tokenizer(String source) throws TokenError {
        this.SourceString = source;
        this.line = 1;
        this.current = 0;
        this.column = 1;
        this.tokens = null;
        this.tokenize();
    }

    private String SourceString;

    private ArrayList<Token> tokens;

    // Points to next character to be read
    private int current;

    /*
     * Stores where in input we are
     */
    private int line;
    private int column;

    // Increases the line number
    private void nextLine() {
        this.line++;
        this.column = 1;
    }

    // Checks if we have reached end of input
    private boolean isEnd() {
        if (current == SourceString.length()) {
            return true;
        } else {
            return false;
        }
    }

    // Consumes next readable character
    // will never be called if isEnd
    private char consume() {
        if (SourceString.charAt(current) == NEWLINE) {
            nextLine();
        } else {
            this.column++;
        }
        this.current++;
        return SourceString.charAt(current - 1);
    }

    // Returns the next character
    // without moving to next character
    private char peek() {
        // If already ended return 'NULL'
        if (isEnd()) {
            return NULL_CHAR;
        }
        return SourceString.charAt(current);
    }

    // Return true if next character matches ch
    // Also moves to next character, else returns false
    // without moving to next character
    private boolean match(char ch) {
        if (isEnd()) {
            return false;
        } else if (SourceString.charAt(current) != ch) {
            return false;
        } else {
            consume();
            return true;
        }
    }

    // Returns escaped version of character
    private char escapedChar(char ch) {
        switch (ch) {
        case 'b':
            return '\b';
        case 'f':
            return '\f';
        case 'r':
            return '\r';
        case 'n':
            return '\n';
        case 't':
            return '\t';
        case '\\':
            return '\\';
        case '"':
            return '"';
        case '/':
            return '/';
        default:
            return NULL_CHAR;
        }
    }

    // Parses a string
    private StringToken parseString() throws TokenError {
        int line = this.line;
        int column = this.column;
        consume(); // To consume opening Double Quote
        StringBuilder str = new StringBuilder(50); // Create a String Builder for 50 characters
        while (true) {
            if (isEnd()) {
                throw new TokenError("Unexpected end of input while parsing String at " + line + ", " + column + ".",
                        this.line, this.column);
            }
            if (peek() == NEWLINE) { // cannot spread strings across two lines
                throw new TokenError("Unexpected end of line while parsing String at " + line + ", " + column + ".",
                        this.line, this.column);
            } else if (match(SLASH)) { // If we encounter a escape sequence
                char ch = escapedChar(peek());
                if (ch == NULL_CHAR) {
                    throw new TokenError(
                            "Invalid escape sequence while parsing String at " + line + ", " + column + ".", this.line,
                            this.column);
                } else {
                    str.append(ch);
                    consume();
                }
            } else if (match(DOUBLE_QUOTE)) {
                return new StringToken(str.toString(), line, column);
            } else {
                char ch = peek();
                if (ch >= 0x20 && ch <= 0x10FFFF) {
                    str.append(consume());
                } else {
                    throw new TokenError(
                            "Invalid character found while parsing String at " + line + ", " + column + ".", this.line,
                            this.column);
                }
            }
        }
    }

    // Parses a number
    private NumberToken parseNumber() throws TokenError {
        int line = this.line;
        int column = this.column;
        StringBuilder num = new StringBuilder(30);
        boolean dot = false;
        if (peek() == '-') {
            num.append('-');
            consume();
        }
        // If at end, stop parsing
        while (!isEnd()) {
            char ch = peek();
            // Dot is allowed only once
            if (ch == DOT && !dot) {
                dot = true;
                num.append(DOT);
                consume();
            } else if (isDigit(ch)) {
                num.append(ch);
                consume();
            } else {
                break;
            }
        }
        try {
            double input = Double.parseDouble(num.toString());
            return new NumberToken(input, line, column);
        } catch (NumberFormatException exception) {
            throw new TokenError("Error in parsing number.", line, column);
        }
    }

    // Parses words
    private Token parseWord() throws TokenError {
        int line = this.line;
        int column = this.column;
        StringBuilder word = new StringBuilder(20);
        while (!isEnd()) {
            // while not end or a letter is found keep adding
            if (isLetter(peek())) {
                word.append(consume());
            } else {
                break;
            }
        }
        String keyword = word.toString();
        if (keyword.equals("null")) {
            return new Token(TokenTypes.NULL, line, column);
        } else if (keyword.equals("true")) {
            return new Token(TokenTypes.TRUE, line, column);
        } else if (keyword.equals("false")) {
            return new Token(TokenTypes.FALSE, line, column);
        } else {
            throw new TokenError(keyword + " is not a valid keyword.", line, column);
        }
    }

    private void tokenize() throws TokenError {
        this.tokens = new ArrayList<Token>();
        Token token;
        while (!isEnd()) {
            token = null;
            if (match(CURLY_BRACKETS_CLOSE)) {
                token = new Token(TokenTypes.CURLY_BRACKET_CLOSE, this.line, this.column - 1);
            } else if (match(CURLY_BRACKETS_OPEN)) {
                token = new Token(TokenTypes.CURLY_BRACKET_OPEN, this.line, this.column - 1);
            } else if (match(SQUARE_BRACKET_CLOSE)) {
                token = new Token(TokenTypes.SQAURE_BRACKET_CLOSE, this.line, this.column - 1);
            } else if (match(SQUARE_BRACKET_OPEN)) {
                token = new Token(TokenTypes.SQAURE_BRACKET_OPEN, this.line, this.column - 1);
            } else if (match(COLON)) {
                token = new Token(TokenTypes.COLON, this.line, this.column - 1);
            } else if (match(COMMA)) {
                token = new Token(TokenTypes.COMMA, this.line, this.column - 1);
            } else {
                char ch = peek();
                if (ch == DOUBLE_QUOTE) {
                    token = parseString();
                } else if (isDigit(ch) || ch == '-') {
                    token = parseNumber();
                } else if (isLetter(ch)) {
                    token = parseWord();
                } else if (isWhiteSpace(ch)) {
                    consume();
                } else {
                    throw new TokenError("Invalid character found.", this.line, this.column);
                }
            }
            if (token != null) {
                this.tokens.add(token);
            }
        }
        tokens.add(new Token(TokenTypes.END_OF_FILE, this.line, this.column));
    }

    public static void printTokens(ArrayList<Token> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            System.out.print(tokens.get(i));
            System.out.print(" ");
        }
        System.out.println();
    }

    public ArrayList<Token> getTokens() {
        return this.tokens;
    }
}