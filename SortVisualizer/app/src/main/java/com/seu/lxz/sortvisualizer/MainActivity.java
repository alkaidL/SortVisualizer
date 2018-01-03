package com.seu.lxz.sortvisualizer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.buttonSendArray);
        text = (EditText) findViewById(R.id.inputArrayString);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidIuput(text.getText().toString())) {
                    Intent intent = new Intent(MainActivity.this, QuickSortActivity.class);
                    intent.putExtra("input", text.getText().toString());
                    startActivity(intent);
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "无效输入，请再试一次";
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private Boolean isValidIuput(String input) {
        try {
            List<String> inputArray = Arrays.asList(input.split(","));
            for (String s : inputArray) {
                if (!isInteger(s) || s.isEmpty() || s.equals(""))
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(String str) {
        try {
            int d = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}