package jp.fishnetwork.shop.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jp.fishnetwork.shop.Main;
import jp.fishnetwork.shop.util.Message;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class ShopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            Main.getInstance().getMainCategory().getCategoryMenu().open(player);
            return true;
        }
        sender.sendMessage(Message.asComponent("error.onlyPlayer", Placeholder.component("prefix", Message.asComponent("prefix"))));
        return true;
    }

}
