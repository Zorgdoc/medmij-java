package nl.zorgdoc.medmij;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.net.MalformedURLException;
import org.junit.jupiter.api.Test;

public class ZAOAuthTest {
    @Test
    public void testMakeRedirectURL() throws MalformedURLException {
        final String UriString = "https://example.com";
        var url = ZAOAuth.makeRedirectURL(UriString, "abc", "xyz");

        assertEquals("example.com", url.getAuthority());
        assertEquals("/cb", url.getPath());
        assertEquals("code=abc&state=xyz", url.getQuery());
    }
}
