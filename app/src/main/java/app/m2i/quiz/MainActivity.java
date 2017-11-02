package app.m2i.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.m2i.quiz.model.Question;

public class MainActivity extends Activity implements View.OnClickListener {

    //Constante de classe utilisée dans les logs
    private static final String TAG = "MainActivity";

    private Question currentQuestion;
    private int currentQuestionIndex = 0;

    private TextView questionTextView;

    private Button buttonTrue;
    private Button buttonFalse;
    private List<Question> questionList;

    private Button prevButton;
    private Button nextButton;
    private TextView navTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Référence au widget TextView de la question
        questionTextView = (TextView) findViewById(R.id.questionText);

        //Référence aux boutons
        buttonFalse = (Button) findViewById(R.id.buttonAnswerFalse);
        buttonTrue = (Button) findViewById(R.id.buttonAnswerTrue);

        //Référence aux éléments de navigation
        prevButton = (Button) findViewById(R.id.previousButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        navTextView = (TextView) findViewById(R.id.navTextView);

        //Gestion des événements
        buttonFalse.setOnClickListener(this);
        buttonTrue.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

        //Définition de la liste des questions
        questionList = new ArrayList<>();
        questionList.add(new Question("Linus Torwald a inventé la poudre", false));
        questionList.add(new Question("Javascript c'est comme java", false));
        questionList.add(new Question("Python est plus vieux que Java", true));
        questionList.add(new Question("Android tourne sur IOS", false));
        questionList.add(new Question("Bill Gate a inventé MS-DOS", true));

        //Restauration de l'état sauvegardé de l'application
        if(savedInstanceState != null){
            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex");
        }

        //Affichage de la question
        showQuestion();
    }

    private void showQuestion(){
        //Définition de la question en cours
        currentQuestion = questionList.get(currentQuestionIndex);
        //Affichage de la question
        questionTextView.setText(currentQuestion.getText());

        //Affichage du récapitulatif de la navigation
        String navigationText = (currentQuestionIndex + 1) + " sur " + questionList.size();
        navTextView.setText(navigationText);
    }

    @Override
    //Gestion du clic sur les boutons
    public void onClick(View v) {

        if(v == buttonFalse){
            checkAnswer(false);
        } else if(v == buttonTrue){
            checkAnswer(true);
        } else if(v == prevButton){
            goToPreviousQuestion();
        } else if (v == nextButton){
            goToNextQuestion();
        }
    }

    private void goToPreviousQuestion() {
        if(currentQuestionIndex >0){
            currentQuestionIndex --;
            showQuestion();
        }
    }

    private void goToNextQuestion(){
        if(currentQuestionIndex < questionList.size()){
            currentQuestionIndex ++;
            showQuestion();
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

    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
    }
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
    }
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
    }
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
    }
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    //Sauvegarde de l'état de l'application
    public void onSaveInstanceState(Bundle savedInstanceState){
        //Sauvegarde de l'index en cours
        savedInstanceState.putInt("currentQuestionIndex", currentQuestionIndex);

        super.onSaveInstanceState(savedInstanceState);
    }
}
