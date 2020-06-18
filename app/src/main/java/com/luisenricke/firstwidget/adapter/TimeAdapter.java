package com.luisenricke.firstwidget.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luisenricke.firstwidget.R;
import com.luisenricke.firstwidget.bd.Time;

import java.util.ArrayList;

/**
 * Created by mac on 28/3/18.
 */

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private ArrayList<Time> listTime;
    private OnItemClickListener listener;

    @Override
    public TimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position, listener);

    }

    @Override
    public int getItemCount() {
        return listTime.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Time time);
    }

    public TimeAdapter(ArrayList<Time> listTime, OnItemClickListener listener) {
        this.listTime = listTime;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTime;
        private TextView txtDistance;
        private TextView txtDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtDelete = itemView.findViewById(R.id.txtDelete);
        }

        public void bind(final int position, final OnItemClickListener listener) {
            final Time time = listTime.get(position);
            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Este true es porque queremos escribir
                    // llamamos a public Time(Context context, boolean write){
                    // y por eso le pasamos, true , porque queremos escribir.
                    //
                    // ahora lo metemos en un if
                    if (new Time(itemView.getContext(), true).delete(time.getId())) {
                        // El anterior time.getId() es para pasar el is del item

                        // Una vez metido en el if, miramos si se elimina. Y si es cierto
                        // entonces hacemos esto.
                        listTime.remove(position);
                        // y actualizamos nuestro recycleview
                        notifyDataSetChanged();


                    }

                }
            });

            //Añadimos aquí lo siguiente:
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(time);
                    // Y hasta aquí este commit que hace que esté listo
                    // el adaptador para nuestro recycler
                }
            });

            txtDistance.setText(time.getDistance());
            txtTime.setText(" en " + time.getTime()); // Le doy un espacio porque se juntaba

        }

    }

}
