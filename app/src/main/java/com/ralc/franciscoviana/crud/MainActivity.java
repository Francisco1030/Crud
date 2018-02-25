package com.ralc.franciscoviana.crud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ralc.franciscoviana.crud.modelo.Pessoa;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
   EditText editNome, editEmail;
   ListView listV_dados;

   //Contruir obj de conexão com o banco de dados
   FirebaseDatabase firebaseDatabase;
   DatabaseReference databaseReference;

   //Listagem
    private List<Pessoa> listPessoa = new ArrayList<Pessoa>();
    private ArrayAdapter<Pessoa> arrayAdapterPessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Identificação dos componetes
        editNome = (EditText) findViewById(R.id.editNome);
        editEmail = (EditText) findViewById(R.id.editEmail);
        listV_dados = (ListView) findViewById(R.id.listV_dados);

        //Metodo para inicializar o banco
        inicializaFirebase();

        //Metodo listagem
        eventoDatabase();
    }

    private void eventoDatabase() {
        databaseReference.child("Pessoa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPessoa.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Pessoa p = objSnapshot.getValue(Pessoa.class);
                    listPessoa.add(p);
                }
                arrayAdapterPessoa = new ArrayAdapter<Pessoa>(MainActivity.this,android.R.layout.simple_list_item_1,listPessoa);
                listV_dados.setAdapter(arrayAdapterPessoa);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializaFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Metodo de add
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_novo){
            Pessoa p = new Pessoa();
            p.setId(UUID.randomUUID().toString());
            p.setNome(editNome.getText().toString());
            p.setEmail(editEmail.getText().toString());
            databaseReference.child("Pessoa").child(p.getId()).setValue(p);
            limpaCampus();
            Toast.makeText(MainActivity.this,"Usuário Salvo", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void limpaCampus() {
        editNome.setText("");
        editEmail.setText("");
        editNome.selectAll();
        editNome.requestFocus();
    }

}
