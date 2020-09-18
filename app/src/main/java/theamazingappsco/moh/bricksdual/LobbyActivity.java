package theamazingappsco.moh.bricksdual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class LobbyActivity extends AppCompatActivity {

    Button btnJoin,btnCreate;
    EditText etJoin,etCreate;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        btnJoin = (Button)findViewById(R.id.btnJoinRoom);
        btnCreate = (Button)findViewById(R.id.btnCreateRoom);
        etJoin = (EditText)findViewById(R.id.etJoinRoom);
        etCreate = (EditText)findViewById(R.id.etCreateRoom);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomId = etCreate.getText().toString();
                Log.d("MD","Create button clicked with "+roomId);

                mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Games").child(roomId);
                DatabaseReference newPost = mFirebaseDatabase.push();

                ArrayList<String> get3words = find3Words();


                newPost.child("Game1").setValue(get3words.get(0));
                newPost.child("Game2").setValue(get3words.get(1));
                newPost.child("Game3").setValue(get3words.get(2));

            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomId = etJoin.getText().toString();
                Log.d("MD","Join button clicked with "+roomId);

                mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Games").child(roomId);
                Log.d("MD","mFirebaseDatabase is "+mFirebaseDatabase);

                mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String Word1, Word2, Word3;
                        Log.d("MD", "dataSnapshot is " + dataSnapshot);
                        if(dataSnapshot.exists()) {
                            HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                            for (String key : dataMap.keySet()) {

                                Object data = dataMap.get(key);

                                HashMap<String, Object> userData = (HashMap<String, Object>) data;
                                ArrayList<String> threeWords = new ArrayList<String>();

                                //User mUser = new User((String) userData.get("name"), (int) (long) userData.get("age"));

                                Word1 = (String) userData.get("Game1");
                                Word2 = (String) userData.get("Game2");
                                Word3 = (String) userData.get("Game3");

                                threeWords.add(Word1);
                                threeWords.add(Word2);
                                threeWords.add(Word3);

                                Log.d("MD", "Word1 is " + Word1);
                                Log.d("MD", "Word2 is " + Word2);
                                Log.d("MD", "Word3 is " + Word3);

                                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                                i.putExtra("mylist", threeWords);
                                startActivity(i);
                                finish();

                            }
                        }else {
                            Log.d("MD", "NO room found");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private ArrayList<String> find3Words() {
        ArrayList<String> wordsList = new ArrayList<String>(Arrays.asList("able","acid","aged","also","area","army","away","baby","back","ball",
                "band","bank","base","bath","bear","beat","been","beer","bell","belt","best","bill","bird","blow","blue","boat","body",
                "bomb","bond","bone","book","boom","born","boss","both","bowl","bulk","burn","bush","busy","call","calm","came","camp",
                "card","care","case","cash","cast","cell","chat","chip","city","club","coal","coat","code","cold","come","cook","cool",
                "cope","copy","core","cost","crew","crop","dark","data","date","dawn","days","dead","deal","dean","dear","debt","deep",
                "deny","desk","dial","dick","diet","disc","disk","does","done","door","dose","down","draw","drew","drop","drug","dual",
                "duke","dust","duty","each","earn","ease","east","easy","edge","else","even","ever","evil","exit","face","fact","fail",
                "fair","fall","farm","fast","fate","fear","feed","feel","feet","fell","felt","file","fill","film","find","fine","fire",
                "firm","fish","five","flat","flow","food","foot","ford","form","fort","four","free","from","fuel","full","fund","gain",
                "game","gate","gave","gear","gene","gift","girl","give","glad","goal","goes","gold","golf","gone","good","gray","grew",
                "grey","grow","gulf","hair","half","hall","hand","hang","hard","harm","hate","have","head","hear","heat","held","hell",
                "help","here","hero","high","hill","hire","hold","hole","holy","home","hope","host","hour","huge","hung","hunt","hurt",
                "idea","inch","into","iron","item","jury","join","jean","jean","join","jump","jury","just","keen","keep","kent","kept",
                "kick","kill","kind","king","knee","knew","know","lack","lady","laid","lake","land","lane","last","late","lead","left",
                "less","life","lift","like","line","link","list","live","load","loan","lock","logo","long","look","lord","lose","loss",
                "lost","love","luck","made","mail","main","make","male","many","mark","mass","matt","meal","mean","meat","meet","menu",
                "mere","mike","mile","milk","mill","mind","mine","miss","mode","mood","moon","more","most","move","much","must","name",
                "navy","near","neck","need","news","next","nice","nick","nine","none","nose","note","okay","once","only","onto","open",
                "oral","over","pace","pack","page","paid","pain","pair","palm","park","part","pass","past","path","peak","pick","pink",
                "pipe","plan","play","plot","plug","plus","poll","pool","poor","port","post","pull","pure","push","race","rail","rain",
                "rank","rare","rate","read","real","rear","rely","rent","rest","rice","rich","ride","ring","rise","risk","road","rock",
                "role","roll","roof","room","root","rose","rule","rush","ruth","safe","said","sake","sale","salt","same","sand","save",
                "seat","seed","seek","seem","seen","self","sell","send","sent","sept","ship","shop","shot","show","shut","sick","side",
                "sign","site","size","skin","slip","slow","snow","soft","soil","sold","sole","some","song","soon","sort","soul","spot",
                "star","stay","step","stop","such","suit","sure","take","tale","talk","tall","tank","tape","task","team","tech","tell",
                "tend","term","test","text","than","that","them","then","they","thin","this","thus","till","time","tiny","told","toll",
                "tone","tony","took","tool","tour","town","tree","trip","true","tune","turn","twin","type","unit","upon","used","user",
                "vary","vast","very","vice","view","vote","wage","wait","wake","walk","wall","want","ward","warm","wash","wave","ways",
                "weak","wear","week","well","went","were","west","what","when","whom","wide","wife","wild","will","wind","wine","wing",
                "wire","wise","wish","with","wood","word","wore","work","yard","yeah","year","your","zero","zone","week"));



        ArrayList<String> my3Words = new ArrayList<String>();
        for(int i=0;i<=2;i++) {
            Random r = new Random();
            int check = 1;
            while (check == 1) {
                String originalWord = wordsList.get(r.nextInt(500)).toLowerCase();
                if (isUnique(originalWord)) {
                    check = 0;
                    my3Words.add(originalWord);
                }
                else
                    check = 1;
            }
        }

        return my3Words;
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

}
