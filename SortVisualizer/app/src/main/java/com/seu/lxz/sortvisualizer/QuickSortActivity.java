package com.seu.lxz.sortvisualizer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.seu.lxz.sortvisualizer.algorithm.QuickSortAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/10/12.
 */

public class QuickSortActivity extends BaseActivity {
    private QuickSortAlgorithm qsorter;
    private Timer timer;
    private AlertDialog.Builder builder;

    private int dip2px(int dip)       //将dip转化为px
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    public static Integer tryParse(String text){
        try {
            return Integer.parseInt(text);
        }catch (NumberFormatException nfe){
            return null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quicksort_activity);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("input");
        List<String> array_string = Arrays.asList(message.split(","));      //输入数组转List

        List<Integer> array_int = new ArrayList<>();
        for(int i = 0; i < array_string.size(); i++)
        {
            Integer element = tryParse(String.valueOf(array_string.get(i)));
            if(element != null)
                array_int.add(element);
        }

        int[] input = new int[array_int.size()];
        for(int i = 0; i < array_int.size(); i++)
            input[i] = array_int.get(i);

        qsorter = new QuickSortAlgorithm(this, input);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_content);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        params.width = dip2px(40);
        params.height = dip2px(40);

        for(int i = 0; i < input.length; i++){
            final TextView nText = new TextView(this);
            nText.setId(i);
            nText.setText(Integer.toString(input[i]));
            nText.setTextColor(ContextCompat.getColor(this, R.color.circleTextColor));
            nText.setBackgroundResource(R.drawable.circle);
            nText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            nText.setLayoutParams(params);
            nText.setFocusableInTouchMode(true);
            linearLayout.addView(nText);
        }

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    startExecution();
                }
                else{
                    stopExecution();
                }
            }
        });
        builder = new AlertDialog.Builder(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView c = (TextView) findViewById(R.id.status);
        c.setText("就绪");
        showToast("就绪");
        c.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.ready));
    }

    private void startExecution(){
        int delay = 0;
        TextView c = (TextView) findViewById(R.id.status);
        c.setText("运行中");
        c.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error));
        executionSchedule(delay);
    }

    private void executionSchedule(int delay) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!qsorter.stackIsEmpty()) {
                            int temp = qsorter.quickSort() * 1600;
                            cancel();
                            executionSchedule(temp);
                        } else {
                            finishExecution();
                            cancel();
                        }
                    }
                });
            }
        }, delay);
    }

    private void stopExecution(){
        if(timer != null){
            TextView c = (TextView) findViewById(R.id.status);
            c.setText("暂停中");
            timer.cancel();
            timer = null;
        }
    }

    public void nextStep(View v){
        if(!qsorter.stackIsEmpty()){
            while(qsorter.quickSort() == 0 && !qsorter.stackIsEmpty()) {}
        }else{
            finishExecution();
        }
    }

    private void finishExecution(){
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setChecked(false);
        TextView c = (TextView) findViewById(R.id.status);
        String x = "";
        for(int i = 0; i < qsorter.getArray().length; i++)
        {
            if(i == qsorter.getArray().length - 1)
            {
                x += Integer.toString(qsorter.getArray()[i]);
            }
            else{
                x += Integer.toString(qsorter.getArray()[i]) + ",";
            }
        }
        c.setText(x + " 排序完成");
        showToast("排序完成");
        c.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.done));
    }

    public void showToast(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void showLog(View v){
        String content = "空";
        if(qsorter.getLog().size() != 0){
            content = "";
            for(String s : qsorter.getLog())
                content += s + "\n";
        }
        builder.setMessage(content)
                .setTitle("交换记录")
                .setNeutralButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
