import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.LINE_START;

public class BinDialog extends JDialog implements ActionListener {
    private JPanel panel, buttonPanel;
    private int binId;
    private Main main;

    public BinDialog(JFrame owner, int binId, Main main) {
        super(owner, true);
        this.binId = binId;
        this.main = main;
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));
        setUndecorated(true);

        panel = (JPanel)this.getContentPane();
        panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,1));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel melding = new JLabel("Vervang Bin");
        melding.setFont(new Font("Ariel", Font.BOLD, 40));
        melding.setHorizontalAlignment(JLabel.CENTER);
        add(melding);

        JLabel bin = new JLabel("Bin " + binId);
        bin.setHorizontalAlignment(JLabel.CENTER);
        add(bin);

        JButton jbOk = new JButton("OK");
        jbOk.addActionListener(this);
        buttonPanel.add(jbOk);
        add(buttonPanel);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        main.createPakBon(binId);
        for (Bin b: main.getFinalBinList()) {
            if (b.getBinNumber() == binId + 3) {
                if (binId == 1 || binId % 3 == 1) {
                    main.setBinId1(binId + 3);
                } else if (binId == 2 || binId % 3 == 2) {
                    main.setBinId2(binId + 3);
                } else {
                    main.setBinId3(binId + 3);
                }
            }
        }
        while (binId > 3) {
            binId = binId - 3;
        }

        main.getInpak().sendToArduino("Reset led" + binId);
        System.out.println("Reset led" + binId);
        dispose();
    }
}
