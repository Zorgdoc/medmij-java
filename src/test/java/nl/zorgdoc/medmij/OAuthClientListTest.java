package nl.zorgdoc.medmij;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class OAuthClientListTest {
    @Test
    public void testOAuthClientListParseOK() throws SAXException, IOException {
        var data = new String(TestData.OAuthClientListExampleXMLURL.openStream().readAllBytes());

        OAuthclientList.fromString(data);
        OAuthclientList.fromUrl(TestData.OAuthClientListExampleXMLURL);
    }
}
