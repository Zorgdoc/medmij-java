package nl.zorgdoc.medmij;

import java.net.URL;

public class TestData {
    private TestData() {
    }
    public static final URL GNExampleXMLURL = TestData.class.getResource("/GegevensdienstnamenlijstExample.xml");

    public static final URL WhiltelistExampleXMLURL = TestData.class.getResource("/WhitelistExample1.xml");
    public static final URL WhiltelistExampleSingleXMLURL = TestData.class.getResource("/WhitelistExampleSingle.xml");
    public static final URL ZALExampleXMLURL = TestData.class.getResource("/ZALExample.xml");
    public static final URL ZALExampleSingleXMLURL = TestData.class.getResource("/ZALExampleSingle.xml");
    public static final URL OAuthClientListExampleXMLURL = TestData.class.getResource("/OAuthClientListExample.xml");
}
