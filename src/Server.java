import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  Classe permettant de gérer le serveur.
 *
 *  Le serveur calcule les trois meilleurs messages d'un réseau social sur une machine virtuelle et peut
 *  les envoyer à un client (on utilise RMI).
 *
 * @author Nady Saddik
 * @author Alexandre Karakas
 * @version 2.0
 * @since Février 2020
 */
public class Server {
    private static ServerSocket serverSocket;
    private static ExecutorService pool;

    /**
     *  Méthode permettant de démarrer le serveur et de permettre l'envoi des trois meilleurs messages avec RMI.
     *
     * @return Vrai si le démarrage s'est passé sans encombre, faux sinon.
     */
    public static boolean start() {
        try{
            BestMessages imp = (BestMessages) UnicastRemoteObject.exportObject(new RunImpl(), 33333);
            Registry registry = LocateRegistry.createRegistry(33333);
            registry.rebind("BestMessages", imp);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  Permet d'instancier un serveur avec le port voulu et une taille de pool de threads voulus
     *  (plus utilisée depuis l'implémentation de RMI).
     *
     * @param port Port
     * @param poolSize Taille du pool de threads
     * @throws IOException Si le ServeurSocket n'a pas pu être créé
     */
    public Server(int port, int poolSize) throws IOException {
        serverSocket = new ServerSocket(port);
        // On crée un pool de threads de taille poolSize
        pool = Executors.newFixedThreadPool(poolSize);
    }
}