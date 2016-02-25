package de.kavallerie.ellipsizedtextview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Display a mark to indicate that there is more text
 * if the text exceeds its boundaries.
 */
public class EllipsizedTextView extends TextView {

    public static final String DEFAULT_MORE = "...";
    private int moreColor;

    private String moreText;

    public EllipsizedTextView(Context context) {
        super(context);
    }

    public EllipsizedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public EllipsizedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EllipsizedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(attrs);
    }

    private void initialize(@Nullable AttributeSet attributeSet) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable
                .EllipsizedTextView, 0, 0);

        moreText = typedArray.getString(R.styleable.EllipsizedTextView_moreText);

        // Assign default value
        if (moreText == null)
            moreText = DEFAULT_MORE;

        moreColor = typedArray.getColor(R.styleable.EllipsizedTextView_moreColor, getCurrentTextColor());
        typedArray.recycle();

    }

    @Override
    public boolean onPreDraw() {

        // Number of the available lines
        final int availableLines = getHeight() / getLineHeight();


        if (getLineCount() > availableLines) {

            int lastIndex = getLayout().getLineEnd(availableLines - 1);


            String textWithoutMore = getText().toString().substring(0, lastIndex -
                    moreText.length());

            String textTillLastSpace = textWithoutMore.substring(0, textWithoutMore
                    .lastIndexOf(" ") + 1);

            String newText = textTillLastSpace + moreText;

            SpannableString decoratedText = new SpannableString(newText);

            int length = newText.length();

            int firstIndexOfMore = newText.lastIndexOf(moreText);

            decoratedText.setSpan(new UnderlineSpan(), firstIndexOfMore, length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            decoratedText.setSpan(new ForegroundColorSpan(moreColor), firstIndexOfMore, length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            setText(decoratedText);

        }
        return super.onPreDraw();
    }
}
