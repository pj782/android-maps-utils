package com.google.maps.android.data.kml;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

import static com.google.maps.android.data.kml.KmlTestUtil.createParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class KmlParserTest {

    @Test
    public void testInlineStyle() throws Exception {
        XmlPullParser parser = createParser(com.google.maps.android.test.R.raw.amu_inline_style);
        KmlParser mParser = new KmlParser(parser);
        mParser.parseKml();
        assertNotNull(mParser.getPlacemarks());
        assertEquals(mParser.getPlacemarks().size(), 1);
        for (KmlPlacemark placemark : mParser.getPlacemarks().keySet()) {
            KmlStyle inlineStyle = placemark.getInlineStyle();
            assertNotNull(inlineStyle);
            assertEquals(inlineStyle.getPolylineOptions().getColor(), Color.parseColor("#000000"));
            assertEquals(
                    inlineStyle.getPolygonOptions().getFillColor(), Color.parseColor("#ffffff"));
            assertEquals(
                    inlineStyle.getPolylineOptions().getColor(),
                    inlineStyle.getPolygonOptions().getStrokeColor());
            assertEquals(placemark.getGeometry().getGeometryType(), "MultiGeometry");
        }
    }

    @Test
    public void testEmptyHotSpotStyle() throws Exception {
        XmlPullParser parser = createParser(com.google.maps.android.test.R.raw.amu_empty_hotspot);
        KmlParser mParser = new KmlParser(parser);
        mParser.parseKml();
        assertNotNull(mParser.getPlacemarks());
        assertEquals(1, mParser.getPlacemarks().size());
    }

    @Test
    public void testPolyStyleBooleanNumeric() throws Exception {
        XmlPullParser parser =
                createParser(com.google.maps.android.test.R.raw.amu_poly_style_boolean_numeric);
        KmlParser mParser = new KmlParser(parser);
        mParser.parseKml();
        assertNotNull(mParser.getPlacemarks());
        assertEquals(1, mParser.getContainers().size());
        KmlContainer kmlContainer = mParser.getContainers().get(0);
        assertTrue(kmlContainer.hasPlacemarks());

        HashMap<String, KmlStyle> styles = kmlContainer.getStyles();
        KmlStyle kmlStyle = styles.get("#fireadvisory");
        assertNotNull(kmlStyle);
        assertTrue(kmlStyle.hasFill());
        assertFalse(kmlStyle.hasOutline());
    }

    @Test
    public void testPolyStyleBooleanAlpha() throws Exception {
        XmlPullParser parser =
                createParser(com.google.maps.android.test.R.raw.amu_poly_style_boolean_alpha);
        KmlParser mParser = new KmlParser(parser);
        mParser.parseKml();
        assertNotNull(mParser.getPlacemarks());
        assertEquals(1, mParser.getContainers().size());
        KmlContainer kmlContainer = mParser.getContainers().get(0);
        assertTrue(kmlContainer.hasPlacemarks());

        Map<String, KmlStyle> styles = kmlContainer.getStyles();
        KmlStyle kmlStyle = styles.get("#fireadvisory");
        assertNotNull(kmlStyle);
        assertTrue(kmlStyle.hasFill());
        assertFalse(kmlStyle.hasOutline());
    }

    @Test
    public void testContainerHeirarchy() throws Exception {
        XmlPullParser parser = createParser(com.google.maps.android.test.R.raw.amu_document_nest);
        KmlParser mParser = new KmlParser(parser);
        mParser.parseKml();
        assertEquals(mParser.getContainers().get(0).getContainerId(), "hasId");
        assertEquals(mParser.getContainers().size(), 1);
        assertTrue(mParser.getContainers().get(0).hasContainers());
    }

    @Test
    public void testPlacemarkParsing() throws Exception {
        XmlPullParser parser = createParser(com.google.maps.android.test.R.raw.amu_unsupported);
        KmlParser mParser = new KmlParser(parser);
        mParser.parseKml();
        assertEquals(1, mParser.getPlacemarks().size());
    }
}
