**W Design — +240 222 886 007 (WhatsApp)**
WhatsApp : +240 222 886 007
# Wise IPTV — Projet Android Multi-Module
**W Design — +240 222 886 007 (WhatsApp)**

---

## Architecture

```
WiseIPTV/
├── build.gradle              ← Versions centralisées
├── settings.gradle           ← Modules : core, mobile, tv
│
├── core/                     ← Logique métier partagée
│   └── src/main/java/com/wdesign/wiseiptv/core/
│       ├── db/               ← Room DB (WiseIptvDatabase)
│       │   ├── entity/       ← ChannelEntity, UserEntity, WatchHistoryEntity
│       │   └── dao/          ← ChannelDao, UserDao, WatchHistoryDao
│       ├── license/          ← LicenseManager, AuthManager
│       └── parser/           ← M3UParser, PlaylistSyncWorker
│
├── mobile/                   ← APK Phone/Tablette
│   └── src/main/java/com/wdesign/wiseiptv/mobile/
│       ├── WiseIptvApp.java
│       ├── ui/               ← SplashActivity, LoginActivity, HomeActivity
│       │                        PlayerActivity, AdminActivity, SettingsActivity
│       └── adapter/          ← ChannelGridAdapter, RecentHistoryAdapter
│
└── tv/                       ← APK Smart TV / Android TV Box
    └── src/main/java/com/wdesign/wiseiptv/tv/
        ├── WiseIptvTvApp.java
        ├── ui/               ← TvSplashActivity, TvLoginActivity, TvHomeActivity
        │                        TvPlayerActivity, TvAdminActivity
        ├── adapter/          ← CardPresenter (Leanback)
        └── receiver/         ← BootReceiver (auto-start TV Box)
```

---

## Comptes

| Rôle  | Username | Mot de passe  | Notes                        |
|-------|----------|---------------|------------------------------|
| Admin | admin    | Jesus@_2026   | Crée les users + licences    |
| User  | (créé par admin) | (défini par admin) | Licence 30/90/180/365j |

---

## Licences

| Type         | Durée  | Description            |
|--------------|--------|------------------------|
| Free Trial   | 7 jours| Automatique au 1er lancement |
| Mensuel      | 30j    | Généré par l'admin     |
| Trimestriel  | 90j    | Généré par l'admin     |
| Semestriel   | 180j   | Généré par l'admin     |
| Annuel       | 365j   | Généré par l'admin     |

---

## Flux de navigation

### Mobile (Phone/Tablette)
```
SplashActivity (2.5s)
    ↓ licence valide + connecté
HomeActivity (Bottom Nav : Accueil | Live | Films | Séries | Favoris | Paramètres)
    ↓ clic chaîne
PlayerActivity (ExoPlayer plein écran, gestes volume/luminosité)
```

### TV (Android TV Box)
```
TvSplashActivity (2.5s) → boot automatique au démarrage
    ↓
TvLoginActivity (D-Pad optimisé)
    ↓
TvHomeActivity (Leanback BrowseFragment : rangées Live/Films/Séries/Favoris)
    ↓ OK sur une carte
TvPlayerActivity (ExoPlayer, D-Pad : OK=pause, ◄►=±10s)
```

---

## Playlist M3U

Modifier `PLAYLIST_URL` dans :
- `mobile/WiseIptvApp.java`
- `tv/WiseIptvTvApp.java`

Format supporté :
```
#EXTM3U
#EXTINF:-1 tvg-id="..." tvg-name="Canal+" tvg-logo="http://..." tvg-country="CM" group-title="Afrique Live",Canal+ Cameroun
http://stream.url/live.m3u8
```

---

## Compilation

```bash
# APK Phone (debug)
./gradlew :mobile:assembleDebug

# APK TV (debug)
./gradlew :tv:assembleDebug

# Les deux en release
./gradlew :mobile:assembleRelease :tv:assembleRelease
```

---

## Configuration requise

- Android Studio Hedgehog ou supérieur
- JDK 17
- Android SDK 34
- Gradle 8.3.0
- compileSdk 34, minSdk 21 (Android 5.0+)

---

## Support

**W Design**
WhatsApp : +240 222 886 007

---

## Mise à jour design (1er novembre 2026)

Nouveau design appliqué aux modules `mobile` et `tv` :

- **Splash animé** (logo + halo + barre de chargement) → **écran d'activation** redessiné → accueil.
- Palette bleu nuit (`colors.xml`), cartes arrondies, boutons dégradés, icônes vectorielles.
- **Mobile** : accueil (salutation, carte d'abonnement, recherche, onglets en pastilles), grille de chaînes, lecteur (OSD), Mon compte (alerte d'expiration, statistiques), Paramètres, À propos, barre de navigation 5 onglets.
- **TV** : splash, activation, accueil (pastille d'abonnement, focus animé), cartes chaînes, OSD du lecteur.
- Animations : module `core/util/Fx.java` (entrées en cascade, pulsation, zoom au focus, compteurs) — animations natives Android, aucune dépendance ajoutée, donc les workflows GitHub Actions restent inchangés.
