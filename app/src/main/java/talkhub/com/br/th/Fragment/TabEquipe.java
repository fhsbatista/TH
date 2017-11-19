package talkhub.com.br.th.Fragment;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import talkhub.com.br.th.Adapter.EquipeListAdapter;
import talkhub.com.br.th.Entities.Equipe;
import talkhub.com.br.th.Entities.Usuario;
import talkhub.com.br.th.LoginActivity;
import talkhub.com.br.th.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabEquipe.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabEquipe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabEquipe extends Fragment {

    ViewHolder mViewHolder = new ViewHolder();

    //Botão para criar equipe
    private Button mNovaEquipe;

    //
    private DatabaseReference mRefUsuario;
    private FirebaseAuth mAuth;
    private String emailUsuarioLogado;
//    private List<HashMap<String, String>> equipes = new ArrayList<HashMap<String, String>>();
    private List<Equipe> equipes = new ArrayList<Equipe>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public TabEquipe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabEquipe.
     */
    // TODO: Rename and change types and number of parameter
    public static TabEquipe newInstance(String param1, String param2) {
        TabEquipe fragment = new TabEquipe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();
        mRefUsuario = FirebaseDatabase.getInstance().getReference();
        emailUsuarioLogado = mAuth.getCurrentUser().getEmail().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_equipe, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleEquipes);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);



         final EquipeListAdapter equipeListAdapter = new EquipeListAdapter(equipes,getContext());
        recyclerView.setAdapter(equipeListAdapter);

        Query query = mRefUsuario.child("usuarios").child(LoginActivity.idUsuario).child("equipes");

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Carregando equipes do usuário");
        progressDialog.show();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                equipes.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()) {
                    if (item.hasChild("nome") && item.hasChild("descricao")) {



                                Equipe equipe = new Equipe();
                                equipe.setId(item.getKey());
                                equipe.setNome(item.child("nome").getValue().toString());
                                equipe.setDescricao(item.child("descricao").getValue().toString());

                                equipes.add(equipe);

                                equipeListAdapter.notifyDataSetChanged();




                    }
                }
                progressDialog.dismiss();

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class ViewHolder {
        RecyclerView recyclerEquipes;
    }
}
