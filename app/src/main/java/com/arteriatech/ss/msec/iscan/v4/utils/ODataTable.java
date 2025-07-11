package com.arteriatech.ss.msec.iscan.v4.utils;
@Deprecated
public class ODataTable<K,V,T> {
    private K key;
    private V value;
    private T type;

    public ODataTable() {
    }

    public ODataTable(K key, V value, T type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }
}
