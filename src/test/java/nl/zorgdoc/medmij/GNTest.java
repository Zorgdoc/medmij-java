package nl.zorgdoc.medmij;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class GNTest {
    @Test
    public void testGNParseOK() throws SAXException, IOException {
        var data = new String(TestData.GNExampleXMLURL.openStream().readAllBytes());

        Gegevensdienstnamenlijst.fromString(data);
        Gegevensdienstnamenlijst.fromUrl(TestData.GNExampleXMLURL);
     
    }

    @Test
    public void testGNInvalidXML() {
        assertThrows(SAXException.class, () -> Gegevensdienstnamenlijst.fromString("<xml/>"));
        assertThrows(SAXException.class, () -> Gegevensdienstnamenlijst.fromString("<xml>"));
        assertThrows(SAXException.class, () -> Gegevensdienstnamenlijst.fromString("geen xml"));
        assertThrows(SAXException.class, () -> Gegevensdienstnamenlijst.fromString("&&"));
    }

    @Test
    public void testGNgetByName() throws SAXException, IOException {
        var gn = Gegevensdienstnamenlijst.fromUrl(TestData.GNExampleXMLURL);
        assertNotNull(gn.getByName("Medicatiegegevens"));
        assertNull(gn.getByName(" Medicatiegegevens"));
        assertNull(gn.getByName("MEDicatiegegevens"));
        assertNull(gn.getByName(null));
    }

}
