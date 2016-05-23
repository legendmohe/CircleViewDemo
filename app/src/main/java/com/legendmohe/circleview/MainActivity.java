package com.legendmohe.circleview;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.circle_view)
    CircleView mCircleView;
    @Bind(R.id.container_layout)
    RelativeLayout mContainerLayout;
    @Bind(R.id.textView1)
    TextView mTextView1;
    @Bind(R.id.textView2)
    TextView mTextView2;
    @Bind(R.id.textView3)
    TextView mTextView3;
    @Bind(R.id.textView4)
    TextView mTextView4;
    @Bind(R.id.dot_number_seekBar)
    SeekBar mDotNumberSeekBar;
    @Bind(R.id.dot_speed_seekBar)
    SeekBar mDotSpeedSeekBar;
    @Bind(R.id.circle_speed_seekBar)
    SeekBar mCircleSpeedSeekBar;
    @Bind(R.id.circle_expend_seekBar)
    SeekBar mCircleExpendSeekBar;
    @Bind(R.id.dot_number_seekBar_textView)
    TextView mDotNumberSeekBarTextView;
    @Bind(R.id.dot_speed_seekBar_textView)
    TextView mDotSpeedSeekBarTextView;
    @Bind(R.id.circle_speed_seekBar_textView)
    TextView mCircleSpeedSeekBarTextView;
    @Bind(R.id.circle_expend_seekBar_textView)
    TextView mCircleExpendSeekBarTextView;

    private AnimatorSet mColorAnimation;
    private int mColorAnimationDuration = 1000;

    private final int[] mColorList = new int[]{Color.BLACK, Color.WHITE, Color.GREEN, Color.CYAN, Color.YELLOW};
    private int mCurrentColorIndex = 0;

    private AnimationUtil.AnimateColorChangeListener mContainerColorChangeListener = new AnimationUtil.AnimateColorChangeListener() {
        @Override
        public void onColorChange(int color) {
            mContainerLayout.setBackgroundColor(color);
        }
    };

    ;
    private AnimationUtil.AnimateColorChangeListener mCircleColorChangeListener = new AnimationUtil.AnimateColorChangeListener() {
        @Override
        public void onColorChange(int color) {
            mCircleView.setPaintColor(color);
            mTextView1.setTextColor(color);
            mTextView2.setTextColor(color);
            mTextView3.setTextColor(color);
            mTextView4.setTextColor(color);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupSeekBar();
    }

    private void setupSeekBar() {
        mDotNumberSeekBar.setMax(100);
        mDotNumberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mCircleView.setNumOfDot(seekBar.getProgress());
                mDotNumberSeekBarTextView.setText(String.valueOf(seekBar.getProgress()));
            }
        });

        mDotSpeedSeekBar.setMax(20);
        mDotSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mCircleView.setDotVelocity(seekBar.getProgress(), (int) (seekBar.getProgress() * 0.4));
                mDotSpeedSeekBarTextView.setText(String.valueOf(seekBar.getProgress()));
            }
        });
        mCircleSpeedSeekBar.setMax(6000);
        mCircleSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getMax() - seekBar.getProgress();
                mCircleView.setOvalRotateDuration(value);
                mCircleSpeedSeekBarTextView.setText(String.valueOf(value));
            }
        });
        mCircleExpendSeekBar.setMax(200);
        mCircleExpendSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mCircleView.setOvalSizeExpendWidth(seekBar.getProgress());
                mCircleExpendSeekBarTextView.setText(String.valueOf(seekBar.getProgress()));
            }
        });
    }

    @OnClick(R.id.button4)
    public void onChangeStateClick() {
//        int color = mColorList[mCurrentColorIndex];
        int color = ColorUtil.randomColor();
        playColorChange(color, ColorUtil.getContrastColor(color));
//        mCurrentColorIndex++;
//        if (mCurrentColorIndex >= mColorList.length)
//            mCurrentColorIndex = 0;
    }

    private void playColorChange(int backgroundColor, int circleColor) {
        if (mColorAnimation != null) {
            if (mColorAnimation.isRunning())
                mColorAnimation.cancel();
        }

        ValueAnimator backgroundColorAnimation = AnimationUtil.animateColorChange(
                ((ColorDrawable) mContainerLayout.getBackground()).getColor(),
                backgroundColor,
                mColorAnimationDuration,
                mContainerColorChangeListener);
        ValueAnimator circleColorAnimation = AnimationUtil.animateColorChange(
                mCircleView.getPaintColor(),
                circleColor,
                mColorAnimationDuration,
                mCircleColorChangeListener);
        mColorAnimation = new AnimatorSet();
        mColorAnimation.playTogether(backgroundColorAnimation, circleColorAnimation);
        mColorAnimation.start();
    }
}
