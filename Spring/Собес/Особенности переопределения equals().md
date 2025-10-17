### 1. **Рефлексивность** (Reflexive)

Объект должен быть равен самому себе.
```java
Object obj = new Object();
System.out.println(obj.equals(obj));  // true
```
***
### 2. **Симметричность** (Symmetric)

Если `x.equals(y)` возвращает `true`, то и `y.equals(x)` также должно возвращать `true`.
```java
String str1 = "hello";
String str2 = "hello";

System.out.println(str1.equals(str2));// true

System.out.println(str2.equals(str1));  // true
```

***
### 3. **Транзитивность** (Transitive)

Если `x.equals(y)` и `y.equals(z)` возвращают `true`, то `x.equals(z)` должно также возвращать `true`.

***
### 5. **Неправда для `null`** (Non-nullity)

Если сравнивать объект с `null`, то метод `equals()` должен ==возвращать FALSE==.
```java
Object obj = new Object();
System.out.println(obj.equals(null));  // false
```

***
### 6. **Использование `instanceof` или `getClass()`**
Также логично использовать оператор `instanceoff` чтобы убедиться, что сравниваемые объекты являются объектами одного класса

### 7. **`hashCode()` и `equals()`**

Если вы переопределяете `equals()`, вы должны также переопределить метод `hashCode()`. Это требование Java-контракта. Если два объекта равны (через `equals()`), то они должны иметь одинаковый хеш-код.



Правила
1. Проверка на null
2. Проверка что ссылки не ссылаются на один и тот же объект
3. Проверка что два объекта являются объектами ОДНОГО И ТОГО ЖЕ класса
4. Проверка полей

```java
class Bar {  
    int x = 10;  
  
    public Bar(int n){  
        x = n;  
    }

//	public boolean equals(Object o){  
//        if(o == null) return false;  
//        if(o == this) return true;  
//        if(!o.getClass().equals(this.getClass())){  
//            return false;  
//        }  
//        Bar b = (Bar) o;  
//        if(b.x == x) return true;  
//        return false;  
//    }

	@Override  
	public boolean equals(Object o) {  
	    if (this == o) return true;  
	    if (o == null || getClass() != o.getClass()) return false;  
	    Bar bar = (Bar) o;  
	    return x == bar.x;  
	}  
	  
	@Override  
	public int hashCode() {  
	    return Objects.hashCode(x);  
	}
}
```