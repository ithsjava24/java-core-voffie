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

    public ProductRecord addProduct(UUID id, String name, Category category, BigDecimal price) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Product name can't be null or empty.");
        if (category == null) throw new IllegalArgumentException("Category can't be null.");

        if (id == null) id = UUID.randomUUID();

        for (ProductRecord product : products) {
            if (id.equals(product.uuid()))
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }

        products.add(new ProductRecord(id, name, category, Objects.requireNonNullElse(price, BigDecimal.ZERO)));
        return products.getLast();
    }

    public List<ProductRecord> getProducts() {
        return List.copyOf(products);
    }

    public Optional<ProductRecord> getProductById(UUID uuid) {
        for (ProductRecord product : products) {
            if (product.uuid().equals(uuid)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    public ProductRecord[] getProductsBy(Category category) {
        List<ProductRecord> output = new ArrayList<>();
        for (ProductRecord product : products) {
            if (product.category().equals(category)) {
                output.add(product);
            }
        }
        return output.toArray(new ProductRecord[0]);
    }


}

