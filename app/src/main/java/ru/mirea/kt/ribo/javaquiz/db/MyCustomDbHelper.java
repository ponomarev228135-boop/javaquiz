package ru.mirea.kt.ribo.javaquiz.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyCustomDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "quiz.db";

    public static final String TABLE_QUIZ = "quiz";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_QUESTION = "question";

    public static final String COLUMN_ANSWERS = "answers";
    public static final String COLUMN_RIGHT_ANSWER = "right_answer";

    public MyCustomDbHelper(@Nullable Context context,
                            @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory,
                            int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_QUIZ + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_QUESTION + " TEXT,"
                + COLUMN_ANSWERS + " TEXT,"
                + COLUMN_RIGHT_ANSWER + " INTEGER"
                + ")";
        sqLiteDatabase.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}