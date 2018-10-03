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
public class ZAL {
	private static Validator validator;

	private static Validator getValidator() {
		if (validator == null) {
			validator = XML.loadValidator("/ZAL.xsd");
		}
		return validator;
	}

	public static class Zorgaanbieder {
		private Map<String, Gegevensdienst> gegevensdiensten;

		public static class Gegevensdienst {
			public final String zorgaanbiedernaam;
			public final String id;
			public final String authorizationEndpointuri;
			public final String tokenEndpointuri;

			public Gegevensdienst(String zorgaanbiedernaam, String id, String authorizationEndpointuri,
					String tokenEndpointuri) {
				this.zorgaanbiedernaam = zorgaanbiedernaam;
				this.id = id;
				this.authorizationEndpointuri = authorizationEndpointuri;
				this.tokenEndpointuri = tokenEndpointuri;
			}
		}

		private Zorgaanbieder(Map<String, Gegevensdienst> gegevensdiensten) {
			this.gegevensdiensten = gegevensdiensten;
		}

		public Gegevensdienst getGegevensdienstById(String id) {
			return gegevensdiensten.get(id);
		}
	}

	private Map<String, Zorgaanbieder> zorgaanbieders;

	ZAL(InputStream xmldata) throws SAXException, IOException {
		var parser = XML.getParser();
		var document = parser.parse(xmldata);
		getValidator().validate(new DOMSource(document));
		zorgaanbieders = parse(document);
	}

	public static ZAL fromString(String xmldata) throws SAXException {
		return XML.fromString(xmldata, ZAL::new);
	}

	public static ZAL fromUrl(URL url) throws SAXException, IOException {
		return XML.fromURL(url, ZAL::new);
	}

	private static Map<String, Zorgaanbieder> parse(Document d) {
		var ctx = new SimpleNamespaceContext("w", "xmlns://afsprakenstelsel.medmij.nl/zorgaanbiederslijst/release2/");
		var xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(ctx);

		try {
			XPathExpression getZorgaanbieders = xpath.compile("//w:Zorgaanbieder");
			XPathExpression getNaam = xpath.compile("w:Zorgaanbiedernaam/text()");
			XPathExpression getGegevensdiensten = xpath.compile(".//w:Gegevensdienst");
			XPathExpression getGegevensdienstId = xpath.compile("w:GegevensdienstId/text()");
			XPathExpression getAuthorizationEndpointuri = xpath.compile(".//w:AuthorizationEndpointuri/text()");
			XPathExpression getTokenEndpointuri = xpath.compile(".//w:TokenEndpointuri/text()");

			var l = (NodeList) getZorgaanbieders.evaluate(d, XPathConstants.NODESET);

			var zal = new HashMap<String, Zorgaanbieder>(l.getLength());
			for (int i = 0; i < l.getLength(); i++) {
				var node = l.item(i);
				var naam = (String) getNaam.evaluate(node, XPathConstants.STRING);
				var gegnodelist = (NodeList) getGegevensdiensten.evaluate(node, XPathConstants.NODESET);
				var gegevensdiensten = new HashMap<String, Gegevensdienst>(gegnodelist.getLength());
				for (int j = 0; j < gegnodelist.getLength(); j++) {
					var gegnode = gegnodelist.item(j);
					var gegevensdienstId = (String) getGegevensdienstId.evaluate(gegnode, XPathConstants.STRING);
					var authorizationEndpointuri = (String) getAuthorizationEndpointuri.evaluate(gegnode,
							XPathConstants.STRING);
					var tokenEndpointuri = (String) getTokenEndpointuri.evaluate(gegnode, XPathConstants.STRING);
					var gegevensdienst = new Gegevensdienst(naam, gegevensdienstId, authorizationEndpointuri,
							tokenEndpointuri);
					gegevensdiensten.put(gegevensdienstId, gegevensdienst);
				}
				zal.put(naam, new Zorgaanbieder(gegevensdiensten));
			}
			return zal;
		} catch (XPathExpressionException e) {
			throw new UnexpectedExceptionError(e);
		}
	}

	public Zorgaanbieder getByName(String name) {
		return zorgaanbieders.get(name);
	}
}
