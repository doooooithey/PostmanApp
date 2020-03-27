package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.androidapp.Setting.httpSetting;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private ImageButton postmanRegister;
    private EditText postmanPasswordText;
    private EditText postmanTelText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=(Button)findViewById(R.id.login);
        postmanRegister=(ImageButton)findViewById(R.id.postmanRegister);
        postmanTelText=(EditText)findViewById(R.id.postmanTelText);
        postmanPasswordText=(EditText)findViewById(R.id.postmanPasswordText);
        //跳转至注册页面
        postmanRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(com.example.androidapp.MainActivity.this, RegisterPage.class);//this前面为当前activty名称，class前面为要跳转到得activity名称
                startActivity(intent);
            }
        });
        //登录按钮
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String tel = postmanTelText.getText().toString();
                final String passwd = postmanPasswordText.getText().toString();
                //判断账号、密码框是否为空
                if(tel.equals("")){
                    Toast.makeText(MainActivity.this, "电话不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwd.equals("")){
                    Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String loginUrl = httpSetting.base + "postmanlogin"
                        + "?postmanTel=" +tel + "&postmanPassword=" + passwd;
                httpSetting.sendOkHttpRequest(loginUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Log.d("连接", "失败");
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String responseText = response.body().string();
                        Log.d("连接",responseText);
                        Log.d("连接", "123456");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (responseText.equals("notExist")){
                                    Toast.makeText(MainActivity.this, "账号不存在！", Toast.LENGTH_SHORT).show();
                                }else if (responseText.equals("wrongPassword")){
                                    Toast.makeText(MainActivity.this, "密码错误！",Toast.LENGTH_SHORT).show();
                                }else if (responseText.equals("success")){
                                    Log.d("LoginState:", "成功");
                                    Intent intent = HomePageActivity.newIntent(MainActivity.this);
                                    httpSetting.postmanPassword=passwd;
                                    httpSetting.postmanTel=tel;
                                    intent.putExtra("key123",passwd);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

}

