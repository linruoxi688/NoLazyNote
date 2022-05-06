package com.example.nolazynote.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nolazynote.R;


/**
 * 番茄钟运行页面
 * */
public class TomatoClockActivity extends AppCompatActivity {

    private TomatoClock tomatoClock; //时钟线程，用于计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_clock);
        //隐去自带的actionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //设置返回按钮
        ImageButton imageButton = findViewById(R.id.returnButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回到番茄钟设置页面
                Intent data = new Intent();
                setResult(0, data);
                finish();
            }
        });

        //设置背景颜色
        getWindow().setBackgroundDrawableResource(R.color.colorDarkPurple);

        //直接开始运行番茄钟的计时线程
        int clockNum = getIntent().getIntExtra("tomatoNum", -1);
        if (clockNum <= 0) {
            //数据传递出错
            clockNum = 1;
        }
        tomatoClock = new TomatoClock(clockNum, true, this);
        tomatoClock.startClock();

        //设置番茄钟的开启与暂停按钮
        ImageButton clockButton = findViewById(R.id.clockButton);
        clockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tomatoClock.getClockState()) {
                    //时钟正在运行，此时按下暂停按钮
                    //改变时钟状态
                    tomatoClock.suspendClock();
                    //改变界面为暂停状态
//                    changeUItoStopState(view);
                } else {
                    //时钟已经暂停，此时按下启动按钮
                    //改变时钟状态
                    tomatoClock.continueClock();
                    //改变界面为启动状态
//                    changeUItoRunningState(view);
                }
            }
        });
    }


    private void changeUItoStopState(View view) {
        //切换图标
//        ((ImageButton)view).setImageDrawable(getResources().getDrawable(R.drawable.ic_clock_icon_stop));
        //切换背景颜色为亮紫色
        getWindow().setBackgroundDrawableResource(R.color.colorBrightPurple);
        //切换提示文字
        ((TextView)getWindow().findViewById(R.id.tipView)).setText(R.string.tip_on_clock_stopped);
        //暂停进度条
//                    ProgressBar progressBar = getWindow().findViewById(R.id.progressBar);
//                    progressBar.setIndeterminate(false);
        //修改进度条颜色为暗紫色
//                    progressBar.setIndeterminateDrawable(getDrawable(R.color.colorDarkPurple));
    }

    private void changeUItoRunningState(View view) {
        //切换图标
//        ((ImageButton)view).setImageDrawable(getResources().getDrawable(R.drawable.ic_clock_icon_running));
        //切换背景颜色为暗紫色
        getWindow().setBackgroundDrawableResource(R.color.colorDarkPurple);
        //切换提示文字
        ((TextView)getWindow().findViewById(R.id.tipView)).setText(R.string.tip_on_clock_running);
        //启动进度条
//                    ProgressBar progressBar = getWindow().findViewById(R.id.progressBar);
//                    progressBar.setIndeterminate(true);
        //修改进度条颜色为亮紫色
//                    progressBar.setIndeterminateDrawable(getDrawable(R.color.colorBrightPurple));
    }



    @Override
    protected void onDestroy() {
        tomatoClock.stopClock();
        super.onDestroy();
    }
}

//class ClockButtonListener implements View.OnClickListener {
//    private TomatoClock tomatoClock;
//
//    public ClockButtonListener(TomatoClock tomatoClock) {
//        super();
//        this.tomatoClock = tomatoClock;
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (tomatoClock.getClockState()) {
//            //时钟正在运行，此时按下暂停按钮
//            ((ImageButton)view).setImageDrawable();
//        } else {
//            //时钟已经暂停，此时按下启动按钮
//        }
//    }
//}

class TomatoClock {

    private boolean clockRunning;
    private int clockNumLeft; //还剩多少个番茄钟要跑（不包含当前正在跑的这个番茄钟）
//    private long totalTime; //番茄钟总共需要跑多少秒
    private long timeLeft; //番茄钟还剩多少毫秒要跑
//    private static final int TOMATO_CLOCK_LEN = 25 * 60 * 1000; //一个番茄钟的长度，单位是毫秒
    private static final long TOMATO_CLOCK_LEN = 25 * 60 * 1000 + 1000; //一个番茄钟里任务的长度，单位是毫秒，多出1s方便显示时间
    private static final long TOMATO_CLOCK_REST_LEN = 5 * 60 * 1000 + 1000; //一个番茄钟里休息的长度，单位是毫秒，多出1s方便显示时间
    private static final long CNT_DOWN_INTERVAL = 1000; //计时器回调频率，单位是毫秒
    private boolean rest; //是否处在番茄钟的休息周期里
    private ProgressBar progressBar;
    private TextView clockTimeTextView;
    private CountDownTimer countDownTimer;
    private TomatoClockActivity activity;

    public TomatoClock(int tomatoNum, boolean clockRunning, TomatoClockActivity activity) {
        super();
        this.clockNumLeft = tomatoNum;
        this.clockRunning = clockRunning;
        this.timeLeft = TOMATO_CLOCK_LEN;
        this.progressBar = activity.findViewById(R.id.progressBar);
        this.clockTimeTextView = activity.findViewById(R.id.clockTimeTextView);
        this.activity = activity;
        this.rest = false; //初始时不休息，直接进行番茄钟计时
    }

//    //设置时钟状态
//    public void setClockRunning(boolean clockRunning) { this.clockRunning = clockRunning; }

    //获取时钟状态，true表示正在运行
    public Boolean getClockState() { return clockRunning; }

//    //设置番茄个数
//    public void setTomatoNum(int tomatoNum) {this.tomatoNum = tomatoNum;}



    public void startClock() {
        clockRunning = true;

        countDownTimer = new CountDownTimer(timeLeft, CNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long l) {
                clockTick(l);
            }

            @Override
            public void onFinish() {
                clockFinish();
            }

        };
        countDownTimer.start();

        //UI
        if (rest) {
            countDownTimer.onTick(TOMATO_CLOCK_REST_LEN + 1000);
        } else {
            countDownTimer.onTick(TOMATO_CLOCK_LEN + 1000);
        }
    }

    //暂停番茄钟，具体做法是删去之前的countDownTimer,然后记录剩余多少时间(已经在每次ontick记录了)
    public void suspendClock() {
        clockRunning = false;
        countDownTimer.cancel();
    }

    public void continueClock() {
        clockRunning = true;
        //重新创建countDownTimer
        countDownTimer = new CountDownTimer(timeLeft, CNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long l) {
                clockTick(l);
            }

            @Override
            public void onFinish() {
                clockFinish();
            }
        };
        countDownTimer.start();
    }

    public void stopClock() {
        clockRunning = false;
        countDownTimer.cancel();
    }



    //每过1秒钟，都刷新一次时间的显示
    public void clockTick(long l) {
        //记录一下还剩多少时间
        timeLeft = l;

        //刷新时间显示
        l = l / 1000; //单位转化为秒
        l--; //之前+1s ，这里再-1s，方便计算
        Long second = l % 60; //秒数
        Long minute = l / 60; //分钟数
        String secondStr = second.toString();
        if (second < 10) secondStr = "0" + secondStr;
        String minuteStr = minute.toString();
        if (minute < 10) minuteStr = "0" + minuteStr;
        String clockTimeText = minuteStr + ":" + secondStr;
        clockTimeTextView.setText(clockTimeText);

    }

    //倒计时到0时，应该做的事情
    public void clockFinish() {
        timeLeft = 0;
        //倒计时归零
        clockTimeTextView.setText("00:00");
        //等待一小会儿，让0:00显示出来
        try {
            Thread.currentThread().sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (rest) {
            //休息结束
            rest = false;
            clockNumLeft--;
            if (clockNumLeft <= 0) {
                //完成了所有的番茄钟
                TextView tipView = activity.findViewById(R.id.tipView);
                tipView.setText("计时完毕！");
                finishTomato();
            } else {
                //继续进行下一个番茄钟
                timeLeft = TOMATO_CLOCK_LEN;
                startClock();
                //暂停按钮修改为亮色
                ImageButton clockButton = activity.findViewById(R.id.clockButton);
                clockButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_clock_icon_running));
                //屏幕颜色调成暗紫色
                activity.getWindow().setBackgroundDrawableResource(R.color.colorDarkPurple);

            }
        } else {
            //番茄钟25min计时结束
            rest = true;
            //进入休息
            clockTimeTextView.setText("05:00");
            //屏幕颜色调成亮紫色
            activity.getWindow().setBackgroundDrawableResource(R.color.colorBrightPurple);
            //提示用户休息
            TextView tipView = activity.findViewById(R.id.tipView);
            tipView.setText("休息一下！");
            //暂停按钮修改为暗色
            ImageButton clockButton = activity.findViewById(R.id.clockButton);
            clockButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_clock_icon_stop));
            //开始5min休息
            timeLeft = TOMATO_CLOCK_REST_LEN;
            startClock();
        }


    }

    //用户成功度过所有番茄钟之后，会自动调用此方法
    public void finishTomato() {
        //...编写代码
        System.out.println("自律成功！");
    }
}
