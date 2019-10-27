import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminHtml extends javax.swing.JFrame {
    private JTextField textField1;
    private JButton button1;
    private JPanel panel1;
    private JComboBox comboBox1;

    public AdminHtml() {
        setContentPane(panel1);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null,"hello world");
            }
        });
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminHtml();
            }
        });
    }

}
