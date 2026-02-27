package ru.mirea.kt.ribo.javaquiz.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.mirea.kt.ribo.javaquiz.QuizActivity;
import ru.mirea.kt.ribo.javaquiz.R;

public class MainActivity extends AppCompatActivity {

    private static final String ADDRESS = "https://android-for-students.ru/coursework/login.php";

    private String login;

    private String password;

    private String group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logButton = findViewById(R.id.login_button);
        TextView errorTV = findViewById(R.id.logError);
        EditText loginET = findViewById(R.id.login);
        EditText passwordET = findViewById(R.id.password);

        logButton.setOnClickListener(v -> {
            try {
                login = loginET.getText().toString();
                password = passwordET.getText().toString();
                group = "RIBO-03-23";
                Log.i("Login", login);
                Log.i("Password", password);
                Log.i("Group", group);

                Map<String, String> map = new HashMap<>();
                map.put("lgn", login);
                map.put("pwd", password);
                map.put("g", group);

                HTTPRunnable httpRunnable = new HTTPRunnable(ADDRESS, map);
                Thread th = new Thread(httpRunnable);
                th.start();

                try {
                    th.join();
                } catch (InterruptedException exception) {
                    Toast.makeText(getApplicationContext(), "Ошибка!", Toast.LENGTH_LONG).show();
                } finally {
                    try {
                        JSONObject jsonObject = new JSONObject(httpRunnable.getResponseBody());

                        Log.i("Title", "Title: " + jsonObject.getString("title"));
                        Log.i("Task", "Task: " + jsonObject.getString("task"));
                        Log.i("Variant", "Variant: " + jsonObject.getString("variant"));

                        errorTV.setVisibility(View.GONE);

                        Intent loadingPageIntent = new Intent(getApplicationContext(), QuizActivity.class);
                        startActivity(loadingPageIntent);
                    } catch (JSONException exception) {
                        Log.i("MainActivityError", "Error, invalid login or pass");
                        errorTV.setVisibility(View.VISIBLE);
                    }
                }
            } catch (RuntimeException exception) {
                Toast.makeText(getApplicationContext(), "Что-то пошло не так...", Toast.LENGTH_LONG).show();
            }
        });
    }

}