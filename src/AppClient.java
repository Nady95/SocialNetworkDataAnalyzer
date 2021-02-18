import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Interface graphique du client
 *
 *  Celle-ci a deux modes de démarrage : textuel ou XML. Le mode textuel permet d'afficher les 3 messages ayant la plus
 *  grande valeur d'importance selon le pattern idMessage|idUser|idMessage|idUser|idMessage|idUser. Le mode XML, quant
 *  à lui, affiche ces 3 messages de manière complète (càd. avec le contenu du message, le score, etc.) sous forme d'un
 *  document XML.
 *
 * @author Nady Saddik
 * @author Alexandre Karakas
 * @version 2.0
 * @since Février 2020
 */
public class AppClient extends JFrame {

    /**
     *  Crée l'interface graphique du client et récupère le texte à afficher
     *
     * @param startMode Mode de démarrage de l'interface client (Textuel ou XML)
     */
    public AppClient(App.StartMode startMode){
        Date date = new Date();
        JPanel intro = new JPanel();
        JPanel client = new JPanel();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy à HH:mm:ss");
        JLabel title = new JLabel(
                "<html>" +
                "<body style='text-align:center;'>" +
                "   <h1>Bienvenue sur notre réseau social.</h1>" +
                "   <p style='font-size:1.2em;'>Nous sommes actuellement le <span style='font-size:1.5em;'>" + dateFormat.format(date) + "</span> " +
                    "<br /> et voici les trois messages avec la plus grande valeur d'importance.</p>" +
                "</body>" +
                "</html>");

        JLabel displayType = new JLabel("Les résultats sont affichés au format " + startMode.getDesc() + " comme vous l'avez demandé.");

        setResizable(false);

        if(startMode.equals(App.StartMode.Text)){
            setTitle("Client (texte) - Projet JAVA 2020 v2 - Social Network");
            setPreferredSize(new Dimension(640, 480));
            setLayout(new GridLayout(2, 1));
            JTextArea taTxt = new JTextArea(Client.start(startMode),2,40);
            taTxt.setEditable(false);

            intro.add(title);
            intro.add(displayType);
            client.add(taTxt);
        }

        else if(startMode.equals(App.StartMode.XML)){
            setTitle("Client (XML) - Projet JAVA 2020 v2 - Social Network");
            setPreferredSize(new Dimension(640, 480));
            setLayout(new GridLayout(2, 1));
            JTextArea taXML = new JTextArea(Client.start(startMode), 10,40);
            taXML.setEditable(false);
            JScrollPane spXML = new JScrollPane(taXML);


            intro.add(title);
            intro.add(displayType);
            client.add(spXML);
        }

        add(intro);
        add(client);
        pack();
        setVisible(true);
    }
}
