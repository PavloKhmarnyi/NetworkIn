package com.example.pavlo.networkin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pavlo.networkin.R;
import com.example.pavlo.networkin.models.NetworkData;
import com.example.pavlo.networkin.utils.Config;

import java.util.ArrayList;


/**
 * Created by pavlo on 5/21/2017.
 */

public class NetworksAdapter extends RecyclerView.Adapter<NetworksAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NetworkData> networks;
    private View.OnClickListener onClickListener;
    private int listenersCode;

    public NetworksAdapter(Context context, ArrayList<NetworkData> networks, int listenerCode) {
        this.context = context;
        this.networks = networks;
        this.listenersCode = listenerCode;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.network_list_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.idTextView.setText(position + 1 + ".");
        viewHolder.ssidTextView.setText(networks.get(position).getSsid());

        switch (listenersCode) {
            case Config.ALLOWED_NETWORKS_ITEM_CLICK_LISTENER_CODE:
                onClickListener = new AllowedNetworksItemClickListener(context, networks, position);
                break;
            case Config.STORED_NETWORKS_ITEM_CLICK_LISTENER_CODE:
                onClickListener = new StoredNetworksItemClickListener(context, networks, position);
                break;
        }
        viewHolder.view.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return networks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private final TextView idTextView;
        private final TextView ssidTextView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            idTextView = (TextView) view.findViewById(R.id.idTextView);
            ssidTextView = (TextView) view.findViewById(R.id.ssidTextView);
        }
    }
}
