import java.util.Date;

/**
 *  Un commentaire est un objet représentant les patterns suivants :
 *  -   idCommentaire|idUser|commentaire|user|pidCommentaire|
 *  -   idCommentaire|idUser|commentaire|user||pidMessage
 *
 *  Il s'agit donc soit d'une réponse à un message, soit à un autre commentaire. Le score de départ d'un commentaire
 *  vaut 20 et décroit avec le temps.
 *
 * @author Nady Saddik
 * @author Alexandre Karakas
 * @version 2.0
 * @since Février 2020
 */
public class Comment implements FileData {
    private Date date;

    private int score;

    private final int   idComment,
                        idUser,
                        pidComment,
                        pidMessage;

    private final String    comment,
                            user;

    private final boolean is_a_comment_to_another_comment;

    /**
     *  Crée un objet "Comment" : il s'agit d'un commentaire répondant soit à un autre commentaire,
     *  soit à un message.
     *
     * @param idComment Id du commentaire
     * @param idUser Id de l'utilisateur qui a écrit le commentaire
     * @param comment Contenu du commentaire
     * @param user Nom de l'utilisateur qui a écrit le commentaire
     * @param pidParent Id du message/commentaire auquel il répond
     */
    public Comment(int idComment, int idUser, String comment, String user, int pidParent, boolean is_a_comment_to_another_comment){
        if(is_a_comment_to_another_comment){
            pidMessage = -1;
            pidComment = pidParent;
        } else {
            pidComment = -1;
            pidMessage = pidParent;
        }
        this.idComment = idComment;
        this.idUser = idUser;
        this.comment = comment;
        this.user = user;
        this.is_a_comment_to_another_comment = is_a_comment_to_another_comment;
        score = 20;
    }

    /**
     *  Méthode permettant de décroître le score de 1pt s'il n'a pas encore atteint 0pt.
     */
    public void decreaseScoreByOne() {
        if(score > 0)
            score--;
    }

    /**
     *  Renvoie le score du commentaire
     *
     * @return Score du commentaire
     */
    public int getScore() {
        return score;
    }

    /**
     *  Renvoie l'id du commentaire
     *
     * @return Identifiant du commentaire
     */
    public int getId(){
        return idComment;
    }

    /**
     *  Renvoie l'id de l'utilisateur
     *
     * @return Id de l'utilisateur
     */
    public int getIdUser() {
        return idUser;
    }

    /**
     *  Renvoie le pid du message
     *
     * @return Pid du message
     */
    public int getPidMessage() {
        return pidMessage;
    }

    /**
     *  Renvoie le pid du commentaire
     *
     * @return Pid du commentaire
     */
    public int getPidComment() {
        return pidComment;
    }

    /**
     *  Renvoie le commentaire
     *
     * @return Commentaire
     */
    public String getComment() {
        return comment;
    }

    /**
     *  Renvoie le nom de l'utilisateur
     *
     * @return Nom de l'utilisateur
     */
    public String getUser() {
        return user;
    }

    /**
     *  Renvoie vrai si le commentaire répond à un autre commentaire
     *
     * @return vrai si oui, faux sinon
     */
    public boolean is_a_comment_to_another_comment() {
        return is_a_comment_to_another_comment;
    }
}