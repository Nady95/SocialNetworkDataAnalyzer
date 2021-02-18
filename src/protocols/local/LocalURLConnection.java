package protocols.local;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LocalURLConnection extends URLConnection {

    public LocalURLConnection(URL url) throws MalformedURLException {
        super(url);
        setContentHandlerFactory(new LocalContentHandlerFactory());
    }

    public void connect() throws IOException {

    }

    public InputStream getInputStream(){
        return null;
    }

    public String getHeaderField(String name){
        if(name.equals("content-type")) return "odins";
        return null;
    }
}
