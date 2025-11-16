package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class KeyPressSimulator extends JFrame {

    private boolean isKeyPressEnabled = false;
    private Timer timer;
    private JButton button;
    private JLabel label;

    public KeyPressSimulator() {
        initUI();
    }

    private void initUI() {
        setTitle("按键连点模拟");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        button = new JButton("开始连点");
        label = new JLabel("连点已停止");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isKeyPressEnabled) {
                    // 停止连点
                    timer.stop();
                    button.setText("开始连点");
                    label.setText("连点已停止");
                } else {
                    // 开始连点
                    timer.start();
                    button.setText("停止连点");
                    label.setText("连点进行中...");
                }

                isKeyPressEnabled = !isKeyPressEnabled;
            }
        });

        timer = new Timer(333, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulateKeyPress();
            }
        });

        setLayout(new FlowLayout());
        add(button);
        add(label);
    }

    private void simulateKeyPress() {
        try {
            Robot robot = new Robot();
            // 模拟按下 J 键
            robot.keyPress(KeyEvent.VK_J);
            Thread.sleep(300); // 延迟 100 毫秒
            // 模拟释放 J 键
            robot.keyRelease(KeyEvent.VK_J);
        } catch (AWTException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                KeyPressSimulator simulator = new KeyPressSimulator();
                simulator.setVisible(true);
            }
        });
    }
}