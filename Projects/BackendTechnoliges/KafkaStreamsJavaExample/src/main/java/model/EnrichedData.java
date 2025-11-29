package model;

public class EnrichedData implements Data{
    private int id;
    private String userName;
    private String productName;
    private int amount;
    private String country;

    public EnrichedData() {}
    public EnrichedData(int id, String userName, String productName, int amount, String country) {
        this.id = id;
        this.userName = userName;
        this.productName = productName;
        this.amount = amount;
        this.country = country;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    public String getProductName() {return productName;}
    public void setProductName(String productName) {this.productName = productName;}

    public int getAmount() {return amount;}
    public void setAmount(int amount) {this.amount = amount;}

    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}

    @Override
    public String toString() {
        return "EnrichedData{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                ", country='" + country + '\'' +
                '}';
    }
}
