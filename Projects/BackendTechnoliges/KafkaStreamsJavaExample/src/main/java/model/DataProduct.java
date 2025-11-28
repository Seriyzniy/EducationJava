package model;

public class DataProduct implements Data {
    private int purchaseId;
    private int userId;
    private String productName;
    private int amount;

    public DataProduct() {}
    public DataProduct(int purchaseId, int userId, String productName, int amount) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.productName = productName;
        this.amount = amount;
    }

    public int getPurchaseId() {return purchaseId;}
    public void setPurchaseId(int purchaseId) {this.purchaseId = purchaseId;}

    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}

    public String getProductName() {return productName;}
    public void setProductName(String productName) {this.productName = productName;}

    public int getAmount() {return amount;}
    public void setAmount(int amount) {this.amount = amount;}

    @Override
    public String toString() {
        return "DataProduct{" +
                "purchaseId=" + purchaseId +
                ", userId=" + userId +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                '}';
    }
}
