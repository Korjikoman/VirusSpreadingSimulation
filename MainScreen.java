
import javax.swing.JFrame;

public class MainScreen {
    private JFrame frame;

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 800;
    private static String TITLE = "Some title";

    public MainScreen() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        frame.setTitle(TITLE);

        frame.setVisible(true);

    }

    public void addObject(Object object) {
        frame.add(object);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        MainScreen screen = new MainScreen();
        Object rectangle = new Object(350, 400, 150, 100);
        screen.addObject(rectangle);
    }

}
