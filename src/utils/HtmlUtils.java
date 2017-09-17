package utils;

import java.util.HashMap;
import java.util.Map;

public class HtmlUtils {
    /**
     * Wrap text in html tags
     * @param text to be wrapped
     * @param tag that will wrap given text
     * @param attributes tag attributes can be added in HashMap as key-value pairs
     * @return text wrapped in html tags
     */
    public static String wrap(String text, String tag, HashMap<String, String> attributes){
        StringBuilder openingTag = new StringBuilder(tag);
        for(Map.Entry<String, String> attribute : attributes.entrySet()){
            openingTag.append(String.format(" %s=\"%s\"", attribute.getKey(), attribute.getValue()));
        }
        return String.format("<%s>%s</%s>", openingTag.toString(), text, tag);
    }

    public static String wrap(String text, String tag){
        return wrap(text, tag, new HashMap<>());
    }
}
