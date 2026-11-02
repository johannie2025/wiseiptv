package com.wdesign.wiseiptv.mobile.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wdesign.wiseiptv.core.util.Fx;
import com.wdesign.wiseiptv.mobile.R;

public class AboutActivity extends AppCompatActivity {

    private static final String WEB_URL = "https://wise.alwaysdata.net/iptv/public/";

    private static final String LEGAL_TEXT =
        "ÉDITEUR\n"
      + "Wise IPTV est une application éditée par W Design — Agence d'Architecture · Design · Innovation.\n"
      + "Téléphone / WhatsApp : +240 222 886 007\n"
      + "Site : www.wdesign.ga\n\n"
      + "SERVICE\n"
      + "Wise IPTV est un lecteur multimédia. L'application ne fournit, n'héberge ni ne diffuse aucun contenu : "
      + "les flux sont fournis par le revendeur ou le fournisseur de l'abonnement de l'utilisateur, "
      + "qui reste seul responsable des droits de diffusion correspondants.\n\n"
      + "HÉBERGEMENT\n"
      + "Le panneau d'activation et de gestion des abonnements est accessible à l'adresse "
      + "wise.alwaysdata.net.\n\n"
      + "PROPRIÉTÉ INTELLECTUELLE\n"
      + "Le nom Wise IPTV, son logo, l'interface et le code de l'application sont la propriété de W Design. "
      + "Toute reproduction sans autorisation est interdite. Les logos de chaînes appartiennent à leurs détenteurs respectifs.\n\n"
      + "RESPONSABILITÉ\n"
      + "W Design ne garantit pas la disponibilité continue des flux tiers et ne peut être tenue responsable "
      + "des interruptions dues aux fournisseurs de contenu ou au réseau de l'utilisateur.\n\n"
      + "CONTACT\n"
      + "Pour toute question : +240 222 886 007 (WhatsApp).";

    private static final String PRIVACY_TEXT =
        "DONNÉES UTILISÉES\n"
      + "• Clé de l'appareil (Device Key) : générée sur votre appareil pour identifier votre abonnement auprès du panneau d'activation.\n"
      + "• Identifiants et adresses des serveurs fournis par votre revendeur, utilisés pour charger votre liste de chaînes.\n"
      + "• Préférences de l'application (favoris, chaînes récemment regardées, réglages, statistiques de consommation de données) : "
      + "stockées uniquement sur votre appareil.\n\n"
      + "CE QUE NOUS NE FAISONS PAS\n"
      + "• Aucune vente ni cession de vos données à des tiers à des fins publicitaires.\n"
      + "• L'historique de visionnage et les statistiques de données ne quittent pas votre appareil.\n\n"
      + "COMMUNICATION RÉSEAU\n"
      + "L'application contacte le panneau d'activation pour vérifier l'état de votre abonnement, "
      + "puis les serveurs de votre abonnement pour la liste des chaînes et la lecture des flux. "
      + "Les logos de chaînes sont téléchargés depuis les adresses indiquées dans votre liste.\n\n"
      + "CONTRÔLE PARENTAL\n"
      + "Le code PIN est conservé sous forme chiffrée (empreinte) sur l'appareil uniquement.\n\n"
      + "VOS DROITS\n"
      + "Vous pouvez effacer les données locales depuis les réglages Android (Stockage → Effacer les données) "
      + "ou en désinstallant l'application. Pour toute demande concernant votre abonnement, contactez le support WhatsApp : +240 222 886 007.";

    private android.animation.AnimatorSet logoAnim;

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_about);

        // Sous-écran de Paramètres : on garde "Paramètres" actif dans la barre
        NavHelper.bind(this, (BottomNavigationView) findViewById(R.id.bottom_nav), R.id.nav_settings, true);

        String v = "2.0.0";
        try { v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName; }
        catch (Exception ignored) { }
        ((TextView) findViewById(R.id.tv_about_version)).setText("v" + v);

        View dev = bindRow(R.id.row_dev, R.drawable.ic_info, "Développé par W Design",
            "Agence d'Architecture · Design · Innovation", null);
        View web = bindRow(R.id.row_web, R.drawable.ic_link, "Site web", "WiseIPTV — Solutions Premium OTT & Panel Revendeur", () ->
            open(new Intent(Intent.ACTION_VIEW, Uri.parse(WEB_URL))));
        View contact = bindRow(R.id.row_contact, R.drawable.ic_phone, "Nous contacter",
            "+240 222 886 007", () ->
            open(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+240222886007"))));

        View legal = bindRow(R.id.row_legal, R.drawable.ic_info, "Mentions légales",
            "Éditeur, hébergement, propriété", () -> showText("Mentions légales", LEGAL_TEXT));
        View privacy = bindRow(R.id.row_privacy, R.drawable.ic_check_circle, "Politique de confidentialité",
            "Données collectées et usage", () -> showText("Politique de confidentialité", PRIVACY_TEXT));

        Fx.pop(findViewById(R.id.about_logo));
        final View aboutLogo = findViewById(R.id.about_logo);
        aboutLogo.postDelayed(() -> { if (!isFinishing()) logoAnim = Fx.logoLife(aboutLogo); }, 450); // le logo lui-même s'anime
        Fx.stagger(150, 70, dev, web, contact, legal, privacy);
        Fx.pressable(web); Fx.pressable(contact); Fx.pressable(legal); Fx.pressable(privacy);
    }

    private View bindRow(int includeId, int icon, String title, String sub, Runnable action) {
        View row = findViewById(includeId);
        ((ImageView) row.findViewById(R.id.row_icon)).setImageResource(icon);
        ((TextView) row.findViewById(R.id.row_title)).setText(title);
        ((TextView) row.findViewById(R.id.row_sub)).setText(sub);
        if (action != null) row.setOnClickListener(v -> action.run());
        else { row.setClickable(false); row.setFocusable(false); }
        return row;
    }

    private void open(Intent i) {
        try { startActivity(i); } catch (Exception ignored) { }
    }

    private void showText(String title, String body) {
        new android.app.AlertDialog.Builder(this).setTitle(title).setMessage(body)
            .setPositiveButton("Fermer", null).show();
    }

    @Override protected void onResume() {
        super.onResume();
        com.wdesign.wiseiptv.mobile.util.ResumeStore.setScreen(this,
            com.wdesign.wiseiptv.mobile.util.ResumeStore.SCREEN_ABOUT);
    }

    @Override protected void onDestroy() {
        if (logoAnim != null) logoAnim.cancel();
        super.onDestroy();
    }
}
