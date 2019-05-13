import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.LINE_START;

public class BinDialog extends JDialog implements ActionListener {
    private JPanel topPanel;
    private JPanel bottomPanel;
    private int binId;

    public BinDialog(JFrame owner, int binId) {
        super(owner, true);
        this.binId = binId;
        setLocationRelativeTo(null);
        setSize(400, 250);
        setLayout(new GridLayout(2, 1));

        setTitle("Bins vervangen");

        JLabel melding = new JLabel("Vervang Bin " + binId);
        melding.setFont(new Font("Ariel", Font.BOLD, 50));
        add(melding);

        JButton jbOk = new JButton("OK");
        jbOk.addActionListener(this);
        add(jbOk);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
    }
}
