package com.example.sketchanimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URI;

public class MainActivity extends AppCompatActivity {


    Bitmap imageReturned;
    ImageView openedImage,sketchedImage;
    Bitmap resizedImage,sketchedResized,sketch;
    Button share,convert,discard,save,select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openedImage = findViewById(R.id.imageView);
        share = findViewById(R.id.share);
        convert = findViewById(R.id.convert);
        discard = findViewById(R.id.discard);
        save = findViewById(R.id.save);
        select = findViewById(R.id.selectimage);
        sketchedImage = findViewById(R.id.imageView2);
        resizedImage = null;
        imageReturned = null;

    }


    public void convert(View view){

        if(resizedImage == null && imageReturned == null){
            Toast.makeText(this,"Kindly select an Image", Toast.LENGTH_SHORT).show();
        }else {

            BackGround backGround = new BackGround();
            try {
                sketch = backGround.execute(imageReturned).get();
            }catch (Exception e){
                e.printStackTrace();
            }

            convert.animate().alpha(0);
            convert.setEnabled(false);
            select.animate().alpha(0);
            select.setEnabled(false);
            discard.animate().alpha(1);
            discard.setEnabled(true);
            save.animate().alpha(1);
            save.setEnabled(true);
            openedImage.animate().translationX(-(openedImage.getWidth()/2));

            if(sketch.getWidth()>300 || sketch.getHeight()>300){

                sketchedResized = imageScaling(sketch, 300);
                sketchedImage.setImageBitmap(sketchedResized);
                sketchedImage.animate().translationX(-(openedImage.getWidth()/2));
            }else{
                sketchedResized = sketch;
                sketchedImage.setImageBitmap(sketchedResized);
                sketchedImage.animate().translationX(-(openedImage.getWidth()/2));
            }

        }
    }

    public void discardImages(View view){

        sketchedImage.setImageBitmap(null);
        openedImage.setImageBitmap(null);
        imageReturned = null;
        resizedImage = null;
        sketchedResized = null;
        convert.animate().alpha(1);
        convert.setEnabled(true);
        select.animate().alpha(1);
        select.setEnabled(true);
        discard.animate().alpha(0);
        discard.setEnabled(false);
        save.animate().alpha(0);
        save.setEnabled(false);
        sketchedImage.animate().translationX(openedImage.getWidth()/2);
        openedImage.animate().translationX(openedImage.getWidth()/6);
    }

    public void save(View view){



        String uri = MediaStore.Images.Media.insertImage(getContentResolver(), sketch, "sketch", "just a sketch");
        if(uri == null){
            Toast.makeText(this,"image has not been saved", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"image saved", Toast.LENGTH_SHORT).show();
            discard.animate().alpha(0);
            discard.setEnabled(false);
            save.animate().alpha(0);
            save.setEnabled(false);
            share.animate().alpha(1);
            share.setEnabled(true);
        }

    }


    public void shareImage(View view){

        share.setEnabled(false);
        share.animate().alpha(0);
        sketchedImage.setImageBitmap(null);
        openedImage.setImageBitmap(null);
        imageReturned = null;
        resizedImage = null;
        sketchedResized = null;
        sketchedImage.animate().translationX(openedImage.getWidth()/2);
        openedImage.animate().translationX(openedImage.getWidth()/6);
        convert.animate().alpha(1);
        convert.setEnabled(true);
        select.animate().alpha(1);
        select.setEnabled(true);
    }



    public void selectImage(View view){


        openGallery();


    }


    public void openGallery(){

        Intent imageSelectionIntent = new Intent();
        imageSelectionIntent.setType("image/*");
        imageSelectionIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imageSelectionIntent.createChooser(imageSelectionIntent, "Select an Image from the Gallery"), 1);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intentResult){

        super.onActivityResult(requestCode,resultCode,intentResult);

        if(requestCode == 1 && resultCode == RESULT_OK && intentResult != null && intentResult.getData() != null){

            Uri imageURI = intentResult.getData();
            try {
                imageReturned = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);

//                resizedImage = Bitmap.createScaledBitmap(imageReturned, 500, 500, true);

  //              openedImage.setImageBitmap(resizedImage);
                if(imageReturned.getHeight() > 300 || imageReturned.getWidth() > 300) {

                    resizedImage = imageScaling(imageReturned, 300);
                    openedImage.setImageBitmap(resizedImage);

                }else{

                    resizedImage = imageReturned;
                    openedImage.setImageBitmap(imageReturned);

                }



            }catch (Exception e){
                e.printStackTrace();
            }


        }



    }


    public Bitmap imageScaling(Bitmap image,int pixelength){

        int scaling;
        float scalingPercentage;
        int scaledHeight;
        int scaledWidth;


        if (image.getWidth() < image.getHeight()) {


            scalingPercentage = (float) (image.getHeight() - pixelength)/image.getHeight();
            scalingPercentage = scalingPercentage * 100;
            scaling = (int) ((scalingPercentage/100) * image.getHeight());
            scaledHeight = image.getHeight() - scaling;
            scaling = (int) ((scalingPercentage/100) * image.getWidth());
            scaledWidth = image.getWidth() - scaling;
//            Log.i("info", "scalingpercentage: " + scalingPercentage);
//            Log.i("info", "scaling: " + scaling);
//            Log.i("info", "Image width: " + scaledWidth);
//            Log.i("info", "Image height: " + scaledHeight);

            return Bitmap.createScaledBitmap(image, scaledWidth, scaledHeight, true);

        }else{

            scalingPercentage = (float) (image.getWidth() - pixelength)/image.getWidth();
            scalingPercentage = scalingPercentage * 100;
            scaling = (int) ((scalingPercentage/100) * image.getHeight());
            scaledHeight = image.getHeight() - scaling;
            scaling = (int) ((scalingPercentage/100) * image.getWidth());
            scaledWidth = image.getWidth() - scaling;
//            Log.i("info", "scalingpercentage: " + scalingPercentage);
//            Log.i("info", "scaling: " + scaling);
//            Log.i("info", "Image width: " + scaledWidth);
//            Log.i("info", "Image height: " + scaledHeight);

            return Bitmap.createScaledBitmap(image, scaledWidth, scaledHeight, true);


        }


    }







}

