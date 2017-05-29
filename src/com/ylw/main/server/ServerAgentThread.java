package com.ylw.main.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ServerAgentThread extends Thread {

    Server father;
    Socket sc;

    DataInputStream din;
    DataOutputStream dout;

    boolean flag = true;

    public ServerAgentThread(Server father, Socket sc) {

        this.father = father;
        this.sc = sc;

        try {

            din = new DataInputStream(sc.getInputStream());
            dout = new DataOutputStream(sc.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while (flag) {
            try {
                String msg = din.readUTF().trim();//接收客户端传来的信息
                if (msg.startsWith("<#NICK_NAME#>"))//收到新用户的信息
                {
                    this.nick_name(msg);
                } else if (msg.startsWith("<#CLIENT_LEAVE#>")) {
                    this.client_leave(msg);
                } else if (msg.startsWith("<#TIAO_ZHAN#>")) {
                    this.tiao_zhan(msg);
                } else if (msg.startsWith("<#TONG_YI#>")) {
                    this.tong_yi(msg);
                } else if (msg.startsWith("<#BUTONG_YI#>")) {
                    this.butong_yi(msg);
                } else if (msg.startsWith("<#BUSY#>")) {
                    this.busy(msg);
                } else if (msg.startsWith("<#MOVE#>")) {
                    this.move(msg);
                } else if (msg.startsWith("<#RENSHU#>")) {
                    this.renshu(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void nick_name(String msg) {
        try {

            String name = msg.substring(13);//获得用户的昵称
            this.setName(name);//用该昵称给该线程取名

            Vector v = father.onlineList;//获得在线用户列表
            boolean isChongMing = false;

            int size = v.size();

            for (int i = 0; i < size; i++) {
                ServerAgentThread tempSat = (ServerAgentThread) v.get(i);

                if (tempSat.getName().equals(name)) {
                    isChongMing = true;
                    break;
                }
            }
            if (isChongMing == true) {
                dout.writeUTF("<#NAME_CHONGMING#>");
                din.close();
                dout.close();
                sc.close();

                flag = false;
            } else {
                v.add(this);
                father.refreshList();
                String nickListMsg = "";
                size = v.size();

                for (int i = 0; i < size; i++) {
                    ServerAgentThread tempSat = (ServerAgentThread) v.get(i);
                    nickListMsg = nickListMsg + "|" + tempSat.getName();
                }
                nickListMsg = "<#NICK_LIST#>" + nickListMsg;
                Vector tempv = father.onlineList;
                size = tempv.size();

                for (int i = 0; i < size; i++) {
                    ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);
                    satTemp.dout.writeUTF(nickListMsg);

                    if (satTemp != this) {
                        satTemp.dout.writeUTF("<#MSG#>" + this.getName() + "上线了...");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void client_leave(String msg) {
        try {

            Vector tempv = father.onlineList;
            tempv.remove(this);
            int size = tempv.size();
            String nl = "<#NICK_LIST#>";

            for (int i = 0; i < size; i++) {
                ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);

                satTemp.dout.writeUTF("<#MSG#>" + this.getName() + "离线了...");

                nl = nl + "|" + satTemp.getName();
            }
            for (int i = 0; i < size; i++) {

                ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);
                satTemp.dout.writeUTF(nl);
            }
            this.flag = false;//终止该服务器代理线程
            father.refreshList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tiao_zhan(String msg) {
        try {
            String name1 = this.getName();//获得发出挑战信息用户的名字
            String name2 = msg.substring(13);//获得被挑战的用户名字

            Vector v = father.onlineList;
            int size = v.size();
            for (int i = 0; i < size; i++) {//遍历列表，搜索被挑战的用户
                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);

                if (satTemp.getName().equals(name2)) {//向该用户发送挑战信息，附带提出挑战用户的名字
                    satTemp.dout.writeUTF("<#TIAO_ZHAN#>" + name1);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tong_yi(String msg) {
        try {

            String name = msg.substring(11);//获得提出挑战的用户的名字
            Vector v = father.onlineList;
            int size = v.size();
            for (int i = 0; i < size; i++) {
                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);
                if (satTemp.getName().equals(name)) {
                    satTemp.dout.writeUTF("<#TONG_YI#>");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void butong_yi(String msg) {
        try {
            String name = msg.substring(13);
            Vector v = father.onlineList;

            int size = v.size();
            for (int i = 0; i < size; i++) {
                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);

                if (satTemp.getName().equals(name)) {
                    satTemp.dout.writeUTF("<#BUTONG_YI#>");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void busy(String msg) {
        try {
            String name = msg.substring(8);
            Vector v = father.onlineList;

            int size = v.size();
            for (int i = 0; i < size; i++) {
                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);

                if (satTemp.getName().equals(name)) {
                    satTemp.dout.writeUTF("<#BUSY#>");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move(String msg) {
        try {
            String name = msg.substring(8, msg.length() - 4);
            Vector v = father.onlineList;

            int size = v.size();
            for (int i = 0; i < size; i++) {

                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);
                if (satTemp.getName().equals(name)) {
                    satTemp.dout.writeUTF(msg);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renshu(String msg) {
        try {
            String name = msg.substring(10);
            Vector v = father.onlineList;

            int size = v.size();
            for (int i = 0; i < size; i++) {
                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);

                if (satTemp.getName().equals(name)) {
                    satTemp.dout.writeUTF(msg);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}