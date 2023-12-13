package VueControleur;

import Modele.Couleur;
import Modele.GrilleJeu;
import VueControleur.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.util.Observable;
import java.util.Observer;
import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;

public class VC extends JFrame implements Observer {
    boolean jeuCommencé = false;
    //String projectRoot = System.getProperty("user.dir");
    String sond_defondPath = "/Users/janeaziz/Documents/L3/LIFAPOO/tetris2/tetris/src/VueControleur/Sons/fullgame.wav" ;

     private Sound sonjeu ;

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
        //String projectRoot = System.getProperty("user.dir");
        //String sond_defondPath = projectRoot + "/src/VueControleur/Sons/fullgame.wav";
        //son de fond
        //son_jeu= new Sound(sond_defondPath);

        //String projectRoot = System.getProperty("user.dir");
        //File sonDefondFile = new File(projectRoot, "/src/VueControleur/Sons/fullgame.wav");
        //String sonDefondPath = sonDefondFile.getAbsolutePath();
// Son de fond
         sonjeu = new Sound(sond_defondPath);
         sonjeu.son_de_fond(sond_defondPath);


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
        BoutonQuitter= new JButton ("Arreter");

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
                    lastTime = 0; // Recommence temps à 0
                    grille.attribuer_piecetype();
                    vueGrille.update(null, null);
                    prochPieceGrille.update(null, null);
                    jeuCommencé = true;
                    BoutonDemarrer.setEnabled(false);
                    sonjeu.jouer_boucle();

                }

            }
        });

        BoutonPause.addActionListener(new ActionListener() {
            long pausedTime;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jeuCommencé) {
                    grille.setPaused(!grille.isPaused());

                    if (grille.isPaused()) {
                        // sauvegarder le temps en pause
                        pausedTime = System.currentTimeMillis();
                        BoutonPause.setText("Continuer");
                    } else {
                        // recommencer le temps au bon moment par rapport au temps passé en pause
                        lastTime += System.currentTimeMillis() - pausedTime;
                        pausedTime = 0;
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

        // actions pour evenements claviers
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
               fenetrePerdu = false;
                grille.setPaused(false);
                BoutonPause.setText("Pause");
                grille.remplir_Grille();
               // grille.setPieceCourante(grille.getProchainePiecePiece());
                grille.resetgrid();
               //grille.attribute_piecetype();
                grille.setPerdu(false);
                vueGrille.update(null, null);
                prochPieceGrille.update(null, null);
                BoutonDemarrer.setEnabled(true);
                sonjeu.arreter_son2();
            }
        });
    }



static long lastTime = System.currentTimeMillis();
    long elapsedseconds;

    @Override
    public void update(Observable o, Object arg) {
        SwingUtilities.invokeLater(new Runnable() {
            //@Override
            public void run() {
                if (jeuCommencé) {
                    vueGrille.update(o, arg);
                    prochPieceGrille.update(null, null);
                    Perdu();

                    if (!grille.isPaused()) {
                        if (lastTime == 0) {
                            lastTime = System.currentTimeMillis();  // temps de debut du jeu
                        } else {
                            long currentTime = System.currentTimeMillis();
                            long elapsedTime = currentTime - lastTime;
                            long elapsedSeconds = elapsedTime / 1000;  // Convertir millisecondes en secondes
                            temps.setText("Elapsed time : " + elapsedSeconds + "s  ");
                        }
                    }

                    vueGrille.update(o, arg);
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