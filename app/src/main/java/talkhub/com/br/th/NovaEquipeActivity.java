package talkhub.com.br.th;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import talkhub.com.br.th.Entities.Equipe;
import talkhub.com.br.th.Entities.Usuario;

public class NovaEquipeActivity extends AppCompatActivity {


    private EditText mNomeEquipe;
    private EditText mDescEquipe;
    private List<String> administradores = new ArrayList<String>();
    private List<String> membros = new ArrayList<String>();
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_equipe);

        mNomeEquipe = (EditText) findViewById(R.id.et_nome_equipe);
        mDescEquipe = (EditText) findViewById(R.id.et_desc_equipe);
        mRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
        mAuth = FirebaseAuth.getInstance();


        String nomeEquipe = mNomeEquipe.getText().toString();
        String descEquipe = mDescEquipe.getText().toString();
        String usuarioCriador = mAuth.getCurrentUser().getEmail().toString();
        administradores.add(mAuth.getCurrentUser().getEmail().toString());
        membros.add(mAuth.getCurrentUser().getEmail().toString());

        Query query = mRef.orderByChild("email").equalTo(usuarioCriador);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    idUsuario = item.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Equipe equipe = new Equipe(nomeEquipe, descEquipe, usuarioCriador, administradores, membros);
        //TODO DÁ DANDO PAU AQUI, O BOTÃO QUE ESTÁ NA FRAGMENTE ESTÁ FICANDO NULL
        equipe.novaEquipe(idUsuario);








    }
}