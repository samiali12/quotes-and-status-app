package android.example.quotes_and_status.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.example.quotes_and_status.HomeActivity;
import android.example.quotes_and_status.QoutesModel.CategoryImageMode;
import android.example.quotes_and_status.QoutesModel.CategoryModel;
import android.example.quotes_and_status.R;
import android.example.quotes_and_status.ShowQuotes;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private final HomeActivity homeActivity;
    private final ArrayList<CategoryImageMode> categoryImageList;
    private final ArrayList<CategoryModel> categoryList;

    private MediaPlayer mediaPlayer;

    public CategoryAdapter(HomeActivity homeActivity, ArrayList<CategoryModel> categoryList, ArrayList<CategoryImageMode> categoryImageList) {
        this.categoryList = categoryList;
        this.homeActivity = homeActivity;
        this.categoryImageList = categoryImageList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String name = categoryList.get(position).getName();
        String first = name.substring(0,1);
        String remaning = name.substring(1);

        first = first.toUpperCase();

        name = first + remaning;

        holder.textView.setText(name);

        for(int i=0; i<categoryImageList.size(); i++){
            if(categoryImageList.get(i).getImgName().equalsIgnoreCase(name)){
                //Log.d("name", "Name ===> " + name);
                //Log.d("name", "Name ===> " + categoryImageList.get(i).getImgName());
                holder.imageView.setImageResource(categoryImageList.get(i).getImgId());
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(homeActivity.sound == true){
                mediaPlayer = MediaPlayer.create(homeActivity, R.raw.card_sound_effect);
                mediaPlayer.start(); }

                Intent intent = new Intent(homeActivity, ShowQuotes.class);
                intent.putExtra("id", categoryList.get(position).getId());
                intent.putExtra("name", categoryList.get(position).getName());
                homeActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
       return categoryList.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        CardView cardView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.categoryID);
            imageView = itemView.findViewById(R.id.categoryImage);
            cardView = itemView.findViewById(R.id.card_action);
        }
    }
}
