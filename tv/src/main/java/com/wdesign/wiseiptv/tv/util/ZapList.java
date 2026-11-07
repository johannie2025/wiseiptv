package com.wdesign.wiseiptv.tv.util;

import com.wdesign.wiseiptv.core.db.entity.ChannelEntity;
import java.util.ArrayList;
import java.util.List;

/** Liste des chaînes telle que l'utilisateur la voit : le zapping plein écran suit exactement cet ordre. */
public final class ZapList {
    private static List<ChannelEntity> list = new ArrayList<>();
    private ZapList() {}
    public static synchronized void set(List<ChannelEntity> l) { list = l == null ? new ArrayList<>() : new ArrayList<>(l); }

    /** Chaîne voisine (dir = -1 précédente, +1 suivante), en boucle ; ignore les doublons du même flux. */
    public static synchronized ChannelEntity neighbor(long currentId, String currentUrl, int dir) {
        int n = list.size(), idx = -1;
        for (int i = 0; i < n; i++) if (list.get(i).id == currentId) { idx = i; break; }
        if (idx < 0 || n < 2) return null;
        for (int step = 1; step < n; step++) {
            ChannelEntity c = list.get(((idx + dir * step) % n + n) % n);
            if (c.id != currentId && (currentUrl == null || !currentUrl.equals(c.streamUrl))) return c;
        }
        return null;
    }
}
