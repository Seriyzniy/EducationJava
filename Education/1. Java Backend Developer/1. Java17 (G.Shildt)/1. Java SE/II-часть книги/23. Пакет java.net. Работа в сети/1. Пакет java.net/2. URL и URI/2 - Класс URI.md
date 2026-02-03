
> [!NOTE] Класс **`java.net.URI`** (Uniform Resource Identifier — _универсальный `ИДЕНТИФИКАТОР` ресурса_)
> Представляет **уникальный идентификатор ресурса**, НЕ ОБЯЗАТЕЛЬНО ДОСТУПНОГО В СЕТИ
> 
> ⭐Реализует интерфейсы: `Comparable<URI>, Serializable`

В отличие от `URL`, который подразумевает доступ к ресурсу через конкретный **протокол** (HTTP, FTP и т.д.),  
`URI` — это просто **==строковое обозначение ресурса==**, без необходимости подключаться к нему.

### Пару слов о назначении
- `URI` используется для **разбора и анализа адресов**,  
    — например, в API `HttpClient`, `Path`, `FileSystem`, REST-запросах.
    
- ⭐Не открывает соединения — лишь **логическое представление адреса**.
    
- Поддерживает абсолютные и относительные URI, а также фрагменты (anchor, query и т.д.).

## Конструкторы
```java
//✅ Создаёт объект URI из одной строки.
// Параметр:
//  str — полный URI (например: "https://example.com/index.html?x=1#top").
// Если строка имеет неверный формат → URISyntaxException.
URI(String str)
    throws URISyntaxException

//✅ Создаёт URI из отдельных компонентов.
// Параметры:
//  scheme — схема (протокол, например "http");
//  userInfo — данные пользователя (например "user:pass");
//  host — хост или домен;
//  port — порт (или -1 для значения по умолчанию);
//  path — путь к ресурсу;
//  query — строка запроса ("x=1&y=2");
//  fragment — якорь (например "section1").
URI(String scheme, String userInfo, String host, int port,
    String path, String query, String fragment)
    throws URISyntaxException

//✅ Создаёт URI с указанной схемой, авторитетом, путём, запросом и фрагментом.
// Используется, если адрес не содержит данных пользователя или порта.
URI(String scheme, String authority, String path,
    String query, String fragment)
    throws URISyntaxException

//✅ Создаёт URI с указанием схемы, пути и фрагмента (для простых относительных ссылок).
URI(String scheme, String path, String fragment)
    throws URISyntaxException
```
💬 В отличие от `URL`, конструктор `URI`==**никогда НЕ открывает соединение**,== 
он только проверяет корректность синтаксиса строки (по RFC 2396 / RFC 3986).

## Методы
### Получение компонентов URI
```java
//✅ Возвращает схему (например, "http", "ftp", "file").
String getScheme()

//✅ Возвращает имя хоста ("example.com").
String getHost()

//✅ Возвращает порт (или -1, если не указан).
int getPort()

//✅ Возвращает путь к ресурсу (например, "/docs/index.html").
String getPath()

//✅ Возвращает часть с запросом ("x=1&y=2").
String getQuery()

//✅ Возвращает якорь (фрагмент после '#').
String getFragment()

//✅ Возвращает userInfo (например, "user:pass"), если указано.
String getUserInfo()

//✅ Возвращает authority (например, "user:pass@host:port").
String getAuthority()

//✅ Возвращает полную строку URI.
String toString()
```

### Сравнение, анализ и преобразование
```java
//✅ Проверяет, является ли URI абсолютным (содержит схему).
boolean isAbsolute()

//✅ Проверяет, является ли URI относительным.
boolean isOpaque()

//✅ Возвращает новый URI, разрешая относительный путь относительно текущего.
URI resolve(URI relative)

//✅ Возвращает относительный URI (разность между двумя URI).
URI relativize(URI uri)

//✅ Сравнивает два URI лексикографически.
int compareTo(URI uri)

//✅ Проверяет равенство двух URI (с учётом нормализации).
boolean equals(Object obj)
```

### Работа с нормализацией и кодированием
```java
//✅ Возвращает новый URI с удалёнными избыточными компонентами (./, ../).
URI normalize()

//✅ Возвращает строку, пригодную для безопасной передачи в URL (percent-encoding).
String toASCIIString()

//✅ Возвращает декодированное представление URI.
String getRawPath()
String getRawQuery()
String getRawFragment()
String getRawAuthority()
```
💬 Методы с `getRaw...()` возвращают "сырые" версии строк без декодирования символов (`%20`, `%2F` и т.п.).

---

## 💡 Особенности и подводные камни 💡

- `URI` — **чисто синтаксическая** сущность: не выполняет сетевых операций.
    
- 💥 Если строка содержит недопустимые символы (пробелы, кириллицу и т.д.),  
    нужно использовать **percent-encoding** (`%20`, `%D0%AF` и т.д.) или класс `URLEncoder`.
    
- 💡`URI.resolve()` полезен при построении относительных ссылок, особенно в HTML-парсерах.
    
- 💡`URI` часто используется совместно с `Path` (NIO) и `HttpClient`.
    
- 🚫 `URI` не проверяет доступность ресурса — только правильность синтаксиса.
    
- 🧠 При сравнении `URI` учитывает точную форму (например, заглавные буквы в хосте могут отличаться).

---
## 💎 Best Practices 💎

✅ При создании URI всегда **обрабатывай `URISyntaxException`**.  
✅ Используй `normalize()` для очистки путей перед сравнением или логированием.  
✅ Для относительных ссылок применяй `resolve()` и `relativize()`:
```java
URI base = new URI("https://example.com/docs/");
URI full = base.resolve("page.html");
// → ❓https://example.com/docs/page.html❓
```

✅ Для безопасной передачи URI в HTTP-заголовках используй `toASCIIString()` (кодирует Unicode).  
✅ При построении URI с параметрами используй **`URIBuilder`** (внешние библиотеки, например Apache HttpComponents) или формируй строку вручную с `URLEncoder`.

