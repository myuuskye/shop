package jp.fishnetwork.shop.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.component.DropdownComponent;
import org.geysermc.cumulus.response.CustomFormResponse;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;

import jp.fishnetwork.shop.Main;
import jp.fishnetwork.shop.object.Product;
import jp.fishnetwork.shop.util.ItemUtil;
import jp.fishnetwork.shop.util.Message;
import jp.fishnetwork.shop.util.Util;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

@AllArgsConstructor
public class TransactionMenu extends Menu {

    private final Product product;

    @Override
    public void onJava(Player player) {
        ChestGui gui = new ChestGui(5, product.getName());
        StaticPane pane = new StaticPane(9, 5);
        List<Integer> amounts = getChoiceAmounts(product);
        pane.addItem(new GuiItem(product.getItem().clone()), 7, 2);
        for(int i = 0; i < amounts.size(); i++) {
            int amount = amounts.get(i);
            if(product.canBuy()) pane.addItem(
                new GuiItem(ItemUtil.setDisplayName(
                    new ItemStack(Material.GREEN_STAINED_GLASS_PANE, amount),
                    Message.asComponent("menu.transaction.java.buy", Placeholder.component("amount", Component.text(amount)))
                ), event -> product.buy((Player)event.getWhoClicked(), amount / product.getItem().getAmount())
            ), i + 1, 1);
            if(product.canSell()) pane.addItem(
                new GuiItem(ItemUtil.setDisplayName(
                    new ItemStack(Material.RED_STAINED_GLASS_PANE, amount),
                    Message.asComponent("menu.transaction.java.sell", Placeholder.component("amount", Component.text(amount)))
                ), event -> product.sell((Player)event.getWhoClicked(), amount / product.getItem().getAmount())
            ), i + 1, 3);
        }
        pane.fillWith(Main.getInstance().getBackgroundItem());
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.addPane(pane);
        gui.show(player);
    }

    @Override
    public void onBedrock(Player player) {
        DropdownComponent.Builder dropdownBuilder = DropdownComponent
            .builder()
            .text("取引タイプ");
        CustomForm.Builder formBuilder = CustomForm
            .builder()
            .title(product.getName())
            .label(Message.asText(
                "menu.transaction.bedrock.details",
                Placeholder.component("name", Component.text(product.getName())),
                Placeholder.component("amount", Component.text(product.getItem().getAmount())),
                Placeholder.component("buy",
                    product.canBuy()
                    ? Message.asComponent("prefix.price", Placeholder.component("price", Component.text(product.getBuy())))
                    : Message.asComponent("prefix.price.none")
                ),
                Placeholder.component("sell",
                    product.canSell()
                    ? Message.asComponent("prefix.price", Placeholder.component("price", Component.text(product.getSell())))
                    : Message.asComponent("prefix.price.none")
                )
            ));
        if(product.canBuy()) dropdownBuilder.option("購入");
        if(product.canSell()) dropdownBuilder.option("売却").option("全て売却");
        formBuilder.dropdown(dropdownBuilder);
        formBuilder.input(Message.asText("menu.transaction.bedrock.amount"));
        formBuilder.responseHandler((form, responseStr) -> {
            CustomFormResponse response = form.parseResponse(responseStr);
            if(response == null) return;
            try {
                switch(response.getDropdown(1)) {
                    case 0:
                        if(product.canBuy())
                            product.buy(player, Integer.parseInt(response.getInput(2)));
                        else
                            product.sell(player, Integer.parseInt(response.getInput(2)));
                        break;
                    case 1:
                        if(product.canBuy())
                            product.sell(player, Integer.parseInt(response.getInput(2)));
                        else
                            product.allSell(player);
                        break;
                    case 2:
                        product.allSell(player);
                        break;
                }
            }catch(NumberFormatException exception) {
                player.sendMessage(Message.asComponent("error.invalidAmount", Placeholder.component("prefix", Message.asComponent("prefix"))));
            }
        });
        Util.sendForm(player, formBuilder.build());
    }

    private List<Integer> getChoiceAmounts(Product product) {
        ItemStack item = product.getItem();
        List<Integer> amounts = new ArrayList<>();
        for(int i = item.getAmount(); i <= item.getMaxStackSize(); i *= 2) amounts.add(i);
        if(amounts.size() > 5) amounts.removeAll(amounts.subList(1, amounts.size() - 4));
        return amounts;
    }

}
