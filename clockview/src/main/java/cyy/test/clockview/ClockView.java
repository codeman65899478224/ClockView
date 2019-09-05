package cyy.test.clockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;


/**
 * @author chenyyb
 * @date 2019/9/3
 */

public class ClockView extends View {
    public static final String TAG = ClockView.class.getSimpleName();
    /**
     * 时针颜色
     */
    private int hourLineColor;

    /**
     * 分针颜色
     */
    private int minuteLineColor;

    /**
     * 秒针颜色
     */
    private int secondLineColor;

    /**
     * 时针长度
     */
    private float hourLineLength;

    /**
     * 分针长度
     */
    private float minuteLineLength;

    /**
     * 秒针长度
     */
    private float secondLineLength;

    /**
     * 时针宽度
     */
    private float hourLineWidth;

    /**
     * 分针宽度
     */
    private float minuteLineWidth;

    /**
     * 秒针宽度
     */
    private float secondLineWidth;

    /**
     * 时间区块总数
     */
    private static final int TIME_BLOCK = 12;

    private static final String[] TIME_ARRAY= {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    /**
     * 表盘画笔
     */
    private Paint mPaint = new Paint();

    /**
     * 文本画笔
     */
    private Paint textPaint = new Paint();

    /**
     * 时针画笔
     */
    private Paint hourPaint = new Paint();

    /**
     * 分针画笔
     */
    private Paint minutePaint = new Paint();

    /**
     * 秒针画笔
     */
    private Paint secondPaint = new Paint();

    /**
     * 圆画笔
     */
    private Paint circlePaint = new Paint();

    /**
     * 表盘半径
     */
    private int radius;



    public ClockView(@NonNull Context context) {
        this(context, null);
    }

    public ClockView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
        initView(context, attrs);
    }

    public ClockView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                     int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        hourLineColor = a.getColor(R.styleable.ClockView_hour_line_color, Color.BLACK);
        minuteLineColor = a.getColor(R.styleable.ClockView_minute_line_color, Color.BLACK);
        secondLineColor = a.getColor(R.styleable.ClockView_second_line_color,Color.BLACK);
        hourLineLength = a.getDimension(R.styleable.ClockView_hour_line_length, 0);
        minuteLineLength = a.getDimension(R.styleable.ClockView_minute_line_length, 0);
        secondLineLength = a.getDimension(R.styleable.ClockView_second_line_length, 0);
        hourLineWidth = a.getDimension(R.styleable.ClockView_hour_line_width, 0);
        minuteLineWidth = a.getDimension(R.styleable.ClockView_minute_line_width, 0);
        secondLineWidth = a.getDimension(R.styleable.ClockView_second_line_width, 0);

        a.recycle();

        initPaint();
    }

    private void initPaint() {
        //初始化表盘画笔
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);

        //初始化文本画笔
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(2);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40);

        //初始化圆画笔
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setStrokeWidth(2);
        circlePaint.setAntiAlias(true);

        //初始化时针画笔
        hourPaint.setStyle(Paint.Style.FILL);
        hourPaint.setColor(hourLineColor);
        hourPaint.setStrokeWidth(5);
        hourPaint.setAntiAlias(true);

        //初始化分针画笔
        minutePaint.setStyle(Paint.Style.FILL);
        minutePaint.setColor(minuteLineColor);
        minutePaint.setStrokeWidth(2);
        minutePaint.setAntiAlias(true);

        //初始化秒针画笔
        secondPaint.setStyle(Paint.Style.FILL);
        secondPaint.setColor(secondLineColor);
        secondPaint.setStrokeWidth(1);
        secondPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw");
        super.onDraw(canvas);

        radius = Math.min(getWidth(), getHeight())/2;
        canvas.drawCircle(getWidth()/2, getHeight()/2, radius, mPaint);
        canvas.drawCircle(getWidth()/2, getHeight()/2, hourLineWidth * 2, circlePaint);

        computeTimeCoordinate(canvas, radius);

        drawPointer(canvas);

        postInvalidateDelayed(1000);
    }

    private void drawPointer(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        float hourAngle = (hour + minute/60) * 360/12;
        float minuteAngle = (minute + second/60)* 360/60;
        float secondAngle = second * 360/60;

        canvas.save();
        canvas.rotate(hourAngle, getWidth()/2, getHeight()/2);
        canvas.drawRoundRect(createRect(hourLineLength, hourLineWidth), 20, 20, hourPaint);

        canvas.restore();
        canvas.rotate(minuteAngle, getWidth()/2, getHeight()/2);
        canvas.drawRoundRect(createRect(minuteLineLength, minuteLineWidth), 20, 20, minutePaint);
        canvas.save();

        canvas.restore();
        canvas.rotate(secondAngle, getWidth()/2, getHeight()/2);
        canvas.drawRect(createRect(secondLineLength, secondLineWidth), secondPaint);

    }

    private RectF createRect(float secondLineLength, float secondLineWidth) {
        return new RectF((getWidth()- secondLineWidth)/2, getHeight()/2 - secondLineLength, (getWidth() + secondLineWidth)/2, getHeight()/2);
    }

    private void computeTimeCoordinate(Canvas canvas, int radius) {
        Rect mBounds = new Rect();
        for (String time : TIME_ARRAY){
            double angle = Integer.valueOf(time) * 360 / TIME_BLOCK;
            double sin = Math.sin(Math.toRadians(angle));
            double cos = Math.cos(Math.toRadians(angle));
            textPaint.getTextBounds(time, 0, time.length(), mBounds);
            float x = (float) ((getWidth() - mBounds.width())/2 + (radius - 20) * sin);
            float y = (float) ((getHeight() + mBounds.height())/2 - (radius - 20) * cos);
            canvas.drawText(time, x, y, textPaint);
        }
    }

}
