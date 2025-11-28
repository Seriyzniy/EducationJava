package model;

public class DataUser implements Data{
    private int userId;
    private String userName;
    private String country;

    public DataUser() {}
    public DataUser(int userId, String userName, String country) {
        this.userId = userId;
        this.userName = userName;
        this.country = country;
    }

    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}

    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}

    @Override
    public String toString() {
        return "DataUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
