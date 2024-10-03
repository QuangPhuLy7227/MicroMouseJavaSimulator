import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MazeGUI implements ActionListener, KeyListener, PopupMenuListener {
    private static final Color PERISCOPE_PANEL_COLOR = new Color( 43, 50, 56);
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel northButtonPanel;
    private JPanel southButtonPanel;
    private JPanel periscopePanel;

    /*Buttons*/
    private JButton animateButton;
    private JButton clearButton;
    private JButton mazeButton;
    private JButton nextButton;
    private JButton periscopeButton;
    private JButton sendButton;
    private JTextField periscopePrompt;
    private JComboBox<String> portComboBox;

    private void begin() {
        JFrame main_frame = new JFrame("Java Sim MMS");
        main_frame.setSize(800, 800);
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_frame.setResizable(true);
        main_frame.setBackground(Color.BLACK);
        main_frame.setLayout(new BorderLayout());

        /* main sections of layout, north, south, center */
        northPanel  = new JPanel();
        southPanel  = new JPanel();
        /* sub-panels for cosmetic needs */
        northButtonPanel = new JPanel();
        periscopePanel   = new JPanel();
        southButtonPanel = new JPanel();

        /* set layout for button panels */
        northButtonPanel.setLayout( new BoxLayout(northButtonPanel, BoxLayout.LINE_AXIS) );
        southButtonPanel.setLayout( new BoxLayout(southButtonPanel, BoxLayout.LINE_AXIS) );

        /* sets names of new buttons */
        animateButton   = new JButton( "Animate" );
        clearButton     = new JButton( "Clear" );
        mazeButton      = new JButton( "New Maze" );
        nextButton      = new JButton( "Next" );
        periscopeButton = new JButton( "Periscope" );
        sendButton      = new JButton( "Send" );
        periscopePrompt = new JTextField( 25 );

        /* Activates button/comboBox to register state change */
        clearButton.addActionListener( this );
        animateButton.addActionListener( this );
        mazeButton.addActionListener( this );
        nextButton.addActionListener( this );
        periscopeButton.addActionListener( this );
//        portComboBox.addActionListener( this );
        sendButton.addActionListener(this);
        periscopePrompt.addKeyListener( this );

        /* add button to panels */
        northButtonPanel.add( animateButton );
        northButtonPanel.add( Box.createHorizontalGlue() );
//        northButtonPanel.add( portComboBox );
        northButtonPanel.add( Box.createHorizontalGlue() );
        northButtonPanel.add( mazeButton );
        /* south button panel buttons */
        southButtonPanel.add( nextButton );
        southButtonPanel.add( Box.createHorizontalGlue() );
        southButtonPanel.add( periscopeButton );
        southButtonPanel.add( Box.createHorizontalGlue() );
        southButtonPanel.add( clearButton );
        /* periscope panel buttons */
        periscopePanel.add( periscopePrompt );
        periscopePanel.add( Box.createHorizontalGlue() );
        periscopePanel.add( sendButton );
        periscopePanel.setVisible( false );

        /* background color of button panels */
        northButtonPanel.setBackground( Color.BLACK );
        southButtonPanel.setBackground( Color.BLACK );
        periscopePanel.setBackground( PERISCOPE_PANEL_COLOR );

        /* set up north panel */
        northPanel.setLayout( new GridLayout(0, 1));
        northPanel.add(northButtonPanel);
        /* set up south panel  */
        southPanel.setLayout( new BoxLayout(southPanel, BoxLayout.Y_AXIS) );
        southPanel.add( periscopePanel );
        southPanel.add( southButtonPanel );

        /* add panels with their buttons on the final window */
        Container contentPane = main_frame.getContentPane();
        contentPane.add( northPanel, BorderLayout.NORTH );
        contentPane.add( southPanel, BorderLayout.SOUTH );
//        contentPane.add( renderPanel, BorderLayout.CENTER );
        contentPane.validate();

        main_frame.setVisible(true);
    }

    public static void main(String[] args) {
        MazeGUI maze = new MazeGUI();
        maze.begin();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {

    }
}
