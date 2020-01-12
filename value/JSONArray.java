package json.value;

import java.util.ArrayList;

import json.value.Types;
import json.value.Value;

import json.value.JSONError;

import json.value.JSONArray;

public class JSONArray extends Value {

    private ArrayList<Value> array;

    JSONArray() {
        super(Types.ARRAY);
        this.array = new ArrayList<Value>();
    }

    private void insertValue(int index, Value value) {
        if (index < 0) {
            return;
        }
        if (index >= this.array.size()) {
            int a = this.array.size();
            while (a < index) {
                this.array.add(new Value.JSONNull());
            }
            this.array.add(value);
        } else {
            this.array.set(index, value);
        }
    }

    public int size() {
        return this.array.size();
    }

    public boolean isEmpty() {
        return this.array.isEmpty();
    }

    public Types getIndexType(int index) {
        if (index >= 0 && index < this.array.size()) {
            return this.array.get(index).getType();
        }
        return null;
    }

    public Value getValue(int index) {
        try {
            return this.array.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void deleteIndex(int index) {
        this.array.remove(index);
    }

    public void putBoolean(int index, boolean value) {
        insertValue(index, new Value.JSONBoolean(value));
    }

    public void putString(int index, String value) {
        insertValue(index, new Value.JSONString(value));
    }

    public void putNumber(int index, double value) {
        insertValue(index, new Value.JSONNumber(value));
    }

    public void putNull(int index) {
        insertValue(index, new Value.JSONNull());
    }

    private Value getTypeHelper(int index, Types type) throws JSONError {
        if (index < 0 || index >= this.array.size()) {
            throw new JSONError("This index does not exist in this Array.");
        } else if (this.array.get(index).getType() != type) {
            throw new JSONError("This index is not of type " + type.toString() + ".");
        } else {
            return this.array.get(index);
        }
    }

    public boolean getBoolean(int index) throws JSONError {
        JSONBoolean value = (JSONBoolean) getTypeHelper(index, Types.BOOLEAN);
        return value.getValue();
    }

    public double getNumber(int index) throws JSONError {
        JSONNumber value = (JSONNumber) getTypeHelper(index, Types.NUMBER);
        return value.getValue();
    }

    public String getString(int index) throws JSONError {
        JSONString value = (JSONString) getTypeHelper(index, Types.STRING);
        return value.getValue();
    }

    public boolean isNull(int index) throws JSONError {
        if (index < 0 || index >= this.array.size()) {
            throw new JSONError("This index does not exist in this Array.");
        } else {
            return this.array.get(index).getType() == Types.NULL;
        }
    }

    public JSONObject getObject(int index) throws JSONError {
        JSONObject value = (JSONObject) getTypeHelper(index, Types.OBJECT);
        return value;
    }

    public JSONArray getArray(int index) throws JSONError {
        JSONArray value = (JSONArray) getTypeHelper(index, Types.ARRAY);
        return value;
    }
}