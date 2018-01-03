package com.seu.lxz.sortvisualizer.algorithm;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seu.lxz.sortvisualizer.QuickSortActivity;
import com.seu.lxz.sortvisualizer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by Administrator on 2017/10/13.
 */

public class QuickSortAlgorithm {
    private QuickSortActivity context;
    private LinearLayout list_view;
    private List<List<Integer>> views_for_animation;
    private List<String> log;

    public int[] getArray() {
        return array;
    }

    public List<String> getLog(){
        return log;
    }

    public boolean stackIsEmpty(){
        return stack.isEmpty();
    }

    private int array[];
    private int length;
    Stack<Integer> stack;

    public QuickSortAlgorithm(QuickSortActivity _context, int[] inputArr){
        this.context = _context;
        this.list_view = (LinearLayout) context.findViewById(R.id.main_content);
        this.array = inputArr;
        this.length = inputArr.length;
        this.stack = new Stack<>();
        stack.push(0);
        stack.push(inputArr.length);
        views_for_animation = new ArrayList<>();
        log = new ArrayList<>();
    }

    public int quickSort() {
        views_for_animation = new ArrayList<>();
        int end = stack.pop();
        int start = stack.pop();
        if(end - start < 2) return 0;
        int p = start + ((end - start) / 2);

        p = partition(array, p, start, end);

        showAnimation(0);

        setCircle(p, true);

        stack.push(p+1);
        stack.push(end);

        stack.push(start);
        stack.push(p);

        return views_for_animation.size();
    }

    private void  showAnimation(final int offset){
        if(offset == views_for_animation.size()){
            return;
        }
        final int new_offset = offset + 1;

        final int l = views_for_animation.get(offset).get(0);
        final int h = views_for_animation.get(offset).get(1);

        final TextView t1 = (TextView) list_view.findViewById(l);
        final TextView t2 = (TextView) list_view.findViewById(h);

        final String txt1 = (String) t1.getText();
        final String txt2 = (String) t2.getText();

        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(200);            //设置动画持续时间
        animation1.setStartOffset(500);         //执行前的等待时间
        animation1.setFillAfter(true);          //动画执行完后是否停留在执行完的状态

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                TextView c = (TextView) context.findViewById(R.id.status);
                c.setText("交换 " + txt1 + "与" + txt2);
                log.add("元素" + txt1 + " 下标(" + l + ") 交换 元素" + txt2 + " 下标(" + h + ")");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                t1.setText(txt2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        AlphaAnimation animation2 = new AlphaAnimation(0.2f, 1.0f);
        animation2.setDuration(200);            //设置动画持续时间
        animation2.setStartOffset(510);         //执行前的等待时间
        animation2.setFillAfter(true);          //动画执行完后是否停留在执行完的状态

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                t2.setText(txt1);
                showAnimation(new_offset);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        t1.startAnimation(animation1);
        t2.startAnimation(animation2);
    }

    private int partition(int[] array, int p, int start, int end){
        int l = start;
        int h = end - 2;

        int piv = array[p];

        swap(array, p, end-1);
        while(l < h){
            if(array[l] < piv){
                l++;
            }
            else if (array[h] >= piv){
                h--;
            }
            else{
                swap(array, l, h);
            }
        }
        int idx = h;
        if(array[h] < piv)
            idx++;
        swap(array, end-1, idx);
        return idx;
    }

    private void swap(int[] array, final int l, final int h){
        int temp = array[l];
        array[l] = array[h];
        array[h] = temp;

        if(l != h)
            views_for_animation.add(new ArrayList<>(Arrays.asList(l, h)));
    }

    private void setCircle(int position, boolean pivot) {
        TextView current;
        if(pivot){
            for(int i = 0; i < list_view.getChildCount(); i++){
                current = (TextView) list_view.getChildAt(i);
                if(i == position){
                    current.setBackgroundResource(R.drawable.circle_pivot);
                    current.requestFocus();
                }
                else
                    current.setBackgroundResource(R.drawable.circle);
            }
        }
    }
}
