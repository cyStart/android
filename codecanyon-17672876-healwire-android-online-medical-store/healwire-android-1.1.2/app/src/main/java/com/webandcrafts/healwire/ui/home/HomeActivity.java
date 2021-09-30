package com.webandcrafts.healwire.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.adapter.CustomMenuList;
import com.webandcrafts.healwire.ui.medicine_details.MedicineInformationActivity;
import com.webandcrafts.healwire.ui.my_cart.MyCartActivity;
import com.webandcrafts.healwire.ui.my_orders.MyOrdersActivity;
import com.webandcrafts.healwire.ui.my_prescription.MyPrescriptionActivity;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.utils.PreferenceManager;
import com.webandcrafts.healwire.ui.profile.ProfileActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;
import com.webandcrafts.healwire.ui.upload_photo.UploadActivity;
import com.webandcrafts.healwire.ui.about_us.AboutusActivity;
import com.webandcrafts.healwire.ui.login.LoginActivity;
import com.webandcrafts.healwire.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends NewBaseActivity {

    public CustomMenuList adapterMenuList;
    ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawerLayout;
    private String email;
    public static Bitmap cameraBitmap;
    TextView textViewName;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    Toolbar toolbar;
    private DrawerLayout drawerLayoutHome;
    private ListView listViewNavigationDrawer;
    public static final int DRAWER_ITEM__HOME = 0;
    public static final int DRAWER_ITEM__PROFILE = 1;
    public static final int DRAWER_ITEM__MYPRESCRIPTION = 2;
    public static final int DRAWER_ITEM__MYORDERS = 3;
    public static final int DRAWER_ITEM__ITEMSINCART = 4;
    public static final int DRAWER_ITEM__ABOUT = 5;
    public static final int DRAWER_ITEM__EXIT = 6;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int MY_REQUEST_CODE = 1;
    RelativeLayout relativeLayoutTakePicture, relativeLayoutChooseFile, relativeSearchButtonInner;
    AutoCompleteTextView autoCompleteTextViewSearchMedicine;
    public String TAG_LOAD_MEDICINE_REQUEST = "tag-load-medicine";
    public String item_name, item_code, composition, discount_type, tax_type, manufacturer, group;
    public int id, mrp, discount, tax, is_delete, is_pres_required;
    public static List<JSONObject> paidPrescription = new ArrayList<JSONObject>();
    public static List<JSONObject> unpaidPrescription = new ArrayList<JSONObject>();
    JSONObject jsonObjectPrescription;
    private String medicineStatus;
    SimpleAdapter adapterMedicine;
    ArrayAdapter<String> adapter;
    List<HashMap<String, String>> medicineList;
    private static final int requestCode = 100;
    //List<String> aList;
    public List<String> aList = new ArrayList<String>();
    private static int RESULT_LOAD_IMAGE = 1;
    String[] menuTitles = {
            "Home",
            "Profile",
            "My Prescription",
            "My Orders",
            "Items in Cart",
            "About",
            "Exit"
    };
    Integer[] menuIcons = {
            R.drawable.home,
            R.drawable.profile_grey,
            R.drawable.prescription_grey,
            R.drawable.my_orders_grey,
            R.drawable.cart_grey,
            R.drawable.about_grey,
            R.drawable.exit

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
        AppController.userStatus = sharedPreferenceHandler.getUserStatus(getApplicationContext());
        sharedPreferenceHandler.getPrescriptionStatus(getApplicationContext());
        sharedPreferenceHandler.getInvoiceStatus(getApplicationContext());
        sharedPreferenceHandler.getPaymentStatus(getApplicationContext());
        sharedPreferenceHandler.getShippingStatus(getApplicationContext());
        Log.d("PRES", String.valueOf(SharedPreferenceHandler.UNVERIFIED));
        Log.d("INV", String.valueOf(SharedPreferenceHandler.PAID));
        Log.d("PAY", String.valueOf(SharedPreferenceHandler.FAILURE));
        Log.d("SHIP", String.valueOf(SharedPreferenceHandler.RECEIVED));
        menuTitles = getResources().getStringArray(R.array.menu_item_array);
        initNavigationDrawer();
        AppController.userEmail = sharedPreferenceHandler.getUserEmail(getApplicationContext());
        sharedPreferenceHandler.getUserProfile(getApplicationContext());
        email = sharedPreferenceHandler.getUserEmail(getApplicationContext());

        relativeLayoutTakePicture = (RelativeLayout) findViewById(R.id.relativeLayoutTakePicture);
        relativeLayoutChooseFile = (RelativeLayout) findViewById(R.id.relativeLayoutChooseFile);
        relativeSearchButtonInner = (RelativeLayout) findViewById(R.id.relativeSearchButtonInner);
        adapterMenuList = new CustomMenuList(HomeActivity.this, menuTitles, menuIcons);


        sharedPreferenceHandler.getUserProfile(getApplicationContext());
        sharedPreferenceHandler.getUserStatus(getApplicationContext());


        if (AppController.userStatus.equals("logout")) {


            textViewName.setText("Hello User");

        } else {

            AppController.userFullName = SharedPreferenceHandler.FIRST_NAME + " " + SharedPreferenceHandler.LAST_NAME;
            textViewName.setText(AppController.userFullName);
        }

        autoCompleteTextViewSearchMedicine = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewSearchMedicine);


        relativeLayoutTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Intent for Take picture activity

                if (AppController.userStatus.equals("login")) {
                    dispatchTakePictureIntent();
                } else {
                    Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentLogin);
                    overridePendingTransition(R.anim.right, R.anim.left);
                }

            }
        });


        relativeLayoutChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppController.userStatus.equals("login")) {

                    if (SharedPreferenceHandler.CART_LENGTH > 0) {

                        Intent intentUpload = new Intent(getApplicationContext(), MyCartActivity.class);
                        startActivity(intentUpload);
                        overridePendingTransition(R.anim.right, R.anim.left);
                    } else {
                        Intent intentUpload = new Intent(getApplicationContext(), UploadActivity.class);
                        startActivity(intentUpload);
                    }
                } else {
                    Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentLogin);
                    overridePendingTransition(R.anim.right, R.anim.left);
                }
            }
        });


        relativeSearchButtonInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (autoCompleteTextViewSearchMedicine.length() > 0) {

                    displayMedicineDetails(autoCompleteTextViewSearchMedicine.getText().toString());

                }

            }
        });


        autoCompleteTextViewSearchMedicine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aList.clear();
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() != 0) {
                    medicineList = new ArrayList<HashMap<String, String>>();
                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner_dropdown_item, aList);
                    autoCompleteTextViewSearchMedicine.setAdapter(adapter);
                    autoCompleteTextViewSearchMedicine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Utils.hideKeyboard(HomeActivity.this);
                        }
                    });
                    aList.clear();
                    searchMedicine(autoCompleteTextViewSearchMedicine.getText().toString());
                }
            }
        });

    }


    @Override
    public void onBackPressed() {

        AlertDialog alertDialog;
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Do you want to exit?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult(Activity.RESULT_OK);
                HomeActivity.super.onBackPressed();
                System.exit(0);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(Activity.RESULT_CANCELED);
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("GET_PRE_THUMB", "API CALLING...");
        paidPrescription.clear();
        unpaidPrescription.clear();
        getPrescriptionThumbComplete(email);
        initNavigationDrawer();
    }

    private void dispatchTakePictureIntent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        requestCode);
            }else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
        }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public void setLoginPreferences(String status) {

        SharedPreferences.Editor editor = getSharedPreferences(PreferenceManager.MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("login_status", status);
        editor.commit();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode == requestCode){

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                dispatchTakePictureIntent();
            }
        }
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

        mDrawerToggle.syncState();

    }


    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);


        View headerLayout = navigationView.getHeaderView(0); // 0-index header
        textViewName = (TextView) headerLayout.findViewById(R.id.textViewName);

        SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
        sharedPreferenceHandler.getUserProfile(getApplicationContext());
        sharedPreferenceHandler.getUserStatus(getApplicationContext());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.home:

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.profile:


                        if (AppController.userStatus.equals("login")) {

                            Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                            startActivity(intentProfile);
                            overridePendingTransition(R.anim.right, R.anim.left);

                        } else {

                            Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentLogin);
                            overridePendingTransition(R.anim.right, R.anim.left);
                        }
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.myprescription:

                        if (AppController.userStatus.equals("login")) {

                            Intent intentPrescription = new Intent(getApplicationContext(), MyPrescriptionActivity.class);
                            startActivity(intentPrescription);
                            overridePendingTransition(R.anim.right, R.anim.left);
                        } else {

                            Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentLogin);
                            overridePendingTransition(R.anim.right, R.anim.left);

                        }

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.myorders:

                        if (AppController.userStatus.equals("login")) {

                            Intent intentOrders = new Intent(getApplicationContext(), MyOrdersActivity.class);
                            startActivity(intentOrders);
                            overridePendingTransition(R.anim.right, R.anim.left);
                        } else {

                            Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentLogin);
                            overridePendingTransition(R.anim.right, R.anim.left);
                        }

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.itemsincart:
                        if (AppController.userStatus.equals("login")) {

                            Intent intentItemsInCart = new Intent(getApplicationContext(), MyCartActivity.class);
                            startActivity(intentItemsInCart);
                            overridePendingTransition(R.anim.right, R.anim.left);
                        } else {

                            Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentLogin);
                            overridePendingTransition(R.anim.right, R.anim.left);

                        }

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.about:

                        Intent intentAbout = new Intent(getApplicationContext(), AboutusActivity.class);
                        startActivity(intentAbout);
                        overridePendingTransition(R.anim.right, R.anim.left);

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.exit:
                        drawerLayout.closeDrawers();
                        AlertDialog alertDialog;
                        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(HomeActivity.this);
                        builder.setMessage("Do you want to exit?");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
//        builder.setIcon(R.mipmap.app_icon_blue_and_yellow);
                        builder.setCancelable(false);

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setResult(Activity.RESULT_OK);
                                HomeActivity.super.onBackPressed();
                                System.exit(0);
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(Activity.RESULT_CANCELED);
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.show();


                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
//        TextView tv_email = (TextView)header.findViewById(R.id.textViewName);
//        tv_email.setText("raj.amalw@learn2crack.com");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);


            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
                Utils.hideKeyboard(HomeActivity.this);
                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                sharedPreferenceHandler.getUserProfile(getApplicationContext());
                sharedPreferenceHandler.getUserStatus(getApplicationContext());


                if (AppController.userStatus.equals("logout")) {


                    textViewName.setText("Hello User");

                } else {

                    AppController.userFullName = SharedPreferenceHandler.FIRST_NAME + " " + SharedPreferenceHandler.LAST_NAME;
                    textViewName.setText(AppController.userFullName);
                }


            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


    public synchronized void searchMedicine(final String search) {

        StringRequest searchMedicineRequest = new StringRequest(Request.Method.POST, AppController.LOAD_MEDICINE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //medicineList.removeAll(medicineList);

                            aList.clear();


                            JSONObject mainResponse = new JSONObject(response);
                            medicineStatus = mainResponse.getString("status");


                            if (medicineStatus.equals("SUCCESS")) {

                                JSONArray jsonArrayData = mainResponse.getJSONArray("data");


                                for (int i = 0; i < jsonArrayData.length(); i++) {

                                    JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);

                                    HashMap<String, String> hm = new HashMap<String, String>();
                                    hm.put("txt", jsonObjectData.getString("item_name"));

//                                hm.put("mrp",  jsonObjectData.getString("mrp"));
//                                hm.put("discount", jsonObjectData.getString("discount"));

                                    String itemName = jsonObjectData.getString("item_name");
                                    int id = jsonObjectData.getInt("id");


                                    aList.add(itemName);


                                }

                                //medicineList.add(hm);

                                //adapter.notifyDataSetChanged();


                                //adapterMedicine.notifyDataSetChanged();
                            }

                            //Load names to drop down


                            adapter.notifyDataSetChanged();

//                            autoCompleteTextViewSearchMedicine.setAdapter(adapter);


                        } catch (Exception e) {
                            // messageHandler.sendEmptyMessage(99);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //messageHandler.sendEmptyMessage(98);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("n", search);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(searchMedicineRequest, TAG_LOAD_MEDICINE_REQUEST);

        // AppController.getInstance().cancelPendingRequests(TAG_LOAD_MEDICINE_REQUEST);


        //  Toast.makeText(getApplicationContext(),"Retrieved data",Toast.LENGTH_SHORT).show();


        searchMedicineRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20), 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }//volley


    // To display medicine information


    public synchronized void displayMedicineDetails(final String name) {

        Utils.hideKeyboard(HomeActivity.this);

        StringRequest displayMedicineRequest = new StringRequest(Request.Method.POST, AppController.LOAD_MEDICINE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //medicineList.removeAll(medicineList);

                            JSONObject mainResponse = new JSONObject(response);
                            medicineStatus = mainResponse.getString("status");


                            if (medicineStatus.equals("SUCCESS")) {

                                JSONArray jsonArrayData = mainResponse.getJSONArray("data");

                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);

                                    item_code = jsonObjectData.getString("item_code");
                                    item_name = jsonObjectData.getString("item_name");
                                    mrp = jsonObjectData.getInt("mrp");
                                    composition = jsonObjectData.getString("composition");
                                    id = jsonObjectData.getInt("id");
                                    is_pres_required = jsonObjectData.getInt("is_pres_required");

                                }

                            }

                            Intent intentMedicineInfo = new Intent(getApplicationContext(), MedicineInformationActivity.class);
                            intentMedicineInfo.putExtra("item_code", item_code);
                            intentMedicineInfo.putExtra("item_name", item_name);
                            intentMedicineInfo.putExtra("mrp", mrp);
                            intentMedicineInfo.putExtra("composition", composition);
                            intentMedicineInfo.putExtra("id", id);
                            intentMedicineInfo.putExtra("is_pres_required", is_pres_required);
                            startActivity(intentMedicineInfo);
                            overridePendingTransition(R.anim.right, R.anim.left);
                            autoCompleteTextViewSearchMedicine.setText(null);

                        } catch (Exception e) {
                            // messageHandler.sendEmptyMessage(99);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //messageHandler.sendEmptyMessage(98);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("n", name);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(displayMedicineRequest, TAG_LOAD_MEDICINE_REQUEST);



    }


    //To load detials of My Prescription of a user


    public synchronized void getPrescriptionThumbComplete(final String email) {

//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();


        StringRequest doLogin = new StringRequest(Request.Method.POST, AppController.GET_PRESCRIPTION_THUMB_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


//                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

//                        progressDialog.dismiss();


                        try {

                            JSONObject mainResponse = new JSONObject(response);


                            String prescriptionStatus = mainResponse.getString("status");
                            String responseMessage = mainResponse.getString("msg");

                            if (prescriptionStatus.equals("SUCCESS")) {

                                JSONObject jsonObjectData = mainResponse.getJSONObject("data");
                                JSONArray jsonArrayPrescriptions = jsonObjectData.getJSONArray("prescriptions");


                                //Toast.makeText(getApplicationContext(),"Array : "+jsonArrayPrescriptions.length(),Toast.LENGTH_LONG).show();


                                for (int i = 0; i < jsonArrayPrescriptions.length(); i++) {


                                    int invoice_id = jsonArrayPrescriptions.getJSONObject(i).getInt("invoice_id");


                                    int invoice_status_id = jsonArrayPrescriptions.getJSONObject(i).getInt("invoice_status_id");

                                    jsonObjectPrescription = jsonArrayPrescriptions.getJSONObject(i);


                                    if (invoice_status_id == 1 || invoice_status_id == 3 || invoice_status_id == 4) {


                                        unpaidPrescription.add(jsonObjectPrescription);


                                    } else {

                                        paidPrescription.add(jsonObjectPrescription);

                                    }


                                }


                                Log.d("UNPAID JSONOBJECT", String.valueOf(unpaidPrescription));
                                Log.d("PAID JSONOBJECT", String.valueOf(paidPrescription));


                                //listPrescriptionAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            // messageHandler.sendEmptyMessage(99);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  messageHandler.sendEmptyMessage(98);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);


                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map headers = new HashMap();
                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();

                if (!sharedPreferenceHandler.getCookie(getApplicationContext()).equals(""))
                    headers.put("Cookie", sharedPreferenceHandler.getCookie(getApplicationContext()));

                Log.d("HEADER_LOG", headers.toString());

                // return super.getHeaders();
                return headers;

            }


        };
        AppController.getInstance().addToRequestQueue(doLogin);

    }//volley


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            cameraBitmap = (Bitmap) extras.get("data");
            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intent);
            AppController.DEVICE_CAMERA = 1;


        }


    }


}
