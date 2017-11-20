package talkhub.com.br.th;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import talkhub.com.br.th.Adapter.ChatListAdapter;
import talkhub.com.br.th.Entities.ChatMensagem;
import talkhub.com.br.th.Entities.Usuario;

public class ChatEquipeActivity extends AppCompatActivity {

    private String idEquipe;
    private String nomeEquipe;

    private ImageButton mButtonEnviarMensagem;
    private EditText mEditTextMensagem;

    private Usuario usuario = new Usuario();
    private List<ChatMensagem> mensagens = new ArrayList<ChatMensagem>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_equipe);


        Bundle bundle = getIntent().getExtras();
        idEquipe = bundle.getString("idEquipe");
        nomeEquipe = bundle.getString("nomeEquipe");

        mButtonEnviarMensagem = (ImageButton) findViewById(R.id.bt_enviar_msg_chat_equipe);
        mEditTextMensagem = (EditText) findViewById(R.id.et_Mensagem_chat_equipe);

        preencheChat();


        //Recuperação de dados do usuário logado
        DatabaseReference mRefUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios")
                .child(LoginActivity.idUsuario);

        mRefUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    usuario.setId(LoginActivity.idUsuario);
                    usuario.setNome(dataSnapshot.child("nome").getValue().toString());
                    usuario.setSobrenome(dataSnapshot.child("sobrenome").getValue().toString());


                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mButtonEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensagem = mEditTextMensagem.getText().toString();
                ChatMensagem chatMensagem = new ChatMensagem(mensagem, usuario);
                chatMensagem.novaMensagemEquipe(idEquipe);
                mEditTextMensagem.setText("");
            }
        });

        mEditTextMensagem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(mEditTextMensagem.getText().toString()))
                    mButtonEnviarMensagem.setEnabled(true);
                else
                    mButtonEnviarMensagem.setEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





    }

    public void preencheChat(){


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_chat_equipe);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL
                , false);
        recyclerView.setLayoutManager(layoutManager);

        final ChatListAdapter chatListAdapter = new ChatListAdapter(mensagens, this);
        recyclerView.setAdapter(chatListAdapter);

        DatabaseReference mRefMsg = FirebaseDatabase.getInstance().getReference().child("mensagens")
                .child("mensagens_equipe").child(idEquipe);

        mRefMsg.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //O if abaixo é para evitar que o app tente ler o child que contém os usuários que ainda não leram as mensagens
                //Caso o app tente ler este child, vai dar pau pois ele vai tentar pegar dados de horário por exemplo, e isto não existe neste child
                if(!dataSnapshot.getKey().equals("leitura_usuarios_pendentes")) {
                    ChatMensagem msg = new ChatMensagem();
                    msg.setId(dataSnapshot.getKey());
                    msg.setIdUsuario(dataSnapshot.child("idUsuario").getValue().toString());
                    msg.setNomeUsuario(dataSnapshot.child("nomeUsuario").getValue().toString());
                    msg.setSobrenomeUsuario(dataSnapshot.child("sobrenomeUsuario").getValue().toString());
                    msg.setTexto(dataSnapshot.child("texto").getValue().toString());
                    msg.setHoraMsg(Long.valueOf(dataSnapshot.child("horaMsg").getValue().toString()));
                    mensagens.add(msg);
                    chatListAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(mensagens.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
