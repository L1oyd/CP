package com.erop.spmapp.SPM;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erop.spmapp.R;
import com.erop.spmapp.wallet.Card;
import com.erop.spmapp.wallet.SelectListener;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Card> cards;
    private SelectListener listener;

    CardAdapter(Context context, List<Card> cards, SelectListener listener){
        this.cards = cards;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.cardName.setText(card.getName());
        holder.cardNumber.setText(card.getNumber());
        holder.cardBalance.setText(Integer.toString(card.getBalance()));

        switch (card.getColor()){
            case 0: holder.cardImage.setImageResource(R.drawable.card_0);
                break;
            case 1: holder.cardImage.setImageResource(R.drawable.card_1);
                break;
            case 2: holder.cardImage.setImageResource(R.drawable.card_2);
                break;
            case 3: holder.cardImage.setImageResource(R.drawable.card_3);
                break;
            case 4: holder.cardImage.setImageResource(R.drawable.card_4);
                break;
            case 5: holder.cardImage.setImageResource(R.drawable.card_5);
                break;
            case 6: holder.cardImage.setImageResource(R.drawable.card_6);
                break;
            case 7: holder.cardImage.setImageResource(R.drawable.card_7);
                break;
        }
        Log.d("CardAdapter", "LoadCard: "+ card.getColor());



        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClicked(card);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView cardName, cardNumber, cardBalance;
        ImageView cardImage;

        public RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View view) {
            super(view);

            cardName = view.findViewById(R.id.cardName);
            cardNumber = view.findViewById(R.id.cardNumber);
            cardBalance = view.findViewById(R.id.cardBalance);
            cardImage = view.findViewById(R.id.cardImage);

            relativeLayout = view.findViewById(R.id.main_container);
        }
    }
}
