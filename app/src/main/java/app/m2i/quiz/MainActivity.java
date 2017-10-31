package app.m2i.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.m2i.quiz.model.Question;

public class MainActivity extends Activity implements View.OnClickListener {

    private Question currentQuestion;
    private TextView questionTextView;

    private Button buttonTrue;
    private Button buttonFalse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Création de la question
        currentQuestion = new Question("Java c'est prise de tête ?", true);

        //Référence au widget TextView de la question
        questionTextView = (TextView) findViewById(R.id.questionText);
        //Affichage de la question
        questionTextView.setText(currentQuestion.getText());

        //Référence aux boutons
        buttonFalse = (Button) findViewById(R.id.buttonAnswerFalse);
        buttonTrue = (Button) findViewById(R.id.buttonAnswerTrue);

        //Gestion des événements
        buttonFalse.setOnClickListener(this);
        buttonTrue.setOnClickListener(this);
    }

    @Override
    //Gestion du clic sur les boutons
    public void onClick(View v) {

        if(v == buttonFalse){
            checkAnswer(false);
        } else if(v == buttonTrue){
            checkAnswer(true);
        }
    }

    private void checkAnswer(boolean userAnswer) {
        String message;
        //Définition du message en fonction de la réponse
        if(userAnswer == currentQuestion.getGoodAnswer()){
            message = "Bonne réponse";
        } else {
            message = "Mauvaise réponse";
        }
        //Affichage du message
        TextView questionResult = (TextView) findViewById(R.id.questionResultTextView);
        questionResult.setText(message);

    }
}
