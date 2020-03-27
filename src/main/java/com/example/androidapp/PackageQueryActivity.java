package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidapp.Setting.httpSetting;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PackageQueryActivity extends AppCompatActivity {
    private Button goback;
    private Button goquery;
    private EditText queryText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_query);
        goback=(Button)findViewById(R.id.goback);
        goquery=(Button)findViewById(R.id.goquery);
        queryText=(EditText)findViewById(R.id.queryText);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(com.example.androidapp.PackageQueryActivity.this, HomePageActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                startActivity(intent);
            }
        });
        goquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String packageId = queryText.getText().toString();
                String url = httpSetting.base + "querybyId?packageId=" + packageId + "&postmanTel=" + httpSetting.postmanTel;
                httpSetting.sendOkHttpRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        PackageQueryActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //提示网络连接异常
                                Toast.makeText(PackageQueryActivity.this, "网络连接异常！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String resText = response.body().string();
                        System.out.println(resText);
                        PackageQueryActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resText.equals("wrong")) {
                                    Toast.makeText(PackageQueryActivity.this, "查询有误", Toast.LENGTH_SHORT).show();
                                } else {
                                    try {
                                        JSONObject json = new JSONObject(resText);
                                        json=json.getJSONObject("packageInfo");
                                        PackageInfo.packageId=json.getString("packageId");
                                        PackageInfo.destination=json.getString("destination");
                                        PackageInfo.senderName=json.getString("senderName");
                                        PackageInfo.senderTel=json.getString("senderTel");
                                        PackageInfo.receiverName=json.getString("receiverName");
                                        PackageInfo.receiverTel=json.getString("receiverTel");
                                        Intent intent = new Intent();
                                        intent.setClass(com.example.androidapp.PackageQueryActivity.this, PackageInfo.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                                        startActivity(intent);
                                    }catch (Exception e){
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
