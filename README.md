# first-test

Автотесты на Java: UI-тесты интернет-магазина [Demo Web Shop](https://demowebshop.tricentis.com) и API-тесты сервиса бронирования [Restful Booker](https://restful-booker.herokuapp.com).

## Стек

Java 17, Gradle (Kotlin DSL), JUnit 5, Selenide, REST Assured, AssertJ, Lombok, Owner, Datafaker, Allure.

## Структура

```
src/test/java
├── webshop                     UI: demowebshop.tricentis.com
│   ├── config                  настройки браузера (Owner) и их перенос в Selenide
│   ├── data                    тестовые данные: User
│   ├── pages                   Page Object, общая шапка сайта в BasePage
│   ├── steps                   переиспользуемые шаги: регистрация
│   ├── tests                   тесты и BaseUiTest
│   └── util                    вложения Allure
└── booking                     API: restful-booker.herokuapp.com
    ├── api                     спека и клиент: пути, методы, токен
    ├── config                  адрес стенда и учётка (Owner)
    ├── dto                     тела запросов и ответов
    ├── extensions              JUnit-расширение: удаление созданных броней после теста
    ├── steps                   предусловия: бронирование существует
    ├── testdata                фабрики тел бронирования
    └── tests                   тесты и BaseApiTest
src/test/resources
├── config                      local / remote / booking .properties
└── invalid-emails.csv          данные параметризованного теста регистрации
```

Принципы:

- Page Object отдаёт данные со страницы, ожидания и расчёты живут в тесте. В пейдже остаются только проверки состояния UI: уведомление, счётчик корзины, текст ошибки.
- API-клиент возвращает `Response` и не делает ассертов, поэтому подходит и для позитивных, и для негативных тестов.
- Тест убирает за собой: принимает параметр `BookingCleaner`, регистрирует в нём созданные брони, и `BookingCleanupExtension` удаляет их после теста. Браузер закрывается после каждого UI-теста.

## Покрытие

| Область | Позитивные | Негативные |
|---|---|---|
| Регистрация | новый пользователь | невалидный email (13 вариантов), занятый email |
| Вход | верные email и пароль | неверный пароль |
| Корзина | товар с процессором: имя, количество, цена, subtotal | — |
| Авторизация API | токен по валидной учётке | неверные логин и пароль, пустые поля, пустое тело |
| Бронирование API | создание с подтверждением через GET, PUT, PATCH, DELETE | обязательные поля, удаление без токена, известные дефекты стенда |

## Запуск

```bash
./gradlew test                      # всё
./gradlew uiTest                    # только UI
./gradlew apiTest                   # только API
./gradlew apiTest -Dtag=negative    # слой + второй тег: positive / negative
```

UI-тесты по умолчанию идут в локальном Chrome. Удалённый запуск в Selenoid:

```bash
./gradlew uiTest -Drun=remote -DremoteLogin=<login> -DremotePassword=<password>
```

Любой ключ из `src/test/resources/config/*.properties` переопределяется системным свойством: `-Dbrowser=firefox`, `-DbrowserSize=1366x768`, `-DbookingUrl=...`.

## Отчёт

```bash
./gradlew allureServe
```

В отчёте: шаги Page Object и API-клиента, запросы и ответы REST Assured, скриншот и консоль браузера после каждого UI-теста, видео при удалённом запуске.
