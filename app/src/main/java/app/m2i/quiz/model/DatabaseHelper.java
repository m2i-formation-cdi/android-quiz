package app.m2i.quiz.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "quiz";
    private static final int DB_VERSION = 2;

    private SQLiteDatabase writableDB;
    private SQLiteDatabase readableDB;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.writableDB = getWritableDatabase();
        this.readableDB = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Script SQL de création de la table
        String sql = "CREATE TABLE questions (id INTEGER PRIMARY KEY AUTOINCREMENT, question_text TEXT NOT NULL, good_answer INTEGER NOT NULL)";
        //Exécution de la requête
        db.execSQL(sql);
        //Chargement des données
        loadFixtures(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Suppression de la table
        String sql = "DROP TABLE IF EXISTS questions";
        db.execSQL(sql);

        //Recréation de la table
        onCreate(db);
    }

    /**
     * Chargement des données du quiz
     */
    private void loadFixtures(SQLiteDatabase db){

        //Valeurs à insérer
        ContentValues values = new ContentValues();
        values.put("question_text", "un litre d'eau fait à peu près 100 centi-litres ?");
        values.put("good_answer", true);

        try {
            //Commande d'insertion
            db.insert("questions", null, values);

            //Autre méthode d'insertion avec du code SQL
            String sql = "INSERT INTO questions (question_text, good_answer) VALUES " +
                    "('Tim Cook est le patron de Microsoft ?', 0)," +
                    "('La lune est plus grosse que la Terre', 0)," +
                    "('Donald Trump est président des Etats Unis', 0)," +
                    "('C++ est un langage compilé', 1)," +
                    "('Android a été inventé par Bill gates', 0)";

            db.execSQL(sql);
        }catch (Exception ex){
            Log.d("DatabaseHelper", ex.getMessage());
        }
    }

    /**
     * Liste des questions du quiz
     * @return
     */
    public List<Question> findAllQuestions(){
        List<Question> questionList = new ArrayList<>();

        //Récupération de l'objet SQLiteDatabase
        SQLiteDatabase db = readableDB;

        //La commande SQL
        String sql = "SELECT id, question_text, good_answer FROM questions";

        //Exécution de la commande SQL
        //retourne un objet de type Cursor
        Cursor cursor = db.rawQuery(sql, null);

        //Boucle sur le curseur pour remplir questionList
        while(cursor.moveToNext()){
            //Instanciation d'une nouvelle question
            //et hydratation de l'objet
            Question question = new Question();
            question.setId(cursor.getLong(0));
            question.setText(cursor.getString(1));
            question.setGoodAnswer(cursor.getInt(2) >= 1);

            //Ajout à la liste
            questionList.add(question);
        }

        //Fermeture du cuseur
        cursor.close();

        return questionList;
    }

    /**
     * Suppression dans la base SQLite
     * @param questionId
     */
    public void delete(Long questionId) {
        String [] params  = {String.valueOf(questionId)};
        writableDB.delete("questions", "id=?", params);
    }

    /**
     * Insertion de données dans la base
     * @param questionText
     * @param goodAnswer
     */
    public void insert(String questionText, Boolean goodAnswer) {
        ContentValues values = new ContentValues();
        values.put("question_text", questionText);
        values.put("good_answer", goodAnswer);

        writableDB.insert("questions", null, values);
    }
}
