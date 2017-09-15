package utils;

import java.util.HashMap;

public class HtmlUtils {
    /**
     * Wrap text in html tags
     * @param text to be wrapped
     * @param tag that will wrap given text
     * @return text wrapped in html tags
     */
    public static String wrap(String text, String tag, HashMap<String, String> attributes){
        StringBuilder openingTag = new StringBuilder(tag);
        for(String attribute : attributes.keySet()){
            openingTag.append(" ").append(attribute).append("=\"").append(attributes.get(attribute)).append("\"");
        }
        return String.format("<%s>%s</%s>", openingTag.toString(), text, tag);
    }

    public static String wrap(String text, String tag){
        return wrap(text, tag, new HashMap<>());
    }
}
