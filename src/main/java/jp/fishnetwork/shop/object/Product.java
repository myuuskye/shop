package jp.fishnetwork.shop.object;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import jp.fishnetwork.shop.Main;
import jp.fishnetwork.shop.menu.TransactionMenu;
import jp.fishnetwork.shop.util.InventoryUtil;
import jp.fishnetwork.shop.util.ItemUtil;
import jp.fishnetwork.shop.util.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.milkbowl.vault.economy.Economy;

@Getter
@AllArgsConstructor
public class Product {

    private final String name;

    private final ItemStack item;

    private final Integer buy;
    private final Integer sell;

    private final TransactionMenu transactionMenu = new TransactionMenu(this);

    @SuppressWarnings("unchecked")
    public static Product deserialize(Map<String, Object> data) {
        String name = data.get("name").toString();
        ItemStack item = ItemUtil.deserialize((Map<String, Object>)data.get("item"));
        Integer buy = ((Map<String, Integer>)data.get("price")).get("buy");
        Integer sell = ((Map<String, Integer>)data.get("price")).get("sell");
        return new Product(name, item, buy, sell);
    }

    public boolean canBuy() {
        return buy != null;
    }

    public boolean canSell() {
        return sell != null;
    }

    public void buy(Player player, int amount) {
        Economy economy = Main.getInstance().getEconomy();
        PlayerInventory inventory = player.getInventory();
        ItemStack item = this.item.clone();
        int price = buy * amount;
        item.setAmount(item.getAmount() * amount);
        if(amount < 0) {
            player.sendMessage(Message.asComponent("error.invalidAmount", Placeholder.component("prefix", Message.asComponent("prefix"))));
            return;
        }
        if(!canBuy()) {
            player.sendMessage(Message.asComponent("error.cantBuy", Placeholder.component("prefix", Message.asComponent("prefix"))));
            return;
        }
        if(!economy.has(player, price)) {
            player.sendMessage(Message.asComponent("error.insufficientMoney", Placeholder.component("prefix", Message.asComponent("prefix"))));
            return;
        }
        if(!InventoryUtil.canAddItem(inventory, item)) {
            player.sendMessage(Message.asComponent("error.insufficientSlot", Placeholder.component("prefix", Message.asComponent("prefix"))));
            return;
        }
        economy.withdrawPlayer(player, price);
        inventory.addItem(item.clone());
        player.sendMessage(Message.asComponent(
            "transaction.buy",
            Placeholder.component("prefix", Message.asComponent("prefix")),
            Placeholder.component("item", Component.text(name)),
            Placeholder.component("amount", Component.text(item.getAmount())),
            Placeholder.component("price", Message.asComponent("prefix.price", Placeholder.component("price", Component.text(price))))
        ));
    }

    public void sell(Player player, int amount) {
        Economy economy = Main.getInstance().getEconomy();
        PlayerInventory inventory = player.getInventory();
        ItemStack item = this.item.clone();
        int price = sell * amount;
        item.setAmount(item.getAmount() * amount);
        if(amount < 0) {
            player.sendMessage(Message.asComponent("error.invalidAmount", Placeholder.component("prefix", Message.asComponent("prefix"))));
            return;
        }
        if(!canSell()) {
            player.sendMessage(Message.asComponent("error.cantSell", Placeholder.component("prefix", Message.asComponent("prefix"))));
            return;
        }
        if(!inventory.containsAtLeast(item, item.getAmount())) {
            player.sendMessage(Message.asComponent("error.insufficientItem", Placeholder.component("prefix", Message.asComponent("prefix"))));
            return;
        }
        economy.depositPlayer(player, price);
        inventory.removeItem(item.clone());
        player.sendMessage(Message.asComponent(
            "transaction.sell",
            Placeholder.component("prefix", Message.asComponent("prefix")),
            Placeholder.component("item", Component.text(name)),
            Placeholder.component("amount", Component.text(item.getAmount())),
            Placeholder.component("price", Message.asComponent("prefix.price", Placeholder.component("price", Component.text(price))))
        ));
    }

    public void allSell(Player player) {
        Economy economy = Main.getInstance().getEconomy();
        PlayerInventory inventory = player.getInventory();
        ItemStack item = this.item.clone();
        int amount = InventoryUtil.getItemAllCount(item, inventory);
        int price = sell * amount;
        item.setAmount(item.getAmount() * amount);
        if(amount < 0) {
            player.sendMessage(Message.asComponent("error.invalidAmount", Placeholder.component("prefix", Message.asComponent("prefix"))));
            return;
        }
        if(!canSell()) {
            player.sendMessage(Message.asComponent("error.cantSell", Placeholder.component("prefix", Message.asComponent("prefix"))));
            return;
        }
        if(!inventory.containsAtLeast(item, item.getAmount())) {
            player.sendMessage(Message.asComponent("error.insufficientItem", Placeholder.component("prefix", Message.asComponent("prefix"))));
            return;
        }
        economy.depositPlayer(player, price);
        inventory.removeItem(item.clone());
        player.sendMessage(Message.asComponent(
            "transaction.sell",
            Placeholder.component("prefix", Message.asComponent("prefix")),
            Placeholder.component("item", Component.text(name)),
            Placeholder.component("amount", Component.text(item.getAmount())),
            Placeholder.component("price", Message.asComponent("prefix.price", Placeholder.component("price", Component.text(price))))
        ));
    }

}
