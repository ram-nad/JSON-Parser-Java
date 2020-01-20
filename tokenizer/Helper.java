package json.tokenizer;

class Helper {
    final static char DOUBLE_QUOTE = '\"';

    final static char COLON = ':';

    final static char COMMA = ',';

    final static char SLASH = '\\';

    final static char NEWLINE = '\n';

    final static char SQUARE_BRACKET_OPEN = '[';

    final static char SQUARE_BRACKET_CLOSE = ']';

    final static char CURLY_BRACKETS_OPEN = '{';

    final static char CURLY_BRACKETS_CLOSE = '}';

    final static char NULL_CHAR = '\0';

    final static char DOT = '.';

    static boolean isWhiteSpace(char character) {
        if (character == ' ') {
            return true;
        } else if (character == '\t') {
            return true;
        } else if (character == '\n') {
            return true;
        } else if (character == '\r') {
            return true;
        }
        return false;
    }

    static boolean isDigit(char character) {
        if (character >= '0' && character <= '9') {
            return true;
        } else {
            return false;
        }
    }

    static boolean isLetter(char character) {
        if (character >= 'a' && character <= 'z') {
            return true;
        } else {
            return false;
        }
    }
}