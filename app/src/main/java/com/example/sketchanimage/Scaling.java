package com.example.sketchanimage;

import android.graphics.Bitmap;

public class Scaling {

    public Bitmap imageScaling(Bitmap image, int pixelength){

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
