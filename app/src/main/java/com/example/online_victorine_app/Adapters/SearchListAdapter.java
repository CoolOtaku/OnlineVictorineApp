package com.example.online_victorine_app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_victorine_app.Activitys.PassingTestActivity;
import com.example.online_victorine_app.Obj.TestObj;
import com.example.online_victorine_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>{

    private Context context;
    private List<TestObj> list;

    private Animation animClick;

    public SearchListAdapter(Context context, List<TestObj> list) {
        this.context = context;
        this.list = list;
        animClick = AnimationUtils.loadAnimation(context, R.anim.btn_click1);
    }

    @NonNull
    @Override
    public SearchListAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_test, parent, false);
        return new SearchListAdapter.SearchViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.SearchViewHolder holder, int position) {
        TestObj item = list.get(position);
        Picasso.get().load(item.getCover()).into(holder.ItemTestImage);
        holder.ItemTestTitle.setText(item.getTitle());
        View.OnClickListener GoToTest = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animClick);
                Intent intent = new Intent(context, PassingTestActivity.class);
                intent.putExtra("data", item.toString());
                context.startActivity(intent);
            }
        };
        holder.ItemTestContainer.setOnClickListener(GoToTest);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout ItemTestContainer;
        ImageView ItemTestImage;
        TextView ItemTestTitle;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemTestContainer = itemView.findViewById(R.id.ItemTestContainer);
            ItemTestImage = itemView.findViewById(R.id.ItemTestImage);
            ItemTestTitle = itemView.findViewById(R.id.ItemTestTitle);
        }
    }
}
