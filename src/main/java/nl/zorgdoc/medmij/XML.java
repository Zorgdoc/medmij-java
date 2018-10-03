package nl.zorgdoc.medmij;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * XML
 */
class XML {
    private XML() {
    }

    public static Validator loadValidator(String resourcename) {
        var f = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        var xsdStream = XML.class.getResourceAsStream(resourcename);
        try {
            return f.newSchema(new StreamSource(xsdStream)).newValidator();
        } catch (SAXException e) {
            throw new RuntimeException("Error loading xsd " + resourcename, e);
        }
    }

    public static DocumentBuilder getParser() {
        var f = DocumentBuilderFactory.newInstance();
        f.setNamespaceAware(true);
        try {
            return f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new UnexpectedExceptionError(e);
        }
    }

    interface TFromStream<T> {
        public T operation(InputStream s) throws SAXException, IOException;
    }

    public static <T> T fromString(String xmldata, TFromStream<T> constructor) throws SAXException {
        var stream = new ByteArrayInputStream(xmldata.getBytes(StandardCharsets.UTF_8));
        try {
            return constructor.operation(stream);
        } catch (IOException e) {
            throw new UnexpectedExceptionError(e);
        }
    }

    public static <T> T fromURL(URL url, TFromStream<T> constructor) throws SAXException, IOException {
        try (var stream = url.openStream()) {
            return constructor.operation(stream);
        }
    }
}
