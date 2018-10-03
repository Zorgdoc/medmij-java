package nl.zorgdoc.medmij;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class ZAOAuth {
    private ZAOAuth() {
    }

    public static URL makeRedirectURL(String baseUri, String authCode, String state) throws MalformedURLException {
        String urlspec = String.format("%s/cb?code=%s&state=%s", baseUri, urlEncode(authCode), urlEncode(state));
        return new URL(urlspec);
    }

    private static String urlEncode(String v) {
        try {
            return URLEncoder.encode(v, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnexpectedExceptionError(e);
        }
    }
}
