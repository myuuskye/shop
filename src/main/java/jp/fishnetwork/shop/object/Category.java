package jp.fishnetwork.shop.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.fishnetwork.shop.menu.CategoryMenu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Category {

    private final String name;

    private final int rows;

    private final List<Element> elements;

    private final CategoryMenu categoryMenu = new CategoryMenu(this);

    @SuppressWarnings("unchecked")
    public static Category deserialize(Map<String, Object> data) {
        String name = data.get("name").toString();
        int rows = Integer.valueOf(data.get("rows").toString());
        List<Element> elements = new ArrayList<>();
        for(Map<String, Object> elementData: (List<Map<String, Object>>)data.get("elements")) {
            elements.add(Element.deserialize(elementData));
        }
        return new Category(name, rows, elements);
    }

    public Element getElement(int index) {
        return elements.get(index);
    }

}
