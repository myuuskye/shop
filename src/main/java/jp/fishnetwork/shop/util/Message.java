package jp.fishnetwork.shop.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import jp.fishnetwork.shop.Main;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@UtilityClass
public class Message {

    private static final String FILE_NAME = "messages.properties";

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(new InputStreamReader(Main.getInstance().getResource(FILE_NAME)));
        }catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public static Component asComponent(String path, TagResolver ...tagResolvers) {
        return MiniMessage.miniMessage().deserialize(properties.getProperty(path), tagResolvers);
    }


    public static String asText(String path, TagResolver ...tagResolvers) {
        Component component = MiniMessage.miniMessage().deserialize(properties.getProperty(path), tagResolvers);
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

}
