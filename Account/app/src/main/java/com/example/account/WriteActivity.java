package com.example.account;

/**
 * Created by 枯芒草 on 2016/4/26.
 */
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WriteActivity extends Activity{
    Button button=null;
    EditText textView1=null;
    EditText textView2=null;
    TextView textview;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writeactivity);
        button=(Button)findViewById(R.id.button);
        textView1=(EditText)findViewById(R.id.name);
        textView2=(EditText)findViewById(R.id.money);
        textview=(TextView)findViewById(R.id.time);
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String    date=sDateFormat.format(new  java.util.Date());
        textview.setText(date);
        button.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v){
                AlertDialog.Builder builder=new AlertDialog.Builder(WriteActivity.this);
                builder.setTitle("");
                if (textView1.getText().toString().equals("")||textView2.getText().toString().equals("")){
                    builder.setMessage("输入的消费项目或金额不能为空！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                }
                else {
                    builder.setMessage("确定添加这条记录吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        { //添加函数
                            DatabaseHelper dbHelper=new DatabaseHelper(WriteActivity.this,"moneydb");
                            SQLiteDatabase db=dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                            String    date=sDateFormat.format(new  java.util.Date());
                            textview.setText(date);
                            values.put("time",date);
                            values.put("name",textView1.getText().toString());
                            values.put("money",textView2.getText().toString());
                            db.insert("consume", null, values);
                            db.close();
                            dbHelper.close();
                            textView1.setText("");
                            textView2.setText("");
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();
                        }
                    });
                }


                builder.create().show();
            }
        });

    }
}