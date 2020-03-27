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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PackageInfo extends AppCompatActivity {
    public static String packageId="";
    public static String destination="";
    public static String senderName="";
    public static String senderTel="";
    public static String receiverName="";
    public static String receiverTel="";
    private TextView packageIdText=null;
    private TextView destinationText=null;
    private TextView receiverNameText=null;
    private TextView receiverTelText=null;
    private TextView senderNameText=null;
    private TextView senderTelText=null;

    private Button backButton=null;
    private Button endpackage=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_info_show);
        packageIdText=findViewById(R.id.queryText);
        destinationText=findViewById(R.id.destinationText);
        senderNameText=findViewById(R.id.senderNameText);
        senderTelText=findViewById(R.id.senderTelText);
        receiverNameText=findViewById(R.id.receiverNameText);
        receiverTelText=findViewById(R.id.receiverTelText);
        endpackage=findViewById(R.id.endpackage);


        packageIdText.setText(packageId);
        destinationText.setText(destination);
        receiverNameText.setText(receiverName);
        receiverTelText.setText(receiverTel);
        senderNameText.setText(senderName);
        senderTelText.setText(senderTel);

        backButton=findViewById(R.id.okButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageInfo.this.finish();


            }
        });
        endpackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String packageId = packageIdText.getText().toString();
                String url = httpSetting.base +"operationEdit?operation=delete&packageId="+packageId;
                httpSetting.sendOkHttpRequest(url, new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        PackageInfo.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //提示网络连接异常
                                Toast.makeText(PackageInfo.this, "网络连接异常！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String resText = response.body().string();
                        PackageInfo.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                if (resText.equals("success")) {
                                    Toast.makeText(PackageInfo.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                    PackageInfo.this.finish();
                                    }else if(resText.equals("fail")){
                                    Toast.makeText(PackageInfo.this, "删除失败！", Toast.LENGTH_SHORT).show();
                                }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                }
                            });
                    }
                });

            }
        });

    }
}
