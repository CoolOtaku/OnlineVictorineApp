package com.example.online_victorine_app.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_victorine_app.Activitys.PassingTestActivity;
import com.example.online_victorine_app.Obj.Helper;
import com.example.online_victorine_app.Obj.TestObj;
import com.example.online_victorine_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int LAYOUT_ONE= 0;
    private static final int LAYOUT_TWO= 1;
    private Animation animClick;

    private Context context;
    public static List<String> themes = new ArrayList<>();
    public static Map<String, List<TestObj>> savedData = new HashMap<>();

    public TestListAdapter(Context context){
        this.context = context;
        animClick = AnimationUtils.loadAnimation(context, R.anim.btn_click1);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return LAYOUT_ONE;
        else
            return LAYOUT_TWO;
    }
    @Override
    public int getItemCount() {
        return themes.size();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =null;
        RecyclerView.ViewHolder viewHolder = null;

        if(viewType==LAYOUT_ONE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_widget,parent,false);
            viewHolder = new ViewHolderOne(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_btn_widget,parent,false);
            viewHolder= new ViewHolderTwo(view);
        }
        return viewHolder;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder.getItemViewType()== LAYOUT_ONE) {
            ViewHolderOne vaultItemHolder = (ViewHolderOne) holder;
            vaultItemHolder.constraintLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(context.getString(R.string.instagram_url));
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                    likeIng.setPackage("com.instagram.android");
                    try {
                        context.startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(context.getString(R.string.instagram_url))));
                    }
                }
            });
            vaultItemHolder.ContainerHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(animClick);
                    AlertDialog.Builder dialogComm = new AlertDialog.Builder(context);
                    View v = View.inflate(context, R.layout.rusian_fack_you_widget, null);
                    dialogComm.setView(v);
                    AlertDialog newDialog = dialogComm.create();
                    newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    newDialog.show();
                }
            });
        }else{
            ViewHolderTwo vaultItemHolder = (ViewHolderTwo) holder;
            vaultItemHolder.TitleTheme.setText(themes.get(position));
            vaultItemHolder.ThemeContainer.setVisibility(View.GONE);
            View.OnClickListener VisibilityRow = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vaultItemHolder.ThemeContainer.getVisibility() == View.VISIBLE) {
                        vaultItemHolder.ThemeContainer.setVisibility(View.GONE);
                    } else {
                        vaultItemHolder.ThemeContainer.setVisibility(View.VISIBLE);
                    }
                }
            };
            vaultItemHolder.BtnTheme.setOnClickListener(VisibilityRow);

            vaultItemHolder.ThemeContainer.removeAllViews();
            try {
                List<TestObj> localTestList = savedData.get(themes.get(position));
                for (int index = 0; index < localTestList.size(); index++) {
                    TestObj item = localTestList.get(index);
                    View itemView = LayoutInflater.from(context).inflate(R.layout.item_test, null, false);
                    ImageView ItemTestImage = (ImageView) itemView.findViewById(R.id.ItemTestImage);
                    Picasso.get().load(item.getCover()).into(ItemTestImage);
                    TextView ItemTestTitle = (TextView) itemView.findViewById(R.id.ItemTestTitle);
                    ItemTestTitle.setText(item.getTitle());
                    vaultItemHolder.ThemeContainer.addView(itemView);

                    View.OnClickListener GoToTest = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.startAnimation(animClick);
                            Intent intent = new Intent(context, PassingTestActivity.class);
                            intent.putExtra("data", item.toString());
                            context.startActivity(intent);
                        }
                    };
                    itemView.setOnClickListener(GoToTest);
                }
            }catch (Exception e){
                e.printStackTrace();
                Helper.getDataInStorage();
            }
        }
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {

        public ConstraintLayout ContainerHeader;
        public ConstraintLayout constraintLayout2;

        public ViewHolderOne(View itemView) {
            super(itemView);
            ContainerHeader = (ConstraintLayout) itemView.findViewById(R.id.ContainerHeader);
            constraintLayout2 = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout2);
        }
    }
    public class ViewHolderTwo extends RecyclerView.ViewHolder{

        public TextView TitleTheme;
        public LinearLayout ThemeContainer;
        public ConstraintLayout BtnTheme;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            TitleTheme = (TextView) itemView.findViewById(R.id.TitleTheme);
            ThemeContainer = (LinearLayout) itemView.findViewById(R.id.ThemeContainer);
            BtnTheme = (ConstraintLayout) itemView.findViewById(R.id.BtnTheme);
        }
    }
}
