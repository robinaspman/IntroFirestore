package com.example.introfirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MainActivity";
    private EditText enterTitle, enterThought;
    private Button saveButton, showButton, updateTitle, deleteThought;
    private TextView recTitle;

    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHT = "thought";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference journalRef = db.document("Journal/First Thoughts");
    private CollectionReference collectionReference = db.collection("Journal");

//    private DocumentReference journalRef = db.document("Journal")
//            .getFirestore().document("First Thoughts");
//This didn't work "Document references must have an even number of segments, but Journal has 1"

//StackOverflow "Document ids can't have slashes, since they are interpreted as dividers between
// collections and documents when forming the "path" to a document."

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = findViewById(R.id.save_button);
        updateTitle = findViewById(R.id.update_data);
        enterTitle = findViewById(R.id.edit_text_title);
        enterThought = findViewById(R.id.edit_text_thought);
        showButton = findViewById(R.id.show_dataButton);
        recTitle = findViewById(R.id.rec_title);
        deleteThought = findViewById(R.id.delete_thought);

        updateTitle.setOnClickListener(this);
        deleteThought.setOnClickListener(this);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getThoughts();
//                journalRef.get()
//                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                if (documentSnapshot.exists()) {
//
//                                    Journal journal = documentSnapshot.toObject(Journal.class);
////                                    String title = documentSnapshot.getString(KEY_TITLE);
////                                    String thought = documentSnapshot.getString(KEY_THOUGHT);
//
//                                    if (journal != null) {
//                                        recTitle.setText(journal.getTitle());
//                                        recThought.setText(journal.getThought());
//                                    }
//
//                                } else {
//                                    Toast.makeText(
//                                            MainActivity.this,
//                                            "No data exists",
//                                            Toast.LENGTH_LONG)
//                                            .show();
//
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "onFailure: " + e.toString());
//                            }
//                        });

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addThought();
//                String title = enterTitle.getText().toString().trim();
//                String thought = enterThought.getText().toString().trim();
//
//                Journal journal = new Journal();
//                journal.setTitle(title);
//                journal.setThought(thought);

//                Map<String, Object> data = new HashMap<>();
//                data.put(KEY_TITLE, title);
//                data.put(KEY_THOUGHT, thought);

//                        journalRef.set(journal)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(MainActivity.this, "Success",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "onFailure: " + e.toString());
//                            }
//                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.d(TAG, "onEvent: " + e.toString());
                }

                String data = "";
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots) {

    //                                    String title = documentSnapshot.getString(KEY_TITLE);
    //                                    String thought = documentSnapshot.getString(KEY_THOUGHT);

    //                            Log.d(TAG, "onSuccess: " + snapshots.getId());
                        Journal journal = snapshots.toObject(Journal.class);

                        data += "Title:" + journal.getTitle() + " \n"
                                + " Thought: " + journal.getThought() + "\n\n";

    //                            recTitle.setText(journal.getTitle());
                    }
                }
                recTitle.setText(data);
            }
        });
//        journalRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
//                                @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    Toast.makeText(MainActivity.this,
//                            "Something went wrong",
//                            Toast.LENGTH_SHORT).show();
//                }
//                if (documentSnapshot != null && documentSnapshot.exists()) {
//
//                    String data = "";
//                    Journal journal = documentSnapshot.toObject(Journal.class);
////                                    String title = documentSnapshot.getString(KEY_TITLE);
////                                    String thought = documentSnapshot.getString(KEY_THOUGHT);
//
//                    data += "Title:" + journal.getTitle() + " \n"
//                            + " Thought: " + journal.getThought();
//                    recTitle.setText(data);
//
//
//                } else {
//                    recTitle.setText("");
//                }
//
//
//            }
//        });
    }

    private void addThought() {
        String title = enterTitle.getText().toString().trim();
        String thought = enterThought.getText().toString().trim();

        Journal journal = new Journal(title, thought);
//        journal.setTitle(title);
//        journal.setThought(thought);

        collectionReference.add(journal)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed" + e.toString());
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_data:
                updateMyTitle();
                break;
            case R.id.delete_thought:
                deleteAll();
                break;
        }

    }

    private void deleteMyThought() {
//        Map<String, Object> data = new HashMap<>();
//        data.put(KEY_THOUGHT, FieldValue.delete());
//        journalRef.update(data);
        journalRef.update(KEY_THOUGHT, FieldValue.delete());
    }

    private void getThoughts() {
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String data = "";
                        for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots) {

//                                    String title = documentSnapshot.getString(KEY_TITLE);
//                                    String thought = documentSnapshot.getString(KEY_THOUGHT);

//                            Log.d(TAG, "onSuccess: " + snapshots.getId());
                            Journal journal = snapshots.toObject(Journal.class);

                            data += "Title:" + journal.getTitle() + " \n"
                                    + " Thought: " + journal.getThought() + "\n\n";

//                            recTitle.setText(journal.getTitle());
                        }
                        recTitle.setText(data);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void deleteAll() {
        journalRef.delete();
    }

    private void updateMyTitle() {
        String title = enterTitle.getText().toString().trim();
        String thought = enterThought.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_TITLE, title);
        data.put(KEY_THOUGHT, thought);

        journalRef.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this,
                        "Updated!",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}