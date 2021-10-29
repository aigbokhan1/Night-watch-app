package com.example.expensemanager;

import com.example.expensemanager.Model.Filter;

import java.util.HashMap;

public abstract class Preferences {
    public  static HashMap<Integer, Filter> ifilters = new HashMap<>();
    public  static HashMap<Integer, Filter> efilters = new HashMap<>();
}
