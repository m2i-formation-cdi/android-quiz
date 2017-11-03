package app.m2i.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SolutionActivity extends Activity implements View.OnClickListener {

    private int questionIndex;
    private boolean questionAnswer;

    private Button backButton, cheatButton;
    private TextView questionTextView, answerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        //Récupération des données passée dans l'intention
        Bundle params = this.getIntent().getExtras();
        questionIndex = params.getInt("questionIndex");
        questionAnswer = params.getBoolean("questionAnswer");

        //Récupération des éléments d'interface
        questionTextView = (TextView) findViewById(R.id.solutionQuestionTextView);
        answerTextView = (TextView) findViewById(R.id.solutionTextView);
        backButton = (Button) findViewById(R.id.solutionBackButton);
        cheatButton = (Button) findViewById(R.id.solutionCheatButton);

        //Liaison evénementielle
        backButton.setOnClickListener(this);
        cheatButton.setOnClickListener(this);

        //Affichage de la triche
        questionTextView.setText("La solution à la question "
                + (questionIndex +1)+ " est :");
        if(questionAnswer){
            answerTextView.setText("VRAI");
        } else {
            answerTextView.setText("FAUX");
        }
    }

    @Override
    public void onClick(View v) {
        //Création d'une intention
        Intent resultIntention = new Intent();
        //Passage de données
        resultIntention.putExtra("questionIndex", questionIndex);
        resultIntention.putExtra("superTriche", v==cheatButton);
        //Définition du résultat
        setResult(RESULT_OK, resultIntention);
        //Envoie de l'intention à l'activité principale
        finish();

    }
}
