package ru.mirea.kt.ribo.javaquiz.model;

public class Question {
    private final String question;
    private final String answers;
    private final int rightAnswer;
    private int id;

    public Question(String question, String answers, int rightAnswer) {
        this.question = question;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answers='" + answers + '\'' +
                ", right_answer=" + rightAnswer +
                ", id=" + id +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswers() {
        return answers;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }
}
