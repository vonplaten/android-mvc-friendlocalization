package com.example.casi.uppg5_3.service.model;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.location.Location;

import com.example.casi.uppg5_3.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class Player{
    private Type type;
    private String name;
    private LatLng latLng;

    public Player(Type type, String name, LatLng latLng) {
        this.type = type;
        this.name = name;
        this.latLng = latLng;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return name.equals( ((Player)obj).name );
    }


    public BitmapDescriptor getIcon(Context context, LatLng mapcenter){

        return BitmapDescriptorFactory.fromBitmap(createBitmap(context, getDistanceText(mapcenter)));
    }
    //Ugly code
    private String getDistanceText(LatLng mapcenter){
        float[] results = new float[1];
        Location.distanceBetween(latLng.latitude, latLng.longitude, mapcenter.latitude, mapcenter.longitude, results);
        return String.format("%.0f", results[0]);
    }
    private String getBitmapCenterText(){
        return name.substring(0,1);
    }
    private LightingColorFilter getColorFilter(){
        if (type.equals(Type.HUMAN))
            return new LightingColorFilter(0xFFFFFFFF, 0x0000FF00);
        if (type.equals(Type.ZOMBIE))
            return new LightingColorFilter(0xFFFFFFFF, 0x00FF0000);
        return null;
    }
    private int getColor(){
        return Color.rgb(0,0,0);
//        if (type.equals(Type.HUMAN))
//            return Color.rgb(0, 255, 0);
//        if (type.equals(Type.ZOMBIE))
//            Color.rgb(255, 0, 0);
//        return 0;
    }
    private Bitmap createBitmap(Context context, String distanceText) {
        VectorDrawable backgroundDrawable = (VectorDrawable)ContextCompat.getDrawable(context, R.drawable.ic_marker);
        backgroundDrawable.setBounds(0,0,40,40);
        //PorterDuffColorFilter cf = new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.DST_ATOP); //just for testing
        backgroundDrawable.setColorFilter(getColorFilter());

        Bitmap backgroundBitmap = vectorToBitmap(backgroundDrawable);
        android.graphics.Bitmap.Config bitmapConfig = backgroundBitmap.getConfig();

        if ( bitmapConfig == null ) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        backgroundBitmap = backgroundBitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(backgroundBitmap);
        backgroundDrawable.draw(canvas);

        //Name
        //Ugly code
        backgroundBitmap = drawText(context,
                canvas,
                backgroundBitmap,
                this.name,
                0,
                38,
                12);

        //Distance
        //Ugly code
        if (!distanceText.equals("0"))
            backgroundBitmap = drawText(
                    context,
                    canvas,
                    backgroundBitmap,
                    distanceText,
                    0,
                    -20,
                    10);
        return backgroundBitmap;
    }

    private Bitmap drawText(Context context, Canvas canvas, Bitmap bitmap, String text, int x_offset, int y_offset, int size){
        Resources resources = context.getResources();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getColor());
        float scale = resources.getDisplayMetrics().density;
        paint.setTextSize((int) (size * scale));
        //paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        int x = (bitmap.getWidth() - bounds.width())/2 - x_offset;
        int y = (bitmap.getHeight() + bounds.height())/2 - y_offset;
        canvas.drawText(text, x, y, paint);
        return bitmap;
    }
    private Bitmap vectorToBitmap(Drawable vectorDrawable){
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        return bitmap;
    }

    public enum Type{
        HUMAN,
        ZOMBIE,
        GONE
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}





//    public BitmapDescriptor getIcon(Context context) {
//        int id;
//
//        if (type == Type.HUMAN) {
//            id = R.drawable.ic_human;
//        } else {
//            id = R.drawable.ic_zombie;
//        }
//        Drawable vectorDrawable = ContextCompat.getDrawable(context, id);
//        Bitmap bitmap = vectorToBitmap(vectorDrawable);
//        Canvas canvas = new Canvas(bitmap);
//        vectorDrawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//
//    }
