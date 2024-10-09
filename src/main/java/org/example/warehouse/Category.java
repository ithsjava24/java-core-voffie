package org.example.warehouse;

import java.util.*;

public class Category {
    private final String name;
    private final static Map<String, Category> instances = new HashMap<>();

    private Category(String name) {
        this.name = capitalize(name);
    }

    public static Category of(String name) {
        if (name == null) throw new IllegalArgumentException("Category name can't be null");
        String capitalizedName = capitalize(name);
        if (instances.containsKey(capitalizedName)) {
            return instances.get(capitalizedName);
        } else {
            Category category = new Category(capitalizedName);
            instances.put(capitalizedName, category);
            return category;
        }
    }

    public String getName() {
        return name;
    }

    private static String capitalize(String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
