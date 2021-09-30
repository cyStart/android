package com.booking.nirbhay.testapp2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button loginbtn;
    TextView signupbtn;
    EditText mobile,password;
    AppController gObject;
    SessionManager session;
    SessionManagerLogin session1;
    SharedPreferences mpreferences;
    SharedPreferences.Editor settingDataPrefe;
    TextView logotext;

    Typeface fontCustome;
    String loginTypeUser="student";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        gObject =(AppController)getApplicationContext();
        mpreferences = getSharedPreferences(String.format("%s_preferences", getPackageName()), Context.MODE_PRIVATE);
        settingDataPrefe = mpreferences.edit();

      // session = new SessionManagerLogin(getApplicationContext());
        session1 = new SessionManagerLogin(getApplicationContext());
        fontCustome= Typeface.createFromAsset(getAssets(),"Raleway_Regular.ttf");
        //TextView headername=(TextView)findViewById(R.id.headername);
      //  headername.setText("Login");
        mobile=(EditText)findViewById(R.id.EditText_email);
        password=(EditText)findViewById(R.id.EditText_password);
        loginbtn=(Button)findViewById(R.id.loginbtn);
        signupbtn=(TextView)findViewById(R.id.signupbtn);
        logotext=(TextView)findViewById(R.id.logotext);
        logotext.setTypeface(fontCustome);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });





        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (v.getId() == R.id.loginbtn) {

                    if (mobile.getText().toString().equalsIgnoreCase("")) {
                        alertDisplay("Please enter your mobile number");
                    }
                    else if (password.getText().toString().equalsIgnoreCase("")) {
                        alertDisplay("Please enter  password");
                    } else {
//                        startActivity(new Intent(LoginActivity.this,StudentActivity.class));
//                        finish();
                        new LoginAsy(mobile.getText().toString(), password.getText().toString()).execute();
                    }


                }
            }
        });



        /*if (session1.isLoggedIn()) {
            // Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
            HashMap<String, String> user = session1.getUserDetails();
            String Mob = user.get(SessionManagerLogin.MOBILE);


            String Pas = user.get(SessionManagerLogin.PASS);
            mobile.setText(Mob);
            password.setText(Pas);
            new LoginAsy(mobile.getText().toString(), password.getText().toString()).execute();
        }*/
    }







    class LoginAsy extends AsyncTask<String, String, String> {
        String respo;
        ProgressDialog pd;
        String number, pass;
        // String ostype="ANDROID";
        String token;
        LoginAsy(String mob, String pas) {
            number = mob;
            pass = pas;
            //token=regiToken;

        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            //String url=AppController.baseURL+AppController.loginUserURL+"user_email=" + userName + "&user_password=" + password + "&token="+regiToken+"";
            String url = AppController.baseURL + AppController.loginUserURL + "mail="+number+ "&password="+pass+"";
            try {
                respo = CustomHttpClient.urlincoding(url);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return respo;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                pd.dismiss();
                JSONObject jObje = new JSONObject(result);
                JSONObject namObj = jObje.getJSONObject("response");
                String st = namObj.getString("status");
                String st1 = namObj.getString("message");
                if (st.equals("1")) {
                    String userID = namObj.getString("id");
                    String username = namObj.getString("name");
                    String useraddress = namObj.getString("aadress");

                    String mobile = namObj.getString("mobile");
                    String pass = namObj.getString("pass");
                    String mailStr = namObj.getString("email");

                    settingDataPrefe.putString("id", userID);
                    settingDataPrefe.putString("name", username);
                    settingDataPrefe.putString("aadress", useraddress);

                    settingDataPrefe.putString("mobile", mobile);

                    settingDataPrefe.putString("email", mailStr);
                    settingDataPrefe.commit();
                    gObject.setUID(userID);

                    gObject.setLoginSessionFlg(true);
                    session1.createLoginSession(number, pass, true, userID);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{

                    Toast.makeText(LoginActivity.this,"incorrect credentials ",Toast.LENGTH_LONG).show();

                }
            }


            catch (JSONException e1) {


            }

        }
    }
    public void alertDisplay(String msg)
    {


        final Intent poke = new Intent();
        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
        poke.setData(Uri.parse("3"));
        sendBroadcast(poke);
        if (!isFinishing()) {
            new android.app.AlertDialog.Builder(LoginActivity.this)
                    //.setTitle("KartBuddy")
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }
}
