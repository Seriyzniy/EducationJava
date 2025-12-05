package model;

public class UserInfo {
    private String uname;
    private int age;

    public UserInfo() {}
    public UserInfo(String userName, int age) {
        this.uname = userName;
        this.age = age;
    }

    public String getUname() {return uname;}
    public void setUname(String uname) {this.uname = uname;}

    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}

    @Override
    public String toString() {
        return "userName: " + uname + ", age: " + age;
    }
}
