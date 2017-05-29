package com.ylw.main.client;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class ClientAgentThread extends Thread {

    XiangQi father;
    boolean flag = true;

    DataInputStream din;
    DataOutputStream dout;

    String tiaoZhanZhe = null;

    public ClientAgentThread(XiangQi father) {
        this.father = father;
        try {

            din = new DataInputStream(father.sc.getInputStream());
            dout = new DataOutputStream(father.sc.getOutputStream());

            String name = father.jtfNickName.getText().trim();
            dout.writeUTF("<#NICK_NAME#>" + name);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (flag) {
            try {

                String msg = din.readUTF().trim();

                if (msg.startsWith("<#NAME_CHONGMING#>")) {
                    this.name_chongming();
                } else if (msg.startsWith("<#NICK_LIST#>")) {
                    this.nick_list(msg);
                } else if (msg.startsWith("<#SERVER_DOWN#>")) {
                    this.server_down();
                } else if (msg.startsWith("<#TIAO_ZHAN#>")) {
                    this.tiao_zhan(msg);
                } else if (msg.startsWith("<#TONG_YI#>")) {
                    this.tong_yi();
                } else if (msg.startsWith("<#BUTONG_YI#>")) {
                    this.butong_yi();
                } else if (msg.startsWith("<#BUSY#>")) {
                    this.busy();
                } else if (msg.startsWith("<#MOVE#>")) {
                    this.move(msg);
                } else if (msg.startsWith("<#RENSHU#>")) {
                    this.renshu();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void name_chongming() {
        try {
            JOptionPane.showMessageDialog(this.father, "该玩家名称已经被占用，请重新填写！",
                    "错误", JOptionPane.ERROR_MESSAGE);

            din.close();
            dout.close();

            this.father.jtfHost.setEnabled(!false);
            this.father.jtfPort.setEnabled(!false);
            this.father.jtfNickName.setEnabled(!false);
            this.father.jbConnect.setEnabled(!false);
            this.father.jbDisconnect.setEnabled(!true);
            this.father.jbChallenge.setEnabled(!true);
            this.father.jbYChallenge.setEnabled(false);
            this.father.jbNChallenge.setEnabled(false);
            this.father.jbFail.setEnabled(false);

            father.sc.close();
            father.sc = null;
            father.cat = null;
            flag = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nick_list(String msg) {

        String s = msg.substring(13);
        String[] na = s.split("\\|");

        Vector v = new Vector();

        for (int i = 0; i < na.length; i++) {
            if (na[i].trim().length() != 0 && (!na[i].trim().equals(father.jtfNickName.getText().trim()))) {
                v.add(na[i]);
            }
        }

        father.jcbNickList.setModel(new DefaultComboBoxModel(v));
    }

    public void server_down() {

        this.father.jtfHost.setEnabled(!false);
        this.father.jtfPort.setEnabled(!false);
        this.father.jtfNickName.setEnabled(!false);
        this.father.jbConnect.setEnabled(!false);
        this.father.jbDisconnect.setEnabled(!true);
        this.father.jbChallenge.setEnabled(!true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);

        this.flag = false;
        father.cat = null;

        JOptionPane.showMessageDialog(this.father, "服务器停止！！！", "提示",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void tiao_zhan(String msg) {

        try {

            String name = msg.substring(13);

            if (this.tiaoZhanZhe == null) {

                tiaoZhanZhe = msg.substring(13);

                this.father.jtfHost.setEnabled(false);
                this.father.jtfPort.setEnabled(false);
                this.father.jtfNickName.setEnabled(false);
                this.father.jbConnect.setEnabled(false);
                this.father.jbDisconnect.setEnabled(!true);
                this.father.jbChallenge.setEnabled(!true);
                this.father.jbYChallenge.setEnabled(!false);
                this.father.jbNChallenge.setEnabled(!false);
                this.father.jbFail.setEnabled(false);

                JOptionPane.showMessageDialog(this.father, tiaoZhanZhe + "向你挑战!!!",
                        "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                this.dout.writeUTF("<#BUSY#>" + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tong_yi() {

        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(!true);
        this.father.jbChallenge.setEnabled(!true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(!false);

        JOptionPane.showMessageDialog(this.father, "对方接受您的挑战!您先走棋(红棋)",
                "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public void butong_yi() {

        this.father.caiPan = false;
        this.father.color = 0;
        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(true);
        this.father.jbChallenge.setEnabled(true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);

        JOptionPane.showMessageDialog(this.father, "对方拒绝您的挑战!", "提示",
                JOptionPane.INFORMATION_MESSAGE);

        this.tiaoZhanZhe = null;
    }

    public void busy() {

        this.father.caiPan = false;
        this.father.color = 0;

        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(true);
        this.father.jbChallenge.setEnabled(true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);

        JOptionPane.showMessageDialog(this.father, "对方忙碌中！！！", "提示",
                JOptionPane.INFORMATION_MESSAGE);

        this.tiaoZhanZhe = null;
    }

    public void move(String msg) {

        int length = msg.length();

        int startI = Integer.parseInt(msg.substring(length - 4, length - 3));
        int startJ = Integer.parseInt(msg.substring(length - 3, length - 2));
        int endI = Integer.parseInt(msg.substring(length - 2, length - 1));
        int endJ = Integer.parseInt(msg.substring(length - 1));
        this.father.jpz.move(startI, startJ, endI, endJ);

        this.father.caiPan = true;
    }

    public void renshu() {

        JOptionPane.showMessageDialog(this.father, "恭喜你,你获胜,对方认输", "提示",
                JOptionPane.INFORMATION_MESSAGE);

        this.tiaoZhanZhe = null;
        this.father.color = 0;
        this.father.caiPan = false;
        this.father.next();

        this.father.jtfHost.setEnabled(false);
        this.father.jtfPort.setEnabled(false);
        this.father.jtfNickName.setEnabled(false);
        this.father.jbConnect.setEnabled(false);
        this.father.jbDisconnect.setEnabled(true);
        this.father.jbChallenge.setEnabled(true);
        this.father.jbYChallenge.setEnabled(false);
        this.father.jbNChallenge.setEnabled(false);
        this.father.jbFail.setEnabled(false);
    }
}