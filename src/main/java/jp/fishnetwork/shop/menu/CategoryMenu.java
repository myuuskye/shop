package jp.fishnetwork.shop.menu;

import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.SimpleForm.Builder;
import org.geysermc.cumulus.response.SimpleFormResponse;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import jp.fishnetwork.shop.Main;
import jp.fishnetwork.shop.object.Category;
import jp.fishnetwork.shop.object.Element;
import jp.fishnetwork.shop.object.Product;
import jp.fishnetwork.shop.util.ItemUtil;
import jp.fishnetwork.shop.util.Message;
import jp.fishnetwork.shop.util.Util;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

@AllArgsConstructor
public class CategoryMenu extends Menu {

    private final Category category;

    @Override
    public void onJava(Player player) {
        ChestGui gui = new ChestGui(category.getRows(), category.getName());
        StaticPane pane = new StaticPane(9, gui.getRows());
        for(Element element: category.getElements()) {
            switch(element.getType()) {
                case PRODUCT:
                    Product product = element.getProduct();
                    pane.addItem(new GuiItem(ItemUtil.addLore(
                        element.getItem(),
                        Component.text(""),
                        Message.asComponent("menu.category.java.border"),
                        Component.text(""),
                        Message.asComponent("menu.category.java.buy", Placeholder.component("price", product.canBuy() ?
                            Message.asComponent(
                                "prefix.price", Placeholder.component("price", Component.text(product.getBuy()))
                            ) : Message.asComponent("prefix.price.none")
                        )),
                        Message.asComponent("menu.category.java.sell", Placeholder.component("price", product.canSell() ?
                            Message.asComponent(
                                "prefix.price", Placeholder.component("price", Component.text(product.getSell()))
                            ) : Message.asComponent("prefix.price.none")
                        )),
                        Component.text(""),
                        Message.asComponent("menu.category.java.click")
                    ), event -> product.getTransactionMenu().open(player)), element.getSlot() % 9, element.getSlot() / 9);
                    break;
                case CATEGORY:
                    Category category = element.getCategory();
                    pane.addItem(new GuiItem(ItemUtil.addLore(
                        element.getItem(),
                        Component.text(""),
                        Message.asComponent("menu.category.java.border"),
                        Component.text(""),
                        Message.asComponent("menu.category.java.click")
                    ), event -> category.getCategoryMenu().open(player)), element.getSlot() % 9, element.getSlot() / 9);
                    break;
            }
        }
        pane.fillWith(Main.getInstance().getBackgroundItem());
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.addPane(pane);
        gui.show(player);
    }

    @Override
    public void onBedrock(Player player) {
        Builder builder = SimpleForm.builder()
        .title(category.getName())
        .responseHandler((form, responseStr) -> {
            SimpleFormResponse response = form.parseResponse(responseStr);
            if(response.getClickedButtonId() == -1) return;
            Element element = category.getElement(response.getClickedButtonId());
            switch(element.getType()) {
                case PRODUCT:
                    element.getProduct().getTransactionMenu().open(player);
                    break;
                case CATEGORY:
                    element.getCategory().getCategoryMenu().open(player);
                    break;
            }
        });
        for(Element element: category.getElements()) {
            switch(element.getType()) {
                case PRODUCT:
                    Product product = element.getProduct();
                    builder.button(Message.asText(
                        "menu.category.bedrock.product",
                        Placeholder.component("name", Component.text(product.getName())),
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
                    ), element.getImage());
                    break;
                case CATEGORY:
                    Category category = element.getCategory();
                    builder.button(category.getName(), element.getImage());
                    break;
            }
        }
        Util.sendForm(player, builder.build());
    }

}
