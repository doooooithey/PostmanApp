package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterPage extends AppCompatActivity {
    private EditText newName;
    private EditText newTel;
    private EditText newPwd;
    private EditText newPwdAgain;
    private Button newpostman;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        newpostman=(Button)findViewById(R.id.newpostman);
        newName=(EditText)findViewById(R.id.newName);
        newTel=(EditText)findViewById(R.id.newTel);
        newPwd=(EditText)findViewById(R.id.newPwd);
        newPwdAgain=(EditText)findViewById(R.id.newPwdAgain);
        newpostman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断注册信息是否为空
                String tel=newTel.getText().toString();
                String name= newName.getText().toString();
                String passwd=newPwd.getText().toString();
                String confPass=newPwdAgain.getText().toString();

                if(tel.equals("")){
                    Toast.makeText(RegisterPage.this, "电话号码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd.equals("")){
                    Toast.makeText(RegisterPage.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(confPass.equals("")){
                    Toast.makeText(RegisterPage.this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!passwd.equals(confPass)){
                    Toast.makeText(RegisterPage.this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String registerUrl = httpSetting.base + "postmanRegister"
                        + "?postmanTel=" +tel + "&postmanPassword=" + passwd + "&postmanName=" + name;
                System.out.println(registerUrl);

                httpSetting.sendOkHttpRequest(registerUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Log.d("注册", "失败");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterPage.this, "注册失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (responseText.equals("fail")){
                                    Log.d("注册", "已被注册");
                                    Toast.makeText(RegisterPage.this, "该用户已被注册!", Toast.LENGTH_SHORT).show();
                                }else if (responseText.equals("success")){
                                    Log.d("注册","成功");
                                    Toast.makeText(RegisterPage.this, "注册成功，现在返回登录界面！",Toast.LENGTH_SHORT).show();
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



