package json.value;

import java.util.HashMap;

import json.value.Types;
import json.value.Value;

import json.value.JSONError;

import json.value.JSONArray;

public class JSONObject extends Value {
    private HashMap<String, Value> map;

    JSONObject() {
        super(Types.OBJECT);
        this.map = new HashMap<String, Value>();
    }

    private void insertValue(String key, Value value) {
        if (key.isEmpty()) {
            return;
        }
        this.map.put(key, value);
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean hasKey(String key) {
        return this.map.containsKey(key);
    }

    public Types getKeyType(String key) {
        if (this.map.containsKey(key)) {
            return this.map.get(key).getType();
        }
        return null;
    }

    public Value getValue(String key) {
        return this.map.get(key);
    }

    public void deleteKey(String key) {
        this.map.remove(key);
    }

    public void putBoolean(String key, boolean value) {
        insertValue(key, new Value.JSONBoolean(value));
    }

    public void putString(String key, String value) {
        insertValue(key, new Value.JSONString(value));
    }

    public void putNumber(String key, double value) {
        insertValue(key, new Value.JSONNumber(value));
    }

    public void putNull(String key) {
        insertValue(key, new Value.JSONNull());
    }

    private Value getTypeHelper(String key, Types type) throws JSONError {
        Value value = this.map.get(key);
        if (value == null) {
            throw new JSONError("This key does not exist in this Object.");
        } else if (value.getType() != type) {
            throw new JSONError("This key is not of type " + type.toString() + ".");
        } else {
            return value;
        }
    }

    public boolean getBoolean(String key) throws JSONError {
        JSONBoolean value = (JSONBoolean) getTypeHelper(key, Types.BOOLEAN);
        return value.getValue();
    }

    public double getNumber(String key) throws JSONError {
        JSONNumber value = (JSONNumber) getTypeHelper(key, Types.NUMBER);
        return value.getValue();
    }

    public String getString(String key) throws JSONError {
        JSONString value = (JSONString) getTypeHelper(key, Types.STRING);
        return value.getValue();
    }

    public boolean isNull(String key) throws JSONError {
        Value value = this.map.get(key);
        if (value == null) {
            throw new JSONError("This key does not exist in this Object.");
        } else {
            return value.getType() == Types.NULL;
        }
    }

    public JSONObject getObject(String key) throws JSONError {
        JSONObject value = (JSONObject) getTypeHelper(key, Types.OBJECT);
        return value;
    }

    public JSONArray getArray(String key) throws JSONError {
        JSONArray value = (JSONArray) getTypeHelper(key, Types.ARRAY);
        return value;
    }
}