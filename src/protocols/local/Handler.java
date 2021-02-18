package protocols.local;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 *  Ouvre un fichier depuis une URL spécifiée.
 *
 * @author Nady Saddik
 * @author Alexandre Karakas
 * @version 2.0
 * @since Février 2020
 */
public class Handler extends URLStreamHandler {
    private final ClassLoader classLoader;

    /**
     *  Instancie un objet Handler
     */
    public Handler(){
        classLoader = getClass().getClassLoader();
    }

    /**
     *  Instancie un objet Handler
     * @param classLoader1 Classloader voulu
     */
    public Handler(ClassLoader classLoader1) {
        classLoader = classLoader1;
    }

/*
    protected LocalURLConnection openConnection(URL url) throws IOException {
        final URL ressourceURL = classLoader.getResource(url.getPath());
        return new LocalURLConnection(ressourceURL);
    }*/

    /**
     *  Démarre une connection pour l'objet spécifié en paramètre.
     *  Cela permet de récupérer un fichier en local
     *
     * @param u URL
     * @return Un objet URLConnection de l'URL u
     * @throws IOException Si une erreur I/O arrive lors du démarrage de la connection
     */
    protected URLConnection openConnection(URL u) throws IOException {
        final URL ressourceURL = classLoader.getResource(u.getPath());
        return ressourceURL.openConnection();
    }
}
