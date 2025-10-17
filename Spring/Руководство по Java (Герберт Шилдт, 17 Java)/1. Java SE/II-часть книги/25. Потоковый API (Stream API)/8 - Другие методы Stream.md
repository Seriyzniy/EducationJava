[[#📕1. `flatMap`]]
[[#📕2. `distinct`]]
[[#📕3. `sorted`]]
[[#📕4. `peek`]]
[[#📕5. `limit` / `skip`]]
[[#📕6. `forEach`]]
[[#📕7. `forEachOrdered`]]
[[#📕8. `anyMatch`]]
[[#📕9. `allMatch`]]
[[#📕10. `noneMatch`]]
[[#📕11. `findFirst`]]
[[#📕12. `findAny`]]
[[#📕13. `takeWhile`]]
[[#📕14. `dropWhile`]]

# 📕1. `flatMap` 

> [!NOTE] ⭐⭐`flatMap`⭐⭐
> Преобразует `каждый элемент потока` В `поток объектов`, а потом **«сплющивает» (flatten)** их в один общий поток


```java
<R> Stream<R> flatMap(
	Function<? super T, ? extends Stream<? extends R>> mapper);
```
`mapper` — функция, которая принимает элемент потока (`T`) и возвращает **новый поток** элементов (`Stream<R>`).
## Особенности
- ✅**Тип операции:** ==промежуточная==.
    
- 💡Преобразует каждый элемент потока в _новый поток_, а затем «расплющивает» (flatten) эти потоки в один общий.
> [!NOTE] Каждый `элемент` `преобразуется в ПОТОК` элементов, `потом` все потоки `объединяются`
> 

    
- Применяется, когда ==из одного элемента== нужно получить ==**множество** элементов==.
    
- Часто используется при работе со **вложенными коллекциями**, **JSON**, **XML**, или результатами парсинга.

## Примеры
```java
List<List<Integer>> numbers = List.of(
    List.of(1, 2),
    List.of(3, 4),
    List.of(5, 6)
);

List<Integer> result = numbers.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());
// [1, 2, 3, 4, 5, 6]
```

```java
Stream<String> words = Stream.of("hello world", "java streams");

List<String> letters = words 
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .collect(Collectors.toList());
// ["hello", "world", "java", "streams"]
```

## map VS flatMap
![[Pasted image 20251010122915.png]]

```java
List<List<Integer>> list = List.of(
    List.of(1, 2, 3),
    List.of(4, 5)
);
```
- `❗❗❗map(List::stream)` → `Stream<Stream<Integer>>`
    
- 🧠`flatMap(List::stream)` → `Stream<Integer>`

### Образно: разница в одной фразе
- `map` → превращает элементы потока в **коробки** с другими элементами.
    
- `flatMap` → **раскрывает эти коробки**, создавая **один общий поток**.

---
# 📕2. `distinct`

```java
Stream<T> distinct();
```
## Особенности
- ✅**Тип:** промежуточная операция.
    
- ⭐Убирает дубликаты, используя `equals()` и `hashCode()`.
    
- Работает как `Set`: сохраняет только уникальные элементы.
    
- Эффективен только при корректно переопределённых `equals`/`hashCode`.

## Примеры
```java
Stream.of(1, 2, 2, 3, 1, 4)
    .distinct()
    .forEach(System.out::println);
// 1 2 3 4
```

```java
Stream.of("apple", "banana", "apple")
    .distinct()
    .toList();
// ["apple", "banana"]
```

---
# 📕3. `sorted`
```java
Stream<T> sorted(Comparator<? super T> comparator);
Stream<T> sorted();
```
`comparator` — компаратор для задания пользовательского порядка сортировки.
## Особенности
- ✅**Тип:** промежуточная операция.
    
- ⭐Возвращает новый поток, элементы которого отсортированы по:
    - естественному порядку (`Comparable`);
    - пользовательскому `Comparator`.
        
- Требует полной загрузки элементов в память, поэтому **дорогая операция**

## Примеры
```java
Stream.of(3, 1, 2)
    .sorted()
    .forEach(System.out::println);
// 1 2 3
```

```java
Stream.of("java", "python", "c++")
    .sorted(Comparator.comparingInt(String::length))
    .forEach(System.out::println);
// c++ java python
```

---
# 📕4. `peek`

```java
Stream<T> peek(Consumer<? super T> action)
```
`action` — функция, выполняющая побочный эффект (например, `System.out::println`).
## Особенности
- ✅**Тип:** промежуточная операция.
    
- ⭐Используется ==для **временного просмотра**== элементов, например для логирования.
    
- ❗❗❗Не изменяет элементы потока.
    
- Не гарантируется выполнение, если нет терминальной операции.

## Примеры
```java
Stream.of(1, 2, 3)
    .peek(i -> System.out.println("Before map: " + i))
    .map(i -> i * 2)
    .peek(i -> System.out.println("After map: " + i))
    .toList();
    
Before map: 1
After map: 2

Before map: 2
After map: 4

Before map: 3
After map: 6
```

```java
Stream.of("a", "b", "c")
    .peek(System.out::println)
    .count(); // peek сработает, потому что есть терминальная операция
```

# 📕5. `limit` / `skip`

```java
Stream<T> limit(long maxSize)
Stream<T> skip(long n)
```
- `maxSize` — количество элементов, которое нужно взять; если меньше 0 — исключение.
- `n` — количество элементов, которые нужно пропустить.
## Особенности
- ✅**Тип:** промежуточные операции.
- ⭐⭐`limit(n)` — берёт **первые n элементов**.
    
- ⭐⭐`skip(n)` — **пропускает первые n элементов**.
    
- Очень эффективны, т.к. поток не загружается полностью.

## Примеры
```java
Stream.of(1, 2, 3, 4, 5)
    .limit(3)
    .forEach(System.out::println);
// 1 2 3
```

```java
Stream.of(1, 2, 3, 4, 5)
    .skip(2)
    .forEach(System.out::println);
// 3 4 5
```

---
# 📕6. `forEach`

```java
void forEach(Consumer<? super T> action)
```
`action` — функция, принимающая элемент потока и выполняющая над ним действие.
## Особенности
- ❌**Тип:** терминальная операция.
    
- ==Выполняет действие== (`Consumer`) ==для каждого элемента== потока.
    
- ❗❗❗**Не гарантирует порядок** в параллельных потоках.
    
- Часто используется для вывода, логирования, записи в коллекции.

## Примеры
```java
Stream.of("A", "B", "C")
    .forEach(System.out::println);
```

```java
List<String> result = new ArrayList<>();
Stream.of("x", "y", "z")
    .forEach(result::add);
```

---
# 📕7. `forEachOrdered`

```java
void forEachOrdered(Consumer<? super T> action)
```
`action` — операция, выполняемая для каждого элемента **в порядке их следования**.
## Особенности
- ❌**Тип:** терминальная операция.
    
- ==Гарантирует **порядок выполнения**==, даже в параллельных потоках.
    
- 💡Медленнее, чем `forEach`, из-за синхронизации.

## Примеры
```java
Stream.of("A", "B", "C")
    .parallel()
    .forEachOrdered(System.out::println);
// A B C
```

```java
IntStream.range(1, 5)
    .parallel()
    .forEachOrdered(System.out::print);
// 1234
```


---
# 📕8. `anyMatch`
```java
boolean anyMatch(Predicate<? super T> predicate)
```
`predicate` — функция, возвращающая `true`, если элемент удовлетворяет условию.
## Особенности
- ❌**Тип:** терминальная операция.
    
- Проверяет, ==удовлетворяет ли== ==**хотя бы один элемент**== ==условию==.
    
- ⭐**Короткозамыкающая** — останавливается на первом совпадении.
    
- Используется для быстрых проверок существования элементов.

## Примеры
```java
boolean hasEven = Stream.of(1, 3, 5, 6)
    .anyMatch(n -> n % 2 == 0);
// true
```

```java
boolean containsA = Stream.of("b", "c", "a")
    .anyMatch(s -> s.equals("a"));
// true
```

---
# 📕9. `allMatch`

```java
boolean allMatch(Predicate<? super T> predicate)
```
`predicate` — функция, проверяющая условие для каждого элемента
## Особенности
- ❌**Тип:** терминальная операция.
    
- Проверяет, ==удовлетворяют ли== **==все элементы==** условию.
    
- 💡Останавливается при первом элементе💡, НЕ подходящем под условие.

## Примеры
```java
boolean allPositive = Stream.of(1, 2, 3)
    .allMatch(n -> n > 0);
// true
```

```java
boolean allStartsWithJ = Stream.of("java", "javascript")
    .allMatch(s -> s.startsWith("java"));
// true
```

---
# 📕10. `noneMatch`

```java
boolean noneMatch(Predicate<? super T> predicate)
```
`predicate` — функция, возвращающая `true`, если элемент ПОДХОДИТ под исключаемое условие.
## Особенности
- ❌**Тип:** терминальная операция.
    
- Проверяет, ==что **НИ ОДИН элемент** НЕ удовлетворяет условию==. (true если так)
    
- 💡Обратна по смыслу `anyMatch`.
    
- 💡**Короткозамыкающая.**

## Примеры
```java
boolean noneNegative = Stream.of(1, 2, 3)
    .noneMatch(n -> n < 0);
// true
```

```java
boolean noJava = Stream.of("python", "c++")
    .noneMatch(s -> s.contains("java"));
// true
```

---
# 📕11. `findFirst`

```java
Optional<T> findFirst()
```
Возвращает `Optional<T>`, который:
- содержит элемент, если поток не пуст;
- пустой (`Optional.empty()`), если поток не содержит элементов.

## Особенности

- **Тип:** терминальная операция.
    
- ==Возвращает **первый элемент потока**==, обёрнутый в `Optional<T>`.
    
- Используется для потоков с **определённым порядком**.
    
- 💡**Короткозамыкающая** — поток не просматривает все элементы.
    
- 🧠В **параллельных потоках** может снижать производительность, так как требует сохранения порядка.

## Примеры
```java
Optional<String> first = Stream.of("A", "B", "C")
    .findFirst();

first.ifPresent(System.out::println); // A
```

```java
Optional<Integer> result = Stream.<Integer>empty()
    .findFirst();
// result.isEmpty() == true
```

---
# 📕12. `findAny`

```java
Optional<T> findAny()
```
Возвращает `Optional<T>` (аналогично `findFirst`).
## Особенности

- **Тип:** терминальная операция.
    
- Возвращает ==**любой элемент** потока== (не обязательно первый).
    
- В **параллельных потоках** может вернуть элемент, найденный первым любым потоком.
    
- Обычно быстрее `findFirst` при `parallelStream()`.
    
- Результат неопределён в терминах порядка, но корректен с точки зрения данных.

## Примеры
```java
Optional<String> any = Stream.of("A", "B", "C")
    .findAny();

any.ifPresent(System.out::println); // Может быть A, B или C
```

```java
Optional<Integer> result = Stream.<Integer>empty()
    .findAny();
// result.isEmpty() == true
```

```java
List<Integer> list = IntStream.rangeClosed(1, 1000)
    .parallel()
    .boxed()
    .toList();

System.out.println(list.parallelStream().findAny().get());
// Может вернуть любое число от 1 до 1000
```

---
# 📕13. `takeWhile`

```java
Stream<T> takeWhile(Predicate<? super T> predicate)
```
`predicate` — логическое условие, определяющее, следует ли включать элемент в результирующий поток.

## Особенности

- **Тип:** промежуточная операция (появилась в **Java 9**).
    
- ==Возвращает элементы== **до тех пор**, ==пока условие== (`Predicate`) истинно.
    
- 💡Как только условие нарушается, поток **обрывается**.
    
- ❗❗❗Элементы **после первого несоответствия не проверяются вообще**.
    
- Работает **только для упорядоченных потоков** (например, `Stream.of(...)`, `List.stream()`).

## Примеры
```java
Stream.of(1, 2, 3, 4, 5, 0, 6)
    .takeWhile(n -> n < 5)
    .forEach(System.out::println);
// 1 2 3 4
```

```java
Stream.of("apple", "apricot", "banana", "avocado")
    .takeWhile(s -> s.startsWith("a"))
    .forEach(System.out::println);
// apple apricot
```

---
# 📕14. `dropWhile`

```java
Stream<T> dropWhile(Predicate<? super T> predicate)
```
`predicate` — логическое условие, определяющее, какие элементы нужно пропустить.
## Особенности

- **Тип:** промежуточная операция (также с **Java 9**).
    
- ==Пропускает элементы==, пока условие (`Predicate`) истинно.
    
- Как только встречается первый элемент, **НЕ удовлетворяющий** условию, — ==он и все последующие включаются в поток==.
    
- Противоположна `takeWhile`.
    
- Эффективна для потоков с естественным порядком.

## Примеры
```java
Stream.of(1, 2, 3, 4, 5, 0, 6)
    .dropWhile(n -> n < 5)
    .forEach(System.out::println);
// 5 0 6
```

```java
Stream.of("apple", "apricot", "banana", "avocado")
    .dropWhile(s -> s.startsWith("a"))
    .forEach(System.out::println);
// banana avocado
```

