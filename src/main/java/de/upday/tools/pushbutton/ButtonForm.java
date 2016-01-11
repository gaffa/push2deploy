package de.upday.tools.pushbutton;

import org.springframework.stereotype.Component;

import java.awt.*;

import javax.swing.*;

import de.upday.tools.pushbutton.button.ButtonEventListener;

@Component
public class ButtonForm extends JFrame implements ButtonEventListener {

    private JTextArea titleText;

    public ButtonForm() {

        super("CI Push Button");
        setVisible(true);
        setSize(1000, 800);
        titleText = new JTextArea("awaiting button events...");
        titleText.setFont(new Font("Verdana", Font.BOLD, 50));
        add(titleText);
    }

    @Override
    public void lidOpened() {
        setText("opened");
    }

    @Override
    public void lidClosed() {
        setText("closed");
    }

    @Override
    public void buttonPressed() {
        setText("pressed");
    }

    void setText(String text) {
        EventQueue.invokeLater(() -> {
            titleText.setText(text);
            forceToFront();
        });
    }

    private void forceToFront() {
        super.setVisible(true);
        int state = super.getExtendedState();
        state &= ~JFrame.ICONIFIED;
        super.setExtendedState(state);
        super.setAlwaysOnTop(true);
        super.toFront();
        super.requestFocus();
        super.setAlwaysOnTop(false);
    }
}
