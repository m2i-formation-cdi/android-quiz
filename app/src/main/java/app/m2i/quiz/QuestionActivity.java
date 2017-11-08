package app.m2i.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.m2i.quiz.model.DatabaseHelper;
import app.m2i.quiz.model.Question;

public class QuestionActivity extends Activity implements AdapterView.OnItemClickListener {

    private DatabaseHelper dbHelper;
    private List<String> listData;
    private List<Question> questionList;
    private ListView listView;
    private Long questionId;

    private static final int QUESTION_FORM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Instanciation de DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        //Référence à la listView
        listView = (ListView) findViewById(R.id.questionListView);

        loadQuestions();

        listView.setOnItemClickListener(this);
    }

    private void loadQuestions(){
        //Récupération de la liste des question depuis la BD
        questionList = dbHelper.findAllQuestions();
        //Conversion de la liste de Question en liste de String
        listData = new ArrayList<>();
        for (Question item: questionList){
            listData.add(item.getText());
        }
        //Création de l'ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, listData);

        //Laison entre la liste et la source de données
        listView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        questionId = questionList.get(position).getId();

        Toast.makeText(this,
                "L'id de la question est : " + String.valueOf(questionId),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_crud, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            //Suppression de la question sélectionnée
            case R.id.optionCrudDelete:
                questionDelete();
                break;
            case R.id.optionCrudAdd:
                questionAdd();
                break;
        }

        return true;
    }

    /**
     * Lancement de l'activité pour créer une nouvelle question
     */
    private void questionAdd() {
        Intent intention = new Intent(this, QuizFormActivity.class);
        startActivityForResult(intention, QUESTION_FORM);
    }

    //Suppression d'une question
    private void questionDelete() {
        if(questionId != null){
            dbHelper.delete(questionId);
            //raffraichissement de la liste
            loadQuestions();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Raffraichissement de la liste uniquement si le code de retour est OK
        if(resultCode == RESULT_OK){
            loadQuestions();
        }
    }
}
