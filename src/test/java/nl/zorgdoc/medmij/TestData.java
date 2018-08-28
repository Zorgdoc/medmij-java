package nl.zorgdoc.medmij;

import java.net.URL;

public class TestData {
    private TestData() {
    }

    public static final URL WhiltelistExampleXMLURL = TestData.class.getResource("/WhitelistExample.xml");
    public static final URL WhiltelistExampleSingleXMLURL = TestData.class.getResource("/WhitelistExampleSingle.xml");
}
