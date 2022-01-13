package theamazingappsco.moh.bricksdual;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreBoard extends AppCompatActivity {

    TextView txtMyName,txtMyScore,txtOppName,txtOppScore;
    ArrayList<String> threeWords;
    Button delRoom;
    String roomID;
    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        threeWords = (ArrayList<String>) getIntent().getSerializableExtra("mylist");

        txtMyName = (TextView)findViewById(R.id.txtMyName);
        txtMyScore = (TextView)findViewById(R.id.txtMyScore);
        txtOppName = (TextView)findViewById(R.id.txtOppName);
        txtOppScore = (TextView)findViewById(R.id.txtOppScore);
        delRoom = (Button)findViewById(R.id.btnDltRoom);
        delRoom.setVisibility(View.INVISIBLE);


        uploadAndSetMyScore();
        getOppScore();
    }

    private void getOppScore() {
        String name,score,roomId;

        Log.d("MD",""+threeWords);
        roomId = threeWords.get(3);
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Games").child(roomId);
        Log.d("MD", "mFirebaseDatabase is " + mFirebaseDatabase);

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mFirebaseDatabase.getRef()==null){
                    Log.d("MD","Database delted");
                }
                else{
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);

                        HashMap<String, Object> userData = (HashMap<String, Object>) data;
                        Log.d("MD","userData: "+userData);
                        if(userData.get("Game1")==(null) && (userData.get(threeWords.get(4)) == null)) {
                            String scoreString = userData.toString().split("=")[1];
                            String name = scoreString.split(",")[0];
                            String score = scoreString.split(",")[1].replace("}","");
                            txtOppName.setText(name);
                            txtOppScore.setText(score);
                            deleteRoom();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deleteRoom() {
        delRoom.setVisibility(View.VISIBLE);
        delRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseDatabase.removeValue();
            }
        });
    }

    private void uploadAndSetMyScore() {
        String name,score;
        name = threeWords.get(4);
        score = threeWords.get(5);
        txtMyName.setText(name);
        txtMyScore.setText(score);
        Log.d("MD",""+threeWords);

        roomID = threeWords.get(3);
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Games").child(roomID);
        DatabaseReference newPost = mFirebaseDatabase.push();
        newPost.child(""+name).setValue(name+","+score);
    }
}
