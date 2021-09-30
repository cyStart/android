package com.webandcrafts.healwire.ui.about_us;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.webandcrafts.healwire.NewBaseActivity;
import com.webandcrafts.healwire.R;

public class AboutusActivity extends NewBaseActivity {

    private ImageView mBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        mBackIcon = findViewById(R.id.iv_backicon);
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutusActivity.this.finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AboutusActivity.this.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        super.onBackPressed();
    }
}
