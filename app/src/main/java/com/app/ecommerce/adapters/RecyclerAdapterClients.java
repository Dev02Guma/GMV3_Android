package com.app.ecommerce.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import android.widget.TextView;

import com.app.ecommerce.Config;
import com.app.ecommerce.R;
import com.app.ecommerce.models.Clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapterClients extends RecyclerView.Adapter<RecyclerAdapterClients.MyViewHolder> implements Filterable {

    private Context context;
    private List<Clients> ClientList;
    private List<Clients> ClientListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cliente_Nombre, cliente_direccion,cliente_disponible,cliente_saldo,cliente_limite,cliente_moroso;

        public MyViewHolder(View view) {
            super(view);
            cliente_Nombre = view.findViewById(R.id.client_name);
            cliente_direccion = view.findViewById(R.id.client_dir);
            cliente_disponible = view.findViewById(R.id.id_cliente_disponible);
            cliente_saldo = view.findViewById(R.id.id_cliente_saldo);
            cliente_limite = view.findViewById(R.id.id_cliente_limite);
            cliente_moroso = view.findViewById(R.id.client_moroso);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onContactSelected(ClientListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public RecyclerAdapterClients(Context context, List<Clients> productList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.ClientList = productList;
        this.ClientListFiltered = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Clients clients = ClientListFiltered.get(position);
        holder.cliente_Nombre.setText(clients.getCLIENTE() + " - " + clients.getNOMBRE());
        holder.cliente_direccion.setText(clients.getDIRECCION());

        if (Config.ENABLE_DECIMAL_ROUNDING) {
            String p1 = String.format(Locale.GERMAN, "%1$,.0f", clients.getDIPONIBLE());
            String p2 = String.format(Locale.GERMAN, "%1$,.0f", clients.getSALDO());
            String p3 = String.format(Locale.GERMAN, "%1$,.0f", clients.getLIMITE());

            holder.cliente_disponible.setText("C$ " + p1);
            holder.cliente_saldo.setText("C$ " + p2);
            holder.cliente_limite.setText("C$ " + p3);
        } else {
            holder.cliente_disponible.setText("C$ " + clients.getDIPONIBLE());
            holder.cliente_saldo.setText("C$ " + clients.getSALDO());
            holder.cliente_limite.setText("C$ " + clients.getLIMITE());
        }

        if (clients.getMOROSO().equals("S")){
            holder.cliente_moroso.setText("CLIENTE MOROSO");
        }




    }

    @Override
    public int getItemCount() {
        return ClientListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    ClientListFiltered = ClientList;
                } else {
                    List<Clients> filteredList = new ArrayList<>();
                    for (Clients row : ClientList) {
                        if (row.getNOMBRE().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    ClientListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = ClientListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ClientListFiltered = (ArrayList<Clients>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Clients product);
    }
}
