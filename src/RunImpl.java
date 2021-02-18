import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  Implémentation du thread permettant de lire un fichier et calculer continuellement les trois meilleurs messages
 *  (ceux qui ont la plus grande valeur d'importance)
 *
 * @author Nady Saddik
 * @author Alexandre Karakas
 * @version 2.0
 * @since Février 2020
 */
public class RunImpl extends Thread implements Runnable, BestMessages {
    private Map<Integer, GenericTree<FileData>> map_threads_of_discussion;
    private List<Message> messageList;
    private final Timer timer = new Timer();

    /**
     *  Permet de démarrer le thread
     */
    public RunImpl(){
        start();
    }

    /**
     *  Calcule la valeur d'importance du message qui initie le thread de discussion (représenté par un noeud racine
     *  dans un arbre).
     *
     * @param root Noeud racine de l'arbre contenant le message qui initie le thread de discussion
     */
    public void computeValueOfImportance(GenericTreeNode<FileData> root){
        if(((Message) root.getData()).isActive()){
            AtomicInteger value_of_importance = new AtomicInteger(root.getData().getScore());

            for(GenericTreeNode<FileData> node : root.getChildren())
                value_of_importance.addAndGet(node.getData().getScore());

            ((Message) root.getData()).setValueOfImportance(value_of_importance.get());
        }
    }

    /**
     *  Permet de planifier la diminution du score d'un message/commentaire (représenté par un noeud d'un arbre) de 1pt
     *  toutes les 30 secondes, et recalcule la valeur d'importance de chaque thread de discussion (représenté
     *  par un arbre).
     *
     * @param node Noeud de l'arbre dont on souhaite diminuer le score
     * @param tree Arbre dont on souhaite recalculer la valeur d'importance
     */
    private void scheduleScoreDecrease(GenericTreeNode<FileData> node, GenericTree<FileData> tree){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                node.getData().decreaseScoreByOne();
                // On n'oublie pas de recalculer la VOI du thread
                computeValueOfImportance(tree.getRoot());
            }
        }, 30000, 30000);
    }

    /**
     *  "Super"-méthode qui se charge de réaliser toutes les tâches demandées sur chaque ligne lue : elle détermine
     *  s'il s'agit d'un message ou d'un commentaire, crée un noeud contenant le message/commentaire lu.
     *  S'il s'agit d'un message, il crée un thread de discussion (donc un arbre dont la racine est le message).
     *  S'il s'agit d'un commentaire, il crée un noeud et rajoute ce noeud dans le thread correspondant.
     *  Puis calcule continuellement les scores et valeurs d'importances.
     *
     * @param strData Ligne du fichier qu'on a lu
     */
    private void doAllTheWork(String strData){
        // On patiente 1-3s avant de réaliser les opérations
        wait_random_time(1000,3000);

        // On délimite la ligne lue afin de séparer les différentes valeurs pour les stocker dans un tableau
        String[] r = strData.split("\\|", -1);
        // On crée ensuite une variable de type FileData, qui peut désigner soit un commentaire, soit un message
        FileData data = createData(r);

        // On crée un noeud contenant le message/commentaire lu
        GenericTreeNode<FileData> node = new GenericTreeNode<>(data);
        // On crée une variable qui contiendra le thread de discussion
        GenericTree<FileData> thread_of_discussion;

        // Si la ligne lue est un message, on exécute les opérations suivantes
        if(data instanceof Message){
            // On crée un nouveau thread de discussion (TdD), dont la racine sera le message lu
            thread_of_discussion = new GenericTree<>();
            thread_of_discussion.setRoot(node);

            // On programme la diminution du score du message de 1 toutes les 30 secondes
            // Et on met à jour la Value of Importance du thread
            scheduleScoreDecrease(node, thread_of_discussion);

            // On ajoute ensuite ce TdD à la map de tous les TdD
            map_threads_of_discussion.put(data.getId(), thread_of_discussion);

            // Et enfin on ajoute le message à liste des messages
            messageList.add((Message) data);
        }

        // Sinon (donc si la ligne lue est un commentaire), on exécute les opérations suivantes
        else{
            // Si le commentaire répond à un autre commentaire, on cherche dans quel thread ce dernier se situe
            // dans le but de récupérer le message racine du thread pour modifier la value of importance
            if(((Comment) data).is_a_comment_to_another_comment()){
                thread_of_discussion = findThread(((Comment) data).getPidComment());
                GenericTreeNode<FileData> root = thread_of_discussion.getRoot();

                // On ajoute le nouveau noeud enfant à notre arbre
                root.addChild(node);
                thread_of_discussion.setRoot(root);
            }

            // Si le commentaire répond à un message, on récupère le noeud racine et on met à jour la value of importance
            else{
                // On récupère le thread de discussion (TdD) dont l'ID de la racine correspond au PID du message auquel le commentaire répond
                thread_of_discussion = map_threads_of_discussion.get(((Comment) data).getPidMessage());
                GenericTreeNode<FileData> root = thread_of_discussion.getRoot();

                // On ajoute le nouveau noeud enfant à notre arbre
                root.addChild(node);
                thread_of_discussion.setRoot(root);
            }

            // Enfin, on programme la diminution du score du commentaire de 1 toutes les 30 secondes
            // Et on met à jour la Value of Importance du thread
            scheduleScoreDecrease(node, thread_of_discussion);
            // On calcule le nouveau score du message racine
            computeValueOfImportance(thread_of_discussion.getRoot());
        }
    }

    /**
     *  Permet de trouver le thread contenant l'identifiant du commentaire passé en paramètre
     *
     * @param idComment Identifiant du commentaire à chercher
     * @return Arbre du thread de discussion contenant le commentaire (repéré par son id)
     */
    public GenericTree<FileData> findThread(int idComment){
        for(Map.Entry<Integer, GenericTree<FileData>> thread : map_threads_of_discussion.entrySet()){
            GenericTree<FileData> curr = thread.getValue();
            if(curr.find(idComment) != null)
                return curr;
        }
        return null;
    }

    /**
     *  Méthode permettant de lancer le thread (invoquée grâce à start()).
     *  Elle lit une URL et récupère un fichier, dont on lira chaque ligne.
     */
    @Override
    public void run() {
        // On crée une map contenant la liste de tous les threads de discussion, identifiés par l'ID du message racine
        map_threads_of_discussion = new HashMap<>();
        // On crée une liste contenant les messages
        messageList = new ArrayList<>();

        try {
            // On crée une nouvelle URL récupérant le fichier reseauSocial.txt en local
            URL file_url = new URL("local:reseauSocial.txt");

            // On lit chaque ligne de ce fichier, et on exécute la methode doAllTheWork pour chacune d'entre elles
            BufferedReader br = new BufferedReader(new InputStreamReader(file_url.openStream()));
            String line;
            while((line = br.readLine()) != null)
                doAllTheWork(line);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Retourne les trois meilleurs messages au format XML
     *
     * @return Le document XML sous forme d'une chaîne de caractères
     * @throws JAXBException Si on a pas pu créer le JAXBContext
     */
    public String getThreeBestMessagesToXML() throws JAXBException {
        // On instancie un BestMessagesResponse : il contient de manière formatée les 3 "meilleurs" messages
        BestMessagesResponse bmr = new BestMessagesResponse(getThreeBestMessages());

        // On crée le JAXB Context
        JAXBContext jaxbContext = JAXBContext.newInstance(BestMessagesResponse.class);
        // On crée le Marshaller
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        // On instancie un StringWriter dans lequel on va récupérer le XML
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(bmr, sw);

        // On retourne le contenu XML sous forme d'une String
        return sw.toString();
    }

    /**
     *  Retourne les trois meilleurs messages sous forme d'une chaîne de caractères
     *
     * @return Les trois meilleurs messages au format idMessage|idUser|idMessage|idUser|idMessage|idUser|
     */
    public String getThreeBestMessagesToString() {
        if(getThreeBestMessages().isEmpty())
            return "Aucun message n'a encore été lu. Les messages sont lus toutes les 1 à 3 secondes.";

        StringBuilder result = new StringBuilder();
        for(Message m : getThreeBestMessages()){
            result.append(m.getId()).append("|").append(m.getIdUser()).append("|");
        }
        return String.valueOf(result);
    }

    /**
     *  Récupère la liste des trois meilleurs messages
     *
     * @return La liste des trois meilleurs messages
     */
    public List<Message> getThreeBestMessages(){
        // On trie la liste des messages dans l'ordre décroissant des scores pour récupérer les 3 meilleurs messages
        messageList.sort(Comparator.comparingInt(Message::getValueOfImportance).reversed());

        List<Message> three_best_messages = new ArrayList<>(3);

        if(messageList.size() <= 3) three_best_messages.addAll(messageList);
        else for (int i = 0; i < 3; i++) three_best_messages.add(messageList.get(i));

        return three_best_messages;
    }

    /**
     *  Permet d'attendre un temps aléatoire compris entre deux bornes.
     *
     * @param origin Borne inférieure (min)
     * @param bound Borne supérieure (max)
     */
    public void wait_random_time(int origin, int bound){
        int random_time = ThreadLocalRandom.current().nextInt(origin,bound);
        try {
            Thread.sleep(random_time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Cette méthode crée soit un message, soit un commentaire en fonction du pattern reconnu avec le tableau de String
     *  passé en paramètre.
     *
     * @param line Tableau de String contenant la ligne lue parsée
     * @return Un message, ou un commentaire (répondant à un message ou répondant à un commentaire)
     */
    public FileData createData(String[] line){
        // Throws error if line.length != 6

        if(line[4].equals("") && line[5].equals("")){
            int idMessage = Integer.parseInt(line[0]),
                    idUser = Integer.parseInt(line[1]);
            String message = line[2],
                    user = line[3];

            return new Message(idMessage, idUser, message, user);
        }

        else if(line[5].equals("")){
            int idComment = Integer.parseInt(line[0]),
                    idUser = Integer.parseInt(line[1]),
                    pidComment = Integer.parseInt(line[4]);
            String comment = line[2],
                    user = line[3];

            return new Comment(idComment, idUser, comment, user, pidComment, true);

        }

        else {
            int idComment = Integer.parseInt(line[0]),
                    idUser = Integer.parseInt(line[1]),
                    pidMessage = Integer.parseInt(line[5]);
            String comment = line[2],
                    user = line[3];

            return new Comment(idComment, idUser, comment, user, pidMessage, false);
        }

    }
}
