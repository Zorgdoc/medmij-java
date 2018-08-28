package nl.zorgdoc.medmij;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


/**
 * Whitelist
 */
public class Whitelist {
    private static Validator _validator;

    private HashSet<String> _hostnames;

    private Validator loadValidator(String resourcename) {
        var f = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        var xsdStream = Whitelist.class.getResourceAsStream(resourcename);
        try {
            return f.newSchema(new StreamSource(xsdStream)).newValidator();
        } catch (SAXException e) {
            throw new Error("Error loading xsd " + resourcename, e);
        }
    }

    private Validator getValidator() {
        if (_validator == null) {
            _validator = loadValidator("/Whitelist.xsd");
        }
        return _validator;
    }

    private DocumentBuilder getParser() {
        var f = DocumentBuilderFactory.newInstance();
        f.setNamespaceAware(true);
        try {
            return f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new Error("Unexpected ParserConfigurationException", e);
        }
    }

    Whitelist(InputStream xmldata) throws SAXException, IOException {
        var parser = getParser();
        var document = parser.parse(xmldata);
        getValidator().validate(new DOMSource(document));
        _hostnames = parse(document);
    }

    public static Whitelist fromString(String xmldata) throws SAXException {
        var stream = new ByteArrayInputStream(xmldata.getBytes(StandardCharsets.UTF_8));
        try {
            return new Whitelist(stream);
        } catch (IOException e) {
            throw new Error("Unexpected IOException from ByteArrayInputStream", e);
        }
    }

    public static Whitelist FromUrl(URL url) throws SAXException, IOException {
        try (var s = url.openStream()) {
            return new Whitelist(s);
        }
    }

    public boolean contains(String hostname) {
        return _hostnames.contains(hostname);
    }

    public static class SimpleNamespaceContext implements NamespaceContext {
        String prefix;
        String url;

        public SimpleNamespaceContext(String prefix, String url) {
            this.prefix = prefix;
            this.url = url;
        }

        @Override
        public String getNamespaceURI(String prefix) {
            if (prefix.equals(this.prefix)) {
                return this.url;
            } else {
                return null;
            }
        }

        @Override
        public String getPrefix(String arg0) {
            return null;
        }

        @Override
        public Iterator<String> getPrefixes(String arg0) {
            return null;
        }
    }

    private static HashSet<String> parse(Document d) {
        var ctx = new SimpleNamespaceContext("w", "xmlns://afsprakenstelsel.medmij.nl/whitelist/release2/");
        var xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(ctx);

        NodeList l;
        try {
			l = (NodeList)xpath.compile("//w:MedMijNode").evaluate(d, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
            throw new Error("Unexpected XPathExpressionException", e);
        }

        var hs = new HashSet<String>(l.getLength());
        for (int i = 0; i < l.getLength(); i++)
        {
            var node = l.item(i);
            hs.add(node.getTextContent());
        }
        return hs;
    }
}
