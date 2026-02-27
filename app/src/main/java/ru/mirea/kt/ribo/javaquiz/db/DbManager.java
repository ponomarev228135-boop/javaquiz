package ru.mirea.kt.ribo.javaquiz.db;

import static ru.mirea.kt.ribo.javaquiz.db.MyCustomDbHelper.COLUMN_ANSWERS;
import static ru.mirea.kt.ribo.javaquiz.db.MyCustomDbHelper.COLUMN_ID;
import static ru.mirea.kt.ribo.javaquiz.db.MyCustomDbHelper.COLUMN_QUESTION;
import static ru.mirea.kt.ribo.javaquiz.db.MyCustomDbHelper.COLUMN_RIGHT_ANSWER;
import static ru.mirea.kt.ribo.javaquiz.db.MyCustomDbHelper.TABLE_QUIZ;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.mirea.kt.ribo.javaquiz.model.Question;

public class DbManager {

    private final SQLiteOpenHelper sqLiteOpenHelper;

    public DbManager(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    public void save(Question question) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question.getQuestion());
        values.put(COLUMN_ANSWERS, question.getAnswers());
        values.put(COLUMN_RIGHT_ANSWER, question.getRightAnswer());
        db.insert(TABLE_QUIZ, null, values);
        db.close();
    }

    public Question findById(Integer id) {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUIZ, new String[]{COLUMN_ID, COLUMN_QUESTION, COLUMN_ANSWERS, COLUMN_RIGHT_ANSWER},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        cursor.moveToFirst();

        Question question = new Question(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3));
        question.setId(cursor.getInt(0));
        cursor.close();
        return question;
    }

}