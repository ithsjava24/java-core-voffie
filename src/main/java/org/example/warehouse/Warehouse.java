package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;

public class Warehouse {
    private final String name;
    private final static Map<String, Warehouse> instances = new HashMap<>();
    private final List<ProductRecord> products = new ArrayList<>();
    private final List<ProductRecord> changedProducts = new ArrayList<>();

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
        id = Objects.requireNonNullElse(id, UUID.randomUUID());

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

    public List<ProductRecord> getProductsBy(Category category) {
        List<ProductRecord> output = new ArrayList<>();
        for (ProductRecord product : products) {
            if (product.category().equals(category)) {
                output.add(product);
            }
        }
        return output;
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        Map<Category, List<ProductRecord>> output = new HashMap<>();
        for (ProductRecord product : products) {
            if (!output.containsKey(product.category())) {
                output.put(product.category(), List.of(product));
            } else {
                List<ProductRecord> list = output.get(product.category());
                list.add(product);
                output.put(product.category(), list);
            }
        }
        return output;
    }

    public List<ProductRecord> getChangedProducts() {
        return changedProducts;
    }

    public void updateProductPrice(UUID uuid, BigDecimal price) {
        for (int i = 0; i < products.size(); i++) {
            ProductRecord product = products.get(i);
            if (product.uuid().equals(uuid)) {
                changedProducts.add(products.get(i));
                products.set(i, new ProductRecord(product.uuid(), product.name(), product.category(), price));
                return;
            }
        }
        throw new IllegalArgumentException("Product with that id doesn't exist.");
    }
}

