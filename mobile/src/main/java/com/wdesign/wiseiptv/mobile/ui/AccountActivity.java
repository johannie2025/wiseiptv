package com.wdesign.wiseiptv.mobile.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.wdesign.wiseiptv.core.util.SubscriptionInfo;
import com.wdesign.wiseiptv.mobile.R;

public class AccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView tvDeviceId = findViewById(R.id.tv_device_id);
        TextView tvStatus = findViewById(R.id.tv_status);

        SharedPreferences prefs = getSharedPreferences("wise_settings", MODE_PRIVATE);
        String deviceId = prefs.getString("device_id", "WISE-" + android.os.Build.SERIAL);
        
        if (tvDeviceId != null) {
            tvDeviceId.setText(deviceId);
        }
        if (tvStatus != null) {
            tvStatus.setText("Abonnement actif — " + SubscriptionInfo.WHATSAPP);
        }
    }
}
