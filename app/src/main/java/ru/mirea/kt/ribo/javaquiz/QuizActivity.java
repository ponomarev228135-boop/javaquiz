package ru.mirea.kt.ribo.javaquiz;

import static ru.mirea.kt.ribo.javaquiz.db.MyCustomDbHelper.DATABASE_NAME;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mirea.kt.ribo.javaquiz.db.DbManager;
import ru.mirea.kt.ribo.javaquiz.db.MyCustomDbHelper;
import ru.mirea.kt.ribo.javaquiz.model.Question;

public class QuizActivity extends AppCompatActivity {
    private DbManager dbManager;
    private int counter = 0;
    private Boolean[] rightAnswersArr = new Boolean[20];
    private int selectedAnswer = -1;
    private int right_answer = -1;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        dbManager = new DbManager(new MyCustomDbHelper(getApplicationContext(), DATABASE_NAME, null, 1));
        initializeDB();
        Arrays.fill(rightAnswersArr, false);
        findViewById(R.id.start_test).setOnClickListener(v -> {
            findViewById(R.id.start_test).setVisibility(View.GONE);
            findViewById(R.id.questions_layout).setVisibility(View.VISIBLE);
            counter++;
            Question model = dbManager.findById(counter);
            setQuestion(model);
            right_answer = model.getRightAnswer();
            ((RadioGroup) findViewById(R.id.radioGroup)).clearCheck();
        });

        findViewById(R.id.next_question).setOnClickListener(v -> {
            if ( ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Выберите ответ!", Toast.LENGTH_SHORT).show();
            } else {
                if (counter == 20) {
                    Toast.makeText(getApplicationContext(), "Тест завершён!", Toast.LENGTH_SHORT).show();
                    rightAnswersArr[counter - 1] = selectedAnswer == right_answer;
                    resultTest();
                    findViewById(R.id.questions_layout).setVisibility(View.GONE);
                    findViewById(R.id.resultsLayout).setVisibility(View.VISIBLE);
                } else {
                    rightAnswersArr[counter - 1] = selectedAnswer == right_answer;
                    counter++;
                    Question model = dbManager.findById(counter);
                    setQuestion(model);
                    right_answer = model.getRightAnswer();
                    ((RadioGroup) findViewById(R.id.radioGroup)).clearCheck();
                }
            }
        });
        findViewById(R.id.previous_question).setOnClickListener(v -> {
            Log.i("counter", String.valueOf(counter));
            if (counter == 1) {
                Toast.makeText(getApplicationContext(), "Это первый вопрос!", Toast.LENGTH_SHORT).show();
            } else {
                counter--;
                Question model = dbManager.findById(counter);
                setQuestion(model);
                right_answer = model.getRightAnswer();
                ((RadioGroup) findViewById(R.id.radioGroup)).clearCheck();
            }
        });
        findViewById(R.id.restart_test).setOnClickListener(v -> {
            counter = 0;
            Arrays.fill(rightAnswersArr, false);
            findViewById(R.id.start_test).setVisibility(View.VISIBLE);
            findViewById(R.id.resultsLayout).setVisibility(View.GONE);
        });
        ((RadioGroup) findViewById(R.id.radioGroup)).setOnCheckedChangeListener((group, checkedId) -> {
            if (R.id.firstRadio == checkedId) {
                selectedAnswer = 0;
            }
            if (R.id.secondRadio == checkedId) {
                selectedAnswer = 1;
            }
            if (R.id.thirdRadio == checkedId) {
                selectedAnswer = 2;
            }
            if (R.id.fourthRadio == checkedId) {
                selectedAnswer = 3;
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void resultTest() {
        final int rightAnswersCounter = (int) Arrays.stream(rightAnswersArr).filter(item -> item == true).count();
        ((TextView) findViewById(R.id.correct_answers)).setText("Правильных ответов: " + rightAnswersCounter);
        findViewById(R.id.share_img).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            String body = "Поделиться результатами теста";

            intent.putExtra(Intent.EXTRA_TEXT, body);
            String shareMessage = "Правильных ответов: " + rightAnswersCounter;
            intent.putExtra(Intent.EXTRA_TEXT, shareMessage);

            startActivity(Intent.createChooser(intent, "Share using"));
        });
    }

    private void setQuestion(Question model) {
        String[] answers = model.getAnswers().split("\\|");
        ((TextView) findViewById(R.id.question)).setText(model.getQuestion());
        ((RadioButton) findViewById(R.id.firstRadio)).setText(answers[0]);
        ((RadioButton) findViewById(R.id.secondRadio)).setText(answers[1]);
        ((RadioButton) findViewById(R.id.thirdRadio)).setText(answers[2]);
        ((RadioButton) findViewById(R.id.fourthRadio)).setText(answers[3]);
    }

    private void initializeDB() {
        List<Question> questions = new ArrayList<>() {{
            add(new Question("Какой символ используется для однострочного комментария в Java?", "//|/*|#|--", 0));
            add(new Question("Что такое bytecode в Java?", "Исходный код программы|Код, понятный процессору|Промежуточный код для JVM|Сжатый файл .jar", 2));
            add(new Question("Какой оператор проверяет равенство по значению и типу в Java?", "equals()|==|!=|===", 1));
            add(new Question("Что вернет 'Hello'.length()?", "4|5|6|Ошибку компиляции", 1));
            add(new Question("Как создать массив целых чисел?", "int[] arr = {1,2,3};|Array arr = [1,2,3];|int arr = (1,2,3);|int arr = new [1,2,3];", 0));
            add(new Question("Что такое autoboxing?", "Автоматическая упаковка примитивов в объекты|Сжатие файлов .jar|Упаковка классов в пакеты|Автоматическая компиляция", 0));
            add(new Question("Какой метод является точкой входа в Java-программу?", "start()|main()|run()|execute()", 1));
            add(new Question("Что такое checked exception?", "Ошибка времени выполнения|Ошибка компиляции|Исключение, которое нужно обрабатывать|Ошибка JVM", 2));
            add(new Question("Какой класс является суперклассом для всех классов в Java?", "Class|Super|Object|Root", 2));
            add(new Question("Что такое interface?", "Класс без методов|Абстрактный класс|Набор абстрактных методов и констант|Тип данных для графики", 2));
            add(new Question("Какое ключевое слово используется для наследования?", "extends|inherits|implements|super", 0));
            add(new Question("Что такое аннотация @Override?", "Указывает на переопределение метода|Указывает на устаревший метод|Показывает версию класса|Отключает проверки компилятора", 0));
            add(new Question("Какой из этих типов не является примитивом?", "String|int|boolean|char", 0));
            add(new Question("Что такое volatile?", "Модификатор для постоянных переменных|Модификатор для потокобезопасности|Тип данных|Исключение", 1));
            add(new Question("Какой метод вызывается при создании объекта?", "Constructor|new()|create()|init()", 0));
            add(new Question("Что такое Maven?", "Фреймворк для веб-разработки|Инструмент сборки проектов|Библиотека для работы с БД|Среда разработки", 1));
            add(new Question("Что такое JDBC?", "Библиотека для работы с JSON|API для работы с базами данных|Фреймворк для тестирования|Jar Dependency Manager", 1));
            add(new Question("Какой из этих модификаторов доступа самый строгий?", "public|protected|default|private", 3));
            add(new Question("Что такое лямбда-выражения?", "Анонимные функции|Тип данных|Способ обработки исключений|Синтаксис для циклов", 0));
            add(new Question("Как проверить равенство строк в Java?", "str1 == str2|str1 = str2|str1.equals(str2)|str1.compare(str2)", 2));
        }};
        for (Question model : questions) {
            dbManager.save(model);
        }
    }
}
