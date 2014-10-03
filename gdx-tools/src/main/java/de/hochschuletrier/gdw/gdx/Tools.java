package de.hochschuletrier.gdw.gdx;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * GDX Tools Launcher
 *
 * @author Santo Pfingsten
 */
public class Tools extends JFrame {

    public Tools() {
        super("GDX Tools");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        addButton("Font Creator", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String args[] = {};
                try {
                    setVisible(false);
                    com.badlogic.gdx.tools.hiero.Hiero.main(args);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addButton("Particle Editor", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String args[] = {};
                try {
                    setVisible(false);
                    com.badlogic.gdx.tools.particleeditor.ParticleEditor.main(args);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setMinimumSize(new Dimension(200, 0));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public final void addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setAlignmentX(CENTER_ALIGNMENT);
        add(button);
    }

    public static void main(String[] args) throws Exception {
        new Tools();
    }
}
