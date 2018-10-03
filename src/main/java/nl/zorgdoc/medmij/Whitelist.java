package nl.zorgdoc.medmij;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;

import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Whitelist
 */
public class Whitelist {
	private static Validator validator;

	private HashSet<String> hostnames;

	private static Validator getValidator() {
		if (validator == null) {
			validator = XML.loadValidator("/Whitelist.xsd");
		}
		return validator;
	}

	Whitelist(InputStream stream) throws SAXException, IOException {
		var parser = XML.getParser();
		var document = parser.parse(stream);
		getValidator().validate(new DOMSource(document));
		hostnames = parse(document);
	}

	public static Whitelist fromString(String xmldata) throws SAXException {
		return XML.fromString(xmldata, Whitelist::new);
	}

	public static Whitelist fromUrl(URL url) throws SAXException, IOException {
		return XML.fromURL(url, Whitelist::new);
	}

	public boolean contains(String hostname) {
		return hostnames.contains(hostname);
	}

	private static HashSet<String> parse(Document d) {
		var ctx = new SimpleNamespaceContext("w", "xmlns://afsprakenstelsel.medmij.nl/whitelist/release2/");
		var xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(ctx);

		NodeList l;
		try {
			l = (NodeList) xpath.compile("//w:MedMijNode").evaluate(d, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new UnexpectedExceptionError(e);
		}

		var hs = new HashSet<String>(l.getLength());
		for (int i = 0; i < l.getLength(); i++) {
			var node = l.item(i);
			hs.add(node.getTextContent());
		}
		return hs;
	}
}
