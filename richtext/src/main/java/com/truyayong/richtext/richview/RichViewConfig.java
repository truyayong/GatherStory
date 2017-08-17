package com.truyayong.richtext.richview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.truyayong.richtext.richview.callback.ImageFixCallback;
import com.truyayong.richtext.richview.callback.ImageGetter;
import com.truyayong.richtext.richview.callback.LinkFixCallback;
import com.truyayong.richtext.richview.callback.OnImageClickListener;
import com.truyayong.richtext.richview.callback.OnImageLongClickListener;
import com.truyayong.richtext.richview.callback.OnUrlClickListener;
import com.truyayong.richtext.richview.callback.OnUrlLongClickListener;
import com.truyayong.richtext.richview.ig.DefaultImageGetter;

import java.lang.ref.WeakReference;

/**
 * Created by alley_qiu on 2017/3/24.
 * RichView各种配置
 */
@SuppressWarnings("WeakerAccess")
public final class RichViewConfig {

    public final String source; // 源文本
    @RichType
    public final int richType;// 富文本类型，默认HTML
    public final boolean autoFix; // 图片自动修复，默认true
    public final boolean resetSize; // 是否放弃使用img标签中的尺寸属性，默认false
    @CacheType
    public final int cacheType; // 缓存类型
    public final ImageFixCallback imageFixCallback; // 自定义图片修复接口只有在autoFix为false时有效
    public final LinkFixCallback linkFixCallback; // 链接修复回调
    public final boolean noImage; // 不显示图片，默认false
    public final int clickable; // 是否可点击，默认0：可点击，使用默认的方式处理点击回调；1：可点击，使用设置的回调接口处理；-1：不可点击
    public final OnImageClickListener onImageClickListener; // 图片点击回调接口
    public final OnUrlClickListener onUrlClickListener; // 链接点击回调接口
    public final OnImageLongClickListener onImageLongClickListener; // 图片长按回调接口
    public final OnUrlLongClickListener onUrlLongClickListener; // 链接长按回调接口
    public final Drawable placeHolder; // placeHolder
    public final Drawable errorImage; // errorImage
    final ImageGetter imageGetter; // 图片加载器，默认为GlideImageGetter

    private RichViewConfig(RichViewConfigBuild config) {
        this(config.source, config.richType, config.autoFix, config.resetSize, config.cacheType, config.imageFixCallback,
                config.linkFixCallback, config.noImage, config.clickable, config.onImageClickListener,
                config.onUrlClickListener, config.onImageLongClickListener, config.onUrlLongClickListener,
                config.placeHolder, config.errorImage, config.imageGetter);
    }

    private RichViewConfig(String source, int richType, boolean autoFix, boolean resetSize, int cacheType, ImageFixCallback imageFixCallback, LinkFixCallback linkFixCallback, boolean noImage, int clickable, OnImageClickListener onImageClickListener, OnUrlClickListener onUrlClickListener, OnImageLongClickListener onImageLongClickListener, OnUrlLongClickListener onUrlLongClickListener, Drawable placeHolder, Drawable errorImage, ImageGetter imageGetter) {
        this.source = source;
        this.richType = richType;
        this.autoFix = autoFix;
        this.resetSize = resetSize;
        this.imageFixCallback = imageFixCallback;
        this.linkFixCallback = linkFixCallback;
        this.noImage = noImage;
        this.cacheType = cacheType;
        this.onImageClickListener = onImageClickListener;
        this.onUrlClickListener = onUrlClickListener;
        this.onImageLongClickListener = onImageLongClickListener;
        this.onUrlLongClickListener = onUrlLongClickListener;
        this.placeHolder = placeHolder;
        this.errorImage = errorImage;
        this.imageGetter = imageGetter;
        if (clickable == 0) {
            if (onImageLongClickListener != null || onUrlLongClickListener != null ||
                    onImageClickListener != null || onUrlClickListener != null) {
                clickable = 1;
            }
        }
        this.clickable = clickable;
    }

    @SuppressWarnings("unused")
    public static final class RichViewConfigBuild {

        final String source;
        @RichType
        int richType;
        boolean autoFix;
        boolean resetSize;
        @CacheType
        int cacheType;
        ImageFixCallback imageFixCallback;
        LinkFixCallback linkFixCallback;
        boolean noImage;
        int clickable;
        OnImageClickListener onImageClickListener;
        OnUrlClickListener onUrlClickListener;
        OnImageLongClickListener onImageLongClickListener;
        OnUrlLongClickListener onUrlLongClickListener;
        Drawable placeHolder;
        Drawable errorImage;
        @DrawableRes
        int placeHolderRes;
        @DrawableRes
        int errorImageRes;
        ImageGetter imageGetter;
        WeakReference<Object> tag;

        RichViewConfigBuild(String source, int richType) {
            this.source = source;
            this.richType = richType;
            this.autoFix = true;
            this.resetSize = false;
            this.noImage = false;
            this.clickable = 0;
            this.cacheType = CacheType.LAYOUT;
            this.imageGetter = new DefaultImageGetter();
        }

        /**
         * 绑定到某个tag上，方便下次取用
         *
         * @param tag TAG
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild bind(Object tag) {
            this.tag = new WeakReference<>(tag);
            return this;
        }

        /**
         * 是否图片宽高自动修复自屏宽，默认true
         *
         * @param autoFix autoFix
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild autoFix(boolean autoFix) {
            this.autoFix = autoFix;
            return this;
        }

        /**
         * 不使用img标签里的宽高，img标签的宽高存在才有用
         *
         * @param resetSize false：使用标签里的宽高，不会触发SIZE_READY的回调；true：忽略标签里的宽高，触发SIZE_READY的回调获取尺寸大小。默认为false
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild resetSize(boolean resetSize) {
            this.resetSize = resetSize;
            return this;
        }

        /**
         * 是否开启缓存
         *
         * @param cacheType 默认为NONE
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild cache(@CacheType int cacheType) {
            this.cacheType = cacheType;
            return this;
        }

        /**
         * 手动修复图片宽高
         *
         * @param callback ImageFixCallback回调
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild fix(ImageFixCallback callback) {
            this.imageFixCallback = callback;
            return this;
        }

        /**
         * 链接修复
         *
         * @param callback LinkFixCallback
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild linkFix(LinkFixCallback callback) {
            this.linkFixCallback = callback;
            return this;
        }

        /**
         * 不显示图片
         *
         * @param noImage 默认false
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild noImage(boolean noImage) {
            this.noImage = noImage;
            return this;
        }

        /**
         * 是否屏蔽点击，不进行此项设置只会在设置了点击回调才会响应点击事件
         *
         * @param clickable clickable，false:屏蔽点击事件，true不屏蔽不设置点击回调也可以响应响应的链接默认回调
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild clickable(boolean clickable) {
            this.clickable = clickable ? 1 : -1;
            return this;
        }

        /**
         * 数据源类型
         *
         * @param richType richType
         * @return RichTextConfigBuild
         * @see RichType
         */
        @SuppressWarnings("WeakerAccess")
        public RichViewConfigBuild type(@RichType int richType) {
            this.richType = richType;
            return this;
        }

        /**
         * 图片点击回调
         *
         * @param imageClickListener 回调
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild imageClick(OnImageClickListener imageClickListener) {
            this.onImageClickListener = imageClickListener;
            return this;
        }

        /**
         * 链接点击回调
         *
         * @param onUrlClickListener 回调
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild urlClick(OnUrlClickListener onUrlClickListener) {
            this.onUrlClickListener = onUrlClickListener;
            return this;
        }

        /**
         * 图片长按回调
         *
         * @param imageLongClickListener 回调
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild imageLongClick(OnImageLongClickListener imageLongClickListener) {
            this.onImageLongClickListener = imageLongClickListener;
            return this;
        }

        /**
         * 链接长按回调
         *
         * @param urlLongClickListener 回调
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild urlLongClick(OnUrlLongClickListener urlLongClickListener) {
            this.onUrlLongClickListener = urlLongClickListener;
            return this;
        }

        /**
         * 图片加载过程中的占位图
         *
         * @param placeHolder 占位图
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild placeHolder(Drawable placeHolder) {
            this.placeHolder = placeHolder;
            return this;
        }

        /**
         * 图片加载失败的占位图
         *
         * @param errorImage 占位图
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild error(Drawable errorImage) {
            this.errorImage = errorImage;
            return this;
        }

        /**
         * 图片加载过程中的占位图
         *
         * @param placeHolder 占位图
         * @return RichViewConfigBuild
         */
        public RichViewConfigBuild placeHolder(@DrawableRes int placeHolder) {
            this.placeHolderRes = placeHolder;
            return this;
        }

        /**
         * 图片加载失败的占位图
         *
         * @param errorImage 占位图
         * @return RichTextConfigBuild
         */
        public RichViewConfigBuild error(@DrawableRes int errorImage) {
            this.errorImageRes = errorImage;
            return this;
        }

        /**
         * 设置imageGetter
         *
         * @param imageGetter ig
         * @return RichTextConfigBuild
         * @see ImageGetter
         */
        public RichViewConfigBuild imageGetter(ImageGetter imageGetter) {
            this.imageGetter = imageGetter;
            return this;
        }

        /**
         * 加载并设置给textView
         *
         * @param textView TextView
         * @return RichTextConfigBuild
         */
        public RichView into(TextView textView) {
            if (placeHolder == null && placeHolderRes != 0) {
                try {
                    placeHolder = ContextCompat.getDrawable(textView.getContext(), placeHolderRes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (placeHolder == null) {
                placeHolder = new ColorDrawable(Color.LTGRAY);
            }
            if (errorImage == null && errorImageRes != 0) {
                try {
                    errorImage = ContextCompat.getDrawable(textView.getContext(), errorImageRes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (errorImage == null) {
                errorImage = new ColorDrawable(Color.DKGRAY);
            }
            RichView richText = new RichView(new RichViewConfig(this), textView);
            if (tag != null) {
                RichView.bind(tag.get(), richText);
            }
            this.tag = null;
            richText.generateAndSet();
            return richText;
        }
    }
}
