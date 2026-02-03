# 0. Заголовок Authorization
Для авторизации при помощи HTTP, используется **заголовок** `Authorization`, в котором можно указать различные схемы аутентификации

1. **Bearer** — доминирует в современных REST API, мобильных приложениях
    
2. **Basic** — legacy-системы, простые сценарии, внутренние сервисы
    
3. **API Key** — публичные API, документация (OpenAPI/Swagger)
    
4. **AWS4-HMAC-SHA256** — облачная инфраструктура AWS
    
5. **Digest/NTLM** — корпоративные сети, интранет
    
6. **Остальные** — нишевые use-case
## **Основные схемы (типы аутентификации):**
### 1. **Basic** — базовая аутентификация
```
Authorization: Basic dXNlcjpwYXNzd29yZA==
```
Используется для простой передачи логина/пароля в Base64.

### 2. **Bearer** — токен-носитель (самый популярный сегодня)
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
Используется для OAuth 2.0, JWT-токенов. Токен даёт доступ к ресурсам без передачи пароля.

### 3. **Digest** — дайджест-аутентификация
```
Authorization: Digest username="user", realm="test", nonce="...", uri="/", response="..."
```
Более безопасная альтернатива Basic Auth, использует хэши (MD5/SHA) вместо Base64.

### 4. **AWS4-HMAC-SHA256** — AWS Signature Version 4
```
Authorization: AWS4-HMAC-SHA256 Credential=AKIAIOSFODNN7EXAMPLE/20240101/us-east-1/s3/aws4_request, SignedHeaders=host;x-amz-date, Signature=...
```
Используется для аутентификации в сервисах Amazon Web Services.

### 5. **NTLM / Negotiate** — Windows-аутентификация
```
Authorization: NTLM TlRMTVNTUAABAAAAB4IIogAAAAAAAAAAAAAAAAAAAAAGAbEdAAAADw==
```

```
Authorization: Negotiate YIIChgYGKwYBBQUCoII...
```
Используется в корпоративных сетях, Active Directory.

### 6. **Hawk** — аутентификация с подписью запроса
```
Authorization: Hawk id="dh37fgj492je", ts="1353832234", nonce="j4h3g2", mac="6R4rV5iE+NPoym+WwjeHzjAGXUtLNIxmo1vpMofpLAE="
```

Аналогична AWS Signature, но для REST API.

### 7. **API Key** — ключ API (часто кастомная схема)
```
Authorization: ApiKey abc123def456ghi789
```
или
```
Authorization: Token 9944b09199c62bcf9418ad846dd0e4bbdfc6ee4b
```

Многие API используют **свои собственные форматы**.

### 8. **OAuth** — разные версии OAuth
```
Authorization: OAuth oauth_consumer_key="...", oauth_token="...", oauth_signature_method="HMAC-SHA1", oauth_signature="..."
```
Версия 1.0 с подписью каждого запроса.

## **Реже встречающиеся, но существующие:**

### 9. **DPoP** — Demonstration of Proof of Possession
```
Authorization: DPoP eyJhbGciOiJSUzI1Ni...
```
Защита от перехвата токенов в OAuth 2.0.

### 10. **HOBA** — HTTP Origin-Bound Authentication
```
Authorization: HOBA challenge="...", signature="..."
```
Аутентификация **на основе цифровых подписей.**