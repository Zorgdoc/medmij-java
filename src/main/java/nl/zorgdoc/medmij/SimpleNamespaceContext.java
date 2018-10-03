package nl.zorgdoc.medmij;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

public class SimpleNamespaceContext implements NamespaceContext {
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
