package com.truyayong.richtext.richview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.support.v7.widget.TintContextWrapper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.truyayong.richtext.richview.ext.HtmlTagHandler;
import com.truyayong.richtext.richview.ext.LongClickableLinkMovementMethod;
import com.truyayong.richtext.richview.ext.MD5;
import com.truyayong.richtext.richview.parser.CachedSpannedParser;
import com.truyayong.richtext.richview.parser.Html2SpannedParser;
import com.truyayong.richtext.richview.parser.ImageGetterWrapper;
import com.truyayong.richtext.richview.parser.ImageLoadNotify;
import com.truyayong.richtext.richview.parser.Markdown2SpannedParser;
import com.truyayong.richtext.richview.parser.SpannedParser;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alley_qiu on 2017/3/24.
 */

public class RichView implements ImageGetterWrapper, ImageLoadNotify {

    private static final LruCache<String, SoftReference<SpannableStringBuilder>> richCache;
    private static final WeakHashMap<Object, HashSet<WeakReference<RichView>>> instances;

    static {
        richCache = new LruCache<>(20);
        instances = new WeakHashMap<>();
    }

    static void bind(Object tag, RichView richText) {
        HashSet<WeakReference<RichView>> richTexts = instances.get(tag);
        if (richTexts == null) {
            richTexts = new HashSet<>();
            instances.put(tag, richTexts);
        }
        richTexts.add(new WeakReference<>(richText));
    }

    /**
     * 清除tag绑定的所有RichText，并清除所有的图片缓存
     *
     * @param tag TAG
     */
    public static void clear(Object tag) {
        HashSet<WeakReference<RichView>> richTexts = instances.get(tag);
        if (richTexts != null) {
            for (WeakReference<RichView> weakReference : richTexts) {
                RichView richText = weakReference.get();
                if (richText != null) {
                    richText.clear();
                }
            }
        }
        instances.remove(tag);
    }

    private static void cache(String source, SpannableStringBuilder ssb) {
        ssb = new SpannableStringBuilder(ssb);
        ssb.setSpan(new CachedSpannedParser.Cached(), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        richCache.put(MD5.generate(source), new SoftReference<>(ssb));
    }

    private static SpannableStringBuilder loadCache(String source) {
        SoftReference<SpannableStringBuilder> cache = richCache.get(MD5.generate(source));
        SpannableStringBuilder ssb = cache == null ? null : cache.get();
        if (ssb != null) {
            return new SpannableStringBuilder(ssb);
        }
        return null;
    }

    private static final String TAG_TARGET = "target";

    private static Pattern IMAGE_TAG_PATTERN = Pattern.compile("<(img|IMG)(.*?)>");
    private static Pattern IMAGE_WIDTH_PATTERN = Pattern.compile("(width|WIDTH)=\"(.*?)\"");
    private static Pattern IMAGE_HEIGHT_PATTERN = Pattern.compile("(height|HEIGHT)=\"(.*?)\"");
    private static Pattern IMAGE_SRC_PATTERN = Pattern.compile("(src|SRC)=\"(.*?)\"");

    private HashMap<String, ImageHolder> imageHolderMap;

    @RichState
    private int state = RichState.ready;

    private final SpannedParser spannedParser;
    private final CachedSpannedParser cachedSpannedParser;
    private final SoftReference<TextView> textViewSoftReference;
    private final RichViewConfig config;
    private int count;
    private int loadingCount;
    private SoftReference<SpannableStringBuilder> richText;

    RichView(RichViewConfig config, TextView textView) {
        this.config = config;
        this.textViewSoftReference = new SoftReference<>(textView);
        if (config.richType == RichType.MARKDOWN) {
            spannedParser = new Markdown2SpannedParser(textView);
        } else {
            spannedParser = new Html2SpannedParser(new HtmlTagHandler(textView));
        }
        if (config.clickable > 0) {
            textView.setMovementMethod(new LongClickableLinkMovementMethod());
        } else if (config.clickable == 0) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        this.cachedSpannedParser = new CachedSpannedParser();
    }

    public static RichViewConfig.RichViewConfigBuild from(String source) {
        return fromHtml(source);
    }

    public static RichViewConfig.RichViewConfigBuild fromHtml(String source) {
        return from(source, RichType.HTML);
    }

    public static RichViewConfig.RichViewConfigBuild fromMarkdown(String source) {
        return from(source, RichType.MARKDOWN);
    }

    public static RichViewConfig.RichViewConfigBuild from(String source, @RichType int richType) {
        return new RichViewConfig.RichViewConfigBuild(source, richType);
    }

    void generateAndSet() {
        final TextView textView = textViewSoftReference.get();
        if (textView != null) {
            textView.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(generateRichText());
                }
            });
        }
    }

    /**
     * 生成富文本
     *
     * @return Spanned
     */
    private CharSequence generateRichText() {
        TextView textView = textViewSoftReference.get();
        if (textView == null) {
            return null;
        }
        if (config.richType != RichType.MARKDOWN) {
            analyzeImages(config.source);
        } else {
            imageHolderMap = new HashMap<>();
        }
        SpannableStringBuilder spannableStringBuilder = null;
        if (config.cacheType > CacheType.NONE) {
            spannableStringBuilder = loadCache(config.source);
        }
        if (spannableStringBuilder == null) {
            spannableStringBuilder = parseRichText();
        }
        richText = new SoftReference<>(spannableStringBuilder);
        config.imageGetter.registerImageLoadNotify(this);
        count = cachedSpannedParser.parse(spannableStringBuilder, this, config);
        return spannableStringBuilder;
    }

    @NonNull
    private SpannableStringBuilder parseRichText() {
        SpannableStringBuilder spannableStringBuilder;
        state = RichState.loading;
        String source = config.source;

        Spanned spanned = spannedParser.parse(source);
        if (spanned instanceof SpannableStringBuilder) {
            spannableStringBuilder = (SpannableStringBuilder) spanned;
        } else {
            spannableStringBuilder = new SpannableStringBuilder(spanned);
        }
        return spannableStringBuilder;
    }

    /**
     * 从文本中拿到<img/>标签,并获取图片url和宽高
     */
    private synchronized void analyzeImages(String text) {
        imageHolderMap = new HashMap<>();
        ImageHolder holder;
        int position = 0;
        Matcher imageTagMatcher = IMAGE_TAG_PATTERN.matcher(text);
        while (imageTagMatcher.find()) {
            String image = imageTagMatcher.group(2).trim();
            Matcher imageSrcMatcher = IMAGE_SRC_PATTERN.matcher(image);
            String src = null;
            if (imageSrcMatcher.find()) {
                src = imageSrcMatcher.group(2).trim();
            }
            if (TextUtils.isEmpty(src)) {
                continue;
            }
            holder = new ImageHolder(src, position);
            Matcher imageWidthMatcher = IMAGE_WIDTH_PATTERN.matcher(image);
            if (imageWidthMatcher.find()) {
                holder.setWidth(parseStringToInteger(imageWidthMatcher.group(2).trim()));
            }
            Matcher imageHeightMatcher = IMAGE_HEIGHT_PATTERN.matcher(image);
            if (imageHeightMatcher.find()) {
                holder.setHeight(parseStringToInteger(imageHeightMatcher.group(2).trim()));
            }
            imageHolderMap.put(holder.getSource(), holder);
            position++;
        }
    }

    /**
     * 判断Activity是否已经结束
     *
     * @param context context
     * @return true：已结束
     */
    private static boolean activityIsAlive(Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof TintContextWrapper) {
            context = ((TintContextWrapper) context).getBaseContext();
        }
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return false;
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) context).isDestroyed()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int parseStringToInteger(String integerStr) {
        int result = -1;
        if (!TextUtils.isEmpty(integerStr)) {
            try {
                result = Integer.parseInt(integerStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static boolean isGif(String path) {
        int index = path.lastIndexOf('.');
        return index > 0 && "gif".toUpperCase().equals(path.substring(index + 1).toUpperCase());
    }

    /**
     * 回收所有图片和任务
     */
    public void clear() {
        TextView textView = textViewSoftReference.get();
        if (textView != null) {
            textView.setText(null);
        }
        config.imageGetter.recycle();
    }


    /**
     * 获取解析的状态
     *
     * @return state
     * @see RichState
     */
    @RichState
    public int getState() {
        return state;
    }

    @Override
    public Drawable getDrawable(String source) {
        loadingCount++;
        if (config.imageGetter == null) {
            return null;
        }
        if (config.noImage) {
            return null;
        }
        TextView textView = textViewSoftReference.get();
        if (textView == null) {
            return null;
        }
        // 判断activity是否已结束
        if (!activityIsAlive(textView.getContext())) {
            return null;
        }
        ImageHolder holder;
        if (config.richType == RichType.MARKDOWN) {
            holder = new ImageHolder(source, loadingCount - 1);
            imageHolderMap.put(source, holder);
        } else {
            holder = imageHolderMap.get(source);
            if (holder == null) {
                holder = new ImageHolder(source, loadingCount - 1);
                imageHolderMap.put(source, holder);
            }
        }
        if (isGif(holder.getSource())) {
            holder.setImageType(ImageHolder.ImageType.GIF);
        } else {
            holder.setImageType(ImageHolder.ImageType.JPG);
        }
        holder.setImageState(ImageHolder.ImageState.INIT);
        if (!config.autoFix && config.imageFixCallback != null) {
            config.imageFixCallback.onFix(holder);
            if (!holder.isShow()) {
                return null;
            }
        } else {
            int w = textView.getWidth() - textView.getPaddingLeft() - textView.getPaddingRight();
            if (holder.getWidth() > w) {
                float r = (float) w / holder.getWidth();
                holder.setWidth(w);
                holder.setHeight((int) (r * holder.getHeight()));
            }
        }
        return config.imageGetter.getDrawable(holder, config, textView);
    }

    @Override
    public void done(Object from) {
        if (from instanceof Integer) {
            int loadedCount = (int) from;
            if (loadedCount >= count) {
                state = RichState.loaded;
                if (config.cacheType >= CacheType.LAYOUT) {
                    SpannableStringBuilder ssb = richText.get();
                    if (ssb != null) {
                        cache(config.source, ssb);
                    }
                }
            }
        }
    }

}
