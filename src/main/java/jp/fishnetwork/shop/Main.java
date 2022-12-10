package jp.fishnetwork.shop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import jp.fishnetwork.shop.command.ShopCommand;
import jp.fishnetwork.shop.object.Category;
import jp.fishnetwork.shop.object.Product;
import jp.fishnetwork.shop.util.ItemUtil;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;

@Getter
public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    private Economy economy;

    private Category mainCategory;

    private ItemStack backgroundItem;

    private Map<String, Product> products = new HashMap<>();

    private Map<String, Category> categories = new HashMap<>();

    @Override
    public void onEnable() {
        saveResource("shop.yml", false);
        RegisteredServiceProvider<Economy> service = getServer().getServicesManager().getRegistration(Economy.class);
        getCommand("shop").setExecutor(new ShopCommand());
        instance = this;
        economy = service != null ? service.getProvider() : null;
        loadShopConfiguration();
    }

    public Category getCategory(String name) {
        return categories.get(name);
    }

    public Product getProduct(String name) {
        return products.get(name);
    }

    @SuppressWarnings("unchecked")
    private void loadShopConfiguration() {
        Configuration configuration = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "shop.yml"));
        for(Map<?, ?> categoryData: configuration.getMapList("categories")) {
            Category category = Category.deserialize((Map<String, Object>)categoryData);
            categories.put(category.getName(), category);
        }
        for(Map<?, ?> productData: configuration.getMapList("products")) {
            Product product = Product.deserialize((Map<String, Object>)productData);
            products.put(product.getName(), product);
        }
        backgroundItem = ItemUtil.deserialize(configuration.getConfigurationSection("background_item").getValues(false));
        mainCategory = getCategory(configuration.getString("main_category"));
    }

}
