package json.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import json.value.*;

public class Printer {

    public static enum OPTION {
        PRETTY, SIMPLE
    }

    private static void printNewLine(StringBuilder buffer, int tabCount, OPTION opt) {
        if (opt == OPTION.SIMPLE) {
            return;
        }
        buffer.append("\n");
        for (int i = 0; i < tabCount; i++) {
            buffer.append("  ");
        }
    }

    private static void printString(StringBuilder buffer, String str) {
        buffer.append('"');
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            switch (ch) {
            case '\b':
                buffer.append("\\\b");
                break;
            case '\f':
                buffer.append("\\\f");
                break;
            case '\r':
                buffer.append("\\\r");
                break;
            case '\n':
                buffer.append("\\\n");
                break;
            case '\t':
                buffer.append("\\\t");
                break;
            case '\\':
                buffer.append("\\\\");
                break;
            case '"':
                buffer.append("\\\"");
                break;
            case '/':
                buffer.append("\\/");
                break;
            default:
                buffer.append(ch);
            }
        }
        buffer.append('"');
    }

    private static void printValue(StringBuilder buffer, Value val, OPTION opt) {
        if (val.getType() == Types.BOOLEAN) {
            buffer.append(((Value.JSONBoolean) val).getValue());
        } else if (val.getType() == Types.NUMBER) {
            buffer.append(((Value.JSONNumber) val).getValue());
        } else if (val.getType() == Types.STRING) {
            Printer.printString(buffer, ((Value.JSONString) val).getValue());
        } else if (val.getType() == Types.NULL) {
            buffer.append("null");
        } else if (val.getType() == Types.ARRAY) {
            Printer.printArray(buffer, (JSONArray) val, opt);
        } else {
            Printer.printObject(buffer, (JSONObject) val, opt);
        }
    }

    private static void printArray(StringBuilder buffer, JSONArray array, OPTION opt) {
        int tabCount = 0;
        ArrayList<Value> list = array.getList();
        buffer.append("[");
        if (list.isEmpty()) {
            buffer.append("]");
            return;
        }
        tabCount++;
        boolean printed = false;
        for (Value val : list) {
            if (printed) {
                buffer.append(",");
            }
            Printer.printNewLine(buffer, tabCount, opt);
            Printer.printValue(buffer, val, opt);
            printed = true;
        }
        tabCount--;
        Printer.printNewLine(buffer, tabCount, opt);
        buffer.append("]");
    }

    private static void printObject(StringBuilder buffer, JSONObject object, OPTION opt) {
        int tabCount = 0;
        HashMap<String, Value> map = object.getMap();
        buffer.append("{");
        if (map.isEmpty()) {
            buffer.append("}");
            return;
        }
        tabCount++;
        boolean printed = false;
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (printed) {
                buffer.append(",");
            }
            Printer.printNewLine(buffer, tabCount, opt);
            Printer.printString(buffer, key);
            buffer.append(": ");
            Printer.printValue(buffer, map.get(key), opt);
            printed = true;
        }
        tabCount--;
        Printer.printNewLine(buffer, tabCount, opt);
        buffer.append("}");
    }

    public static String JSONString(JSONObject object, OPTION opt) {
        StringBuilder buffer = new StringBuilder();
        Printer.printObject(buffer, object, opt);
        return buffer.toString();
    }

    public static String JSONString(JSONArray array, OPTION opt) {
        StringBuilder buffer = new StringBuilder();
        Printer.printArray(buffer, array, opt);
        return buffer.toString();
    }

    public static void print(JSONObject object, OPTION opt) {
        System.out.print(Printer.JSONString(object, opt));
    }

    public static void print(JSONArray array, OPTION opt) {
        System.out.print(Printer.JSONString(array, opt));
    }

    public static String formatString(String str) {
        StringBuilder buffer = new StringBuilder();
        Printer.printString(buffer, str);
        return buffer.toString();
    }
}