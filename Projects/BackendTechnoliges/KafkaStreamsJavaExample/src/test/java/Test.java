import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        DataProduct dataProduct = new DataProduct(5,500);
        DataUser dataUser = new DataUser(7,"Tom");

        Data data = dataUser;
        System.out.println("Interface data link to User = " + data.toString());

        data = dataProduct;
        System.out.println("Interface data link to Product = " + data.toString());


        byte[] serializeProduct = SerializerDeserializer.serialize(dataProduct);
        byte[] serializeUser = SerializerDeserializer.serialize(dataUser);


        Data deserializeData = SerializerDeserializer.deserializeProduct(serializeProduct);
        Data deserializeUser = SerializerDeserializer.deserializeUser(serializeUser);

        System.out.println("getClass = " + deserializeData.getClass() + "\nvalue = "+deserializeData.toString());
        System.out.println(deserializeUser.toString());
    }
}

class SerializerDeserializer{
    private static final ObjectMapper mapper = new ObjectMapper();

    public static byte[] serialize(Data data){
        try {
            System.out.println("On serializer method\nData of data = " + data.toString());
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            System.out.println("[TEST] Exception on serialize data");
            throw new RuntimeException(e);
        }
    }

    public static Data deserializeProduct(byte[] data){
        try {
            return mapper.readValue(data, DataProduct.class);
        } catch (IOException e) {
            System.out.println("[TEST] Exception on DEserialize data");
            throw new RuntimeException(e);
        }
    }

    public static Data deserializeUser(byte[] data){
        try {
            return mapper.readValue(data, DataUser.class);
        } catch (IOException e) {
            System.out.println("[TEST] Exception on DEserialize data");
            throw new RuntimeException(e);
        }
    }
}


interface Data{
}

class DataProduct implements Data{
    private int id;
    private int price;

    public DataProduct() {
    }

    public DataProduct(int id, int price) {
        this.id = id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "DataProduct{" +
                "id=" + id +
                ", price=" + price +
                '}';
    }
}

class DataUser implements Data{
    private int id;
    private String username;

    public DataUser() {
    }

    public DataUser(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DataUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
