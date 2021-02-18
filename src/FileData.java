/**
 *  L'interface FileData représente une ligne (parsée) du fichier lu.
 *  Une ligne (parsée) soit contenir un identifiant, un score, et on doit pouvoir diminuer son score de 1pt.
 *
 * @author Nady Saddik
 * @author Alexandre Karakas
 * @version 2.0
 * @since Février 2020
 */
public interface FileData {
    int getId();
    int getScore();
    void decreaseScoreByOne();
}
