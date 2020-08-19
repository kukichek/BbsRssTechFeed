package org.coursera.bbcrsstechfeed.item.parser;

import org.coursera.bbcrsstechfeed.item.Item;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public final class DOMUtils {
    private static final String itemTagString = "item";
    private static final String titleTagString = "title";
    private static final String descriptTagString = "description";
    private static final String linkTagString = "link";
    private static final String pubDateTagString = "pubDate";

    private DOMUtils() {}

    public static ArrayList<Item> parseItems(InputStream in) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(in);

        ArrayList<Item> items = new ArrayList<>();

        NodeList itemNodeList = document.getDocumentElement().getElementsByTagName(itemTagString);

        for (int i = 0; i < itemNodeList.getLength(); ++i) {
            Element itemElement = (Element) itemNodeList.item(i);

            try {
                Item item = new Item(
                        itemElement.getElementsByTagName(titleTagString).item(0).getTextContent(),
                        itemElement.getElementsByTagName(descriptTagString).item(0).getTextContent(),
                        itemElement.getElementsByTagName(linkTagString).item(0).getTextContent(),
                        itemElement.getElementsByTagName(pubDateTagString).item(0).getTextContent()
                );

                items.add(item);
            } catch (DataFormatException e) {
                e.printStackTrace();
            }
        }

        return items;
    }
}
