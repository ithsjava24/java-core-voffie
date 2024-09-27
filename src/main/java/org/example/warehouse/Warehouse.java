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

        UUID finalId = id;
        List<ProductRecord> filteredProducts = products.stream().filter(product -> product.uuid().equals(finalId)).toList();

        if (!filteredProducts.isEmpty())
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");

        products.add(new ProductRecord(id, name, category, Objects.requireNonNullElse(price, BigDecimal.ZERO)));
        return products.getLast();
    }

    public List<ProductRecord> getProducts() {
        return List.copyOf(products);
    }

    public Optional<ProductRecord> getProductById(UUID uuid) {
        List<ProductRecord> filteredProducts = products.stream().filter(product -> product.uuid().equals(uuid)).toList();
        if (filteredProducts.isEmpty()) return Optional.empty();
        return Optional.of(filteredProducts.getFirst());

    }

    public List<ProductRecord> getProductsBy(Category category) {
        return products.stream().filter(product -> product.category().equals(category)).toList();
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
        List<ProductRecord> filteredProducts = products.stream().filter(product -> product.uuid().equals(uuid)).toList();

        if (filteredProducts.isEmpty()) {
            throw new IllegalArgumentException("Product with that id doesn't exist.");
        }

        ProductRecord product = filteredProducts.getFirst();
        changedProducts.add(product);
        products.set(products.indexOf(product),
                new ProductRecord(product.uuid(),
                        product.name(),
                        product.category(),
                        price
                ));
    }
}

