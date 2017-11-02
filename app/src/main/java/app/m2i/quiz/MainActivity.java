package app.m2i.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.m2i.quiz.model.Question;

public class MainActivity extends Activity implements View.OnClickListener {

    private Question currentQuestion;
    private int currentQuestionIndex = 0;

    private TextView questionTextView;

    private Button buttonTrue;
    private Button buttonFalse;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Référence au widget TextView de la question
        questionTextView = (TextView) findViewById(R.id.questionText);

        //Référence aux boutons
        buttonFalse = (Button) findViewById(R.id.buttonAnswerFalse);
        buttonTrue = (Button) findViewById(R.id.buttonAnswerTrue);

        //Gestion des événements
        buttonFalse.setOnClickListener(this);
        buttonTrue.setOnClickListener(this);

        //Définition de la liste des questions
        questionList = new ArrayList<>();
        questionList.add(new Question("Linus Torwald a inventé la poudre", false));
        questionList.add(new Question("Javascript c'est comme java", false));
        questionList.add(new Question("Python est plus viex que Java", true));
        questionList.add(new Question("Android tourne sur IOS", false));
        questionList.add(new Question("Bill Gate a inventé MS-DOS", true));

        //Définition de la question en cours
        currentQuestion = questionList.get(currentQuestionIndex);
        //Affichage de la question
        showQuestion();
    }

    private void showQuestion(){
        //Affichage de la question
        questionTextView.setText(currentQuestion.getText());
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
