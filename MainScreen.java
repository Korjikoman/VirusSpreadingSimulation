
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainScreen {
    private JFrame frame;

    private static final int POPULATION_NUM = 10;
    private static final int OBJECT_WIDTH = 30;
    private static final int OBJECT_HEIGHT = 30;

    private static String TITLE = "Some title";

    public MainScreen() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setTitle(TITLE);

        Panel panel = new Panel();
        frame.add(panel);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        panel.startThread();
    }

    public static void main(String[] args) {
        MainScreen screen = new MainScreen();

    }

}
