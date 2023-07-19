package android.example.quotes_and_status.adapters;


import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.example.quotes_and_status.BuildConfig;
import android.example.quotes_and_status.QoutesModel.QuotesModel;
import android.example.quotes_and_status.R;
import android.example.quotes_and_status.ShowQuotes;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.QoutesViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback{


    private static final int STORAGE_PERMISSION_CODE = 100;
    private final ShowQuotes showQuotes;
    private final ArrayList<Integer> cardImagesList;
    List<QuotesModel> quotesModelArrayList;
    Context context;




    public QuotesAdapter(ShowQuotes showQuotes, Context context,
                         List<QuotesModel> quotesModelArrayList,
                         ArrayList<Integer> cardImagesList) {

        this.quotesModelArrayList = quotesModelArrayList;
        this.showQuotes = showQuotes;
        this.context = context;
        this.cardImagesList = cardImagesList;
    }

    @NonNull
    @Override
    public QoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent, false);
        return new QoutesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QoutesViewHolder holder, int position) {

        String quote = quotesModelArrayList.get(position).getData();
        String data = "❝ \t\t\n " + quote + " \n \t\t❞";
        holder.textView.setText(data);



       holder.copyBtn.setOnClickListener(v -> {
            // Gets a handle to the clipboard service.
            ClipboardManager clipboard = (ClipboardManager)
                    showQuotes.getSystemService(Context.CLIPBOARD_SERVICE);
            // Creates a new text clip to put on the clipboard
            ClipData clip = ClipData.newPlainText("text", quotesModelArrayList.get(holder.getAdapterPosition()).getData());
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip);
            Toast.makeText(showQuotes, "Text Copy", Toast.LENGTH_SHORT).show();
        });





       holder.likeBtn.setOnClickListener(v -> {
           if(quotesModelArrayList.get(holder.getAdapterPosition()).isLiked()){
               quotesModelArrayList.get(holder.getAdapterPosition()).setLiked(false);
               Drawable drawable = holder.likeBtn.getDrawable();
               holder.likeBtn.setImageResource(R.drawable.ic_icons8_heart__1_);
           }
           else{
               quotesModelArrayList.get(holder.getAdapterPosition()).setLiked(true);
               Drawable drawable = holder.likeBtn.getDrawable();
               holder.likeBtn.setImageResource(R.drawable.ic_icons8_heart__2_);
           }
       });




       holder.saveBtn.setOnClickListener(v -> {

            if(ContextCompat.checkSelfPermission(showQuotes, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {


                holder.card_img.setDrawingCacheEnabled(true);
                holder.card_img.buildLayer();
                holder.card_img.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                Bitmap bmp = holder.card_img.getDrawingCache();
                Canvas canvas = new Canvas(bmp);

                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera/";
                File myDir = new File(root);
                myDir.mkdirs();

                File file = new File(myDir, System.currentTimeMillis() + ".jpg");

                if (file.exists()) file.delete();

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
                Toast.makeText(showQuotes, "Image saved successfully", Toast.LENGTH_SHORT).show();

                holder.card_img.setDrawingCacheEnabled(false);
            }

            else{
               askPermission();
            }

        });


       holder.shareBtn.setOnClickListener(v -> {
           try{
               Intent shareIntent = new Intent(Intent.ACTION_SEND);
               shareIntent.setType("text/plain");
               shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quotes and Status");
               String msg = quotesModelArrayList.get(holder.getAdapterPosition()).getData().toString();
               String shareMsg = "https://play.google.com/store/apps/details?id="
                       + BuildConfig.APPLICATION_ID;

               shareIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
               showQuotes.startActivity(Intent.createChooser(shareIntent, "choose one"));
           }
           catch(Exception e){
               e.printStackTrace();
           }
       });


       holder.card_img.setOnClickListener(v -> {

           Random rnd = new Random();

           int randChoice = (int) (Math.random()*2);

           if (randChoice == 0){
               int color = Color.argb(255, rnd.nextInt(256),
                       rnd.nextInt(256), rnd.nextInt(256));
               holder.card_img.setBackgroundColor(color);

           }else{
               int randImageChoice = rnd.nextInt((4-1)+1);

               holder.card_img.setBackgroundResource(cardImagesList.get(randImageChoice));
           }

       });

    }

    private void askPermission() {

        ActivityCompat.requestPermissions(showQuotes, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 8 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
            else{
                Toast.makeText(showQuotes, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public int getItemCount() {
        return quotesModelArrayList.size();
    }



    class QoutesViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textView;
        ImageView likeBtn;
        ImageView copyBtn;
        ImageView shareBtn;
        ImageView saveBtn;

        RelativeLayout card_img;


        public QoutesViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.post_card_view);
            textView = itemView.findViewById(R.id.singleQuotes);
            likeBtn = itemView.findViewById(R.id.post_like_btn);
            copyBtn = itemView.findViewById(R.id.post_copy_btn);
            shareBtn = itemView.findViewById(R.id.post_share_btn);
            saveBtn = itemView.findViewById(R.id.post_save_btn);

            card_img = itemView.findViewById(R.id.card_layout);
        }

    }

}
