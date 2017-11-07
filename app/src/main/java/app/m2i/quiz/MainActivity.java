package app.m2i.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.m2i.quiz.model.DatabaseHelper;
import app.m2i.quiz.model.Question;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //Constante de classe utilisée dans les logs
    private static final String TAG = "MainActivity";
    private static final int SOLUTION_ACTIVITY = 1;

    private Question currentQuestion;
    private int currentQuestionIndex = 0;

    private TextView questionTextView;

    private Button buttonTrue;
    private Button buttonFalse;
    private List<Question> questionList;

    private Button prevButton;
    private Button nextButton;
    private TextView navTextView;

    private Button solutionButton;
    private Intent intention;

    private Spinner questionSpinner;

    //Déclaration de la variable utilisé pour la synthèse vocale
    private TextToSpeech textToSpeech;

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

        solutionButton = (Button) findViewById(R.id.solutionButton);
        solutionButton.setOnClickListener(this);

        //Référence au widget Spinner
        questionSpinner = (Spinner) findViewById(R.id.questionSpinner);

        //Gestion de l'événement sur le spinner
        questionSpinner.setOnItemSelectedListener(this);

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
        questionList.add(new Question("Christophe Collomb a découvert l'eau tiède", false));


        //Instanciation de la classe TextToSpeech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.FRENCH);
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "La synthèse vocale n'est pas supportée",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Restauration de l'état sauvegardé de l'application
        if(savedInstanceState != null){
            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex");
        }

        //Affichage de la question
        showQuestion();
    }

    private void showQuestion(){

        //Définition du spinner
        //Source de données du spinner
        String[] spinnerData = new String[questionList.size()];
        for(int i =0; i < questionList.size(); i++){
            Question question = questionList.get(i);
            String spinnerText = "Question " + (i+1)+ " ";
            if(question.isAnswered()){
                spinnerText += "(répondue)";
            }

            spinnerData[i] = spinnerText;
        }

        //Création de l'arrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerData);
        //Définition de la vue lorsque le spinner est ouvert
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Attacher l'adapter au spinner
        questionSpinner.setAdapter(adapter);

        questionSpinner.setSelection(currentQuestionIndex);


        //Définition de la question en cours
        currentQuestion = questionList.get(currentQuestionIndex);
        //Affichage de la question
        questionTextView.setText(currentQuestion.getText());

        //Affichage du récapitulatif de la navigation
        String navigationText = (currentQuestionIndex + 1) + " sur " + questionList.size();
        navTextView.setText(navigationText);

        //Lecture de la question avec la synthèse vocale
        textToSpeech.speak(currentQuestion.getText(), TextToSpeech.QUEUE_FLUSH, null);

        //Affichage du résultat de la réponse
        showQuestionResult();
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
        } else if(v== solutionButton){
            intention = new Intent(this, SolutionActivity.class);
            //Transfert d'info à l'activité solution
            intention.putExtra("questionIndex", currentQuestionIndex);
            intention.putExtra("questionAnswer", currentQuestion.getGoodAnswer());
            startActivityForResult(intention, SOLUTION_ACTIVITY);
        }
    }

    private void goToPreviousQuestion() {
        if(currentQuestionIndex >0){
            currentQuestionIndex --;
            questionSpinner.setSelection(currentQuestionIndex);
            showQuestion();
        }
    }

    private void goToNextQuestion(){
        if(currentQuestionIndex < questionList.size()-1){
            currentQuestionIndex ++;
            questionSpinner.setSelection(currentQuestionIndex);
            showQuestion();
        }
    }

    private void checkAnswer(boolean userAnswer) {
        currentQuestion.setUserAnswer(userAnswer);
        showQuestionResult();
    }

    private void showQuestionResult(){
        String message;
        //Définition du message en fonction de la réponse
        Boolean userAnswer = currentQuestion.getUserAnswer();

        if(userAnswer == null){
            message = "";
        }else if(userAnswer == currentQuestion.getGoodAnswer()){
            message = "Bonne réponse";
        } else {
            message = "Mauvaise réponse";
        }

        //Affichage du message
        TextView questionResult = (TextView) findViewById(R.id.questionResultTextView);
        questionResult.setText(message);

        //Activation/désactivation des boutons de réponse
        buttonFalse.setEnabled(! currentQuestion.isAnswered());
        buttonTrue.setEnabled(! currentQuestion.isAnswered());
        //Activation/désactivation des boutons de navigation
        prevButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(currentQuestionIndex < questionList.size()-1);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //L'index de la question en cours doit correspondre à l'index de la ligne sélectionnée dans le spinner
        currentQuestionIndex = position;

        //Affichage du texte de la ligne sélectionnée
        Log.d(TAG, parent.getItemAtPosition(position).toString());
        showQuestion();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onActivityResult (int requestCode, int resultCode, Intent data){
        boolean superTriche = data.getBooleanExtra("superTriche", false);
        long questionId = data.getLongExtra("questionIndex", 0);
        currentQuestionIndex = (int)questionId;
        showQuestion();
        if(superTriche){
            checkAnswer(currentQuestion.getGoodAnswer());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.optionsLoadJson:

                break;
            case R.id.optionsLoadSQL:
                //Changement de la liste des questions
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                questionList = dbHelper.findAllQuestions();

                //Réinitialisation de l'index de la question en cours
                currentQuestionIndex = 0;
                //affichage de la question
                showQuestion();
                break;
        }
        return true;
    }
}
