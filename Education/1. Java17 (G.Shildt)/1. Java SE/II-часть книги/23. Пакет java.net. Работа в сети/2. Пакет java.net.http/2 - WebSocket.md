
> [!NOTE] Класс **`java.net.http.WebSocket`** представляет **WebSocket-соединение**,
> поддерживающее **`ДВУСТОРОННИЙ` обмен данными** между клиентом и сервером в реальном времени.
> WebSocket — это протокол поверх TCP, который `позволяет` `серверу` и `клиенту` `ОБМЕНИВАТЬСЯ СООБЩЕНИЯМИ` `БЕЗ ПОВТОРНОГО` `УСТАНОВЛЕНИЯ СОЕДИНЕНИЯ`, в отличие от HTTP-запросов.
> 
> ⭐Реализует интерфейсы: `AutoCloseable`

## Пару слов о назначении
- ❗❗Класс `WebSocket` используется для создания WebSocket-==клиентов==.
    
- 🧠Он реализует **асинхронную, событийно-ориентированную модель обмена сообщениями**.
    
- 💡Работает через `WebSocket.Listener`, который получает события (`onOpen`, `onMessage`, `onError`, `onClose`).
    
- Поддерживает отправку текстовых и бинарных сообщений, пинг/понг и закрытие соединения.
    
- 💡Используется ==в паре с== `HttpClient`, который выступает ==как фабрика==.

### 🔸 Быстрая сводка классов WebSocket API 🔸

| Класс / Интерфейс                           | Назначение                                          |
| ------------------------------------------- | --------------------------------------------------- |
| **`WebSocket`**                             | Основной интерфейс для общения по WebSocket.        |
| **`WebSocket.Builder`**                     | Строит и конфигурирует WebSocket-клиент.            |
| **`WebSocket.Listener`**                    | Обрабатывает события (сообщения, ошибки, закрытие). |
| **`WebSocket.sendText()` / `sendBinary()`** | Отправляют данные на сервер.                        |
| **`WebSocket.request()`**                   | Контролирует приём сообщений (flow control).        |
| **`WebSocket.abort()`**                     | Принудительно разрывает соединение.                 |

---
## Конструктор / Фабричные методы
Конструктор приватный — экземпляры ==создаются через== HttpClient - `HttpClient.newWebSocketBuilder()`.

```java
//✅ Создаёт билдер для нового WebSocket-клиента.
public static WebSocket.Builder newBuilder(
	URI uri, 
	WebSocket.Listener listener)
```
💬 ❗❗Для создания WebSocket необходимо вызвать `buildAsync()` у билдера.❗❗

### 📙 Вложенный интерфейс Builder
```java
public static interface WebSocket.Builder {

    //✅ Устанавливает заголовок (например, Authorization, Cookie).
    WebSocket.Builder header(String name, String value);

    //✅ Устанавливает подпротоколы (например, "chat", "json").
    WebSocket.Builder subprotocols(String... subprotocols);

    //✅ Устанавливает максимальный размер буфера для сообщений.
    WebSocket.Builder connectTimeout(Duration timeout);

    //✅ Устанавливает Executor для асинхронных callback’ов.
    WebSocket.Builder executor(Executor executor);

    //✅ Создаёт WebSocket и подключается к серверу.
    CompletableFuture<WebSocket> buildAsync(
	    URI uri, 
	    WebSocket.Listener listener);
}
```

### 📙 Вложенный интерфейс Listener
==Определяет== ==ОБРАБОТЧИКИ СОБЫТИЙ событий== WebSocket-сессии.

> [!NOTE] 🧠Готовые реализации есть в 🧠
> javax.websocket.ChatEndpoint, Spring WebSocket

```java
public static interface WebSocket.Listener {

    //✅ Вызывается ПРИ УСТАНОВЛЕНИИ СОЕДИНЕНИЯ.
    void onOpen(WebSocket webSocket);

    //✅ Вызывается при ПОЛУЧЕНИИ ТЕКСТОВОГО сообщения.
    CompletionStage<?> onText(
	    WebSocket webSocket, 
	    CharSequence data, 
	    boolean last);

    //✅ Вызывается при ПОЛУЧЕНИИ БИНАРНОГО сообщения.
    CompletionStage<?> onBinary(
	    WebSocket webSocket, 
	    ByteBuffer data, 
	    boolean last);


    //✅ Вызывается при ПОЛУЧЕНИИ PING-сообщения.
    CompletionStage<?> onPing(
	    WebSocket webSocket, 
	    ByteBuffer message);

    //✅ Вызывается при ПОЛУЧЕНИИ PONG-сообщения.
    CompletionStage<?> onPong(
	    WebSocket webSocket, 
	    ByteBuffer message);


    //✅ Вызывается ПРИ ЗАКРЫТИИ соединения.
    CompletionStage<?> onClose(
	    WebSocket webSocket, 
	    int statusCode, 
	    String reason);

    //✅ Вызывается ПРИ ОШИБКАХ (например, сетевых).
    void onError(
	    WebSocket webSocket, 
	    Throwable error);
}
```

💬 Все методы асинхронны и могут возвращать `CompletableFuture.completedStage(null)` для завершения обработки событий.

---
## Методы
### Отправка сообщений
```java
//✅ Отправляет текстовое сообщение (частичное или полное).
// Параметры:
//  data — строка для отправки;
//  last — true, если это последняя часть сообщения.
CompletableFuture<WebSocket> sendText(CharSequence data, boolean last);


//✅ Отправляет бинарное сообщение (ByteBuffer).
CompletableFuture<WebSocket> sendBinary(ByteBuffer data, boolean last);


//✅ Отправляет ping с указанными данными.
CompletableFuture<WebSocket> sendPing(ByteBuffer message);


//✅ Отправляет pong (ответ на ping).
CompletableFuture<WebSocket> sendPong(ByteBuffer message);


//✅ Инициирует закрытие соединения.
// Параметры:
//  statusCode — код завершения (например, 1000 — normal closure);
//  reason — причина закрытия.
CompletableFuture<WebSocket> sendClose(int statusCode, String reason);
```

### Управление соединением
```java
//✅ Возвращает идентификатор подпротокола, согласованный с сервером.
Optional<String> getSubprotocol();


//✅ Возвращает true, если соединение открыто.
boolean isOutputClosed();

//✅ Возвращает true, если соединение закрыто полностью.
boolean isInputClosed();


//✅ 💡Запрашивает обработку следующего сообщения💡 (для управления потоком событий).
WebSocket request(long n);

//✅ Закрывает WebSocket немедленно.
void abort();
```

---

## 💡 Особенности и подводные камни 💡

1️⃣ ❗❗❗`WebSocket` — ==полностью **асинхронный== API**, работает через `CompletableFuture`.  

2️⃣ ⭐⭐Все callback-и из `WebSocket.Listener` ==вызываются== в потоке ==из== `Executor`.

3️⃣ ⭐Обязательно вызывать `webSocket.request(1)` в методах `onText` / `onBinary`,  чтобы получать последующие сообщения (аналог back-pressure).  

4️⃣ 🧠🧠Метод `sendText()` и `sendBinary()` ==возвращают== `CompletableFuture`,  
==который завершается==, ==когда сообщение== действительно ==отправлено==.  

5️⃣ ❗❗❗Соединение не закрывается автоматически — его нужно закрыть вручную (`sendClose()` или `abort()`).  

6️⃣ Если сервер не поддерживает указанный подпротокол — соединение всё равно установится,  но `getSubprotocol()` будет пустым.  

7️⃣ ⭐При передаче больших сообщений `last=false` позволяет разбивать поток данных на части.  

8️⃣ Пинг/Понг нужны для проверки активности соединения.  

9️⃣ ❓Методы `onPing` и `onPong` можно использовать для heartbeat-механизма.  

🔟 При ошибках (`onError`) соединение может автоматически закрыться.

---

## 💎 Best Practices 💎

✅ ⭐Всегда реализуй `onOpen`, `onText`, `onClose`, `onError`.  

✅ ❗❗❗После получения сообщения **обязательно** вызывать:
```JAVA
webSocket.request(1);
```
иначе приём событий остановится.

✅ Для отправки текстовых сообщений:
```java
webSocket.sendText("Hello, world!", true);
```

✅ Для бинарных сообщений:
```java
webSocket.sendBinary(
	ByteBuffer.wrap(data), 
	true);
```

✅ Для ping/pong:
```java
webSocket.sendPing(
	ByteBuffer.wrap(
		"ping".getBytes(StandardCharsets.UTF_8)
		)
	);
```

✅ ⭐Для корректного завершения:
```java
webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Bye");
```

✅ ⭐Используй `CompletableFuture`-цепочки⭐ для последовательных действий:
```java
webSocket.sendText("Hi", true)
         .thenCompose(ws -> ws.sendPing(ByteBuffer.wrap(new byte[]{1})))
         .thenCompose(ws -> ws.sendClose(1000, "done"));
```

---
## Пример
```java
import java.net.URI;
import java.net.http.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class WebSocketDemo {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
		
		// ⭐⭐⭐ СОБСТВЕННАЯ РЕАЛИЗАЦИЯ СЛУШАТЕЛЯ ⭐⭐⭐
        // 1️⃣ Создаём слушатель
        WebSocket.Listener listener = new WebSocket.Listener() {
            @Override
            public void onOpen(WebSocket webSocket) {
                System.out.println("✅ Соединение открыто");
                webSocket.request(1);
                webSocket.sendText("Привет, сервер!", true);
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                System.out.println("📩 Получено сообщение: " + data);
                webSocket.request(1); // Разрешаем следующее сообщение
                return CompletableFuture.completedStage(null);
            }

            @Override
            public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                System.out.println("🔒 Соединение закрыто: " + statusCode + " (" + reason + ")");
                return CompletableFuture.completedStage(null);
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                System.out.println("❌ Ошибка: " + error.getMessage());
            }
        };



        // 2️⃣ Подключаемся к WebSocket-серверу (пример: echo-сервер)
        CompletableFuture<WebSocket> wsFuture = client
	        .newWebSocketBuilder()
            .buildAsync(URI.create(
	            "wss://echo.websocket.events"),
	             listener);

        // 3️⃣ Ждём завершения работы
        WebSocket ws = wsFuture.join();

        // 4️⃣ Через 3 секунды закрываем соединение
        Thread.sleep(3000);
        ws.sendClose(1000, "Normal closure").join();
    }
}
```
