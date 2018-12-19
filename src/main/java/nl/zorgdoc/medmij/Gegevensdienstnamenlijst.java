package nl.zorgdoc.medmij;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import nl.zorgdoc.medmij.ZAL.Zorgaanbieder.Gegevensdienst;

/**
 * ZAL
 */
public class Gegevensdienstnamenlijst {
	private static Validator validator;

	private static Validator getValidator() {
		if (validator == null) {
			validator = XML.loadValidator("/Gegevensdienstnamenlijst.xsd");
		}
		return validator;
	}

	public static class Gegevensdienst {
		public final String zorgaanbiedernaam;
		public final String id;

		public Gegevensdienst(String zorgaanbiedernaam, String id, String authorizationEndpointuri,
				String tokenEndpointuri) {
			this.zorgaanbiedernaam = zorgaanbiedernaam;
			this.id = id;
		}
	}

	private Map<String, Gegevensdienst> gegevensdiensten;

	Gegevensdienstnamenlijst(InputStream xmldata) throws SAXException, IOException {
		var parser = XML.getParser();
		var document = parser.parse(xmldata);
		getValidator().validate(new DOMSource(document));
		gegevensdiensten = parse(document);
	}

	public static Gegevensdienstnamenlijst fromString(String xmldata) throws SAXException {
		return XML.fromString(xmldata, Gegevensdienstnamenlijst::new);
	}

	public static Gegevensdienstnamenlijst fromUrl(URL url) throws SAXException, IOException {
		return XML.fromURL(url, Gegevensdienstnamenlijst::new);
	}

	private static Map<String, Gegevensdienst> parse(Document d) {
		var ctx = new SimpleNamespaceContext("w", "xmlns://afsprakenstelsel.medmij.nl/zorgaanbiederslijst/release2/");
		var xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(ctx);

		try {

			XPathExpression getGegevensdiensten = xpath.compile(".//w:Gegevensdienst");
			XPathExpression getGegevensdienstId = xpath.compile("w:GegevensdienstId/text()");
			XPathExpression getGegevensdienstName = xpath.compile("w:Weergavenaam/text()");

			var l = (NodeList) getGegevensdiensten.evaluate(d, XPathConstants.NODESET);

			var zal = new HashMap<String, Gegevensdienst>(l.getLength());
			for (int i = 0; i < l.getLength(); i++) {
				var node = l.item(i);
				var naam = (String) getNaam.evaluate(node, XPathConstants.STRING);

				var gegevensdienstId = (String) getGegevensdienstName.evaluate(node, XPathConstants.STRING);
				var gegevensdienst = new Gegevensdienst(naam, gegevensdienstId);
				gegevensdiensten.put(gegevensdienstId, gegevensdienst);
			}
			return gegevensdiensten;
		} catch (XPathExpressionException e) {
			throw new UnexpectedExceptionError(e);
		}
	}

	public Gegevensdienst getByName(String name) {
		return gegevensdiensten.get(name);
	}
	
	public Gegevensdienst getGegevensdienstById(String id) {
		return gegevensdiensten.get(id);
	}
}
