package org.example.warehouse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private final String name;
    private final static Map<String, Warehouse> instances = new HashMap<>();
    private final List<ProductRecord> products = new ArrayList<>();

    private Warehouse(String name) {
        this.name = name;
    }

    public static Warehouse getInstance() {
        return new Warehouse("Default");
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

        final UUID finalId = id;
        List<ProductRecord> filteredProducts = products.stream().filter(product -> product.uuid().equals(finalId)).toList();

        if (!filteredProducts.isEmpty())
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");

        products.add(new ProductRecord(id, name, category, Objects.requireNonNullElse(price, BigDecimal.ZERO), null));
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
        return products.stream().collect(Collectors.groupingBy(ProductRecord::category));
    }

    public List<ProductRecord> getChangedProducts() {
        return products.stream().filter(product -> product.updatedAt() != null).toList();
        //List<ProductRecord> filteredProducts = products.stream().filter(product -> product.updatedAt() != null).toList();
        //return filteredProducts;
    }

    public void updateProductPrice(UUID uuid, BigDecimal price) {
        List<ProductRecord> filteredProducts = products.stream().filter(product -> product.uuid().equals(uuid)).toList();

        if (filteredProducts.isEmpty()) {
            throw new IllegalArgumentException("Product with that id doesn't exist.");
        }

        ProductRecord product = filteredProducts.getFirst();
        ProductRecord updatedProduct = new ProductRecord(product.uuid(),
                product.name(),
                product.category(),
                price,
                LocalDateTime.now()
        );
        products.set(products.indexOf(product),
                updatedProduct);
    }
}

