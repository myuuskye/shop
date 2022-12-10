package jp.fishnetwork.shop.object;

import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.util.FormImage;

import jp.fishnetwork.shop.Main;
import jp.fishnetwork.shop.util.ItemUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Element {

    private final Type type;

    private final String categoryName;

    private final String productName;

    private final ItemStack item;

    private final FormImage image;

    private final int slot;

    @SuppressWarnings("unchecked")
    public static Element deserialize(Map<String, Object> data) {
        Type type = Type.valueOf(data.get("type").toString());
        String categoryName = type == Type.CATEGORY ? data.get("category").toString() : null;
        String productName = type == Type.PRODUCT ? data.get("product").toString() : null;
        ItemStack item = ItemUtil.deserialize((Map<String, Object>)data.get("item"));
        FormImage image = FormImage.of(
            FormImage.Type.valueOf(((Map<String, String>)data.get("image")).get("type").toString()),
            ((Map<String, String>)data.get("image")).get("data").toString()
        );
        int slot = Integer.valueOf(data.get("slot").toString());
        return new Element(type, categoryName, productName, item, image, slot);
    }

    public Category getCategory() {
        return Main.getInstance().getCategory(categoryName);
    }

    public Product getProduct() {
        return Main.getInstance().getProduct(productName);
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public enum Type {
        CATEGORY, PRODUCT
    }

}
