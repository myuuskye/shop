package jp.fishnetwork.shop.menu;

import org.bukkit.entity.Player;

import jp.fishnetwork.shop.util.Util;

public abstract class Menu {

    public void open(Player player) {
        if(!Util.isBedrock(player)) {
            onJava(player);
        }else{
            onBedrock(player);
        }
    }

    public abstract void onJava(Player player);

    public abstract void onBedrock(Player player);

}
