package theamazingappsco.moh.bricksdual;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity3 extends AppCompatActivity {
    TextView lblCounter,lblAnswer;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    EditText txtWord;
    Button btnSend,btnPlayAgain,btnExtra;
    int counter=9;
    String originalWord;
    ArrayList<String> threeWords;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        threeWords = (ArrayList<String>) getIntent().getSerializableExtra("mylist");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getSupportActionBar().setTitle("Round 3");
        originalWord = threeWords.get(2);
        Log.d("MD", "Current word is :" + originalWord);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<DataModel>();

        adapter = new UserTextAdapter(data);
        recyclerView.setAdapter(adapter);

        lblAnswer = (TextView) findViewById(R.id.lblAnswer);
        lblCounter = (TextView) findViewById(R.id.lblCounter);
        txtWord = (EditText) findViewById(R.id.txtText);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnPlayAgain = (Button) findViewById(R.id.btnPlayAgain);
        btnExtra = (Button) findViewById(R.id.extraLife);
        //  btnAlpha = (Button) findViewById(R.id.Alpha);
        btnPlayAgain.setVisibility(View.INVISIBLE);

        lblAnswer.setVisibility(View.INVISIBLE);
        lblAnswer.setText("The Word was: " + originalWord);


//        btnAlpha.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //split the word then find number of red bricks and blue bricks and then decrement counter by 1;
                String myText = txtWord.getText().toString().toLowerCase();
                if (myText.matches("[a-zA-Z]+")) {
                    if (isUnique(myText)) {
                        if (myText.length() == 4) {
                            lblCounter.setText(Integer.toString(counter--));
                            txtWord.setText("");
                            changeColor();
                            String cards = getCards(myText, originalWord);
                            if (cards.contains("123")) {
                                btnSend.setVisibility(View.INVISIBLE);
                                data.add(new DataModel(cards));
//                                listItems.add("You have guessed the right word : " + myText + " Congratulations");
                                txtWord.setVisibility(View.INVISIBLE);
                                lblAnswer.setVisibility(View.VISIBLE);
                                lblAnswer.setTextColor(Color.GREEN);
                                lblAnswer.setText(originalWord + " is correct");
                                btnPlayAgain.setVisibility(View.VISIBLE);
                                btnPlayAgain.setText("View Score");
                                recyclerView.setVerticalScrollBarEnabled(Boolean.TRUE);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(btnSend.getWindowToken(), 0);
                            } else {
//                                listItems.add(cards);
//                                adapter.notifyDataSetChanged();
                                data.add(new DataModel(cards));
                                //recyclerView.scheduleLayoutAnimation();
                                adapter.notifyDataSetChanged();
                            }
                            txtWord.onEditorAction(EditorInfo.IME_ACTION_DONE);

                            if (counter == -1 && !(cards.contains("123"))) {
                                btnSend.setVisibility(View.INVISIBLE);
                                lblAnswer.setVisibility(View.VISIBLE);
                                lblAnswer.setTextColor(Color.RED);
                                btnPlayAgain.setText("View Score");
                                btnPlayAgain.setVisibility(View.VISIBLE);
                                btnExtra.setVisibility(View.INVISIBLE);
                                txtWord.setVisibility(View.INVISIBLE);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(btnSend.getWindowToken(), 0);
                            }

                        } else {
                            Toast.makeText(GameActivity3.this, "Should be a valid 4 letter word",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (myText.equals("shreetha")) {
                            getSupportActionBar().setTitle("hmm..");
                            txtWord.setText("");
                        } else {
                            Toast.makeText(GameActivity3.this, "Letters cannot be repeated",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(GameActivity3.this, "Should contain Valid Letters",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        //Here

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MD","Going to scoreboard");
                setScore();
                Intent i = new Intent(getApplicationContext(), ScoreBoard.class);
                i.putExtra("mylist", threeWords);
                startActivity(i);
                finish();
            }
        });

    }

    private void setScore() {
        int totalScore = Integer.parseInt(threeWords.get(5));
        int currentScore = Integer.parseInt(lblCounter.getText().toString())+2;
        Log.d("MD","currentScore is "+currentScore);
        totalScore = totalScore+currentScore;
        Log.d("MD","totalScore is "+totalScore);
        threeWords.set(5,""+totalScore);
    }

    public String getCards(String input,String Original){
        String cardString;
        char inputA[] = input.toCharArray();
        char mywordA[] = Original.toCharArray();
        int rb=0,gb=0;
        for(int i=0;i<4;i++) {

            if (mywordA[i] == inputA[i]) {
                gb++;
                continue;
            } else {
                if ((input.indexOf(mywordA[i])) >= 0) {
                    rb++;
                    continue;
                }
            }
        }
        String redString = ","+Integer.toString(rb);
        String greenString = ","+Integer.toString(gb);

        cardString = redString+greenString;

        String finalSting = input + cardString;

        if(gb==4)
            finalSting = finalSting + ",123";

        return finalSting;
    }
    public static boolean isUnique(String str) {
        if (str.length() == 0) {
            return true;
        }

        boolean[] seen = new boolean[26];
        for (int i = 0; i < str.length(); i++) {
            int index = Character.toLowerCase(str.charAt(i)) - 'a';
            if (seen[index]) {
                return false;
            }
            else {
                seen[index] = true;
            }
        }
        // invariant
        return true;
    }

    public void changeColor(){
        if((counter<=100) && (counter>=5) ){
            lblCounter.setTextColor(Color.GREEN);
        }
        if((counter<=4) && (counter>=3) ){
            lblCounter.setTextColor(Color.YELLOW);
        }
        if((counter<3)) {
            lblCounter.startAnimation(shakeError());
            lblCounter.setTextColor(Color.RED);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent i = new Intent(getApplicationContext(),GameActivity.class);
            startActivity(i);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.helpmenu, menu);
        return true;
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }


}
