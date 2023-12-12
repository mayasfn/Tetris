package VueControleur;

import Modele.Couleur;
import Modele.GrilleJeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;

public class VC extends JFrame implements Observer {
    boolean jeuCommencé = false;

    JLabel temps = new JLabel("Elapsed time : 0 ms");

    JLabel titre = new JLabel("<html><div style='text-align: center; font-size: 30;'>" +
            "<font color='#FF0000'>T</font>" + // Red
            "<font color='#FFC0CB'>E</font>" + // Pink
            "<font color='#FFA500'>T</font>" + // Orange
            "<font color='#0000FF'>R</font>" + // Blue
            "<font color='#FF00FF'>I</font>" + // Magenta
            "<font color='#00FFFF'>S</font>" + // Cyan
            "</div></html>");

    JLabel score = new JLabel("<html><div style='text-align: center; font-size: 15; '>  <font color=red> SCORE : 0 </font> </div></html>");

    JLabel prochainePiece = new JLabel("<html><div style='text-align: center; font-size: 15; '> <font color=red> Prochaine piece:  </font></div></html>");

    GrilleJeu grille;

    Observer vueGrille;
    Observer prochPieceGrille;
    private Executor ex =  Executors.newSingleThreadExecutor();
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    private JButton BoutonDemarrer, BoutonPause, BoutonQuitter;
    JPanel mainPanel = new JPanel(new BorderLayout());
    private boolean fenetrePerdu = false;



    public VC(GrilleJeu _modele) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        grille = _modele;

        setSize(700, 600);
        setResizable(false);
        mainPanel.setBackground(Color.darkGray);

        // titre
        JPanel titre_centre = new JPanel(new FlowLayout(FlowLayout.CENTER));  // Set FlowLayout.CENTER here
        titre_centre.setBackground(Color.darkGray);
        titre_centre.add(titre);

        //temps + titre
        JPanel North = new JPanel(new BorderLayout());
        North.add(temps, BorderLayout.NORTH);
        North.add(titre_centre, BorderLayout.CENTER);
        mainPanel.add(North, BorderLayout.NORTH);


        //panel pour la prochaine pièce et le score
        JPanel centre = new JPanel(new FlowLayout(FlowLayout.CENTER));  // Set FlowLayout.CENTER here
        centre.setBackground(Color.darkGray);

        //ajout grille
        vueGrille = new VueGrilleV2(grille);
        mainPanel.add((JPanel) vueGrille, BorderLayout.CENTER);

        prochPieceGrille = new VueProchainePiece(grille);

        //ajout score + prochaine piece
        JPanel AdroiteGrille = new JPanel(new BorderLayout());
        JPanel prochainePieceVue = new JPanel(new BorderLayout());
        AdroiteGrille.setBackground(Color.darkGray);
        score.setBackground(Color.darkGray);
        prochainePieceVue.setBackground(Color.darkGray);
        prochainePiece.setBackground(Color.darkGray);
        prochainePieceVue.add(prochainePiece, BorderLayout.NORTH);
        prochainePieceVue.add((JPanel)prochPieceGrille, BorderLayout.CENTER);

        AdroiteGrille.add(score, BorderLayout.NORTH);
        AdroiteGrille.add(prochainePieceVue, BorderLayout.CENTER);

        //ajoute score + prochaine piece à cote de la grille
        centre.add(AdroiteGrille);

        //ajout grille + score au centre de mon panel principal
       mainPanel.add(centre, BorderLayout.EAST);

        //Ajout boutons

        BoutonDemarrer= new JButton("Demarrer");
        BoutonPause= new JButton("Pause");
        BoutonQuitter= new JButton ("Quitter");

        BoutonDemarrer.setFocusTraversalKeysEnabled(false);
        BoutonPause.setFocusTraversalKeysEnabled(false);
        BoutonQuitter.setFocusTraversalKeysEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(BoutonDemarrer);
        buttonPanel.add(BoutonPause);
        buttonPanel.add(BoutonQuitter);


        mainPanel.add(buttonPanel,BorderLayout.SOUTH);

        registerKeyBindings();

        setContentPane(mainPanel);
        setFocusable(true);
        requestFocusInWindow();

        registerKeyBindings();



        BoutonDemarrer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!jeuCommencé) {
                    grille.remplir_Grille();
                    lastTime = 0; // Reset time when the game starts
                    grille.attribuer_piecetype();
                    vueGrille.update(null, null);
                    prochPieceGrille.update(null, null);
                    jeuCommencé = true;
                    BoutonDemarrer.setEnabled(false);
                }

            }
        });

        BoutonPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(jeuCommencé){
                    grille.setPaused(!grille.isPaused());
                    if(grille.isPaused()){
                        BoutonPause.setText("Continuer");
                    }
                    else{
                        BoutonPause.setText("Pause");
                    }
                }
            }
        });

        BoutonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               reset();
            }
        });

    }

    public void Perdu() {
        if (grille.getPerdu() && !fenetrePerdu) {
            fenetrePerdu = true;
            grille.setPerdu(false);
            JOptionPane.showMessageDialog(this, "¨Perdu!", "Fin de la partie", JOptionPane.INFORMATION_MESSAGE);
            reset();
            fenetrePerdu = false;


        }

    }

    private void registerKeyBindings() {
        InputMap inputMap = mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = mainPanel.getActionMap();

        // Define and register actions for key events
        Action rotateAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grille.getPieceCourante().rotation();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "rotate");
        actionMap.put("rotate", rotateAction);

        Action moveLeftAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grille.getPieceCourante().bougeAgauche();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        actionMap.put("moveLeft", moveLeftAction);

        Action moveRightAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grille.getPieceCourante().bougeAdroite();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        actionMap.put("moveRight", moveRightAction);

        Action moveDownAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grille.getPieceCourante().move_down();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        actionMap.put("moveDown", moveDownAction);
    }

    private void reset(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                jeuCommencé=false;
               //fenetrePerdu = false;
                grille.setPaused(false);
                BoutonPause.setText("Pause");
                grille.remplir_Grille();
               // grille.setPieceCourante(grille.getProchainePiecePiece());
                //grille.resetgrid();
               // grille.attribute_piecetype();
                grille.setPerdu(false);
                vueGrille.update(null, null);
                prochPieceGrille.update(null, null);
                BoutonDemarrer.setEnabled(true);
            }
        });
    }



    static long lastTime = System.currentTimeMillis();

    @Override
    public void update(Observable o, Object arg) { // rafraichissement de la vue

        SwingUtilities.invokeLater(new Runnable() {
            //@Override
            public void run() {
                if (jeuCommencé) {


                    vueGrille.update(o, arg);
                    prochPieceGrille.update(null, null);
                    Perdu();
                    vueGrille.update(o, arg);


                    score.setText("<html><div style='text-align: center; font-size: 15;'> <font color=red> SCORE : " +

                           + grille.get_score() +" </font> </div></html>");

                    if (!grille.isPaused()) {
                        if (lastTime == 0) {
                            lastTime = System.currentTimeMillis();  // Set initial time when the game starts
                        }
                    }

                    long elapsedTime = System.currentTimeMillis() - lastTime;
                    temps.setText("Elapsed time : " + elapsedTime + "ms  ");



                }
            }
        });

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

                                       public void run() {
                                           GrilleJeu m = new GrilleJeu();
                                           VC vc = new VC(m);
                                           vc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                           m.addObserver(vc);
                                           vc.setVisible(true);

                                       }
                                   }
        );
    }


}