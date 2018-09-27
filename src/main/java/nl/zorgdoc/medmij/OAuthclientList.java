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

public class OAuthclientList {
	private static Validator validator;

	private static Validator getValidator() {
		if (validator == null) {
			validator = XML.loadValidator("/OAuthclientList.xsd");
		}
		return validator;
	}

	public static class OAuthclient {
		public final String hostname;
		public final String organisatienaam;

		private OAuthclient(String hostname, String organisatienaam) {
			this.hostname = hostname;
			this.organisatienaam = organisatienaam;
		}
	}

	private Map<String, OAuthclient> oauthclients;

	OAuthclientList(InputStream xmldata) throws SAXException, IOException {
		var parser = XML.getParser();
		var document = parser.parse(xmldata);
		getValidator().validate(new DOMSource(document));
		oauthclients = parse(document);
	}

	public static OAuthclientList fromString(String xmldata) throws SAXException {
		return XML.fromString(xmldata, OAuthclientList::new);
	}

	public static OAuthclientList fromUrl(URL url) throws SAXException, IOException {
		return XML.fromURL(url, OAuthclientList::new);
	}

	private static Map<String, OAuthclient> parse(Document d) {
		var ctx = new SimpleNamespaceContext("ocl", "xmlns://afsprakenstelsel.medmij.nl/oauthclientlist/release2/");
		var xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(ctx);

		try {
			XPathExpression getOAuthclientsExpression = xpath.compile("//ocl:OAuthclient");
			XPathExpression getHostnameExpression = xpath.compile("ocl:Hostname/text()");
			XPathExpression getOrgnameExpression = xpath.compile("ocl:OAuthclientOrganisatienaam/text()");

			var l = (NodeList) getOAuthclientsExpression.evaluate(d, XPathConstants.NODESET);

			var ocl = new HashMap<String, OAuthclient>(l.getLength());
			for (int i = 0; i < l.getLength(); i++) {
				var node = l.item(i);
				var hostname = (String) getHostnameExpression.evaluate(node, XPathConstants.STRING);
				var orgname = (String) getOrgnameExpression.evaluate(node, XPathConstants.STRING);
				ocl.put(hostname, new OAuthclient(hostname, orgname));
			}
			return ocl;
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Unexpected XPathExpressionException", e);
		}
	}

	public OAuthclient getByHostname(String name) {
		return oauthclients.get(name);
	}
}
