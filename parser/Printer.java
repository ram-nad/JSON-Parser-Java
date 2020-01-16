package json.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import json.value.*;

public class Printer {
    private static int tabCount;

    public static void initialize() {
        Printer.tabCount = 0;
    }

    private static void printNewLine() {
        System.out.print("\n");
        for (int i = 0; i < Printer.tabCount; i++) {
            System.out.print("  ");
        }
    }

    private static void printString(String str) {
        System.out.print('"');
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            switch (ch) {
            case '\b':
                System.out.print("\\\b");
                break;
            case '\f':
                System.out.print("\\\f");
                break;
            case '\r':
                System.out.print("\\\r");
                break;
            case '\n':
                System.out.print("\\\n");
                break;
            case '\t':
                System.out.print("\\\t");
                break;
            case '\\':
                System.out.print("\\\\");
                break;
            case '"':
                System.out.print("\\\"");
                break;
            case '/':
                System.out.print("\\/");
                break;
            default:
                System.out.print(ch);
            }
        }
        System.out.print('"');
    }

    private static void printValue(Value val) {
        if (val.getType() == Types.BOOLEAN) {
            System.out.print(((Value.JSONBoolean) val).getValue());
        } else if (val.getType() == Types.NUMBER) {
            System.out.print(((Value.JSONNumber) val).getValue());
        } else if (val.getType() == Types.STRING) {
            Printer.printString(((Value.JSONString) val).getValue());
        } else if (val.getType() == Types.NULL) {
            System.out.print("null");
        } else if (val.getType() == Types.ARRAY) {
            Printer.printArray((JSONArray) val);
        } else {
            Printer.printObject((JSONObject) val);
        }
    }

    public static void printArray(JSONArray array) {
        ArrayList<Value> list = array.getList();
        System.out.print("[");
        if (list.isEmpty()) {
            System.out.print("]");
            return;
        }
        Printer.tabCount++;
        boolean printed = false;
        for (Value val : list) {
            if (printed) {
                System.out.print(",");
            }
            Printer.printNewLine();
            Printer.printValue(val);
            printed = true;
        }
        Printer.tabCount--;
        Printer.printNewLine();
        System.out.print("]");
    }

    public static void printObject(JSONObject object) {
        HashMap<String, Value> map = object.getMap();
        System.out.print("{");
        if (map.isEmpty()) {
            System.out.print("}");
            return;
        }
        Printer.tabCount++;
        boolean printed = false;
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (printed) {
                System.out.print(",");
            }
            Printer.printNewLine();
            Printer.printString(key);
            System.out.print(": ");
            Printer.printValue(map.get(key));
            printed = true;
        }
        Printer.tabCount--;
        Printer.printNewLine();
        System.out.print("}");
    }
}