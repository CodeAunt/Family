package family.momo.com.family.DefinedViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;


import family.momo.com.family.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


/**
 * Created by Administrator on 2016/9/27.
 */
public class PasswordInputView extends android.support.v7.widget.AppCompatEditText {
    private String text = "123456";

    private static final int defaultContMargin = 1;
    private static final int defaultSplitLineWidth = 3;

    private int borderColor = 0xFFCCCCCC;
    private float borderWidth = 5;
    private float borderRadius = 3;
    private float borderDistance = 6;

    private int passwordLength = 6;
    private int passwordColor = 0xFFCCCCCC;
    private float passwordSize = 8;
    private float passwordRadius = 3;

    private Paint passwordPaint = new Paint(ANTI_ALIAS_FLAG);
    private Paint borderPaint = new Paint(ANTI_ALIAS_FLAG);
    private int textLength;

    public PasswordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        borderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, borderWidth, dm);
        borderRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, borderRadius, dm);
        passwordLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, passwordLength, dm);
        passwordSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, passwordSize, dm);
        passwordRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, passwordRadius, dm);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordInputView, 0, 0);
        borderColor = a.getColor(R.styleable.PasswordInputView_borderColor, borderColor);
        borderWidth = a.getDimension(R.styleable.PasswordInputView_borderWidth, borderWidth);
        borderRadius = a.getDimension(R.styleable.PasswordInputView_borderRadius, borderRadius);
        borderDistance = a.getDimension(R.styleable.PasswordInputView_borderDistance, borderDistance);
        passwordLength = a.getInt(R.styleable.PasswordInputView_passwordLength, passwordLength);
        passwordColor = a.getColor(R.styleable.PasswordInputView_passwordColor, passwordColor);
        passwordSize = a.getDimension(R.styleable.PasswordInputView_passwordSize, passwordSize);
        passwordRadius = a.getDimension(R.styleable.PasswordInputView_passwordRadius, passwordRadius);



        a.recycle();

        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(borderColor);
        passwordPaint.setStrokeWidth(passwordSize);
        passwordPaint.setStyle(Paint.Style.FILL);
        passwordPaint.setColor(passwordColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        borderPaint.setStrokeWidth(defaultSplitLineWidth);
        for (int i = 0; i < passwordLength; i++) {
            float x = width *i/ passwordLength;
            RectF rect = new RectF(x, 0, x+width/passwordLength, height);

            RectF rectIn = new RectF(rect.left + borderDistance/2, rect.top ,
                    rect.right - borderDistance/2, rect.bottom );
            RectF rectInIn = new RectF(rectIn.left + defaultContMargin, rectIn.top + defaultContMargin,
                    rectIn.right - defaultContMargin, rectIn.bottom - defaultContMargin);
            borderPaint.setColor(borderColor);
            canvas.drawRoundRect(rectIn, borderRadius, borderRadius, borderPaint);
            borderPaint.setColor(Color.WHITE);
            canvas.drawRoundRect(rectInIn, borderRadius, borderRadius, borderPaint);
            borderPaint.setColor(passwordColor);
            passwordPaint.setTextSize(passwordSize);
            passwordPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text,0,height,passwordPaint);
            //canvas.drawText(text,0,width,0,0,passwordPaint);
            //canvas.drawLine(x, 0, x, height, borderPaint);
        }

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.textLength = text.toString().length();;
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        borderPaint.setStrokeWidth(borderWidth);
        invalidate();
    }

    public float getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(float borderRadius) {
        this.borderRadius = borderRadius;
        invalidate();
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        invalidate();
    }

    public int getPasswordColor() {
        return passwordColor;
    }

    public void setPasswordColor(int passwordColor) {
        this.passwordColor = passwordColor;
        passwordPaint.setColor(passwordColor);
        invalidate();
    }

    public float getPasswordWidth() {
        return passwordSize;
    }

    public void setPasswordWidth(float passwordWidth) {
        this.passwordSize = passwordWidth;
        passwordPaint.setStrokeWidth(passwordWidth);
        invalidate();
    }

    public float getPasswordRadius() {
        return passwordRadius;
    }

    public void setPasswordRadius(float passwordRadius) {
        this.passwordRadius = passwordRadius;
        invalidate();
    }
}