package json;

import json.tokenizer.Tokenizer;
import json.tokenizer.tokens.Token;

import json.parser.Parser;
import json.parser.Printer;

import json.value.JSONObject;

import java.util.ArrayList;

public class Test {
    public static void main(String args[]) {
        int n = 1;
        String strs[] = new String[n];

        strs[0] = "{\r\n\"key1\":  \"This is a \'string\'\",\r\n\"key2\": {}\r\n}";

        for (int i = 0; i < n; i++) {
            try {
                ArrayList<Token> tokens = new Tokenizer(strs[i]).getTokens();
                Tokenizer.printTokens(tokens);
                JSONObject obj = new Parser(tokens).getObject();
                Printer.print(obj, Printer.OPTION.PRETTY);
            } catch (Error err) {
                System.out.println(err);
            }
        }
    }
}