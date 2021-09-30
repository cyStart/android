package com.webandcrafts.healwire.ui.my_cart;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.webandcrafts.healwire.AppController;
import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.R;
import com.webandcrafts.healwire.ui.upload_photo.UploadActivity;
import com.webandcrafts.healwire.adapter.MyCartListAdapter;
import com.webandcrafts.healwire.models.MyCartItems;
import com.webandcrafts.healwire.ui.home.HomeActivity;
import com.webandcrafts.healwire.ui.login.LoginActivity;
import com.webandcrafts.healwire.utils.SharedPreferenceHandler;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyCartActivity extends NewBaseActivity {

    ListView listViewMyCart;
    MyCartItems items;
    ImageView imageViewDelete;
    FloatingActionButton floatingActionButtonUpload, floatingActionButtonChooseFile, floatingActionButtonCamera;
    RelativeLayout relativeLayoutMain, relativeLayoutButtons;
    ProgressDialog progressDialog;
    Bitmap pres_image;
    String prescriptionStatus, responseMessage;
    String email;
    ImageView mBackicon;
    int is_pres_required;
    private Snackbar mSnackbar;
    String image, image_thumb;
    private int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static List<MyCartItems> cartItemsList;
    MyCartListAdapter adapter;
    String flag;
    int delete_position;
    Button buttonPlaceOrder;
    TextView textViewEmpty;
    int cart_length;
    String medicineName, quantity;
    private static final int requestCode = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        progressDialog = new ProgressDialog(this);
        relativeLayoutMain = findViewById(R.id.relativeLayoutMain);
        relativeLayoutButtons = findViewById(R.id.relativeLayoutButtons);
        mBackicon = findViewById(R.id.iv_back);
        mSnackbar = Snackbar.make(relativeLayoutMain, "You need to upload a Prescription.", Snackbar.LENGTH_INDEFINITE)
                .setAction("DONE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
        textViewEmpty = findViewById(android.R.id.empty);
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);
        floatingActionButtonUpload = findViewById(R.id.floatingActionButtonUpload);
        floatingActionButtonCamera = findViewById(R.id.floatingActionButtonCamera);
        floatingActionButtonChooseFile = findViewById(R.id.floatingActionButtonChooseFile);
        cartItemsList = new ArrayList<MyCartItems>();
        imageViewDelete = findViewById(R.id.imageViewDelete);
        listViewMyCart = findViewById(R.id.listViewMyCart);
        final SharedPreferenceHandler spHandler = new SharedPreferenceHandler();
        email = AppController.userEmail;
        cart_length = spHandler.getCartSize(getApplicationContext()); // Should be loaded from sharedPreference
        if (cart_length > 0) {
            textViewEmpty.setVisibility(View.GONE);
            medicineName = getIntent().getStringExtra("item_name");
            quantity = getIntent().getStringExtra("quantity");
            flag = "true";
            items = new MyCartItems(medicineName, quantity);
            SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
            cartItemsList = sharedPreferenceHandler.loadCartItems(getApplicationContext());
            buttonPlaceOrder.setVisibility(View.VISIBLE);

            for (int i = 0; i < cart_length; i++) {

                MyCartItems myCartItems = new MyCartItems();
                myCartItems = cartItemsList.get(i);

                if (myCartItems.getIs_pres_required() == 1) {

                    // Prescription is required
                    System.out.println("2nd call to prescription check");
                    mSnackbar.show();
                    break;

                } else {
                    continue;
                }
            }

            adapter = new MyCartListAdapter(this, cartItemsList);
            listViewMyCart.setAdapter(adapter);


        } else {
            textViewEmpty.setVisibility(View.VISIBLE);
            listViewMyCart.setEmptyView(textViewEmpty);
            buttonPlaceOrder.setVisibility(View.INVISIBLE);
        }

        listViewMyCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditText viewLayout;
                TextView textView;
                viewLayout = view.findViewById(R.id.editTextQuantity);
                textView = findViewById(R.id.textViewGroupMedicineName);
                String quan = viewLayout.getText().toString();
                String medNAme = textView.getText().toString();


            }
        });


        listViewMyCart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        listViewMyCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                delete_position = i;
                if (imageViewDelete.getVisibility() == View.GONE) {
                    imageViewDelete.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        floatingActionButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Upload the prescription if either one of the item :  is_prescription_required = 1

                try{
                if (mSnackbar.isShown()) {
                    mSnackbar.dismiss();
                    mSnackbar = null;
                }
                }catch (Exception e ){}

                if (relativeLayoutButtons.getVisibility() == View.GONE) {

                    relativeLayoutButtons.setVisibility(View.VISIBLE);

                } else {
                    relativeLayoutButtons.setVisibility(View.GONE);
                }

            }
        });


        // Camera button

        floatingActionButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppController.userStatus.equals("login")) {

//                    dispatchTakePictureIntent();
                    if (SharedPreferenceHandler.CART_LENGTH > 0) {

//                        Intent intentUpload = new Intent(getApplicationContext(),MyCartActivity.class);
//                        startActivity(intentUpload);

                        AppController.MY_CART_TO_CHOOSE_FILE_PATH = "true";


                        dispatchTakePictureIntent();

                    } else {


                        dispatchTakePictureIntent();


                    }

                } else {

                    Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentLogin);
                    overridePendingTransition(R.anim.right, R.anim.left);
                }

            }
        });

        // Choose file button

        floatingActionButtonChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (AppController.userStatus.equals("login")) {


                    if (SharedPreferenceHandler.CART_LENGTH > 0) {

//                        Intent intentUpload = new Intent(getApplicationContext(),MyCartActivity.class);
//                        startActivity(intentUpload);

                        AppController.MY_CART_TO_CHOOSE_FILE_PATH = "true";


                        Intent intentUpload = new Intent(getApplicationContext(), UploadActivity.class);
                        startActivity(intentUpload);
                        overridePendingTransition(R.anim.right, R.anim.left);
                       // finish();


                    } else {

                        //Cart length is 0
                        // Just upload prescrption only.

                        Intent intentUpload = new Intent(getApplicationContext(), UploadActivity.class);
                        startActivity(intentUpload);
                        overridePendingTransition(R.anim.right, R.anim.left);
                        finish();

                    }

                } else {

                    Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentLogin);
                    overridePendingTransition(R.anim.right, R.anim.left);
                    finish();
                }
            }
        });


        // place order button

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppController.MY_CART_TO_CHOOSE_FILE_PATH.equals("true")) {
                    image = UploadActivity.image_to_MyCart;
                    image_thumb = UploadActivity.image_thumb_to_MyCart;
                    AppController.MY_CART_TO_CHOOSE_FILE_PATH = "false";
                }

                boolean needPrescription = false;
                spHandler.getCartArrayList(getApplicationContext());
                for (int j = 0; j < SharedPreferenceHandler.CART_LENGTH; j++) {

                    if (SharedPreferenceHandler.cartList != null) {
                        MyCartItems myCarts = (MyCartItems) SharedPreferenceHandler.cartList.get(j);
                        if (myCarts.getIs_pres_required() == 1) {
                            needPrescription = true;
                        }
                    }
                }

                if (getIntent().getIntExtra("is_pres_required", 1) == 0 || !needPrescription || image != null && image_thumb != null) {

                    uploadPrescription(image, image_thumb);
                    buttonPlaceOrder.setEnabled(false);

                } else {
                    Toast.makeText(MyCartActivity.this, "Please upload a prescription", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();
                cartItemsList.remove(delete_position);
                sharedPreferenceHandler.removeCartItems(getApplicationContext(), items, delete_position);
                sharedPreferenceHandler.getCartArrayList(getApplicationContext());
                SharedPreferenceHandler.arrayListMedicineName.clear();
                for (int j = 0; j < SharedPreferenceHandler.CART_LENGTH; j++) {
                    MyCartItems myCarts = (MyCartItems) SharedPreferenceHandler.cartList.get(j);
                    SharedPreferenceHandler.arrayListMedicineName.add(myCarts.getMedName());
                }


                adapter.notifyDataSetChanged();
                imageViewDelete.setVisibility(View.GONE);
                cart_length = sharedPreferenceHandler.getCartSize(getApplicationContext()); // Should be loaded from sharedPreference
                if (cart_length == 0) {
                    textViewEmpty.setVisibility(View.VISIBLE);
                    listViewMyCart.setEmptyView(textViewEmpty);
                    buttonPlaceOrder.setVisibility(View.INVISIBLE);
                }

                Log.d("LENGTH", String.valueOf(cart_length));


            }
        });
        mBackicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCartActivity.this.finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                pres_image = bitmap;
                uploadFile(getResizedBitmap(pres_image, 600), getResizedBitmap(pres_image, 100));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            HomeActivity.cameraBitmap = (Bitmap) extras.get("data");
            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intent);
            AppController.DEVICE_CAMERA = 1;
        }
    }

    // upload the file
    public void uploadFile(Bitmap bitmap, Bitmap thumb) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        ByteArrayOutputStream baos_thumb = new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.PNG, 60, baos_thumb);
        byte[] b_thumb = baos_thumb.toByteArray();
        image = Base64.encodeToString(b, Base64.DEFAULT);
        image_thumb = Base64.encodeToString(b_thumb, Base64.DEFAULT);
        Toast.makeText(getApplicationContext(), "file upload success", Toast.LENGTH_SHORT).show();


    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public void uploadPrescription(final String prescription, final String prescription_thumb) {
        Context mContext = this;
//        dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
//        dialog.setContentView(R.layout.custom_dialog_pres);
//        dialog.show();

        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        StringRequest uploadPrescriptionRequest = new StringRequest(Request.Method.POST, AppController.STORE_PRESCRIPTION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONObject mainResponse = new JSONObject(response);
                            prescriptionStatus = mainResponse.getString("status");
                            responseMessage = mainResponse.getString("msg");


                            if (prescriptionStatus.equals("SUCCESS")) {

                                Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();

                                buttonPlaceOrder.setEnabled(true);


                                //Clear the cart items.


                                SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler();

                                cartItemsList.clear();

                                sharedPreferenceHandler.removeAllCart(getApplicationContext());

                                adapter.notifyDataSetChanged();

                                cart_length = sharedPreferenceHandler.getCartSize(getApplicationContext()); // Should be loaded from sharedPreference

                                if (cart_length == 0) {

                                    textViewEmpty.setVisibility(View.VISIBLE);
                                    listViewMyCart.setEmptyView(textViewEmpty);
                                    buttonPlaceOrder.setVisibility(View.INVISIBLE);

                                }


                            } else {

                                Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_LONG).show();


                            }

                        } catch (Exception e) {
                        }

                        MyCartActivity.this.finish();
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // dialog.dismiss();
                progressDialog.dismiss();
                buttonPlaceOrder.setEnabled(true);
                //messageHandler.sendEmptyMessage(98);

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                if (prescription != null && prescription_thumb != null) {
                    params.put("prescription", prescription);
                    params.put("prescription_thumb", prescription_thumb);
                } else {
                    params.put("prescription", "");
                    params.put("prescription_thumb", "");
                }

                if (cart_length > 0) {

                    params.put("cart_length", String.valueOf(cart_length));
                    for (int i = 0; i < cart_length; i++) {

                        MyCartItems myCartItems = new MyCartItems();
                        myCartItems = cartItemsList.get(i);

                        params.put("id" + i, String.valueOf(myCartItems.getId()));
                        params.put("quantity" + i, myCartItems.getQty());

                        if (myCartItems.getIs_pres_required() == 0) {
                            is_pres_required = 0;
                        } else {
                            is_pres_required = 1;
                        }
                    }
                }
                params.put("is_pres_required", String.valueOf(is_pres_required));
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(uploadPrescriptionRequest);
        uploadPrescriptionRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }//volley

    @Override
    public void onBackPressed() {
        MyCartActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        super.onBackPressed();
    }
}
