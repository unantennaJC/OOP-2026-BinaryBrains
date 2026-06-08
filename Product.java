package model;

public class Product {
     private int productID;
     private String name;
     private String category;
     private double price;
     private int ecoRating; //1 to 5 stars
    private String description;

    //Constructor
    public Product(int productID,String name,String category,double price, int ecoRating,String description){
        this.productID = productID;
        this.name = name;
        this.category = category;
        this.price = price;
        this.ecoRating = ecoRating;
        this.description = description;

    }
    //getters
public int getProductID(){return productID; }
public String getName(){return name; }
public String getCategory(){return category; }
public double getPrice(){return price; }
public int getEcoRating(){return ecoRating;}
public String getDescription(){return description; }

 //Setters
public void setName(String name) {this.name=name;}
public void setCategory(String category){this.category = category;}
public void setPrice(double price){this.price = price;}
public void setEcoRating(int ecoRating){this.ecoRating=ecoRating ;}
public void setDescription(String description){this. description = description;}


public String tostring(){
    return "Product [ID"+productID+", Name = " +name+
            ", Category=" + category + ", Price=" + price +
            ", EcoRating=" + ecoRating + "]";


    }
}



