package com.wdesign.wiseiptv.tv.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wdesign.wiseiptv.core.security.DeviceSecurity;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.core.util.SubscriptionInfo;
import com.wdesign.wiseiptv.tv.R;

public class TvAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_account);

        String key = DeviceSecurity.getOrCreateKey(this);
        ((TextView) findViewById(R.id.tv_device_key)).setText(key);

        findViewById(R.id.btn_copy_key).setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) cm.setPrimaryClip(ClipData.newPlainText("device_key", key));
            Toast.makeText(this, "Clé copiée", Toast.LENGTH_SHORT).show();
        });

        SubscriptionInfo info = SubscriptionInfo.read(this, SubscriptionInfo.PREFS_TV);
        TextView status = findViewById(R.id.tv_status_value);
        TextView expiry = findViewById(R.id.tv_expiry_value);
        TextView data = findViewById(R.id.tv_data_usage);
        TextView history = findViewById(R.id.tv_history);
        View renew = findViewById(R.id.btn_renew);

        if (info == null) {
            if (status != null) status.setText("Non activé");
            if (expiry != null) expiry.setText("Aucune date disponible");
            if (data != null) data.setText("0 Mo");
            if (history != null) history.setText("Aucun historique");
        } else {
            if (status != null) status.setText("Actif");
            if (data != null) data.setText("Données actives");
            if (history != null) history.setText("Abonnement TV Standard");
        }

        if (renew != null) {
            Fx.focusZoom(renew, 1.04f);
            renew.setOnClickListener(v -> Toast.makeText(this, "Renouvellement via revendeur / support", Toast.LENGTH_SHORT).show());
        }
    }
}
