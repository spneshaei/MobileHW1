package edu.sharif.ce.mobile.crypto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.sharif.ce.mobile.crypto.models.Crypto;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> {

    private ArrayList<Crypto> mCryptos;
    private Context context;

    public CryptoAdapter(ArrayList<Crypto> cryptos) {
        this.mCryptos = cryptos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cryptoView = inflater.inflate(R.layout.crypto_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(cryptoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Crypto thisCrypto = mCryptos.get(position);
        Glide.with(context).load(thisCrypto.getImageUrl()).into(holder.image);
        holder.name.setText(thisCrypto.getName());
        holder.symbol.setText(thisCrypto.getSymbol() + " |");
        DecimalFormat df = new DecimalFormat("#.###");
        holder.price.setText("$" + df.format(thisCrypto.getPrice()));
        holder.oneHour.setText("1H: " + thisCrypto.getPercentChange1H() + "%");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = ((CryptoAdapter.ViewHolder) view.getTag()).getAdapterPosition();
                Intent intent = new Intent(context, CandleInfoActivity.class);
                intent.putExtra("crypto", mCryptos.get(position));
                context.startActivity(intent);
            }
        });

        int green = Color.parseColor("#3EA623");
        int red = Color.parseColor("#B72C28");

        if (thisCrypto.getPercentChange1H() < 0) {
            holder.oneHour.setTextColor(red);
        } else {
            holder.oneHour.setTextColor(green);
        }
        holder.oneDay.setText("1D: " + thisCrypto.getPercentChange24H() + "%");

        if (thisCrypto.getPercentChange24H() < 0) {
            holder.oneDay.setTextColor(red);
        } else {
            holder.oneDay.setTextColor(green);
        }

        holder.oneWeek.setText("7D: " + thisCrypto.getPercentChange7D() + "%");

        if (thisCrypto.getPercentChange7D() < 0) {
            holder.oneWeek.setTextColor(red);
        } else {
            holder.oneWeek.setTextColor(green);
        }
    }

    @Override
    public int getItemCount() {
        return mCryptos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView price;
        public TextView oneHour;
        public TextView oneDay;
        public TextView oneWeek;
        public TextView symbol;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.crypto_image);
            name = itemView.findViewById(R.id.crypto_name);
            price = itemView.findViewById(R.id.crypto_price);
            oneHour = itemView.findViewById(R.id.price_one_hour);
            oneDay = itemView.findViewById(R.id.price_one_day);
            oneWeek = itemView.findViewById(R.id.price_one_week);
            symbol = itemView.findViewById(R.id.crypto_symbol);
            itemView.setTag(this);
        }
    }
}
