package model;

public class Stock {
    private int stockID;
    private int productID;
    private int quantityOnHand;
    private int reorderLevel;

    //constructor
    public Stock(int stockID,int productID,int quantityOnHand,int reorderLevel) {
        this.stockID = stockID;
        this.productID = productID;
        this.quantityOnHand = quantityOnHand;
        this.reorderLevel = reorderLevel;
    }
    //Getters
    public int getStockID(){return stockID;}
    public int getProductID(){return productID;}
    public int getQuantityOnHand(){return quantityOnHand;}
    public int getReorderLevel(){return reorderLevel;}

    //Setters
    public void setQuantityOnHand(int quantityOnHand){this.quantityOnHand = quantityOnHand;}

    public void setReorderLevel(int reorderLevel) {this.reorderLevel = reorderLevel;}

    //check if stock is low
    public boolean isLowStock() {
        return quantityOnHand <= reorderLevel;
    }

    @Override
    public String toString() {
        return "Stock [ProductID=" + productID +
                ", Quantity=" + quantityOnHand +
                ", ReorderLevel=" + reorderLevel +
                ", LowStock=" + isLowStock() + "]";
    }



}


