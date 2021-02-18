import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 *  Un message est un objet représentant le pattern suivant : idMessage|idUser|message|user||
 *
 *  Il s'agit donc d'un message qui initie un thread de discussion. Il possède ainsi une valeur d'importance, qui
 *  augmentera ou diminuera au fil du temps. Il possède aussi un score qui vaut au départ 20 et décroit avec le temps.
 *
 * @author Nady Saddik
 * @author Alexandre Karakas
 * @version 2.0
 * @since Février 2020
 */
@XmlType(propOrder = {"id", "idUser", "score", "valueOfImportance", "message", "user"})
public class Message implements FileData {
    private Date date;

    private int idMessage,
                idUser,
                score,
                valueOfImportance;

    private String  message,
                    user;


    /**
     *  Crée un objet "Message" vide: il s'agit d'un message initiant un thread de discussion. Ce constructeur est vide
     *  car il n'est pas utilisé, mais nécessaire pour fournir la réponse XML.
     */
    public Message(){
    }

    /**
     *  Crée un objet "Message" : il s'agit d'un message initiant un thread de discussion.
     *
     * @param idMessage Id du message
     * @param idUser Id de l'utilisateur qui a écrit le message
     * @param message Contenu du message
     * @param user Nom de l'utilisateur qui a écrit le message
     */
    public Message(int idMessage, int idUser, String message, String user){
        this.idMessage = idMessage;
        this.idUser = idUser;
        this.message = message;
        this.user = user;
        score = 20;
        valueOfImportance = score;
    }

    /**
     *  Méthode permettant de décroître le score de 1pt s'il n'a pas encore atteint 0pt.
     */
    public void decreaseScoreByOne() {
        score--;
    }

    /**
     *  Méthode permettant de savoir si le thread de discussion est actif, càd. si la valeur d'importance est
     *  strictement supérieure à 0
     *
     * @return Renvoie vrai si c'est le cas, faux sinon
     */
    public boolean isActive(){
        return valueOfImportance > 0;
    }

    /**
     * Renvoie l'identifiant du message
     * @return Identifiant du message
     */
    public int getId(){
        return idMessage;
    }

    /**
     *  Définit l'identifiant du message
     *
     * @param idMessage Identifiant à attribuer
     */
    public void setId(int idMessage){
        this.idMessage = idMessage;
    }

    /**
     *  Renvoie l'identifiant de l'utilisateur
     * @return Identifiant de l'utilisateur
     */
    public int getIdUser() {
        return idUser;
    }

    /**
     *  Définit l'identifiant de l'utilisateur
     *
     * @param idUser Identifiant à attribuer
     */
    public void setIdUser(int idUser){
        this.idUser = idUser;
    }

    /**
     *  Renvoie le score du message
     *
     * @return Score du message
     */
    public int getScore() {
        return score;
    }

    /**
     *  Définit le score du message
     *
     * @param score Score à attribuer
     */
    public void setScore(int score){
        this.score = score;
    }

    /**
     *  Renvoie la valeur d'importance du thread de discussion
     *
     * @return Valeur d'importance du thread
     */
    public int getValueOfImportance() {
        return valueOfImportance;
    }

    /**
     *  Définit la valeur d'importance du thread de discussion
     *
     * @param valueOfImportance Valeur d'importance à attribuer
     */
    public void setValueOfImportance(int valueOfImportance){
        this.valueOfImportance = valueOfImportance;
    }

    /**
     *  Renvoie le contenu du message
     *
     * @return Contenu du message
     */
    public String getMessage() {
        return message;
    }

    /**
     *  Définit le contenu du message
     *
     * @param message Contenu du message à attribuer
     */
    public void setMessage(String message){
        this.message = message;
    }

    /**
     *  Renvoie le nom de l'utilisateur qui a écrit le message
     *
     * @return Nom de l'utilisateur
     */
    public String getUser() {
        return user;
    }

    /**
     *  Définit le nom de l'utilisateur qui a écrit le message
     *
     * @param user Nom d'utilisateur à attribuer
     */
    public void setUser(String user){
        this.user = user;
    }
}