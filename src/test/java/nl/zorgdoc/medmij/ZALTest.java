package nl.zorgdoc.medmij;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class ZALTest {
    @Test
    public void testZALParseOK() throws SAXException, IOException {
        var data = new String(TestData.ZALExampleXMLURL.openStream().readAllBytes());

        ZAL.fromString(data);
        ZAL.fromUrl(TestData.ZALExampleXMLURL);
        ZAL.fromUrl(TestData.ZALExampleSingleXMLURL);
    }

    @Test
    public void testZALInvalidXML() {
        assertThrows(SAXException.class, () -> ZAL.fromString("<xml/>"));
        assertThrows(SAXException.class, () -> ZAL.fromString("<xml>"));
        assertThrows(SAXException.class, () -> ZAL.fromString("geen xml"));
        assertThrows(SAXException.class, () -> ZAL.fromString("&&"));
    }

    @Test
    public void testZALgetByName() throws SAXException, IOException {
        var zal = ZAL.fromUrl(TestData.ZALExampleXMLURL);
        assertNotNull(zal.getByName("umcharderwijk@medmij"));
        assertNull(zal.getByName(" umcharderwijk@medmij"));
        assertNull(zal.getByName("UMCharderwijk@medmij"));
        assertNull(zal.getByName(null));
    }

    @Test
    public void testZALgetById() throws SAXException, IOException {
        var zal = ZAL.fromUrl(TestData.ZALExampleXMLURL);
        var za = zal.getByName("umcharderwijk@medmij");
        assertNotNull(za.getGegevensdienstById("4"));
        assertNull(za.getGegevensdienstById("1"));
        assertNull(za.getGegevensdienstById(" 4"));
        assertNull(za.getGegevensdienstById(""));
        assertNull(za.getGegevensdienstById(null));
    }
}
