package com.consistent.hash;

import java.util.HashMap;
import java.util.Map;

public class Machine {

    private String host;

    private Map<String, String> values;

    public Machine(String host){
        this.host = host;
        this.values = new HashMap<>();
    }

    public String getHost(){
        return host;
    }

    public int getValueSize(){
        return values.size();
    }

    public void add(String key, String value){
        this.values.put(key, value);
    }

}
