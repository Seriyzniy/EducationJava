Интерфейс **`Iterable`** в Java является частью коллекций, и если класс реализует этот интерфейс, его объекты могут быть использованы в цикле **`for-each`**

Когда класс реализует интерфейс **`Iterable`**, он ==обязуется предоставить метод== **`iterator()`**, который возвращает объект типа **`Iterator`**. Этот **`Iterator`** нужен для обхода коллекции элементов в цикле **`for-each`**.

Этот метод возвращает `Iterator`, чаще всего его необходимо определить в виде АНОНИМНОГО КЛАССА

```java
public class MyCollection implements Iterable<String> {
    private String[] data = {"apple", "banana", "cherry"};

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < data.length;
            }

            @Override
            public String next() {
                if (hasNext()) {
                    return data[index++];
                }
                throw new IndexOutOfBoundsException("No more elements");
            }
        };
    }

    public static void main(String[] args) {
        MyCollection collection = new MyCollection();

        // Используем for-each цикл для обхода элементов
        for (String fruit : collection) {
            System.out.println(fruit);
        }
    }
}
```