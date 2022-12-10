package jp.fishnetwork.shop.util;

import org.bukkit.entity.Player;
import org.geysermc.cumulus.Form;
import org.geysermc.floodgate.api.FloodgateApi;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {

    public static boolean isBedrock(Player player) {
        return FloodgateApi.getInstance().isFloodgateId(player.getUniqueId());
    }

    public static void sendForm(Player player, Form form) {
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }

}
