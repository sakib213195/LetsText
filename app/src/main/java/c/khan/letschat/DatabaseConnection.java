package c.khan.letschat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DatabaseConnection extends AppCompatActivity {


    ListView LiveChats;
    ArrayList<String> ChatList = new ArrayList<String>();
    ArrayAdapter ChatAdpt;
    private static final int RC_PHOTO_PICKER = 2;


    String UserName;

    private DatabaseReference FirebaseDB = FirebaseDatabase.getInstance().getReference().getRoot();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_connection);

        final Button signout = (Button) findViewById(R.id.signout);

        View.OnClickListener handler = new View.OnClickListener(){

            public void onClick(View v) {

                if(v==signout){
                    Intent intentMain = new Intent(DatabaseConnection.this ,
                            SignIn.class);
                    DatabaseConnection.this.startActivity(intentMain);
                }

            }
        };
        signout.setOnClickListener(handler);


        LiveChats = (ListView) findViewById(R.id.LiveChats);
        ChatAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ChatList);
        LiveChats.setAdapter(ChatAdpt);

        getUserName();


        FirebaseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());
                }

                ChatAdpt.clear();
                ChatAdpt.addAll(set);
                ChatAdpt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LiveChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent j = new Intent(getApplicationContext(), ChatActivity.class);

                j.putExtra("selected_topic", ((TextView) view).getText().toString());
                j.putExtra("user_name", UserName);
                startActivity(j);
            }
        });


    }

    @Override
    public void onBackPressed() {

    }

    private void getUserName(){
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText userName = new EditText(this);

        builder.setView(userName);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UserName = userName.getText().toString();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getUserName();

            }
        });

        builder.show();*/

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        String personName = account.getDisplayName();
        UserName = personName;

    }


}