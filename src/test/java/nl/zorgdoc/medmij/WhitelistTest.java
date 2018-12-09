package nl.zorgdoc.medmij;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * WhitelistTest
 */
public class WhitelistTest {

    @Test
    public void testWhitelistParseOK() throws SAXException, IOException {
        var data = new String(TestData.WhiltelistExampleXMLURL.openStream().readAllBytes());
        Whitelist.fromString(data);
        Whitelist.fromUrl(TestData.WhiltelistExampleXMLURL);
       // Whitelist.fromUrl(TestData.WhiltelistExampleSingleXMLURL);
    }

    @Test
    public void testWhitelistInvalidXML() {
        assertThrows(SAXException.class, () -> Whitelist.fromString("<xml/>"));
        assertThrows(SAXException.class, () -> Whitelist.fromString("<xml>"));
        assertThrows(SAXException.class, () -> Whitelist.fromString("geen xml"));
        assertThrows(SAXException.class, () -> Whitelist.fromString("&&"));
    }

    @Test
    public void testWhitelistContains() throws SAXException, IOException {
        var whitelist = Whitelist.fromUrl(TestData.WhiltelistExampleXMLURL);
        assertTrue(whitelist.contains("rcf-rso.nl"));
        assertFalse(whitelist.contains("rcf-rso.nl."));
        assertFalse(whitelist.contains("RCF-RSO.NL"));
        assertFalse(whitelist.contains(null));
    }
}
