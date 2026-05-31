# WebView Google OAuth Integration для Desktop

## Обзор

Desktop приложение теперь использует встроенный WebView для загрузки фронтенда. Это позволяет:
- ✅ Использовать существующую логику авторизации фронтенда (включая Google OAuth)
- ✅ Автоматически передавать JWT токен из фронтенда в десктоп приложение
- ✅ Не требует изменений на backend
- ✅ Не требует локального HTTP сервера

## Архитектура

```
Desktop App (JavaFX)
    ↓
WebView загружает фронтенд
    ↓
Пользователь логинится через Google (или email/password)
    ↓
Фронтенд сохраняет токен в localStorage
    ↓
AuthContext отправляет токен через JavaScript bridge
    ↓
Desktop получает токен и автоматически авторизуется
```

## Конфигурация

### 1. Backend (без изменений)

Убедитесь, что Google OAuth настроен:

```bash
export GOOGLE_CLIENT_ID="your-client-id.apps.googleusercontent.com"
export GOOGLE_CLIENT_SECRET="your-client-secret"
```

### 2. Frontend (React)

Фронтенд должен быть доступен по адресу, который указан в десктопе:

```bash
# Запуск фронтенда
cd frontend
npm run dev
# Доступен на http://localhost:3000
```

### 3. Desktop App

При запуске десктопа указать URL фронтенда:

```bash
# По умолчанию ищет на localhost:3000
java -jar desktop.jar

# Или указать кастомный URL
java -Dstudent.tracker.frontendUrl=http://localhost:3000 -jar desktop.jar

# И URL backend (если не localhost:8080)
java -Dstudent.tracker.baseUrl=http://localhost:8080 -jar desktop.jar
```

## Как это работает

### 1. Запуск приложения
```
Desktop App запускается
    ↓
WebView загружает http://localhost:3000
```

### 2. Авторизация пользователя
```
Пользователь видит страницу логина фронтенда внутри приложения
Может выбрать:
  - Вход через Google (открывает браузер для OAuth)
  - Вход через email/password
  - Регистрация
```

### 3. Передача токена
```
После успешной авторизации на фронтенде:
  - Токен сохраняется в localStorage
  - AuthContext.jsx обнаруживает, что window.desktopApp доступен
  - Вызывает window.desktopApp.sendAuthToken(token, userId)
  - Java приложение получает токен через JavaScript bridge
```

### 4. Автоматическая авторизация
```
Desktop приложение:
  - Получает токен от JavaScript bridge
  - Загружает профиль пользователя
  - Показывает главный интерфейс приложения
```

## JavaScript Bridge

Java код инъектирует объект `window.desktopApp` во фронтенд:

```javascript
// Это доступно только в WebView, не в обычном браузере
window.desktopApp.sendAuthToken(token, userId)
window.desktopApp.sendAuthError(error)
```

Это реализовано в файле:
- `LoginWebViewController.java` - создает WebView и инъектирует bridge
- `AuthContext.jsx` - обнаруживает bridge и отправляет токен

## Тестирование

### 1. Запустить backend
```bash
cd backend
mvn spring-boot:run
```

### 2. Запустить frontend
```bash
cd frontend
npm run dev
```

### 3. Запустить desktop app
```bash
cd desktop
mvn javafx:run
```

### 4. Авторизация
- В WebView должна загрузиться страница логина
- Нажмите "Sign in with Google" или введите учетные данные
- После авторизации токен автоматически передастся на десктоп
- Должна загрузиться главная страница приложения

## Отладка

### Проверить загрузку WebView
В консоли Java приложения должны быть логи:
```
Загрузка фронтенда с: http://localhost:3000
Получен токен авторизации от фронтенда
```

### Проверить JavaScript bridge
Откройте консоль браузера (F12) и проверьте:
```javascript
console.log(window.desktopApp)  // Должен быть объект
```

### Если фронтенд не загружается
```
1. Проверьте что фронтенд работает на http://localhost:3000
2. Проверьте CORS настройки если нужны
3. Проверьте логи консоли браузера (F12)
```

### Если токен не передается на десктоп
```
1. Проверьте что пользователь авторизован (токен в localStorage)
2. Проверьте консоль браузера на ошибки
3. Проверьте консоль Java приложения на ошибки
```

## Безопасность

1. **WebView работает локально** - нет внешних подключений
2. **JavaScript bridge ограничен** - только для передачи токена
3. **Токен передается только после успешной авторизации**
4. **Стандартный JWT механизм** - все остальные запросы через Authorization header

## Отличия от веб-версии

### Веб версия
- Хранит токен в localStorage
- Отправляет токен в Authorization header при каждом запросе

### Desktop версия
- Загружает фронтенд в WebView внутри приложения
- Фронтенд работает точно так же, как в браузере
- Токен передается на десктоп через JavaScript bridge
- Десктоп использует токен для всех запросов к backend

## Будущие улучшения

1. Использовать системное хранилище для токенов (KeyStore)
2. Добавить оффлайн режим
3. Кеширование на десктопе для лучшей производительности
4. Полная интеграция с ОС (системные уведомления, меню и т.д.)
