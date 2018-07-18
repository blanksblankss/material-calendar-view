package com.applandeo.materialcalendarview.utils;

import android.widget.ImageView;

/**
 * This class is used to load event image in a day cell
 * <p>
 * Created by Mateusz Kornakiewicz on 23.05.2017.
 */

public class ImageUtils {
    
    public static void loadResource(ImageView imageView, int resource) {
        if (resource == 0) {
            return;
        }

        imageView.setImageResource(resource);
//        imageView.setImageBitmap(getBitmapWithText(imageView.getContext(), R.drawable.ic_arrow_right, "test"));
    }

//    public static Bitmap getBitmapWithText(Context context, int backgroundResource, String text) {
//        Resources resources = context.getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(resources, backgroundResource);
//        Bitmap.Config bitmapConfig = bitmap.getConfig();
//
//        if (bitmapConfig == null) {
//            bitmapConfig = Bitmap.Config.ARGB_8888;
//        }
//
//        bitmap = bitmap.copy(bitmapConfig, true);
//        Canvas canvas = new Canvas(bitmap);
//
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//
//        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        paint.setColor(ContextCompat.getColor(context, R.color.defaultColor));
//
//        float scale = resources.getDisplayMetrics().density;
//        paint.setTextSize((int) (12 * scale));
//
//        Rect bounds = new Rect();
//        paint.getTextBounds(text, 0, text.length(), bounds);
//        int x = (bitmap.getWidth() - bounds.width()) / 2;
//        int y = (bitmap.getHeight() + bounds.height()) / 2;
//        canvas.drawText(text, x, y, paint);
//
//        return bitmap;
//    }


    private ImageUtils() {
    }
}
