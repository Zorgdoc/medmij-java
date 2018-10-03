package nl.zorgdoc.medmij;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import nl.zorgdoc.medmij.ZAL.Zorgaanbieder.Gegevensdienst;

public class PGOOAuth {
    private PGOOAuth() {
    }

    public static URL makeAuthURL(Gegevensdienst gegevensdienst, String clientId, String redirectUri, String state)
            throws MalformedURLException {
        var baseUri = gegevensdienst.authorizationEndpointuri;
        var prefix = gegevensdienst.zorgaanbiedernaam.replace("@medmij", "");
        var scope = String.format("%s~%s", prefix, gegevensdienst.id);

        String urlspec = String.format("%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s", baseUri,
                urlEncode(clientId), urlEncode(redirectUri), urlEncode(scope), urlEncode(state));

        return new URL(urlspec);
    }

    public static String getAccessToken(Gegevensdienst gegevensdienst, String authorizationCode, String redirectUri)
            throws IOException {
        var baseUri = gegevensdienst.tokenEndpointuri;
        var urlspec = String.format("%s?grant_type=authorization_code&code=%s&client_id=&redirect_uri=%s", baseUri,
                urlEncode(authorizationCode), urlEncode(redirectUri));

        var url = new URL(urlspec);
        var c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("POST");
        return readFully(c.getInputStream());
    }

    private static String urlEncode(String v) {
        try {
            return URLEncoder.encode(v, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnexpectedExceptionError(e);
        }
    }

    private static String readFully(InputStream inputStream) throws IOException {
        var baos = new ByteArrayOutputStream();
        var buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }
}
