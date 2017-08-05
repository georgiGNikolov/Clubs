package com.xcomputers.clubs.clubs.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;


/**
 * Created by xComputers on 05/08/2017.
 */

public class GlideHelper {

    public static void loadImageCircle(Context context, String urlTarget, ImageView view, int placeHolder) {

        if (TextUtils.isEmpty(urlTarget)) {
            urlTarget = " ";
        }

        Bitmap picture = BitmapFactory.decodeResource(context.getResources(), placeHolder);
        RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(context.getResources(), picture);
        circularBitmapDrawable.setCircular(true);

        Glide.with(context).load(urlTarget).asBitmap()
                .placeholder(circularBitmapDrawable)
                .centerCrop().into(new BitmapImageViewTarget(view) {
            @Override
            protected void setResource(Bitmap resource) {

                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                view.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

}
