package app.m2i.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import app.m2i.quiz.model.DatabaseHelper;

public class QuizFormActivity extends Activity implements View.OnClickListener {


    private Button cancelButton, validateButton;
    private Switch answerSwitch;
    private EditText questionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_form);

        questionEditText = (EditText) findViewById(R.id.questionEditText);
        answerSwitch = (Switch) findViewById(R.id.answerSwitch);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        validateButton = (Button) findViewById(R.id.validateButton);

        validateButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intentionResult = new Intent();

        if(v == validateButton){
            validateForm();
            setResult(RESULT_OK, intentionResult);
        } else if(v == cancelButton){
            setResult(RESULT_CANCELED, intentionResult);
        }

        finish();
    }

    private void validateForm() {
        //Récupération de la saisie
        String questionText = questionEditText.getText().toString();
        Boolean goodAnswer = answerSwitch.isChecked();

        //Insertion dans la base de données avec la classe DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.insert(questionText, goodAnswer);
    }
}
