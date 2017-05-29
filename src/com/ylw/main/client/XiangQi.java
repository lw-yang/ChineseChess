package com.ylw.main.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

public class XiangQi extends JFrame implements ActionListener {

    public static final Color bgColor = new Color(245, 250, 160);
    public static final Color focusbg = new Color(242, 242, 242);
    public static final Color focuschar = new Color(96, 95, 91);
    public static final Color color1 = new Color(249, 50, 183);
    public static final Color color2 = Color.white;

    JLabel jlHost = new JLabel("主机名");
    JLabel jlPort = new JLabel("端口号");
    JLabel jlNickName = new JLabel("昵    称");

    JTextField jtfHost = new JTextField("127.0.0.1");
    JTextField jtfPort = new JTextField("9999");
    JTextField jtfNickName = new JTextField("Play1");

    JButton jbConnect = new JButton("连  接");
    JButton jbDisconnect = new JButton("断  开");
    JButton jbFail = new JButton("认  输");
    JButton jbChallenge = new JButton("挑  战");

    JComboBox jcbNickList = new JComboBox();
    JButton jbYChallenge = new JButton("接受挑战");
    JButton jbNChallenge = new JButton("拒绝挑战");

    int width = 60;

    QiZi[][] qiZi = new QiZi[9][10];
    QiPan jpz = new QiPan(qiZi, width, this);

    JPanel jpy = new JPanel();
    JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpz, jpy);

    boolean caiPan = false;//可否走棋的标志位
    int color = 0;//0 代表红棋，1代表白棋

    Socket sc;

    ClientAgentThread cat;

    public XiangQi() {

        this.initialComponent();

        this.addListener();

        this.initialState();

        this.initialQiZi();

        this.initialFrame();

    }

    public void initialComponent() {

        jpy.setLayout(null);

        this.jlHost.setBounds(10, 10, 50, 20);
        jpy.add(this.jlHost);

        this.jtfHost.setBounds(70, 10, 80, 20);
        jpy.add(this.jtfHost);

        this.jlPort.setBounds(10, 40, 50, 20);
        jpy.add(this.jlPort);

        this.jtfPort.setBounds(70, 40, 80, 20);
        jpy.add(this.jtfPort);

        this.jlNickName.setBounds(10, 70, 50, 20);
        jpy.add(this.jlNickName);

        this.jtfNickName.setBounds(70, 70, 80, 20);
        jpy.add(this.jtfNickName);

        this.jbConnect.setBounds(10, 100, 80, 20);
        jpy.add(this.jbConnect);

        this.jbDisconnect.setBounds(100, 100, 80, 20);
        jpy.add(this.jbDisconnect);

        this.jcbNickList.setBounds(20, 130, 130, 20);
        jpy.add(this.jcbNickList);

        this.jbChallenge.setBounds(10, 160, 80, 20);
        jpy.add(this.jbChallenge);

        this.jbFail.setBounds(100, 160, 80, 20);
        jpy.add(this.jbFail);

        this.jbYChallenge.setBounds(5, 190, 86, 20);
        jpy.add(this.jbYChallenge);

        this.jbNChallenge.setBounds(100, 190, 86, 20);
        jpy.add(this.jbNChallenge);

        jpz.setLayout(null);
        jpz.setBounds(0, 0, 700, 700);
    }

    public void addListener() {

        this.jbConnect.addActionListener(this);
        this.jbDisconnect.addActionListener(this);
        this.jbChallenge.addActionListener(this);
        this.jbFail.addActionListener(this);
        this.jbYChallenge.addActionListener(this);
        this.jbNChallenge.addActionListener(this);
    }

    public void initialState() {

        this.jbDisconnect.setEnabled(false);
        this.jbChallenge.setEnabled(false);
        this.jbYChallenge.setEnabled(false);
        this.jbNChallenge.setEnabled(false);
        this.jbFail.setEnabled(false);
    }

    public void initialQiZi() {

        qiZi[0][0] = new QiZi(color1, "車", 0, 0);
        qiZi[1][0] = new QiZi(color1, "馬", 1, 0);
        qiZi[2][0] = new QiZi(color1, "相", 2, 0);
        qiZi[3][0] = new QiZi(color1, "仕", 3, 0);
        qiZi[4][0] = new QiZi(color1, "帥", 4, 0);
        qiZi[5][0] = new QiZi(color1, "仕", 5, 0);
        qiZi[6][0] = new QiZi(color1, "相", 6, 0);
        qiZi[7][0] = new QiZi(color1, "馬", 7, 0);
        qiZi[8][0] = new QiZi(color1, "車", 8, 0);
        qiZi[1][2] = new QiZi(color1, "砲", 1, 2);
        qiZi[7][2] = new QiZi(color1, "砲", 7, 2);
        qiZi[0][3] = new QiZi(color1, "兵", 0, 3);
        qiZi[2][3] = new QiZi(color1, "兵", 2, 3);
        qiZi[4][3] = new QiZi(color1, "兵", 4, 3);
        qiZi[6][3] = new QiZi(color1, "兵", 6, 3);
        qiZi[8][3] = new QiZi(color1, "兵", 8, 3);

        qiZi[0][9] = new QiZi(color2, "車", 0, 9);
        qiZi[1][9] = new QiZi(color2, "馬", 1, 9);
        qiZi[2][9] = new QiZi(color2, "象", 2, 9);
        qiZi[3][9] = new QiZi(color2, "士", 3, 9);
        qiZi[4][9] = new QiZi(color2, "將", 4, 9);
        qiZi[5][9] = new QiZi(color2, "士", 5, 9);
        qiZi[6][9] = new QiZi(color2, "象", 6, 9);
        qiZi[7][9] = new QiZi(color2, "馬", 7, 9);
        qiZi[8][9] = new QiZi(color2, "車", 8, 9);
        qiZi[1][7] = new QiZi(color2, "炮", 1, 7);
        qiZi[7][7] = new QiZi(color2, "炮", 7, 7);
        qiZi[0][6] = new QiZi(color2, "卒", 0, 6);
        qiZi[2][6] = new QiZi(color2, "卒", 2, 6);
        qiZi[4][6] = new QiZi(color2, "卒", 4, 6);
        qiZi[6][6] = new QiZi(color2, "卒", 6, 6);
        qiZi[8][6] = new QiZi(color2, "卒", 8, 6);

    }

    public void initialFrame() {

        this.setTitle("中国象棋--客户端");
        Image image = new ImageIcon("ico.gif").getImage();
        this.setIconImage(image);
        this.add(this.jsp);

        jsp.setDividerLocation(730);
        jsp.setDividerSize(4);

        this.setBounds(30, 30, 930, 730);
        this.setVisible(true);
        this.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        if (cat == null)//客户端代理线程为空，直接退出
                        {
                            System.exit(0);
                            return;
                        }
                        try {
                            if (cat.tiaoZhanZhe != null)
                            {
                                try {

                                    cat.dout.writeUTF("<#RENSHU#>" + cat.tiaoZhanZhe);
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }
                            cat.dout.writeUTF("<#CLIENT_LEAVE#>");
                            cat.flag = false;//终止客户端代理线程
                            cat = null;

                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        System.exit(0);
                    }

                }
        );
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.jbConnect) {
            this.jbConnect_event();
        } else if (e.getSource() == this.jbDisconnect) {
            this.jbDisconnect_event();
        } else if (e.getSource() == this.jbChallenge) {
            this.jbChallenge_event();
        } else if (e.getSource() == this.jbYChallenge) {
            this.jbYChallenge_event();
        } else if (e.getSource() == this.jbNChallenge) {
            this.jbNChallenge_event();
        } else if (e.getSource() == this.jbFail) {
            this.jbFail_event();
        }
    }

    public void jbConnect_event() {
        int port = 0;

        try {
            port = Integer.parseInt(this.jtfPort.getText().trim());
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "端口号只能是整数", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (port > 65535 || port < 0) {
            JOptionPane.showMessageDialog(this, "端口号只能是0-65535的整数", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = this.jtfNickName.getText().trim();

        if (name.length() == 0) {
            JOptionPane.showMessageDialog(this, "玩家姓名不能为空", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            sc = new Socket(this.jtfHost.getText().trim(), port);
            cat = new ClientAgentThread(this);
            cat.start();

            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

            JOptionPane.showMessageDialog(this, "已连接到服务器", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "连接服务器失败", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public void jbDisconnect_event() {
        try {
            this.cat.dout.writeUTF("<#CLIENT_LEAVE#>");
            this.cat.flag = false;
            this.cat = null;
            this.jtfHost.setEnabled(!false);
            this.jtfPort.setEnabled(!false);
            this.jtfNickName.setEnabled(!false);
            this.jbConnect.setEnabled(!false);
            this.jbDisconnect.setEnabled(!true);
            this.jbChallenge.setEnabled(!true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void jbChallenge_event() {

        Object o = this.jcbNickList.getSelectedItem();

        if (o == null || ((String) o).equals("")) {
            JOptionPane.showMessageDialog(this, "请选择对方名字", "错误",
                    JOptionPane.ERROR_MESSAGE);//当未选中挑战对象，给出错误提示信息
        } else {

            String name2 = (String) this.jcbNickList.getSelectedItem();

            try {
                this.jtfHost.setEnabled(false);
                this.jtfPort.setEnabled(false);
                this.jtfNickName.setEnabled(false);
                this.jbConnect.setEnabled(false);
                this.jbDisconnect.setEnabled(!true);
                this.jbChallenge.setEnabled(!true);
                this.jbYChallenge.setEnabled(false);
                this.jbNChallenge.setEnabled(false);
                this.jbFail.setEnabled(false);

                this.cat.tiaoZhanZhe = name2;
                this.caiPan = true;
                this.color = 0;

                this.cat.dout.writeUTF("<#TIAO_ZHAN#>" + name2);
                JOptionPane.showMessageDialog(this, "已提出挑战,请等待恢复...", "提示",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    public void jbYChallenge_event() {

        try {

            this.cat.dout.writeUTF("<#TONG_YI#>" + this.cat.tiaoZhanZhe);
            this.caiPan = false;//将caiPan设为false
            this.color = 1;//将color设为1

            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(!true);
            this.jbChallenge.setEnabled(!true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(!false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void jbNChallenge_event() {

        try {

            this.cat.dout.writeUTF("<#BUTONG_YI#>" + this.cat.tiaoZhanZhe);
            this.cat.tiaoZhanZhe = null;
            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void jbFail_event() {

        try {

            this.cat.dout.writeUTF("<#RENSHU#>" + this.cat.tiaoZhanZhe);
            this.cat.tiaoZhanZhe = null;
            this.color = 0;
            this.caiPan = false;

            this.next();

            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void next() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                this.qiZi[i][j] = null;
            }
        }

        this.caiPan = false;
        this.initialQiZi();
        this.repaint();//重绘
    }

    public static void main(String args[]) {
        new XiangQi();
    }
}