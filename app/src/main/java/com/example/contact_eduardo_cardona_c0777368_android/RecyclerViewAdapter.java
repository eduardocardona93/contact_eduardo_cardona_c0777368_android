package com.example.contact_eduardo_cardona_c0777368_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_eduardo_cardona_c0777368_android.model.Contact;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Contact> contactList;
    private Context context;
    private OnContactClickListener onContactClickListener;
    private OnContactLongPressListener onContactLongPressListener;

    public RecyclerViewAdapter(List<Contact> contactList, Context context, OnContactClickListener onContactClickListener, OnContactLongPressListener onContactLongPressListener) {
        this.contactList = contactList;
        this.context = context;
        this.onContactClickListener = onContactClickListener;
        this.onContactLongPressListener = onContactLongPressListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.fullname.setText(contact.getFullname());
        holder.phone.setText(contact.getPhoneNumber());
        holder.email.setText(contact.getEmail());
        holder.address.setText(contact.getAddress());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView fullname;
        private TextView phone;
        private TextView email;
        private TextView address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullnameTV);
            phone = itemView.findViewById(R.id.mainPhoneTV);
            email = itemView.findViewById(R.id.mainEmailTV);
            address = itemView.findViewById(R.id.addressTV);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onContactLongPressListener.onContactLongPress(getAdapterPosition());
                    return true;
                }
            });
        }
        @Override
        public void onClick(View v) {
            onContactClickListener.onContactClick(getAdapterPosition());

        }


    }

    public interface OnContactClickListener {
        void onContactClick(int position);
    }

    public interface OnContactLongPressListener{
        void onContactLongPress(int position);
    }
}
