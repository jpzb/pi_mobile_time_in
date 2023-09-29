package fellipy.gustavo.joao_pedro.pedro.time_in.Adapter;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import fellipy.gustavo.joao_pedro.pedro.time_in.Evento;
import fellipy.gustavo.joao_pedro.pedro.time_in.MyViewHolder;
import fellipy.gustavo.joao_pedro.pedro.time_in.R;

import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;


public class ListAdapter extends PagingDataAdapter<Evento, MyViewHolder>{


    public ListAdapter(@NonNull DiffUtil.ItemCallback<Evento> diffCallback){
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_recycleview_eventos, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
        Evento evento = getItem(position);

        // Titulo do Evento
        TextView tvTituloItemEvento = holder.itemView.findViewById(R.id.tvTituloItemEvento);
        tvTituloItemEvento.setText(Evento.nome);

        // Data do Evento
        TextView tvDataItemEvento = holder.itemView.findViewById(R.id.tvDataItemEvento);
        tvDataItemEvento.setText("Data: " + new SimpleDateFormat("HH:mm dd/MM/yyyy").format(Evento.dataHorario));



    }
}