package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;

public class Warehouse {
    private final List<ProductRecord> products = new ArrayList<>();
    private final String name;
    private final static Map<String, Warehouse> instances = new HashMap<>();

    private Warehouse() {
        this.name = "Default";
    }

    private Warehouse(String name) {
        this.name = name;
    }

    public static Warehouse getInstance() {
        return new Warehouse();
    }

    public static Warehouse getInstance(String name) {
        if (instances.containsKey(name)) {
            return instances.get(name);
        } else {
            Warehouse warehouse = new Warehouse(name);
            instances.put(name, warehouse);
            return warehouse;
        }
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }
}

