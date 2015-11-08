package al.arra.api.flickr.util;

import java.util.Map;

/**
 * Created by Gezim on 11/8/2015.
 */
public class MapUtil {
    private MapUtil(){}

    public static <K,V> String toString(Map<K,V> map)
    {
        if (map == null) return "";
        if (map.isEmpty()) return "{}";

        StringBuilder result = new StringBuilder();
        for(Map.Entry<K,V> entry : map.entrySet())
        {
            result.append(String.format(", %s -> %s ", entry.getKey().toString(), entry.getValue().toString()));
        }
        return "{" + result.substring(1) + "}";
    }
}
