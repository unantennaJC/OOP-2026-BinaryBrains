package com.greenloop.model;

public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private int ecoRating;
    private int quantityOnHand;
    private int reorderLevel;

    public Product(int productId, String name, String description,
                   double price, int ecoRating, int quantityOnHand, int reorderLevel) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.ecoRating = ecoRating;
        this.quantityOnHand = quantityOnHand;
        this.reorderLevel = reorderLevel;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getEcoRating() { return ecoRating; }
    public int getQuantityOnHand() { return quantityOnHand; }
    public int getReorderLevel() { return reorderLevel; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setEcoRating(int ecoRating) { this.ecoRating = ecoRating; }
    public void setQuantityOnHand(int quantityOnHand) { this.quantityOnHand = quantityOnHand; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }
}
