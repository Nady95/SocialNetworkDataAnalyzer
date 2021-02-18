import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 *  La classe BestMessagesResponse permet d'être utilisée pour générer un document XML grâce à des annotations.
 *
 * @author Nady Saddik
 * @author Alexandre Karakas
 * @version 2.0
 * @since Février 2020
 */
@XmlRootElement
public class BestMessagesResponse {
    @XmlElementWrapper(name = "messages")
    @XmlElement(name = "message")
    protected List<Message> messages = new ArrayList<>(3);

    /**
     *  Instancie un objet BestMessagesResponse grâce à une liste de messages passée en paramètre
     * @param messages1 Liste de messages à récupérer
     */
    public BestMessagesResponse(List<Message> messages1){
        messages.addAll(messages1);
    }

    /**
     *  Instancie un objet BestMessagesReponse vide
     */
    public BestMessagesResponse(){
    }

    /**
     *  Récupère la liste des messages
     * @return La liste des messages
     */
    public List<Message> getMessages() {
        return messages;
    }
}

