package com.ylw.main.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.util.Vector;

public class Server extends JFrame implements ActionListener {

    JLabel jlPort = new JLabel("端 口 号");
    JTextField jtfPort = new JTextField("9999");
    JButton jbStart = new JButton("启动");
    JButton jbStop = new JButton("关闭");
    JPanel jps = new JPanel();
    JList jlUserOnline = new JList();

    JScrollPane jspx = new JScrollPane(jlUserOnline);
    JSplitPane jspz = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspx, jps);

    ServerSocket ss;
    ServerThread st;

    Vector onlineList = new Vector();//创建存放当前在线用户的Vector对象

    public Server() {

        this.initialComponent();

        this.addListener();

        this.initialFrame();
    }

    public static void main(String args[]) {
        new Server();
    }

    public void initialComponent() {

        jps.setLayout(null);
        jlPort.setBounds(20, 20, 50, 20);
        jps.add(jlPort);

        this.jtfPort.setBounds(85, 20, 60, 20);
        jps.add(this.jtfPort);

        this.jbStart.setBounds(18, 50, 60, 20);
        jps.add(this.jbStart);

        this.jbStop.setBounds(85, 50, 60, 20);
        jps.add(this.jbStop);

        this.jbStop.setEnabled(false);
    }

    public void addListener() {

        this.jbStart.addActionListener(this);
        this.jbStop.addActionListener(this);
    }

    public void initialFrame() {

        this.setTitle("象棋--服务器端");
        Image image = new ImageIcon("ico.gif").getImage();
        this.setIconImage(image);
        this.add(jspz);

        jspz.setDividerLocation(250);
        jspz.setDividerSize(4);

        this.setBounds(20, 20, 420, 320);
        this.setVisible(true);

        this.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        if (st == null)//当服务器线程为空时直接退出
                        {
                            System.exit(0);//退出
                            return;
                        }
                        try {
                            Vector v = onlineList;
                            int size = v.size();
                            for (int i = 0; i < size; i++) {
                                //当不为空时，向在线用户发送离线信息
                                ServerAgentThread tempSat = (ServerAgentThread) v.get(i);

                                tempSat.dout.writeUTF("<#SERVER_DOWN#>");
                                tempSat.flag = false;//终止服务器代理线程
                            }

                            st.flag = false;//终止服务器线程
                            st = null;
                            ss.close();
                            v.clear();
                            refreshList();

                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        System.exit(0);
                    }
                }
        );
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.jbStart) {
            this.jbStart_event();
        } else if (e.getSource() == this.jbStop) {
            this.jbStop_event();
        }
    }

    public void jbStart_event() {

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

        try {

            this.jbStart.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jbStop.setEnabled(true);

            ss = new ServerSocket(port);
            st = new ServerThread(this);
            st.start();

            JOptionPane.showMessageDialog(this, "服务器启动成功", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ee) {

            JOptionPane.showMessageDialog(this, "服务器启动失败", "错误",
                    JOptionPane.ERROR_MESSAGE);

            this.jbStart.setEnabled(true);
            this.jtfPort.setEnabled(true);
            this.jbStop.setEnabled(false);

        }
    }

    public void jbStop_event() {

        try {

            Vector v = onlineList;
            int size = v.size();
            for (int i = 0; i < size; i++) {

                ServerAgentThread tempSat = (ServerAgentThread) v.get(i);
                tempSat.dout.writeUTF("<#SERVER_DOWN#>");
                tempSat.flag = false;
            }

            st.flag = false;//关闭服务器线程
            st = null;
            ss.close();//关闭ServerSocket

            v.clear();//将在线用户列表清空
            refreshList();

            this.jbStart.setEnabled(true);
            this.jtfPort.setEnabled(true);
            this.jbStop.setEnabled(false);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void refreshList() {

        Vector v = new Vector();
        int size = this.onlineList.size();
        for (int i = 0; i < size; i++) {
            ServerAgentThread tempSat = (ServerAgentThread) this.onlineList.get(i);
            String temps = tempSat.sc.getInetAddress().toString();
            temps = temps + "|" + tempSat.getName();
            v.add(temps);
        }
        this.jlUserOnline.setListData(v);
    }
}