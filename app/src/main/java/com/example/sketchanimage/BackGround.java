package com.example.sketchanimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import static java.lang.Math.sqrt;

public class BackGround extends AsyncTask<Bitmap,Void,Bitmap> {
    @Override
    protected Bitmap doInBackground(Bitmap... bitmaps) {


        Bitmap image = bitmaps[0];
        //for scaling test
        Scaling scale = new Scaling();
        if(image.getWidth()<300 || image.getHeight()<300){
        image = scale.imageScaling(image, 300);}
        //till here
        image = image.copy(Bitmap.Config.ARGB_8888,true);
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixels2d = new int[width][height];
        int red,blue,green,grey;
        int gx;
        int gy;
        int[][] mag = new int[width][height];
        int[] pixels = new int[height*width];
        image.getPixels(pixels,0,width,0,0,width,height);

        // for putting image into a 2d array while getting a greyscale of it as well
        for(int k = 0;k<height; k++){
            for(int j = 0;j<width; j++){
                pixels2d[j][k] = pixels[j+k*width];
                red = Color.red(pixels2d[j][k]);
                green = Color.green((pixels2d[j][k]));
                blue = Color.blue(pixels2d[j][k]);
                grey = (int) ((red * 0.2126) + (green * 0.7152) + (blue * 0.0722));
                // was trying gamma correction didn't work grey = (int) Math.pow(grey,1/2.2);
                // another try at gamma correction
                //grey = (int) Math.pow(grey,1.1);//this one totally works but adds a lot of noise while giving the effect of shading
                //try adding the sigma correction after the edges have been calculated
                //as well try the other method 255*(pixel/255)^gamma
                //already checked this method does not work
                //grey = (int) Math.pow(grey/255,1/1.3)*255;
                pixels2d[j][k] = grey;

            }
        }


        for(int k=0;k<height;k++){
            for(int j=0;j<width;j++){
                if(k==0||k==height-1||j==0||j==width-1){
                    gx = gy = 0;
                    mag[j][k] = (int) sqrt(gx*gx + gy*gy);
                }else {
                    gx = pixels2d[j+1][k-1] + pixels2d[j+1][k]*2 + pixels2d[j+1][k+1]
                            - pixels2d[j-1][k-1] - pixels2d[j-1][k]*2 - pixels2d[j-1][k+1];
                    gy = pixels2d[j-1][k+1] + pixels2d[j][k+1]*2 + pixels2d[j+1][k+1]
                            - pixels2d[j-1][k-1] - pixels2d[j][k-1]*2 - pixels2d[j+1][k-1];//error was here fixed

                    mag[j][k] = (int) sqrt(gx*gx + gy*gy);//the actual working method
//                  this is the try for improvement
//                    int lmo = (int) sqrt(gx*gx + gy*gy);
//                    if(lmo<100) {
//                        mag[j][k] = 0;
//
//                    }else{
//                        mag[j][k] = 255;
//                    }

                }
            }
        }
        for(int k = 0;k<height; k++) {
            for (int j = 0; j < width; j++) {
                pixels2d[j][k] = Color.rgb(mag[j][k],mag[j][k],mag[j][k]);

            }
        }


        for(int k = 0;k<height; k++) {
            for (int j = 0; j < width; j++) {

                red = 255 - Color.red(pixels2d[j][k]);
                green = 255 - Color.green(pixels2d[j][k]);
                blue = 255 - Color.blue(pixels2d[j][k]);
                pixels2d[j][k] = Color.rgb(red,green,blue);

            }
        }

        for(int k = 0;k<height; k++) {
            for (int j = 0; j < width; j++) {

                pixels[j+k*width] = pixels2d[j][k];

            }
        }

        image.setPixels(pixels,0,width,0,0,width,height);

        return image;
    }
}
