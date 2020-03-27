package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidapp.Setting.httpSetting;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class newPackageInfo extends AppCompatActivity {
    private TextView packageIdText = null;
    private TextView destinationText = null;
    private TextView receiverNameText = null;
    private TextView receiverTelText = null;
    private TextView senderNameText = null;
    private TextView senderTelText = null;
    private Button okButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_info);
        packageIdText = findViewById(R.id.queryText);
        destinationText = findViewById(R.id.destinationText);
        senderNameText = findViewById(R.id.senderNameText);
        senderTelText = findViewById(R.id.senderTelText);
        receiverNameText = findViewById(R.id.receiverNameText);
        receiverTelText = findViewById(R.id.receiverTelText);
        okButton = findViewById(R.id.okButton);
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String yearString = year < 10 ? "0" + String.valueOf(year) : String.valueOf(year);
        int month = calendar.get(Calendar.MONTH);
        String monthString = month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
        int day = calendar.get(Calendar.DATE);
        String dayString = day < 10 ? "0" + String.valueOf(day) : String.valueOf(day);
        int minute = calendar.get(Calendar.MINUTE);
        String minuteString = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);
        int second = calendar.get(Calendar.SECOND);
        String secondString = second < 10 ? "0" + String.valueOf(second) : String.valueOf(second);


        //生成包裹编号
        packageIdText.setText(yearString + monthString + dayString + minuteString + secondString);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String packageId = packageIdText.getText().toString();
                final String destination = destinationText.getText().toString();
                final String senderName = senderNameText.getText().toString();
                final String senderTel = senderTelText.getText().toString();
                final String receiverName = receiverNameText.getText().toString();
                final String receiverTel = receiverTelText.getText().toString();

                //验证字段有效性
                if (packageId.equals("")) {
                    Toast.makeText(newPackageInfo.this, "快递编号不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (destination.equals("")) {
                    Toast.makeText(newPackageInfo.this, "收件地址不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (receiverName.equals("")) {
                    Toast.makeText(newPackageInfo.this, "收件人姓名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (receiverTel.equals("")) {
                    Toast.makeText(newPackageInfo.this, "收件人电话不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (senderName.equals("")) {
                    Toast.makeText(newPackageInfo.this, "寄件人姓名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (senderTel.equals("")) {
                    Toast.makeText(newPackageInfo.this, "寄件人电话不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = httpSetting.base + "operationEdit?operation=insert&postmanTel="+httpSetting.postmanTel+"&packageId=" + packageId + "&destination=" + destination
                        + "&senderName=" + senderName + "&senderTel=" + senderTel + "&receiverName=" + receiverName + "&receiverTel=" + receiverTel;
                httpSetting.sendOkHttpRequest(url, new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        newPackageInfo.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //提示网络连接异常
                                Toast.makeText(newPackageInfo.this, "网络连接异常！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String resText = response.body().string();
                        newPackageInfo.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resText.equals("success")) {
                                    JSONObject json = null;
                                    try {
                                        PackageInfo.packageId = packageId;
                                        PackageInfo.destination = destination;
                                        PackageInfo.senderName = senderName;
                                        PackageInfo.senderTel = senderTel;
                                        PackageInfo.receiverName = receiverName;
                                        PackageInfo.receiverTel =receiverTel;
                                        Intent intent = new Intent();
                                        intent.setClass(com.example.androidapp.newPackageInfo.this, PackageInfo.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                                        startActivity(intent);
                                        newPackageInfo.this.finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });

    }
}
