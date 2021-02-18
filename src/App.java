import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  Classe principale de l'application "Projet JAVA 2020 - Social Network"
 *
 *  Cette application a pour but d'analyser des données depuis un réseau social. Ces données sont de deux types :
 *  soit des messages, soit des commentaires. Un message initie un thread de discussion, et un commentaire commente
 *  soit un message, soit un autre commentaire. Pour plus d'informations, voir l'énoncé du projet sur l'ENT.
 *
 *  Cette classe crée et initialise une interface graphique. Depuis cette dernière, il est possible de démarrer le
 *  serveur, et de démarrer des clients (texte ou XML).
 *
 * @author Nady Saddik
 * @author Alexandre Karakas
 * @version 2.0
 * @since Février 2020
 */
public class App extends JFrame implements ActionListener {
    private final JButton startServer = new JButton("Démarrer le serveur");
    private final JButton startClientTxt = new JButton("Démarrer un client (texte)");
    private final JButton startClientXML = new JButton("Démarrer un client (XML)");

    private final JTextArea taServer = new JTextArea(3,27);

    /**
     *  Définis les deux différents modes de démarrage pour le client : il peut soit demander une réponse textuelle,
     *  soit une réponse XML
     */
    public enum StartMode {
        Text("texte"),
        XML("XML");

        private final String desc;

        StartMode(String desc){
            this.desc = desc;
        }

        public String getDesc(){
            return desc;
        }
    }

    /**
     *  Crée l'interface graphique du menu principal de notre application avec les différents boutons et gère
     *  l'interaction avec l'utilisateur
     */
    public App(){
        setTitle("Projet JAVA 2020 v2 - Social Network");
        setLayout(new GridLayout(0,2));
        setPreferredSize(new Dimension(640, 240));
        setResizable(false);
        setLocationRelativeTo(null);

        startServer.addActionListener(this);
        startClientTxt.addActionListener(this);
        startClientXML.addActionListener(this);

        startClientTxt.setEnabled(false);
        startClientXML.setEnabled(false);

        taServer.setEditable(false);

        JPanel server = new JPanel();
        server.setBorder(BorderFactory.createTitledBorder("Serveur"));
        server.add(startServer);
        server.add(taServer);

        JPanel client = new JPanel();
        client.setBorder(BorderFactory.createTitledBorder("Client"));
        client.add(startClientTxt);
        client.add(startClientXML);

        add(server);
        add(client);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /**
     *  Méthode principale de notre programme : elle se charge d'indiquer au système où se trouvent nos protocoles
     *  personnalisés, et instancie l'interface du menu.
     *
     * @param args Texte passé en argument dans la console (mais on ne s'en sert pas ici)
     */
    public static void main(String[] args){
        System.getProperties().put("java.protocol.handler.pkgs", "protocols");
        new App();
    }

    /**
     *  Cette méthode permet de gérer l'intéraction avec la souris sur les différents boutons, et exécute des actions
     *  différentes en fonction du bouton activé
     * @param arg0 Une action effectuée
     */
    public void actionPerformed(ActionEvent arg0){
        if(arg0.getSource() == startServer){
            if(Server.start()){
                taServer.append("Le serveur a démarré.");
                startServer.setEnabled(false);
                startClientTxt.setEnabled(true);
                startClientXML.setEnabled(true);
            } else{
                taServer.append("Le serveur n'a pas pu démarrer (consulter la console pour plus d'informations).");
            }
        }

        else if(arg0.getSource() == startClientTxt){
            new AppClient(StartMode.Text);
        }

        else if(arg0.getSource() == startClientXML){
            new AppClient(StartMode.XML);
        }
    }
}
