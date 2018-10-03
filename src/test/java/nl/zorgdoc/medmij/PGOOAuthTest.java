package nl.zorgdoc.medmij;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import nl.zorgdoc.medmij.ZAL.Zorgaanbieder.Gegevensdienst;

public class PGOOAuthTest {
    @Test
    public void testMakeAuthUri() throws SAXException, IOException {
        final String PGO_REDIR = "https://pgo.example.com/my-endpoint";
        final String PGO_ID = "pgo.example.com";

        var zal = ZAL.fromUrl(TestData.ZALExampleXMLURL);
        var za = zal.getByName("umcharderwijk@medmij");
        var geg = za.getGegevensdienstById("4");

        var url = PGOOAuth.makeAuthURL(geg, PGO_ID, PGO_REDIR, "abc");

        assertTrue(url.toString().startsWith(geg.authorizationEndpointuri));
        assertTrue(url.getQuery().contains("client_id=" + PGO_ID));
        assertTrue(url.getQuery().contains("umcharderwijk%7E4"));
    }

    @Test
    public void testGetAccessToken() throws SAXException, IOException {
        final String TOKEN = "ABC";
        final String URI = "https://example.com/token";

        var zal = ZAL.fromUrl(TestData.ZALExampleXMLURL);
        var za = zal.getByName("umcharderwijk@medmij");
        var geg = za.getGegevensdienstById("4");

        try {
            var t = PGOOAuth.getAccessToken(geg, TOKEN, URI);
            assertNotNull(t);
            assertNotEquals("", t);
        } catch (java.net.UnknownHostException e) {
            System.out.println(e);
        }
    }

}
