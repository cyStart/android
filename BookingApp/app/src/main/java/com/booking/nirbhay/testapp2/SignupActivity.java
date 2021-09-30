package com.booking.nirbhay.testapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {
     EditText  name,number,pass,address,mailId;
    Button submit;
    ProgressDialog pd;
    String jRespons;
    TextView logotext;
    Typeface fontCustome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        /*TextView headername=(TextView)findViewById(R.id.headername);
        headername.setText("Signup");
*/
        fontCustome= Typeface.createFromAsset(getAssets(),"Raleway_Regular.ttf");
        logotext=(TextView)findViewById(R.id.logotext);
        logotext.setTypeface(fontCustome);
        name=(EditText)findViewById(R.id.name);
        number=(EditText)findViewById(R.id.number);
        pass=(EditText)findViewById(R.id.pass);
        address=(EditText)findViewById(R.id.address);
        mailId=(EditText)findViewById(R.id.mailId);
        submit=(Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (name.getText().toString().equalsIgnoreCase("")) {

                    name.setError("Please Enter name");
                }

                else if (number.getText().toString().equalsIgnoreCase("")) {

                    number.setError("Please enter  Mobile Number");
                }
                else if (pass.getText().toString().equalsIgnoreCase("")) {

                    pass.setError("Please enter  password");
                }
                else if (address.getText().toString().equalsIgnoreCase("")) {

                    address.setError("Please enter  Address");
                }
                else if (mailId.getText().toString().equalsIgnoreCase("")) {

                    address.setError("Please enter  Mail Id");
                }

                else {

                    new SignUp(name.getText().toString(),number.getText().toString(),  pass.getText().toString(),address.getText().toString(),mailId.getText().toString()).execute();
                }



            }
        });


    }

    class SignUp extends AsyncTask<String,String,String>
    {
        String tempName;
        String tempnumber;
        String tempPass;
        String tempaddress;
        String tempMail;


        SignUp(String nam, String mob, String pas, String addr,String mail)
        {
            tempName=nam;
            tempnumber=mob;
            tempPass=pas;
            tempaddress=addr;
            tempMail=mail;


        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(SignupActivity.this);
            pd.setMessage("Please wait...");
            pd.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

                nameValuePair.add(new BasicNameValuePair("name", tempName));
                nameValuePair.add(new BasicNameValuePair("mobile", tempnumber));
                nameValuePair.add(new BasicNameValuePair("pass", tempPass));
                nameValuePair.add(new BasicNameValuePair("address", tempaddress));
                nameValuePair.add(new BasicNameValuePair("mail", tempMail));

                jRespons = CustomHttpClient.executeHttpPost(AppController.baseURL +AppController.singupUserURL, nameValuePair);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return jRespons;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try{
                JSONObject jobj=new JSONObject(s);
                JSONObject jName=jobj.getJSONObject("response");
                String  flg=jName.getString("status");
                String msg=jName.getString("message");
                if(flg.equals("1"))
                {

                    //alertDisplay("Registration  successful");
                    Toast.makeText(SignupActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                }
                else {
                   // alertDisplay("Registration not successful");
                    Toast.makeText(SignupActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                }


            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
