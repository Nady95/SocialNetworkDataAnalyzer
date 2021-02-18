import javax.xml.bind.JAXBException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *  L'interface BestMessages contient les méthodes qui peuvent être appelées à distance avec RMI.
 *  Le but est de pouvoir récupérer via RMI les 3 meilleurs messages et les envoyer au client.
 *
 * @author Nady Saddik
 * @author Alexandre Karakas
 * @version 2.0
 * @since Février 2020
 */
public interface BestMessages extends Remote {
    String getThreeBestMessagesToString() throws RemoteException;
    String getThreeBestMessagesToXML() throws RemoteException, JAXBException;
}
