package protocols.local;

import java.io.IOException;
import java.net.ContentHandler;
import java.net.URL;
import java.net.URLConnection;

public class LocalContentHandler extends ContentHandler {

    public Object getContent(URLConnection urlConnection) throws IOException {
        URL url = urlConnection.getURL();
        return url.getFile();
    }
}
