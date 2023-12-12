package VueControleur;

import Modele.Couleur;
import Modele.SimpleGrid;

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
    boolean gameStarted = false;

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

    SimpleGrid grid;

    Observer gridView;
    Observer prochPieceGrille;
    private Executor ex =  Executors.newSingleThreadExecutor();
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    private JButton startButton, pauseButton, quitButton;
    JPanel mainPanel = new JPanel(new BorderLayout());
    private boolean fenetrePerdu = false;



    public VC(SimpleGrid _modele) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        grid = _modele;

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
        gridView = new VueGrilleV2(grid);
        mainPanel.add((JPanel) gridView, BorderLayout.CENTER);

        prochPieceGrille = new VueProchainePiece(grid);

        //ajout score + next piece
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

        //Add buttons

        startButton= new JButton("Start");
        pauseButton= new JButton("Pause");
        quitButton= new JButton ("Quit");

        startButton.setFocusTraversalKeysEnabled(false);
        pauseButton.setFocusTraversalKeysEnabled(false);
        quitButton.setFocusTraversalKeysEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(quitButton);


        mainPanel.add(buttonPanel,BorderLayout.SOUTH);

        registerKeyBindings();

        setContentPane(mainPanel);
        setFocusable(true);
        requestFocusInWindow();

        registerKeyBindings();



        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameStarted) {
                   //grid.remplir_Grille();
                    lastTime = 0; // Reset time when the game starts
                    grid.attribute_piecetype();
                    gridView.update(null, null);
                    prochPieceGrille.update(null, null);
                    gameStarted = true;
                    startButton.setEnabled(false);
                } else {
                    //grid.remplir_Grille();
                   // grid.attribute_piecetype();
                    gridView.update(null, null);
                    prochPieceGrille.update(null, null);

                }

            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gameStarted){
                    grid.setPaused(!grid.isPaused());
                    if(grid.isPaused()){
                        pauseButton.setText("Resume");
                    }
                    else{
                        pauseButton.setText("Pause");
                    }
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               reset();
            }
        });

    }

    public void Perdu() {
        if (grid.getPerdu() && !fenetrePerdu) {
            fenetrePerdu = true;
            grid.setPerdu(false);
            JOptionPane.showMessageDialog(this, "¨Perdu!", "Perdu!", JOptionPane.INFORMATION_MESSAGE);
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
                grid.getcurrentPiece().rotation();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "rotate");
        actionMap.put("rotate", rotateAction);

        Action moveLeftAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grid.getcurrentPiece().move_left();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        actionMap.put("moveLeft", moveLeftAction);

        Action moveRightAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grid.getcurrentPiece().move_right();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        actionMap.put("moveRight", moveRightAction);

        Action moveDownAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grid.getcurrentPiece().move_down();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        actionMap.put("moveDown", moveDownAction);
    }

    private void reset(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                gameStarted=false;
               //fenetrePerdu = false;

                grid.setPaused(false);
                pauseButton.setText("Pause");
                grid.remplir_Grille();
                grid.setCurrentPiece(grid.getProchainePiecePiece());
                grid.resetgrid();
               // grid.attribute_piecetype();
                grid.setPerdu(false);
                gridView.update(null, null);
                prochPieceGrille.update(null, null);
                startButton.setEnabled(true);
            }
        });
    }



    static long lastTime = System.currentTimeMillis();

    @Override
    public void update(Observable o, Object arg) { // rafraichissement de la vue

        SwingUtilities.invokeLater(new Runnable() {
            //@Override
            public void run() {
                if (gameStarted) {


                    gridView.update(o, arg);
                    prochPieceGrille.update(null, null);
                    Perdu();
                    gridView.update(o, arg);


                    score.setText("<html><div style='text-align: center; font-size: 15;'> <font color=red> SCORE : " +

                           + grid.get_score() +" </font> </div></html>");

                    if (!grid.isPaused()) {
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
                                           SimpleGrid m = new SimpleGrid();
                                           VC vc = new VC(m);
                                           vc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                           m.addObserver(vc);
                                           vc.setVisible(true);

                                       }
                                   }
        );
    }


}