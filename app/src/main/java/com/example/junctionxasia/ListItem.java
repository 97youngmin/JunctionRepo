package com.example.junctionxasia;

public class ListItem {
    public String name;
    public String category;
    public String closing;
    private String company;
    private String expiry;
    private String id;
    private String image;
    private String location;
    private String phone;
    private Double lat;
    private Double lon;
    private Double price;
    private Integer quantity;

    public ListItem(String name, String category, String closing, String company, String expiry,
                    String id, String image, String location, String phone, Double lat, Double lon,
                    Double price, Integer quantity){
        this.name = name;
        this.category = category;
        this.closing = closing;
        this.company = company;
        this.expiry = expiry;
        this.id = id;
        this.image = image;
        this.location = location;
        this.phone = phone;
        this.lat = lat;
        this.lon = lon;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getClosing() {
        return closing;
    }

    public String getCompany() {
        return company;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
