package danielworld.giftest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Copyright (c) 2014-2016 daniel@bapul.net
 * Created by Daniel Park on 2016-07-17.
 */
public class PlayGifView extends View {
    private static final int DEFAULT_MOVIEW_DURATION = 1000;

    private int mMovieResourceId;
    private Movie mMovie;

    private long mMovieStart = 0;
    private int mCurrentAnimationTime = 0;

    /**
     * if it's true, then it shows circle gif
     */
    private boolean isCircle = false;

    private Path mCirclePath = new Path();
    private RectF mCircleRectF = new RectF();


    @SuppressLint("NewApi")
    public PlayGifView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**
         * Starting from HONEYCOMB have to turn off HardWare acceleration to draw
         * Movie on Canvas.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    /**
     * When you set circle mode, GIF image will be shown as Circle
     * @param isCircle
     */
    public void setCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }

    public void setImageResource(int mvId){
        this.mMovieResourceId = mvId;
        mMovie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mMovie != null){
            setMeasuredDimension(mMovie.width(), mMovie.height());
        }else{
            setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMovie != null){
            updateAnimationTime();
            drawGif(canvas);
            invalidate();
        }else{
            drawGif(canvas);
        }
    }

    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        int dur = mMovie.duration();
        if (dur == 0) {
            dur = DEFAULT_MOVIEW_DURATION;
        }
        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }


    private void drawGif(Canvas canvas) {
        try {
            if (isCircle) {
                // Daniel (2016-07-17 22:02:13): Start create circle image
                if (mMovie.width() > mMovie.height())
                    mCircleRectF.set(mMovie.width() / 2 - mMovie.height() / 2, 0, mMovie.width() / 2 + mMovie.height() / 2, mMovie.height());
                else
                    mCircleRectF.set(0, mMovie.height() / 2 - mMovie.width() / 2, mMovie.width(), mMovie.height() / 2 + mMovie.width() / 2);

                mCirclePath.addCircle(mMovie.width() / 2, mMovie.height() / 2, mCircleRectF.width() / 2, Path.Direction.CW);
                canvas.clipPath(mCirclePath);
            }
            // ---- End --------------
        } catch (Exception e){
            e.printStackTrace();
            // Sorry, it prints original GIF
        }

        mMovie.setTime(mCurrentAnimationTime);
        mMovie.draw(canvas, 0, 0);
        canvas.restore();
    }

}
