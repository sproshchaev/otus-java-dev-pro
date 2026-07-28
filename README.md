[![Java](https://img.shields.io/badge/Java-E43222??style=for-the-badge&logo=openjdk&logoColor=FFFFFF)](https://www.java.com/)

# otus-java-dev-pro

**Сергей Прощаев**

[![Email](https://img.shields.io/badge/sproshchaev%40gmail.com-red?logo=gmail&logoColor=white)](mailto:sproshchaev@gmail.com)
[![Website](https://img.shields.io/badge/prosoft.pages.dev-blue?logo=googlechrome&logoColor=white)](https://prosoft.pages.dev)

---

## Демо к вебинару «JMM и гарантии многопоточности»

**Курс:** Java-Pro, модуль «Многопоточность», тема 2
**Формат:** лайв-кодинг, 12 примеров
**Пакет:** `ru.otus.jmm.demo`

Двенадцать самостоятельных классов, каждый с методом `main`. Примеры показывают проблемы
атомарности и видимости, средства их решения (`volatile`, `synchronized`, CAS), взаимную
блокировку и гарантии happens-before.

## Примеры

| № | Имя класса | Слайд | Описание |
|---|------------|-------|----------|
| 1 | `CounterRace` | 26 | Потерянный инкремент — проблема атомарности |
| 2 | `VisibilityFlagDemo` | 14, 17 | Поток не видит чужую запись — проблема видимости |
| 3 | `VolatileFlagDemo` | 24 | `volatile` закрывает видимость |
| 4 | `VolatileCounterDemo` | 24, 26 | `volatile` оставляет атомарность открытой |
| 5 | `CounterSynchronized` | 27 | Критическая секция через `synchronized`-метод |
| 6 | `SynchronizedBlockCounter` | 27 | `synchronized`-блок с отдельным объектом-замком |
| 7 | `DeadlockDemo` | 29 | Взаимная блокировка и остановка программы |
| 8 | `CounterFixed` | 31 | `AtomicInteger` — CAS под капотом |
| 9 | `CasLoopCounter` | 31 | Механика `compareAndSet` и цикл повторных попыток |
| 10 | `StartHappensBeforeDemo` | 21 | Happens-before на вызове `start()` |
| 11 | `CounterJoined` | 32 | Локальные переменные потоков и `join()` |
| 12 | `FinalFieldExample` | 22 | Семантика `final`-поля при публикации объекта |

## Сводная таблица результатов

| Пример | Результат счётчика | Синхронизация | Комментарий |
|---|---|---|---|
| `CounterRace` | меньше ожидаемого | отсутствует | инкременты теряются |
| `VolatileCounterDemo` | меньше ожидаемого | `volatile` | видимость есть, атомарности нет |
| `CounterSynchronized` | точный | монитор | потоки идут по очереди |
| `SynchronizedBlockCounter` | точный | монитор на выбранном фрагменте | секция короче, ожидание меньше |
| `CounterFixed` | точный | CAS | блокировок нет |
| `CasLoopCounter` | точный | CAS вручную | видны повторные попытки |
| `CounterJoined` | точный | `join` в конце | общего состояния нет |

Остальные примеры счётчик не ведут: `VisibilityFlagDemo` и `VolatileFlagDemo` показывают
видимость, `DeadlockDemo` — цену блокировок, `StartHappensBeforeDemo` и `FinalFieldExample` —
гарантии публикации данных.

## Запуск

Требуется JDK 17 или выше.

```bash
# Запуск конкретного примера
./gradlew runDemo -PmainClass=ru.otus.jmm.demo.CounterRace

# Сборка
./gradlew build
```

Каждый класс можно запускать и напрямую из IDE — у всех есть метод `main`.

> Примеры `VisibilityFlagDemo` и `DeadlockDemo` намеренно зависают (демонстрируют проблему);
> они помечены как daemon-потоки и завершаются вместе с JVM.

## Структура репозитория

```
otus-java-dev-pro/
├── README.md
├── build.gradle.kts
├── settings.gradle.kts
└── src/main/java/ru/otus/jmm/demo/
    ├── CounterRace.java
    ├── VisibilityFlagDemo.java
    ├── VolatileFlagDemo.java
    ├── VolatileCounterDemo.java
    ├── CounterSynchronized.java
    ├── SynchronizedBlockCounter.java
    ├── DeadlockDemo.java
    ├── CounterFixed.java
    ├── CasLoopCounter.java
    ├── StartHappensBeforeDemo.java
    ├── CounterJoined.java
    └── FinalFieldExample.java
```
