package com.osan.nebula.anim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.osan.nebula.R;
import com.osan.nebula.util.Utils;

/**
 * Created by osan on 9/21.
 */

public class StarryView extends View {

    private static final String TAG = "StarryView";

    private int scwidth, halfWidth, thirdWidth, vortexHalfWidth;
    private int scheight, halfHeight, vortexHalfHeight;

    /**
     * 定制画笔
     */
    private Paint paintBg = new Paint();
    private Paint paintSky = new Paint();
    private Paint paintVortex = new Paint();
    private Paint paintCircleSolid = new Paint();
    private Paint paintCircleHollow = new Paint();
    private Paint paintCircleStar = new Paint();
    private Paint paintText = new Paint();
    private Paint paintTextTmp = new Paint();
    /**
     * app小水波纹画笔
     */
    private Paint paintWave1 = new Paint();
    /**
     * 默认画笔
     */
    private Paint paint = new Paint();

    private Bitmap vortex, sky_layer1, sky_layer2, light, bg, particallevel1;
    private float mValue1, mValue2, mValue3, mValue4, mValue5, mValue6, mValue7, mValue8, mValue9;
    private float mValueMax = 1.0f;
    private int mIntValueMax = 100;
    /**
     * 单位：毫秒
     */
    private long firstOverlay = 1000;
    private long timeFirstStep = 1000;
    private long timeSecondStep = 4500;
    private long timeThirdStep = 800;
    private long timeFourthStep = 2000;
    private long timeFifthStep = 10000;
    private long timeSixthStep = 1000;
    private long timeSeventhStep = 1000;
    private long timeEighthStep = 800;
    private long timeNinthStep = 3000;

    /**
     * ValueAnimator
     */
    private ValueAnimator valueAnimator1;
    private ValueAnimator valueAnimator2;
    private ValueAnimator valueAnimator3;
    private ValueAnimator valueAnimator4;
    private ValueAnimator valueAnimator5;
    private ValueAnimator valueAnimator6;
    private ValueAnimator valueAnimator7;
    private ValueAnimator valueAnimator8;
    private ValueAnimator valueAnimator9;

    /**
     * 小星星半径
     */
    private float radiusStar = Utils.dip2px(8);
    /**
     * 水波纹实心圆半径
     */
    private float radiusMask = Utils.dip2px(33);
    /**
     * 水波纹小圆半径
     */
    private float radiusSm = Utils.dip2px(66);
    /**
     * 水波纹大圆半径
     */
    private float radiusLg = Utils.dip2px(133);
    /**
     * app小水波纹半径
     */
    private float radiusAppWave = Utils.dip2px(13);
    /**
     * 波纹线宽
     */
    private float circleStroke = Utils.dip2px(1.3f);
    /**
     * 文本距离小星星的垂直间距
     */
    private float textVer = Utils.dip2px(20);
    /**
     * 文本字体大小
     */
    private float textSize = Utils.dip2px(14);

    /**
     * app名称
     */
    private String[] appNameInner = {"Facebook", "Twitter", "Instagram", "Snapshot", "Whatapp", "Youtube"};
    private String[] appNameOuter = {"Gmail", "Aello", "Photo", "Message", "CleanMaster", "momoda"};
    private String[] appNameInnerTmp, appNameOuterTmp;
    /**
     * app名称旋转角度
     */
    private double a = 0;
    /**
     * 刷新应用圈
     */
    private boolean isRefresh = false;
    /**
     * 切换内外圈的应用名称
     */
    private boolean appSwitch = false;
    /**
     * 切换app小水波纹
     */
    private int waveRandom = 0;
    /**
     * app小水波纹重复次数
     */
    private int waveRepeatMax = 4;
    /**
     * app小水波纹重复次数计数器
     */
    private int waveRepeatInit = 0;

    private int appNameIndex = 0;

    /**
     * 动画参数数据
     */
    private Rect srcVortex, srcBg, src_sky_layer1, src_sky_layer2, dst_sky_layer2, srcLight, src_vortex, srcparticallevel1;
    private RectF dstFVortex, dstBg, dst_sky_layer1, dstLight, dst_vortex, dstparticallevel1;
    private RectF dst_bg;
    private ColorMatrix cMatrix;
    private float sqrtMask;
    private float halfMask;
    private float coord1, coord2, coord3, coord4;
    private float sqrt3;
    private double pi;


    public StarryView(Context context) {
        this(context, null);
    }

    public StarryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initValue();

        initPaint();

        initBitmap();

        initAnimation();

        initData();
    }

    /**
     * 暂停全部动画
     */
    public void pauseAnimation() {
        try {
            if (valueAnimator1 != null) valueAnimator1.pause();
            if (valueAnimator2 != null) valueAnimator2.pause();
            if (valueAnimator3 != null) valueAnimator3.pause();
            if (valueAnimator4 != null) valueAnimator4.pause();
            if (valueAnimator5 != null) valueAnimator5.pause();
            if (valueAnimator6 != null) valueAnimator6.pause();
            if (valueAnimator7 != null) valueAnimator7.pause();
            if (valueAnimator8 != null) valueAnimator8.pause();
            if (valueAnimator9 != null) valueAnimator9.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复全部动画
     */
    public void resumeAnimation() {
        if (valueAnimator1 != null) valueAnimator1.resume();
        if (valueAnimator2 != null) valueAnimator2.resume();
        if (valueAnimator3 != null) valueAnimator3.resume();
        if (valueAnimator4 != null) valueAnimator4.resume();
        if (valueAnimator5 != null) valueAnimator5.resume();
        if (valueAnimator6 != null) valueAnimator6.resume();
        if (valueAnimator7 != null) valueAnimator7.resume();
        if (valueAnimator8 != null) valueAnimator8.resume();
        if (valueAnimator9 != null) valueAnimator9.resume();
    }

    /**
     * 停止全部动画
     */
    public void stopAnimation() {

        if (valueAnimator1 != null) valueAnimator1.end();
        if (valueAnimator2 != null) valueAnimator2.end();
        if (valueAnimator3 != null) valueAnimator3.end();
        if (valueAnimator4 != null) valueAnimator4.end();
        if (valueAnimator5 != null) valueAnimator5.end();
        if (valueAnimator6 != null) valueAnimator6.end();
        if (valueAnimator7 != null) valueAnimator7.end();
        if (valueAnimator8 != null) valueAnimator8.end();
        if (valueAnimator9 != null) valueAnimator9.end();

    }

    /**
     * 将获取屏幕宽高数值抽取出来,只需获取一次
     */
    private void initValue() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        scwidth = metric.widthPixels;     // 屏幕宽度（像素）
        scheight = metric.heightPixels;   // 屏幕高度（像素）
        halfWidth = scwidth / 2;
        halfHeight = scheight / 2;
        thirdWidth = scwidth / 3;

        sqrtMask = (float) Math.sqrt(3) / 2 * radiusMask;
        halfMask = radiusMask / 2;

    }

    /**
     * 初始化动画参数值
     * 可复用的动画参数直接初始化，这可以有效减小onDraw中计算量，降低CPU计算负荷，提高GPU渲染速度
     */
    private void initData() {

        vortexHalfWidth = vortex.getWidth() / 2;
        vortexHalfHeight = vortex.getHeight() / 2;

        srcVortex = new Rect(0, 0, vortex.getWidth(), vortex.getHeight());
        dstFVortex = new RectF(0, 0, 0, 0);

        srcBg = new Rect(0, 0, bg.getWidth(), bg.getHeight());
        dstBg = new RectF(-halfWidth, -halfHeight, halfWidth, halfHeight);

        src_sky_layer1 = new Rect(0, 0, sky_layer1.getWidth(), sky_layer1.getHeight());
        dst_sky_layer1 = new RectF(-halfWidth, -halfHeight, halfWidth, halfHeight);
        src_sky_layer2 = new Rect(0, 0, sky_layer2.getWidth(), sky_layer2.getHeight());
        dst_sky_layer2 = new Rect(-thirdWidth, -thirdWidth, thirdWidth, thirdWidth);

        srcLight = new Rect(0, 0, light.getWidth(), light.getHeight());
        float tempX = light.getWidth() / 2;
        float tempY = light.getHeight() / 2;
        dstLight = new RectF(-tempX, -tempY, tempX, tempY);

        src_vortex = new Rect(0, 0, vortex.getWidth(), vortex.getHeight());
        dst_vortex = new RectF(0, 0, 0, 0);

        cMatrix = new ColorMatrix();

        dst_bg = new RectF(0, 0, 0, 0);

        srcparticallevel1 = new Rect(0, 0, particallevel1.getWidth(), particallevel1.getHeight());
        dstparticallevel1 = new RectF(0, 0, 0, 0);

        coord1 = (float) Math.sqrt(3) / 2 * radiusSm;
        coord2 = (float) Math.sqrt(3) / 2 * radiusLg;
        coord3 = radiusSm / 2;
        coord4 = radiusLg / 2;

        sqrt3 = (float) Math.sqrt(3) / 2;
        pi = Math.PI / 180;


    }

    /**
     * 初始化画笔
     * 可复用画笔放在初始化处理，避免在onDraw中处理，一切为了效率
     */
    private void initPaint() {

        paintCircleSolid.setColor(this.getContext().getResources().getColor(R.color.mask_bg_color));
        paintCircleSolid.setAntiAlias(true);
        //绘制水波纹(双层:实心半透黑色波纹+加速放大灰白色水波纹)
        paintCircleHollow.setColor(this.getContext().getResources().getColor(R.color.circle_color));
        paintCircleHollow.setStrokeWidth(circleStroke);
        paintCircleHollow.setStyle(Paint.Style.STROKE);
        paintCircleHollow.setAntiAlias(true);

        paintCircleStar.setColor(this.getContext().getResources().getColor(R.color.star_color));
        paintCircleStar.setAntiAlias(true);
        //需关闭硬件加速才能生效(矛盾的是，关闭硬件加速后，GPU渲染耗时飙升，二选一，先忽略光晕效果)
        paintCircleStar.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));

        paintText.setAntiAlias(true);
        paintText.setTextSize(textSize);
        paintText.setColor(Color.GRAY);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintTextTmp.setAntiAlias(true);
        paintTextTmp.setTextSize(textSize);
        paintTextTmp.setColor(Color.GRAY);
        paintTextTmp.setTextAlign(Paint.Align.CENTER);

        //app小水波纹画笔
        paintWave1.setColor(this.getContext().getResources().getColor(R.color.wave_color));
        paintWave1.setStrokeWidth(circleStroke);
        paintWave1.setStyle(Paint.Style.STROKE);
        paintWave1.setAntiAlias(true);

    }

    /**
     * 图片资源初始化
     */
    private void initBitmap() {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inSampleSize = 1;

        light = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.light, localOptions);

        vortex = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.vortex, localOptions);
        sky_layer2 = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.sky_layer2, localOptions);

        particallevel1 = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.particallevel1, localOptions);

        localOptions.inSampleSize = 2;
        localOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        sky_layer1 = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.sky_layer1, localOptions);
        bg = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.bg, localOptions);
    }

    /**
     * 图片资源释放
     */
    public void releaseBitmap() {
        if (vortex != null) vortex.recycle();
        vortex = null;
        if (sky_layer1 != null) sky_layer1.recycle();
        sky_layer1 = null;
        if (sky_layer2 != null) sky_layer2.recycle();
        sky_layer2 = null;
        if (light != null) light.recycle();
        light = null;

    }

    /**
     * 图片资源释放
     */
    public void releaseBitmapEx() {
        if (bg != null) bg.recycle();
        bg = null;
        if (particallevel1 != null) particallevel1.recycle();
        particallevel1 = null;

    }



    /**
     * 简化动画, 简化为一个ValueAnimator
     * 数值变化范围是 1 - 2
     * 规定 0 - 1 为阶段1
     * 规定 1 - 2 为阶段2
     * 根据数值绘制不同的内容
     */
    private void initAnimation() {

        valueAnimator1 = ValueAnimator.ofFloat(0f, mValueMax);

        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue1 = (float) animation.getAnimatedValue();
                invalidate();   // 重绘
            }
        });

        valueAnimator1.setDuration(timeFirstStep + firstOverlay);
        valueAnimator1.start();

        //2
        valueAnimator2 = ValueAnimator.ofFloat(0f, mValueMax);

        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue2 = (float) animation.getAnimatedValue();
                Log.d(TAG, "mValue2:" + mValue2);
                invalidate();
            }
        });

        valueAnimator2.setDuration(timeSecondStep);
        valueAnimator2.setStartDelay(timeFirstStep);
        valueAnimator2.setInterpolator(new AccelerateInterpolator());
        valueAnimator2.start();

        //3
        valueAnimator3 = ValueAnimator.ofFloat(0f, mValueMax);

        valueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue3 = (float) animation.getAnimatedValue();
                invalidate();   // 重绘
            }
        });

        valueAnimator3.setDuration(timeThirdStep);
        valueAnimator3.setStartDelay(timeFirstStep + timeSecondStep);
        valueAnimator3.setInterpolator(new LinearInterpolator());
        valueAnimator3.start();

        //4
        valueAnimator4 = ValueAnimator.ofFloat(0f, mValueMax);

        valueAnimator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue4 = (float) animation.getAnimatedValue();
                invalidate();   // 重绘
            }
        });

        valueAnimator4.setDuration(timeFourthStep);
        valueAnimator4.setStartDelay(timeFirstStep + timeSecondStep + timeThirdStep);
        valueAnimator4.start();

        //5
        valueAnimator5 = ValueAnimator.ofFloat(0f, mValueMax);

        valueAnimator5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mValue5 = (float) valueAnimator.getAnimatedValue();
                invalidate();   // 重绘
            }
        });

        valueAnimator5.setDuration(timeFifthStep);
        valueAnimator5.setStartDelay(timeFirstStep + timeSecondStep + timeFourthStep);
        valueAnimator5.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator5.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator5.start();

        //6
        valueAnimator6 = ValueAnimator.ofFloat(0f, mValueMax);

        valueAnimator6.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mValue6 = (float) valueAnimator.getAnimatedValue();
                if (mValue6 == 1.f) {
                    releaseBitmap();
                    releaseValueAnimator();
                }
                invalidate();   // 重绘
            }
        });

        valueAnimator6.setDuration(timeSixthStep);
        valueAnimator6.setStartDelay(timeFirstStep + timeSecondStep + timeFourthStep);
        valueAnimator6.start();

        //7
        valueAnimator7 = ValueAnimator.ofFloat(0f, mValueMax);

        valueAnimator7.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mValue7 = (float) valueAnimator.getAnimatedValue();
                invalidate();   // 重绘
            }
        });

        valueAnimator7.addListener(new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                appNameIndex += 5;
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        valueAnimator7.setDuration(timeSeventhStep);
        valueAnimator7.setStartDelay(timeFirstStep + timeSecondStep + timeFourthStep + timeSixthStep + timeNinthStep);
        valueAnimator7.setInterpolator(new AccelerateInterpolator());
        valueAnimator7.start();

        //8
        valueAnimator8 = ValueAnimator.ofFloat(0f, mValueMax);

        valueAnimator8.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mValue8 = (float) valueAnimator.getAnimatedValue();
                invalidate();   // 重绘
            }
        });

        valueAnimator8.setDuration(timeEighthStep);
        valueAnimator8.setInterpolator(new AccelerateInterpolator());

        //9
        valueAnimator9 = ValueAnimator.ofInt(0, mIntValueMax);

        valueAnimator9.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mValue9 = (int) valueAnimator.getAnimatedValue();
                invalidate();   // 重绘
            }
        });
        valueAnimator9.addListener(new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (waveRepeatInit++ % waveRepeatMax == 0) {
                    waveRandom = (int) (Math.random() * 12);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (waveRepeatInit % 8 == 0) {

                    if (!valueAnimator7.isRunning()) {
                        waveRepeatInit = 0;
                        mValue9 = 0;
                        mValue7 = 0.f;
                        valueAnimator7.setStartDelay(0);
                        valueAnimator7.start();
                        isRefresh = true;
                        valueAnimator8.start();
                        appSwitch = !appSwitch;
                    }
                } else {
                    valueAnimator9.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        valueAnimator9.setDuration(timeNinthStep);
    }

    /**
     * 资源释放
     */
    private void releaseValueAnimator() {
        valueAnimator1 = null;
        valueAnimator2 = null;
        valueAnimator3 = null;
        valueAnimator4 = null;
        valueAnimator5 = null;
    }

    /**
     * * 绘制时序及动画时序
     * <p>
     * 阶段一 绘制:漩涡 动画:放大 1-4倍  时长:500ms 时间区间:0-500ms
     * <p>
     * 阶段二 绘制:背景 动画:透明 0%-100%  时长:800ms 时间区间:700-1500ms
     * 阶段二 绘制:星空 动画:透明 0%-100%  时长:200ms 时间区间:500-700ms
     * 阶段二 绘制:星空 动画:放大 1-3倍  时长:1000ms  时间区间:500-1500ms
     * 阶段二 绘制:星云 动画:放大 1-3倍 旋转 0-100度  时长:1000ms  时间区间:500-1500ms
     * 阶段二 绘制:星光 动画:放大 0-4倍  时长:1000ms  时间区间:500-1500ms
     * <p>
     * 阶段三 绘制:无 动画:透明 100%-0%  时长:100ms 时间区间:1500-1600ms 动画标的:星空 星云 星光
     * 阶段三 绘制:无 动画:放大 0-0.7倍  时长:300ms 时间区间:1500-1800ms 动画标的:漩涡
     * 阶段三 绘制:无 动画:透明 100%-0%  时长:100ms 时间区间:1800-1900ms 动画标的:漩涡
     * <p>
     * 阶段四 绘制:无 动画:变暗 100%-45% 放大 1-1.3倍 时长:500ms 时间区间:1800-2300ms 动画标的:背景
     * <p>
     * 阶段五 绘制:星团 动画:放大 1-2倍 时长:2000ms 时间区间:2300-4300ms
     * 阶段五 绘制:水波纹 动画:放大 时长: 时间区间:
     * <p>
     * 阶段六 初始化绘制内外圈星星及文字
     * <p>
     * 阶段七 绘制内外圈星星及文字，并负责绘制动态旋转放大
     * <p>
     * 阶段八 绘制星团(一次性动画)
     * <p>
     * 阶段九 绘制某个app的动态小水波纹
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mValue1 < mValueMax / 2) {

            drawState1(canvas);

        } else if (mValue1 < mValueMax) {

            drawState2(canvas);

        } else if (mValue2 < mValueMax) {

            drawState2(canvas);

        } else if (mValue3 < mValueMax) {

            drawState3(canvas);

        } else if (mValue4 < mValueMax) {

            drawState4(canvas);

        } else {

            drawState5(canvas);

            if (mValue6 < mValueMax) {

                drawState6(canvas);

            } else if (mValue7 <= mValueMax) {

                if (!valueAnimator9.isStarted()) {

                    valueAnimator9.start();
                }

                drawState9(canvas);

                if (isRefresh) {
//                    drawState8(canvas);
                    if (mValue7 == mValueMax) {

                        isRefresh = false;
                    }
                }

                drawState7(canvas);
            }
        }
    }

    /**
     * 阶段一
     * 绘制漩涡,主要控制放大
     *
     * @param canvas
     */
    private void drawState1(Canvas canvas) {
        canvas.save();
        canvas.translate(halfWidth, halfHeight);

        float scale = (mValue1 * 9) + 1; // 缩放比例 1 - 4
        float tempX = vortexHalfWidth * scale;
        float tempY = vortexHalfHeight * scale;
        dstFVortex.set(-tempX, -tempY, tempX, tempY);

        canvas.drawBitmap(vortex, srcVortex, dstFVortex, paint);

        canvas.restore();
    }

    /**
     * 阶段二 绘制星空和亮光
     * 绘制星空背景 - 放大
     * 绘制星空漩涡 - 放大+旋转
     * 绘制亮光    - 放大
     *
     * @param canvas
     */
    private void drawState2(Canvas canvas) {

        // 绘制 bg
        if (mValue2 < 200.f / timeSecondStep) {
            paintBg.setAlpha((int) (255 * 0.0f));       // 设置了画笔透明度
        } else {
            paintBg.setAlpha((int) (255 * mValue2));       // 设置了画笔透明度
        }

        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        canvas.drawBitmap(bg, srcBg, dstBg, paintBg);

        canvas.restore();


        // 绘制 sky_layer
        paint.setAlpha((int) (255 * 0.6f));       // 设置了画笔透明度

        if (mValue2 < 0.6f) {
            paintSky.setAlpha((int) (255 * 0.6f));       // 设置了画笔透明度
        } else {
            paintSky.setAlpha((int) (255 * mValue2));       // 设置了画笔透明度
        }

        float tempValue = mValue2;
        float scale = (tempValue * 4) + 1;         // 缩放比例 1 - 3

        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        canvas.scale(scale, scale);              // 放大

        canvas.drawBitmap(sky_layer1, src_sky_layer1, dst_sky_layer1, paintSky);

        canvas.rotate(tempValue * 720f);          // 旋转

        canvas.drawBitmap(sky_layer2, src_sky_layer2, dst_sky_layer2, paintSky);

        canvas.restore();

        // 绘制light
        canvas.save();
        float tempScale = (tempValue * 8);        // 缩放比例 0 - 4
        canvas.translate(halfWidth, halfHeight);
        canvas.scale(tempScale, tempScale);      // 缩放


        canvas.drawBitmap(light, srcLight, dstLight, paintSky);

        canvas.restore();


    }


    /**
     * 阶段三
     * 过渡渐出
     *
     * @param canvas
     */
    private void drawState3(Canvas canvas) {

        // 绘制 bg
        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        canvas.drawBitmap(bg, srcBg, dstBg, paintBg);
        canvas.restore();

        // 绘制 sky_layer
        if (mValue3 < 0.55f) {
            paintSky.setAlpha((int) (255 * (1 - mValue3 * 1.3)));       // 设置了画笔透明度
        } else {
            paintSky.setAlpha((int) (255 * (0.0f)));       // 设置了画笔透明度
        }

        float tempValue = mValue2 - mValue3;
        float scale = (tempValue * 4) + 1;         // 缩放比例 1 - 3

        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        canvas.scale(scale, scale);              // 放大
        canvas.drawBitmap(sky_layer1, src_sky_layer1, dst_sky_layer1, paintSky);
        canvas.rotate((mValue2 + mValue3) * 720f);          // 旋转
        canvas.drawBitmap(sky_layer2, src_sky_layer2, dst_sky_layer2, paintSky);
        canvas.restore();

        // 绘制light
        canvas.save();
        float tempScale = (tempValue * 8);        // 缩放比例 0 - 4
        canvas.translate(halfWidth, halfHeight);
        canvas.scale(tempScale, tempScale);      // 缩放
        canvas.drawBitmap(light, srcLight, dstLight, paintSky);
        canvas.restore();


        //绘制最后的漩涡并消失
        if (mValue3 < 0.75f) {
            paintVortex.setAlpha((int) (255 * (1)));       // 设置了画笔透明度
        } else {
            paintVortex.setAlpha((int) (255 * (1 - mValue3)));       // 设置了画笔透明度
        }

        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        float scaleVortex = (mValue3 * 0.7f); // 缩放比例 0 - 0.7
        float tempXVortex = vortexHalfWidth * scaleVortex;
        float tempYVortex = vortexHalfHeight * scaleVortex;
        dst_vortex.set(-tempXVortex, -tempYVortex, tempXVortex, tempYVortex);
        canvas.drawBitmap(vortex, src_vortex, dst_vortex, paint);
        canvas.restore();


    }

    /**
     * 阶段四
     * 绘制背景变暗 慢速微放大
     *
     * @param canvas
     */
    private void drawState4(Canvas canvas) {

        // 绘制 bg
        float contrast = (1.f - mValue4 > 0.4f) ? (1.f - mValue4) : 0.4f;// 对比度
        float brightness = 0;// 亮度

        cMatrix.set(new float[]{contrast, 0, 0, 0, brightness,
                0, contrast, 0, 0, brightness,
                0, 0, contrast, 0, brightness,
                0, 0, 0, 1, 0});

        paintBg.setColorFilter(new ColorMatrixColorFilter(cMatrix));

        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        float scale = (mValue4 * 0.3f) + 1; // 缩放比例 1 - 1.3
        float tempX = halfWidth * scale;
        float tempY = halfHeight * scale;
        dst_bg.set(-tempX, -tempY, tempX, tempY);
        canvas.drawBitmap(bg, srcBg, dst_bg, paintBg);
        canvas.restore();
    }

    /**
     * 阶段五
     * 绘制星团
     *
     * @param canvas
     */
    private void drawState5(Canvas canvas) {

        drawState4(canvas);//背景状态保持

        //绘制星团
        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        float scale = (mValue5 * 5f) + 1; // 缩放比例 1 - 6
        float tempX = halfWidth * scale;
        float tempY = halfHeight * scale;
        dstparticallevel1.set(-tempX, -tempY, tempX, tempY);
        canvas.drawBitmap(particallevel1, srcparticallevel1, dstparticallevel1, paintBg);//共用背景画笔,保持对比度一致
        canvas.restore();
    }

    /**
     * 阶段六 初始化绘制内外圈星星及文字
     *
     * @param canvas
     */
    private void drawState6(Canvas canvas) {
        //绘制水波纹(双层:实心半透黑色波纹+加速放大灰白色水波纹)
        float radius2 = mValue6 * radiusSm;
        float radius3 = mValue6 * radiusLg;
        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        canvas.drawCircle(0, 0, radius2, paintCircleHollow);
        canvas.drawCircle(0, 0, radius3, paintCircleHollow);
        canvas.restore();

        float radius = mValue6 * radiusMask;
        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        canvas.drawCircle(0, 0, radius, paintCircleSolid);
        canvas.restore();

        if (mValue6 == mValueMax) {

            canvas.save();
            canvas.translate(halfWidth, halfHeight);
            //绘制内圈星星
            canvas.drawCircle(-radius2, 0, radiusStar, paintCircleStar);
            canvas.drawCircle(radius2, 0, radiusStar, paintCircleStar);

            float x = radius2 / 2;
            float y = (float) Math.sqrt(3) / 2 * radius2;
            canvas.drawCircle(-x, -y, radiusStar, paintCircleStar);
            canvas.drawCircle(-x, y, radiusStar, paintCircleStar);
            canvas.drawCircle(x, -y, radiusStar, paintCircleStar);
            canvas.drawCircle(x, y, radiusStar, paintCircleStar);
            //绘制内圈星星名字
            canvas.drawText(appNameInner[0], -radius2, textVer, paintText);
            canvas.drawText(appNameInner[1], radius2, textVer, paintText);
            canvas.drawText(appNameInner[2], -x, -y + textVer, paintText);
            canvas.drawText(appNameInner[3], -x, y + textVer, paintText);
            canvas.drawText(appNameInner[4], x, -y + textVer, paintText);
            canvas.drawText(appNameInner[5], x, y + textVer, paintText);

            //绘制外圈星星
            float xx = (float) Math.sqrt(3) / 2 * radius3;
            float yy = radius3 / 2;
            canvas.drawCircle(0, -radius3, radiusStar, paintCircleStar);
            canvas.drawCircle(0, radius3, radiusStar, paintCircleStar);
            canvas.drawCircle(-xx, -yy, radiusStar, paintCircleStar);
            canvas.drawCircle(-xx, yy, radiusStar, paintCircleStar);
            canvas.drawCircle(xx, -yy, radiusStar, paintCircleStar);
            canvas.drawCircle(xx, yy, radiusStar, paintCircleStar);

            //绘制外圈星星名字
            canvas.drawText(appNameOuter[0], 0, -radius3 + textVer, paintText);
            canvas.drawText(appNameOuter[1], 0, radius3 + textVer, paintText);
            canvas.drawText(appNameOuter[2], -xx, -yy + textVer, paintText);
            canvas.drawText(appNameOuter[2], -xx, yy + textVer, paintText);
            canvas.drawText(appNameOuter[2], xx, -yy + textVer, paintText);
            canvas.drawText(appNameOuter[2], xx, yy + textVer, paintText);


            canvas.restore();
        }
    }

    /**
     * 阶段七 绘制内外圈星星及文字，并负责绘制动态旋转放大
     *
     * @param canvas
     */

    private void drawState7(Canvas canvas) {

        float radius2 = mValue6 * radiusSm;
        float radius3 = mValue6 * radiusLg;

        //绘制水波纹(双层:实心半透黑色波纹+加速放大灰白色水波纹)
        //绘制动态放大的第二三圈
        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        float scale = (mValue7 * 1.f) + 1;         // 缩放比例 1 - 2
        canvas.scale(scale, scale);
        canvas.rotate(mValue7 * 30f);
        canvas.drawCircle(0, 0, radiusMask, paintCircleHollow);
        canvas.drawCircle(0, 0, radius2, paintCircleHollow);
        canvas.restore();

        //绘制无限放大并在主屏上不在显示的第三圈
        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        float scale3 = (mValue7 * 2.f) + 1;         // 缩放比例 1 - 2
        canvas.scale(scale3, scale3);
        canvas.rotate(mValue7 * 30f);
        canvas.drawCircle(0, 0, radius3, paintCircleHollow);
        canvas.restore();

        float radius = mValue6 * radiusMask;
        //绘制阴影圈
        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        canvas.drawCircle(0, 0, radius, paintCircleSolid);
        canvas.restore();

        paintTextTmp.setTextSize(textSize * mValue7);

        canvas.save();
//        setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
        canvas.translate(halfWidth, halfHeight);
        canvas.scale(scale, scale);
        canvas.rotate(30f * mValue7);

        {
            //绘制内圈星星

            float x = radius2 / 2;
            float y = (float) Math.sqrt(3) / 2 * radius2;
            float tmpRadius = radiusStar / scale;
            canvas.drawCircle(-radius2, 0, tmpRadius, paintCircleStar);
            canvas.drawCircle(radius2, 0, tmpRadius, paintCircleStar);
            canvas.drawCircle(-x, -y, tmpRadius, paintCircleStar);
            canvas.drawCircle(-x, y, tmpRadius, paintCircleStar);
            canvas.drawCircle(x, -y, tmpRadius, paintCircleStar);
            canvas.drawCircle(x, y, tmpRadius, paintCircleStar);
        }
        canvas.restore();

        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        canvas.scale(scale3, scale3);
        canvas.rotate(30f * mValue7);
        {
            //绘制外圈星星
            float x = (float) Math.sqrt(3) / 2 * radius3;
            float y = radius3 / 2;
            float tmpRadius3 = radiusStar / scale;
            canvas.drawCircle(0, -radius3, tmpRadius3, paintCircleStar);
            canvas.drawCircle(0, radius3, tmpRadius3, paintCircleStar);
            canvas.drawCircle(-x, -y, tmpRadius3, paintCircleStar);
            canvas.drawCircle(-x, y, tmpRadius3, paintCircleStar);
            canvas.drawCircle(x, -y, tmpRadius3, paintCircleStar);
            canvas.drawCircle(x, y, tmpRadius3, paintCircleStar);
        }
        canvas.restore();

        if (!appSwitch) {
            appNameInnerTmp = appNameInner;
            appNameOuterTmp = appNameOuter;
        } else {
            appNameInnerTmp = appNameOuter;
            appNameOuterTmp = appNameInner;
        }

        a = 30f * mValue7;

        /**
         * 绘制新生内圈星星及名字
         */
        {
            canvas.save();
//            setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
            canvas.translate(halfWidth, halfHeight);
            canvas.scale(scale, scale);
            canvas.rotate(30f * mValue7);

            //绘制新生内圈星星
            float tmpRadius = radiusStar * mValue7 / scale;
            canvas.drawCircle(0, -radiusMask, tmpRadius, paintCircleStar);
            canvas.drawCircle(0, radiusMask, tmpRadius, paintCircleStar);
            canvas.drawCircle(-sqrtMask, -halfMask, tmpRadius, paintCircleStar);
            canvas.drawCircle(-sqrtMask, halfMask, tmpRadius, paintCircleStar);
            canvas.drawCircle(sqrtMask, -halfMask, tmpRadius, paintCircleStar);
            canvas.drawCircle(sqrtMask, halfMask, tmpRadius, paintCircleStar);

            canvas.restore();
        }

        //绘制新生内圈星星的名字
        {
            canvas.save();
            canvas.translate(halfWidth, halfHeight);
            float r = -radiusMask * scale;

            for (int i = 0; i < 6; i++) {
                canvas.drawText(appNameOuterTmp[(appNameIndex + i) % 6], (float) (r * Math.cos((30 + 60 * i + a) * pi)), (float) (r * Math.sin((30 + 60 * i + a) * pi) + textVer), paintTextTmp);
            }
            canvas.restore();
        }
        {
            //绘制内圈星星名字
            canvas.save();
            canvas.translate(halfWidth, halfHeight);
            float tmpRadius2 = -radius2 * scale;
            float tmpRadius3 = -radius3 * scale3;

            for (int i = 0; i < 6; i++) {
                canvas.drawText(appNameInnerTmp[(appNameIndex + i) % 6], (float) (tmpRadius2 * Math.cos((60 * i + a) * pi)), (float) (tmpRadius2 * Math.sin((60 * i + a) * pi) + textVer), paintText);
            }

            //绘制外圈星星名字

            for (int i = 0; i < 6; i++) {
                canvas.drawText(appNameOuterTmp[(appNameIndex + i) % 6], (float) (tmpRadius3 * Math.cos((30 + 60 * i + a) * pi)), (float) (tmpRadius3 * Math.sin((30 + 60 * i + a) * pi) + textVer), paintText);
            }
            canvas.restore();
        }

        if (mValue7 == mValueMax) {

            canvas.save();
            canvas.translate(halfWidth, halfHeight);

            float tmpRadius = radiusStar;
            canvas.drawCircle(-radius2, 0, tmpRadius, paintCircleStar);
            canvas.drawCircle(radius2, 0, tmpRadius, paintCircleStar);
            canvas.drawCircle(-radius2 / 2, -sqrt3 * radius2, tmpRadius, paintCircleStar);
            canvas.drawCircle(-radius2 / 2, sqrt3 * radius2, tmpRadius, paintCircleStar);
            canvas.drawCircle(radius2 / 2, -sqrt3 * radius2, tmpRadius, paintCircleStar);
            canvas.drawCircle(radius2 / 2, sqrt3 * radius2, tmpRadius, paintCircleStar);
            canvas.restore();
            a = 0;
        }

    }

    /**
     * 阶段八
     * 绘制星团(一次性动画)
     *
     * @param canvas
     */
    private void drawState8(Canvas canvas) {

        //绘制星团
        canvas.save();
        canvas.translate(halfWidth, halfHeight);

        Rect src_bg = new Rect(0, 0, particallevel1.getWidth(), particallevel1.getHeight());
        float scale = (mValue8 * 2f) + 4; // 缩放比例 1 - 6
        float tempX = halfWidth * scale;
        float tempY = halfHeight * scale;
        RectF dst_bg = new RectF(-tempX, -tempY, tempX, tempY);

        /*canvas.drawBitmap(particallevel2, src_bg, dst_bg, paintBg);//共用背景画笔,保持对比度一致
        canvas.drawBitmap(particallevel3, src_bg, dst_bg, paintBg);//共用背景画笔,保持对比度一致*/
        canvas.restore();
    }

    /**
     * 阶段九
     * 绘制某个app的动态小水波纹
     *
     * @param canvas
     * @return
     */
    private void drawState9(Canvas canvas) {

        int alpha = 255;

        float coord[][] = {
                {-radiusSm, 0}, {radiusSm, 0}, {-coord3, -coord1},
                {-coord3, coord1}, {coord3, -coord1}, {coord3, coord1},
                {0, -radiusLg}, {0, radiusLg}, {-coord2, -coord4},
                {-coord2, coord4}, {coord2, -coord4}, {coord2, coord4}};

        float x = coord[waveRandom][0];
        float y = coord[waveRandom][1];


        if (mValue9 > mIntValueMax / 2) {
            alpha = (int) (255 * 2f * (100 - mValue9) / 100);
        } else {
            alpha = (int) (255 * 2f * mValue9 / 100);
        }
        paintWave1.setAlpha(alpha);
        canvas.save();
        float scale1 = (mValue9 * 1f / 100) + 1;         // 缩放比例 1 - 2
        float scale2 = (mValue9 * 1.6f / 100) + 1;         // 缩放比例 1 - 2
        canvas.translate(halfWidth, halfHeight);
        canvas.drawCircle(x, y, radiusAppWave, paintWave1);
        canvas.restore();

        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        canvas.drawCircle(x, y, (radiusAppWave - 8) * scale2, paintWave1);
        canvas.restore();

        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        canvas.drawCircle(x, y, (radiusAppWave) * scale1, paintWave1);
        canvas.restore();

        paintWave1.setAlpha(alpha / 2);
        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        canvas.drawCircle(x, y, (radiusAppWave + 8) * scale1, paintWave1);
        canvas.restore();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean isCancel = false;
        int action = event.getAction();

        if (!valueAnimator9.isStarted() || valueAnimator7.isRunning()) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_UP:

                if (!isCancel) {

                    if (!valueAnimator7.isRunning()) {
                        mValue9 = 0;
                        valueAnimator9.end();
                        mValue7 = 0.f;
                        valueAnimator7.setStartDelay(0);
                        valueAnimator7.start();
                        isRefresh = true;
                        valueAnimator8.start();
                        appSwitch = !appSwitch;
                    }

                }
                break;
            case MotionEvent.ACTION_CANCEL:

                isCancel = true;
                break;
        }

        return true;
    }
}
