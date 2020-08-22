package org.coursera.bbcrsstechfeed.item.parser;

import org.coursera.bbcrsstechfeed.item.Item;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

/**
 * Utility class for parsing items from Reader
 */
public final class XMLUtils {
    private static final String itemTagString = "item";

    private static final String titleTagString = "title";
    private static final String descriptTagString = "description";
    private static final String linkTagString = "link";
    private static final String pubDateTagString = "pubDate";


    private XMLUtils() {
    }

    public static ArrayList<Item> parseItems(Reader reader) throws XmlPullParserException, DataFormatException, IOException {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(reader);

        String title = new String();
        String description = new String();
        String link = new String();
        String pubDate = new String();

        ArrayList<Item> items = new ArrayList<>();

        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    switch (parser.getName()) {
                        case titleTagString:
                            parser.next();
                            title = parser.getText();
                            break;
                        case descriptTagString:
                            parser.next();
                            description = parser.getText();
                            break;
                        case linkTagString:
                            parser.next();
                            link = parser.getText();
                            break;
                        case pubDateTagString:
                            parser.next();
                            pubDate = parser.getText();
                            break;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals(itemTagString)) {
                        items.add(new Item(title, description, link, pubDate));
                    }
                    break;
            }

            parser.next();
        }

        return items;
    }
}
