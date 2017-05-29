package com.ylw.main.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class QiPan extends JPanel implements MouseListener {

    private int width;
    boolean focus = false;

    int jiang1_i = 4;
    int jiang1_j = 0;
    int jiang2_i = 4;
    int jiang2_j = 9;
    int startI = -1;
    int startJ = -1;
    int endI = -1;
    int endJ = -1;

    public QiZi qiZi[][];
    XiangQi xq = null;
    GuiZe guiZe;

    public QiPan(QiZi qiZi[][], int width, XiangQi xq) {

        this.xq = xq;
        this.qiZi = qiZi;
        this.width = width;

        guiZe = new GuiZe(qiZi);

        this.addMouseListener(this);
        this.setBounds(0, 0, 700, 700);//设置棋盘的大小
        this.setLayout(null);
    }

    public void paint(Graphics g1) {

        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Color c = g.getColor();
        g.setColor(XiangQi.bgColor);
        g.fill3DRect(60, 30, 580, 630, false);
        g.setColor(Color.black);

        for (int i = 80; i <= 620; i = i + 60) {//绘制棋盘中的横线
            g.drawLine(110, i, 590, i);
        }

        g.drawLine(110, 80, 110, 620);//绘制左边线
        g.drawLine(590, 80, 590, 620);//绘制右边线

        for (int i = 170; i <= 530; i = i + 60) {//绘制中间的竖线
            g.drawLine(i, 80, i, 320);
            g.drawLine(i, 380, i, 620);
        }

        g.drawLine(290, 80, 410, 200);//绘制两边的斜线
        g.drawLine(290, 200, 410, 80);
        g.drawLine(290, 500, 410, 620);
        g.drawLine(290, 620, 410, 500);

        this.smallLine(g, 1, 2);
        this.smallLine(g, 7, 2);
        this.smallLine(g, 0, 3);
        this.smallLine(g, 2, 3);
        this.smallLine(g, 4, 3);
        this.smallLine(g, 6, 3);
        this.smallLine(g, 8, 3);
        this.smallLine(g, 0, 6);
        this.smallLine(g, 2, 6);
        this.smallLine(g, 4, 6);
        this.smallLine(g, 6, 6);
        this.smallLine(g, 8, 6);
        this.smallLine(g, 1, 7);
        this.smallLine(g, 7, 7);

        g.setColor(Color.black);

        Font font1 = new Font("宋体", Font.BOLD, 50);//设置字体
        g.setFont(font1);

        g.drawString("楚 河", 170, 365);
        g.drawString("漢 界", 400, 365);

        Font font = new Font("宋体", Font.BOLD, 30);
        g.setFont(font);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {//绘制棋子
                if (qiZi[i][j] != null) {

                    if (this.qiZi[i][j].getFocus() != false) {//是否被选中
                        g.setColor(XiangQi.focusbg);//选中后的背景色
                        g.fillOval(110 + i * 60 - 25, 80 + j * 60 - 25, 50, 50);
                        g.setColor(XiangQi.focuschar);

                    } else {

                        g.fillOval(110 + i * 60 - 25, 80 + j * 60 - 25, 50, 50);
                        g.setColor(qiZi[i][j].getColor());
                    }

                    g.drawString(qiZi[i][j].getName(), 110 + i * 60 - 15, 80 + j * 60 + 10);
                    g.setColor(Color.black);
                }
            }
        }
        g.setColor(c);
    }

    public void mouseClicked(MouseEvent e) {

        if (this.xq.caiPan == true) {

            int i = -1, j = -1;
            int[] pos = getPos(e);

            i = pos[0];
            j = pos[1];

            if (i >= 0 && i <= 8 && j >= 0 && j <= 9) {

                if (focus == false) {
                    this.noFocus(i, j);
                } else {

                    if (qiZi[i][j] != null) {//如果该处有棋子
                        if (qiZi[i][j].getColor() == qiZi[startI][startJ].getColor()) {//如果是自己的棋子
                            qiZi[startI][startJ].setFocus(false);
                            qiZi[i][j].setFocus(true);//更改选中对象
                            startI = i;
                            startJ = j;
                        } else {//如果是对方棋子
                            endI = i;//保存该点
                            endJ = j;
                            String name = qiZi[startI][startJ].getName();//获得该棋子的名字
                            //看是否可以移动
                            boolean canMove = guiZe.canMove(startI, startJ, endI, endJ, name);
                            if (canMove)
                            {
                                try {
                                    this.xq.cat.dout.writeUTF("<#MOVE#>" +
                                            this.xq.cat.tiaoZhanZhe + startI + startJ + endI + endJ);
                                    this.xq.caiPan = false;
                                    if (qiZi[endI][endJ].getName().equals("帥") ||
                                            qiZi[endI][endJ].getName().equals("將")) {
                                        this.success();
                                    } else {
                                        this.noJiang();
                                    }
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }
                        }
                    } else {
                        endI = i;
                        endJ = j;
                        String name = qiZi[startI][startJ].getName();
                        boolean canMove = guiZe.canMove(startI, startJ, endI, endJ, name);//判断是否可走
                        if (canMove) {
                            this.noQiZi();
                        }
                    }
                }
            }
            this.xq.repaint();//重绘
        }
    }

    public int[] getPos(MouseEvent e) {

        int[] pos = new int[2];

        pos[0] = -1;
        pos[1] = -1;

        Point p = e.getPoint();

        double x = p.getX();
        double y = p.getY();

        if (Math.abs((x - 110) / 1 % 60) <= 25) {//获得对应于数组x下标的位置
            pos[0] = Math.round((float) (x - 110)) / 60;
        } else if (Math.abs((x - 110) / 1 % 60) >= 35) {
            pos[0] = Math.round((float) (x - 110)) / 60 + 1;
        }
        if (Math.abs((y - 80) / 1 % 60) <= 25) {//获得对应于数组y下标的位置
            pos[1] = Math.round((float) (y - 80)) / 60;
        } else if (Math.abs((y - 80) / 1 % 60) >= 35) {
            pos[1] = Math.round((float) (y - 80)) / 60 + 1;
        }
        return pos;
    }

    public void noFocus(int i, int j) {

        if (this.qiZi[i][j] != null)//如果该位置有棋子
        {
            if (this.xq.color == 0)//如果是红方
            {
                if (this.qiZi[i][j].getColor().equals(XiangQi.color1))//如果棋子是红色
                {
                    this.qiZi[i][j].setFocus(true);//将该棋子设为选中状态
                    focus = true;//将focus设为true
                    startI = i;//保存该坐标点
                    startJ = j;
                }
            } else//如果是白方
            {
                if (this.qiZi[i][j].getColor().equals(XiangQi.color2))//如果该棋子是白色
                {
                    this.qiZi[i][j].setFocus(true);//将该棋子设为选中状态
                    focus = true;//将focus设为true
                    startI = i;//保存该坐标点
                    startJ = j;
                }
            }
        }
    }

    public void success() {

        qiZi[endI][endJ] = qiZi[startI][startJ];//吃掉该棋子
        qiZi[startI][startJ] = null;//将原来的位置设为空

        this.xq.repaint();//重绘

        JOptionPane.showMessageDialog(this.xq, "恭喜您，您获胜了", "提示",
                JOptionPane.INFORMATION_MESSAGE);

        this.xq.cat.tiaoZhanZhe = null;
        this.xq.color = 0;
        this.xq.caiPan = false;
        this.xq.next();

        this.xq.jtfHost.setEnabled(false);
        this.xq.jtfPort.setEnabled(false);
        this.xq.jtfNickName.setEnabled(false);
        this.xq.jbConnect.setEnabled(false);
        this.xq.jbDisconnect.setEnabled(true);
        this.xq.jbChallenge.setEnabled(true);
        this.xq.jbYChallenge.setEnabled(false);
        this.xq.jbNChallenge.setEnabled(false);
        this.xq.jbFail.setEnabled(false);

        startI = -1;
        startJ = -1;
        endI = -1;
        endJ = -1;
        jiang1_i = 4;
        jiang1_j = 0;
        jiang2_i = 4;
        jiang2_j = 9;
        focus = false;
    }

    public void noJiang() {

        qiZi[endI][endJ] = qiZi[startI][startJ];
        qiZi[startI][startJ] = null;
        qiZi[endI][endJ].setFocus(false);

        this.xq.repaint();//重绘

        if (qiZi[endI][endJ].getName().equals("帥")) {
            jiang1_i = endI;//更新"帥"的位置坐标
            jiang1_j = endJ;
        } else if (qiZi[endI][endJ].getName().equals("將")) {//如果移动的是"將"
            jiang2_i = endI;//更新"將"的位置坐标
            jiang2_j = endJ;
        }
        if (jiang1_i == jiang2_i) {//如果"將"和"帥"在一条竖线上
            int count = 0;
            for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {//遍历这条竖线
                if (qiZi[jiang1_i][jiang_j] != null) {
                    count++;
                    break;
                }
            }

            if (count == 0) {//如果等于零则照将
                JOptionPane.showMessageDialog(this.xq, "照将！！！你失败了！！！", "提示",
                        JOptionPane.INFORMATION_MESSAGE);//给出失败信息

                this.xq.cat.tiaoZhanZhe = null;
                this.xq.color = 0;
                this.xq.caiPan = false;
                this.xq.next();
                this.xq.jtfHost.setEnabled(false);
                this.xq.jtfPort.setEnabled(false);
                this.xq.jtfNickName.setEnabled(false);
                this.xq.jbConnect.setEnabled(false);
                this.xq.jbDisconnect.setEnabled(true);
                this.xq.jbChallenge.setEnabled(true);
                this.xq.jbYChallenge.setEnabled(false);
                this.xq.jbNChallenge.setEnabled(false);
                this.xq.jbFail.setEnabled(false);

                jiang1_i = 4;
                jiang1_j = 0;
                jiang2_i = 4;
                jiang2_j = 9;

            }
        }
        startI = -1;
        startJ = -1;
        endI = -1;
        endJ = -1;
        focus = false;
    }

    public void noQiZi() {

        try {

            this.xq.cat.dout.writeUTF("<#MOVE#>" + this.xq.cat.tiaoZhanZhe + startI + startJ + endI + endJ);
            this.xq.caiPan = false;
            qiZi[endI][endJ] = qiZi[startI][startJ];
            qiZi[startI][startJ] = null;//走棋
            qiZi[endI][endJ].setFocus(false);//将该棋设为非选中状态

            this.xq.repaint();//重绘
            if (qiZi[endI][endJ].getName().equals("帥")) {
                jiang1_i = endI;
                jiang1_j = endJ;
            } else if (qiZi[endI][endJ].getName().equals("將")) {
                jiang2_i = endI;
                jiang2_j = endJ;
            }
            if (jiang1_i == jiang2_i)
            {
                int count = 0;
                for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {
                    if (qiZi[jiang1_i][jiang_j] != null) {
                        count++;
                        break;
                    }
                }
                if (count == 0) {
                    JOptionPane.showMessageDialog(this.xq, "照将！！！你失败了！！！", "提示",
                            JOptionPane.INFORMATION_MESSAGE);

                    this.xq.cat.tiaoZhanZhe = null;
                    this.xq.color = 0;
                    this.xq.caiPan = false;
                    this.xq.next();

                    this.xq.jtfHost.setEnabled(false);
                    this.xq.jtfPort.setEnabled(false);
                    this.xq.jtfNickName.setEnabled(false);
                    this.xq.jbConnect.setEnabled(false);
                    this.xq.jbDisconnect.setEnabled(true);
                    this.xq.jbChallenge.setEnabled(true);
                    this.xq.jbYChallenge.setEnabled(false);
                    this.xq.jbNChallenge.setEnabled(false);
                    this.xq.jbFail.setEnabled(false);

                    jiang1_i = 4;//"帥"的i坐标
                    jiang1_j = 0;//"帥"的j坐标
                    jiang2_i = 4;//"將"的i坐标
                    jiang2_j = 9;//"將"的j坐标

                }
            }
            startI = -1;
            startJ = -1;
            endI = -1;
            endJ = -1;
            focus = false;

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void move(int startI, int startJ, int endI, int endJ) {

        if (qiZi[endI][endJ] != null && (qiZi[endI][endJ].getName().equals("帥") ||
                qiZi[endI][endJ].getName().equals("將"))) {//如果"将"被吃了
            qiZi[endI][endJ] = qiZi[startI][startJ];
            qiZi[startI][startJ] = null;//走棋
            this.xq.repaint();

            JOptionPane.showMessageDialog(this.xq, "很遗憾，您失败了！！！", "提示",
                    JOptionPane.INFORMATION_MESSAGE);

            this.xq.cat.tiaoZhanZhe = null;
            this.xq.color = 0;
            this.xq.caiPan = false;
            this.xq.next();

            this.xq.jtfHost.setEnabled(false);
            this.xq.jtfPort.setEnabled(false);
            this.xq.jtfNickName.setEnabled(false);
            this.xq.jbConnect.setEnabled(false);
            this.xq.jbDisconnect.setEnabled(true);
            this.xq.jbChallenge.setEnabled(true);
            this.xq.jbYChallenge.setEnabled(false);
            this.xq.jbNChallenge.setEnabled(false);
            this.xq.jbFail.setEnabled(false);

            jiang1_i = 4;
            jiang1_j = 0;
            jiang2_i = 4;
            jiang2_j = 9;

        } else {

            qiZi[endI][endJ] = qiZi[startI][startJ];
            qiZi[startI][startJ] = null;//走棋
            this.xq.repaint();

            if (qiZi[endI][endJ].getName().equals("帥")) {
                jiang1_i = endI;
                jiang1_j = endJ;
            } else if (qiZi[endI][endJ].getName().equals("將")) {
                jiang2_i = endI;
                jiang2_j = endJ;
            }
            if (jiang1_i == jiang2_i) {
                int count = 0;
                for (int jiang_j = jiang1_j + 1; jiang_j < jiang2_j; jiang_j++) {
                    if (qiZi[jiang1_i][jiang_j] != null) {//有棋子
                        count++;
                        break;
                    }
                }
                if (count == 0) {
                    JOptionPane.showMessageDialog(this.xq, "对方照将！！！你胜利了！！！",
                            "提示", JOptionPane.INFORMATION_MESSAGE);
                    this.xq.cat.tiaoZhanZhe = null;

                    this.xq.color = 0;
                    this.xq.caiPan = false;
                    this.xq.next();

                    this.xq.jtfHost.setEnabled(false);
                    this.xq.jtfPort.setEnabled(false);//设置各空间位置
                    this.xq.jtfNickName.setEnabled(false);
                    this.xq.jbConnect.setEnabled(false);
                    this.xq.jbDisconnect.setEnabled(true);
                    this.xq.jbChallenge.setEnabled(true);
                    this.xq.jbYChallenge.setEnabled(false);
                    this.xq.jbNChallenge.setEnabled(false);
                    this.xq.jbFail.setEnabled(false);

                    jiang1_i = 4;
                    jiang1_j = 0;
                    jiang2_i = 4;
                    jiang2_j = 9;
                }
            }
        }
        this.xq.repaint();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void smallLine(Graphics2D g, int i, int j) {

        int x = 110 + 60 * i;
        int y = 80 + 60 * j;

        if (i > 0) {//绘制左上方的标志
            g.drawLine(x - 3, y - 3, x - 20, y - 3);
            g.drawLine(x - 3, y - 3, x - 3, y - 20);
        }
        if (i < 8) {//绘制右上方的标志
            g.drawLine(x + 3, y - 3, x + 20, y - 3);
            g.drawLine(x + 3, y - 3, x + 3, y - 20);
        }
        if (i > 0) {//绘制左下方的标志
            g.drawLine(x - 3, y + 3, x - 20, y + 3);
            g.drawLine(x - 3, y + 3, x - 3, y + 20);
        }
        if (i < 8) {//绘制右下方的标志
            g.drawLine(x + 3, y + 3, x + 20, y + 3);
            g.drawLine(x + 3, y + 3, x + 3, y + 20);
        }
    }
}
