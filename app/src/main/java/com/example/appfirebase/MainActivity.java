package com.example.appfirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText etName, etUsername, etPassword;
    Button btSave, btnSearch, btnEdit, btnDelete;
    String idUserFind, UsernameFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        btSave = findViewById(R.id.btnSave);
        btnSearch = findViewById(R.id.btnSearch);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etUsername.getText().toString().isEmpty() && !etName.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    if (!UsernameFind.equals(etUsername.getText().toString())) {
                        //Busqueda de username
                        db.collection("users").whereEqualTo("username", etUsername.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Usuario existe, Intentelo con otro...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Usuario No existe!...Inténtelo con otro", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verificar que el nombre del usuario se haya digitado
                if (!etUsername.getText().toString().isEmpty()) {
                    //Busqueda de usuario en la coleccion users
                    db.collection("users")
                            .whereEqualTo("username", etUsername.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().isEmpty()) {
                                            //Encontrar el documento con el username especifico
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                UsernameFind = document.getString("username");
                                                idUserFind = document.getId(); //devuelve id del documento
                                                //Asignar el contenido de cada campo a su control respectivo
                                                etName.setText(document.getString("name"));
                                                //etPassword.setText(document.getString("password"));
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Usuario No existe, Intentelo con otro...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario No existe!...Inténtelo con otro", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verificar que el nombre del usuario se haya digitado
                if (!etUsername.getText().toString().isEmpty() && !etName.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    //Busqueda de usuario en la coleccion users
                    db.collection("users")
                            .whereEqualTo("username", etUsername.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().isEmpty()) {
                                            // No encontrar el documento con el username especifico
                                            // Create a new user with a first and last name
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("name", etName.getText().toString());
                                            user.put("username", etUsername.getText().toString());
                                            user.put("password", etPassword.getText().toString());

                                            // Add a new document with a generated ID
                                            db.collection("users")
                                                    .add(user)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getApplicationContext(), "Usuario creado correctamente con id: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                                                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(), "Error al crear el usuario: " + e, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Usuario existe, Intentelo con otro...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Debe ingresar todos los datos... ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}