package com.example.handlerandasynctask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtTitle;
    private Button btnHandler, btnAsyncTask;
    private Handler handler;
    private static final int MESSAGE_COUNT_DOWN = 100;
    private static final int MESSAGE_DONE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
        btnAsyncTask.setEnabled(false);
        initHandler();
    }

    private void getViews(){
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnHandler = (Button) findViewById(R.id.btnHandler);
        btnAsyncTask = (Button) findViewById(R.id.btnAsyncTask);
        btnHandler.setOnClickListener(this);
        btnAsyncTask.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnHandler:
                countDown();
                break;
            case R.id.btnAsyncTask:
                countUp();
                break;
            default:
                break;
        }
    }

    private void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MESSAGE_COUNT_DOWN:
                        txtTitle.setText(String.valueOf(msg.arg1));
                        break;
                    case MESSAGE_DONE:
                        Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                        btnAsyncTask.setEnabled(true);
                        btnHandler.setEnabled(false);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void countDown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 10;
                do {
                    i--;
                    Message message = new Message();
                    message.what = MESSAGE_COUNT_DOWN;
                    message.arg1 = i;
                    handler.sendMessage(message);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (i>0);
                handler.sendEmptyMessage(MESSAGE_DONE);
            }
        }).start();
    }

    // Async Task

    private class myAsyncTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            int count = 0;
            do {
                count++;
                publishProgress(count);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (count<10);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            txtTitle.setText(values[0]+"");
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(MainActivity.this,   "Done!", Toast.LENGTH_SHORT).show();
            btnAsyncTask.setEnabled(false);
            btnHandler.setEnabled(true);
        }
    }

    private void countUp(){
        myAsyncTask task = new myAsyncTask();
        task.execute();
    }
}