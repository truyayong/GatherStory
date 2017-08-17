package com.truyayong.richtext.richview.ig;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.truyayong.richtext.richview.ImageHolder;
import com.truyayong.richtext.richview.RichViewConfig;
import com.truyayong.richtext.richview.drawable.DrawableWrapper;
import com.truyayong.richtext.richview.parser.ImageLoadNotify;

import java.util.concurrent.Future;

/**
 * Created by alley_qiu on 2017/3/24.
 * Base64格式图片解析器
 */

class Base64ImageDecode extends AbstractImageLoader implements Runnable {

    @NonNull
    private byte[] src;

    Base64ImageDecode(@NonNull byte[] src, ImageHolder holder, RichViewConfig config, TextView textView, DrawableWrapper drawableWrapper, ImageLoadNotify iln) {
        super(holder, config, textView, drawableWrapper, iln);
        this.src = src;
    }

    @Override
    public void run() {
        try {
            onLoading();
            BitmapFactory.Options options = new BitmapFactory.Options();
            int[] inDimens = getDimensions(src, options);
            options.inSampleSize = onSizeReady(inDimens[0], inDimens[1]);
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            onResourceReady(decodeBytes(src, options));
        } catch (Exception e) {
            onFailure(e);
        }
    }

    private int[] getDimensions(byte[] bs, BitmapFactory.Options options) {
        options.inJustDecodeBounds = true;
        decodeBytes(bs, options);
        options.inJustDecodeBounds = false;
        return new int[]{options.outWidth, options.outHeight};
    }

    private Bitmap decodeBytes(byte[] bs, BitmapFactory.Options options) {
        return BitmapFactory.decodeByteArray(bs, 0, bs.length, options);
    }
}

class FutureWrapper implements Cancelable {

    private Future future;

    FutureWrapper(Future future) {
        this.future = future;
    }

    @Override
    public void cancel() {
        if (future != null && !future.isDone() && !future.isCancelled()) {
            future.cancel(true);
            future = null;
        }
    }
}
