package json.value;

import json.value.Types;

public abstract class Value {
    private Types type;

    Value(Types type) {
        this.type = type;
    }

    public Types getType() {
        return this.type;
    }

    public static class JSONBoolean extends Value {
        private boolean value;

        public JSONBoolean(boolean value) {
            super(Types.BOOLEAN);
            this.value = value;
        }

        public boolean getValue() {
            return this.value;
        }
    }

    public static class JSONNumber extends Value {
        private double value;

        public JSONNumber(double value) {
            super(Types.NUMBER);
            this.value = value;
        }

        public double getValue() {
            return this.value;
        }
    }

    public static class JSONString extends Value {
        private String value;

        public JSONString(String value) {
            super(Types.STRING);
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static class JSONNull extends Value {

        public JSONNull() {
            super(Types.NULL);
        }
    }
}