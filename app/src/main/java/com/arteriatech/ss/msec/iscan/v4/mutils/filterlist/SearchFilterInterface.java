package com.arteriatech.ss.msec.iscan.v4.mutils.filterlist;

/**
 * Created by e10742 on 21-11-2016.
 */
public interface SearchFilterInterface<T> {
    /*
    * Interface to implement filter condition.
    * */
    boolean applyConditionToAdd(T item); }
