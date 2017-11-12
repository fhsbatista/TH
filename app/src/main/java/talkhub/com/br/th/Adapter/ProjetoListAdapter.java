package talkhub.com.br.th.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import talkhub.com.br.th.Entities.Projeto;
import talkhub.com.br.th.MuralEquipeActivity;
import talkhub.com.br.th.R;
import talkhub.com.br.th.ViewHolder.ProjetoViewHolder;

/**
 * Created by ferna on 28/10/2017.
 */

public class  ProjetoListAdapter extends RecyclerView.Adapter {

    private List<Projeto> projetos;
    private Context context;
    private String idProjeto;
    private String nomeProjeto;

    public ProjetoListAdapter(List<Projeto> projetos, Context context) {
        this.projetos = projetos;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view  = LayoutInflater.from(context).inflate(R.layout.row_projetosequipe_list,
                parent, false);

        final ProjetoViewHolder projetoViewHolder = new ProjetoViewHolder(view);

        return projetoViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ProjetoViewHolder projetoViewHolder = (ProjetoViewHolder) holder;

        Projeto projeto = projetos.get(position);
        projetoViewHolder.mNomeProjeto.setText(projeto.getNome());
        projetoViewHolder.mDescProjeto.setText(projeto.getDescricao());
        idProjeto = projeto.getId();
        nomeProjeto = projeto.getNome();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MuralEquipeActivity.class);
                intent.putExtra("idProjeto", idProjeto);
                intent.putExtra("nomeProjeto", nomeProjeto);
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return projetos.size();
    }
}
