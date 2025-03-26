# UI Benchmark Framework

Фреймворк для измерения и сравнения производительности различных подходов к разработке UI в Android.

## Новый подход: разделенное тестирование UI

В новой версии бенчмарки разделены по типам UI для более удобного и изолированного тестирования. Подробная информация о новом подходе находится в [документе о разделенном тестировании](README_SEPARATE_UI_TESTS.md).

## Описание

Этот модуль предоставляет инструменты для измерения различных метрик производительности UI в Android-приложениях:

* Время инициализации UI (холодный, теплый и горячий старт)
* Использование памяти
* Время реакции на изменение данных
* Сложность кода (количество строк, цикломатическая сложность)
* Производительность при прокрутке списков
* Производительность при сложных анимациях

Бенчмарки предназначены для сравнения трех подходов к разработке UI:
* Jetpack Compose (`app-compose`)
* Традиционный XML-подход (`app-xml`)
* BDUI (Библиотека UI на основе данных, `app-bdui`)

## Структура

Проект организован следующим образом:

```
benchmarks/src/androidTest/java/com/research/uibenchmark/
│
├── compose/                       # Тесты для Jetpack Compose
│   ├── ComposeStartupBenchmark.kt # Время запуска Compose UI
│   ├── ComposeMemoryBenchmark.kt  # Использование памяти в Compose
│   └── ...
│
├── xml/                           # Тесты для XML-based UI
│   ├── XmlStartupBenchmark.kt     # Время запуска XML UI
│   └── ...
│
├── bdui/                          # Тесты для Backend-Driven UI
│   ├── BduiStartupBenchmark.kt    # Время запуска BDUI
│   └── ...
│
├── BenchmarkUtils.kt              # Основные утилиты для запуска и измерения бенчмарков
├── BenchmarkSummary.kt            # Компиляция и форматирование результатов тестов
└── UiMetricType.kt                # Типы метрик производительности
```

## Запуск тестов

### Запуск для конкретной UI-реализации

```bash
# Для Compose
./gradlew benchmarks:connectedBenchmarkAndroidTest -Pandroid.testInstrumentationRunnerArguments.package=com.research.uibenchmark.compose

# Для XML
./gradlew benchmarks:connectedBenchmarkAndroidTest -Pandroid.testInstrumentationRunnerArguments.package=com.research.uibenchmark.xml

# Для BDUI
./gradlew benchmarks:connectedBenchmarkAndroidTest -Pandroid.testInstrumentationRunnerArguments.package=com.research.uibenchmark.bdui
```

### Запуск конкретного типа теста

```bash
# Например, тесты холодного старта для Compose
./gradlew benchmarks:connectedBenchmarkAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.research.uibenchmark.compose.ComposeStartupBenchmark#coldStartup
```

### Подготовка

1. Убедитесь, что нужное приложение (`app-compose`, `app-xml` или `app-bdui`) установлено на устройстве
2. Устройство должно быть подключено с включенным режимом разработчика и отладкой по USB

### Запуск отдельных бенчмарков через Android Studio

1. Откройте класс с нужным тестом (например, `ComposeStartupBenchmark.kt`)
2. Нажмите на зеленый треугольник рядом с классом или методом теста
3. Выберите устройство для запуска

## Интерпретация результатов

После запуска тестов результаты сохраняются в директории `sdcard/Android/data/{package_name}/files/` на устройстве. Основные результаты:

* `benchmark_summary.json` - полные данные бенчмарков в формате JSON
* `benchmark_summary.txt` - человекочитаемый отчет с ключевыми метриками
* `code_complexity_report.txt` - отчет по анализу сложности кода

Для извлечения результатов с устройства:

```bash
adb pull /sdcard/Android/data/com.research.uibenchmark.benchmarks/files/benchmark_summary.txt ./results/
```

## Типы запуска приложения

### Холодный старт (Cold Start)
Приложение запускается с нуля, когда оно не находится в памяти устройства. Процесс приложения не запущен, все его кеши очищены.

### Теплый старт (Warm Start)
Приложение было недавно закрыто, но его процесс все еще находится в памяти. Активности уничтожены, но процесс приложения живет.

### Горячий старт (Hot Start)
Приложение находится в фоновом режиме и быстро выводится на передний план.

## Рекомендации по созданию надежных бенчмарков

1. **Избегайте внешних факторов**:
   - Запускайте тесты при постоянном уровне заряда батареи (предпочтительно подключенное к питанию устройство)
   - Закройте все фоновые приложения
   - Отключите переменную частоту процессора для получения стабильных результатов

2. **Повторение измерений**:
   - Каждый тест должен запускаться несколько раз для получения статистически значимых результатов
   - Используйте `measureRepeated` для автоматического повторения тестов

3. **Изоляция тестов**:
   - Тестируйте только одну UI-реализацию за раз
   - Между тестами убедитесь, что предыдущий тест не влияет на следующий
   - Используйте `pressHome()` для сброса состояния устройства между тестами

## Добавление новых бенчмарков

### Для добавления нового типа бенчмарка:

1. Создайте новый класс в соответствующем пакете (`compose`, `xml` или `bdui`)
2. Унаследуйтесь от `BenchmarkRule`
3. Добавьте необходимые методы для тестирования
4. Используйте `BenchmarkUtils` для выполнения UI-операций

### Пример добавления нового теста:

```kotlin
@LargeTest
@RunWith(AndroidJUnit4::class)
class ComposeNewFeatureBenchmark {
    private val packageName = "com.research.uibenchmark.compose"

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun newFeatureTest() {
        benchmarkRule.measureUiMetric(
            metricType = UiMetricType.YOUR_NEW_METRIC_TYPE,
            packageName = packageName,
            setup = {
                // Код инициализации
            },
            testBlock = {
                // Код теста
            },
            teardown = {
                // Код очистки
            }
        )
    }
}
```
