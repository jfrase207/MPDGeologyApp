package jfrase207.bgsdatastarter.mpd.gcu.mpdgeologyapp;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {

    // We don't use namespaces
    private static final String ns = null;

    public static List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private static List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();


        parser.nextTag();

        parser.require(XmlPullParser.START_TAG, ns, "channel");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                entries.add(Item.readEntry(parser));
            } else  {
                Item.skip(parser);
            }
        }
        return entries;
    }

    public static class Item {

        public String title, link, summary, pubDate, magnitude;
        public float lat,lon;

        public Item(String title, String summary, String link, float lat, float lon, String pubDate, String magnitude) {
            this.title = title;
            this.summary = summary;
            this.link = link;
            this.lat = lat;
            this.lon = lon;
            this.pubDate = pubDate;
            this.magnitude = magnitude;
        }


        // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
        public static Item readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "item");
            //parser.require(XmlPullParser.START_TAG, ns, "item");
            String title = null;
            String summary = null;
            String link = null;
            float lat = 0;
            float lon = 0;
            String pubDate = null;
            String magnitude = null;

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("title")) {
                    title = readString(parser, "title");
                } else if (name.equals("description")) {
                    summary = readString(parser, "description");
                } else if (name.equals("link")) {
                    link = readString(parser, "link");
                }else if (name.equals("geo:lat")) {
                    lat = readFloat(parser,"geo:lat");
                }else if (name.equals("geo:long")) {
                    lon = readFloat(parser,"geo:long");
                }else if (name.equals("pubDate")) {
                    pubDate = readString(parser,"pubDate");
                } else {
                    skip(parser);
                }
            }
            return new Item(title, summary, link, lat, lon, pubDate, magnitude );
        }

        public  static String readString(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {

            parser.require(XmlPullParser.START_TAG, ns, tag);
            String element = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, tag);

            return element;
        }

        public  static float readFloat(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {

            parser.require(XmlPullParser.START_TAG, ns, tag);
            String element = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, tag);

            return Float.parseFloat(element);
        }

        public static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }

        private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }

    }
}




