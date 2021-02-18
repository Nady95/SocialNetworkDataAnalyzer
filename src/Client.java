import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *  Classe permettant de gérer un client.
 *
 *  Le client est celui qui va demander la liste des 3 meilleurs messages auprès du serveur.
 *
 * @author Nady Saddik
 * @author Alexandre Karakas
 * @version 2.0
 * @since Février 2020
 */
public class Client {

    /**
     *  Methode permettant de démarrer un client, selon le mode de démarrage choisi, et de récupérer la liste des
     *  trois meilleurs messages avec RMI.
     *
     * @param startMode Mode de démarrage (Texte ou XML)
     * @return La liste des trois meilleurs messages au format demandé
     */
    public static String start(App.StartMode startMode){
        try{
            Registry registry = LocateRegistry.getRegistry(33333);
            BestMessages bm = (BestMessages) registry.lookup("BestMessages");
            if(startMode.equals(App.StartMode.Text))
                return bm.getThreeBestMessagesToString();
            else
                return bm.getThreeBestMessagesToXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "Une erreur est survenue (consulter la console pour plus d'informations)";
        }
    }
}
