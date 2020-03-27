package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.androidapp.Setting.httpSetting;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONObject;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomePageActivity extends AppCompatActivity {
    private ImageButton qrCodeButton;
    private Button newPackageButton;
    private Button packageQueryButton;
    private Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        qrCodeButton=(ImageButton)findViewById(R.id.qrCodeButton);
        newPackageButton=(Button)findViewById(R.id.newPackageButton);
        packageQueryButton=(Button)findViewById(R.id.packageQueryButton);
        logoutButton=(Button)findViewById(R.id.logoutButton);
        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, CaptureActivity.class);
                HomePageActivity.this.startActivityForResult(intent, 1001);
            }
        });
        newPackageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(com.example.androidapp.HomePageActivity.this, newPackageInfo.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                startActivity(intent);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(com.example.androidapp.HomePageActivity.this, MainActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                startActivity(intent);
            }
        });
        packageQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(com.example.androidapp.HomePageActivity.this, PackageQueryActivity.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                startActivity(intent);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(resultCode, data);
            final String bitMatrixContent = scanResult.getContents();
            //发送HTTP请求
            String url = httpSetting.base + "outQRcode?encode=" + bitMatrixContent + "&postmanpwd=" + httpSetting.postmanPassword;
            httpSetting.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    HomePageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //提示网络连接异常
                            Toast.makeText(HomePageActivity.this, "网络连接异常！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String resText = response.body().string();
                    HomePageActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resText.equals("WrongKey")) {
                                Toast.makeText(HomePageActivity.this, "无权查看此包裹信息", Toast.LENGTH_SHORT).show();
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
                                    intent.setClass(com.example.androidapp.HomePageActivity.this, PackageInfo.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
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
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, HomePageActivity.class);
        return intent;
    }
}
