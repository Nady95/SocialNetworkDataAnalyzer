package protocols.local;

import java.net.ContentHandler;
import java.net.ContentHandlerFactory;

public class LocalContentHandlerFactory implements ContentHandlerFactory {

    public ContentHandler createContentHandler(String s) {
        return new LocalContentHandler();
    }
}
