package com.ylw.main.client;

public class GuiZe {

    QiZi[][] qiZi;
    boolean canMove = false;

    int i;
    int j;

    public GuiZe(QiZi[][] qiZi) {
        this.qiZi = qiZi;
    }

    public boolean canMove(int startI, int startJ, int endI, int endJ, String name) {

        int maxI;
        int minI;
        int maxJ;
        int minJ;

        canMove = true;

        if (startI >= endI)
        {
            maxI = startI;
            minI = endI;
        } else
        {
            maxI = endI;
            minI = startI;
        }
        if (startJ >= endJ)
        {
            maxJ = startJ;
            minJ = endJ;
        } else {
            maxJ = endJ;
            minJ = startJ;
        }
        if (name.equals("車"))
        {
            this.ju(maxI, minI, maxJ, minJ);
        } else if (name.equals("馬"))
        {
            this.ma(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("相"))
        {
            this.xiang1(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("象"))
        {
            this.xiang2(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("士") || name.equals("仕"))
        {
            this.shi(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("帥") || name.equals("將"))
        {
            this.jiang(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("炮") || name.equals("砲"))
        {
            this.pao(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        } else if (name.equals("兵"))
        {
            this.bing(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);

        } else if (name.equals("卒"))
        {
            this.zu(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
        }
        return canMove;
    }

    public void ju(int maxI, int minI, int maxJ, int minJ) {
        if (maxI == minI)
        {
            for (j = minJ + 1; j < maxJ; j++) {
                if (qiZi[maxI][j] != null)
                {
                    canMove = false;
                    break;
                }
            }
        } else if (maxJ == minJ)
        {
            for (i = minJ + 1; i < maxJ; i++) {
                if (qiZi[i][maxJ] != null)
                {
                    canMove = false;
                    break;
                }
            }
        } else if (maxI != minI && maxJ != minJ)
        {
            canMove = false;
        }
    }

    public void ma(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

        int a = maxI - minI;
        int b = maxJ - minJ;

        if (a == 1 && b == 2)
        {
            if (startJ > endJ)
            {
                if (qiZi[startI][startJ - 1] != null)
                {
                    canMove = false;
                }
            } else {//如果是从上往下走
                if (qiZi[startI][startJ + 1] != null)//如果马腿处有棋子
                {
                    canMove = false;//不可以走
                }
            }
        } else if (a == 2 && b == 1)//如果是横着的"日"
        {
            if (startI > endI)//如果是从右往左走
            {
                if (qiZi[startI - 1][startJ] != null)//如果马腿处有棋子
                {
                    canMove = false;
                }
            } else {//如果是从左往右走
                if (qiZi[startI + 1][startJ] != null)//如果马腿处有棋子
                {
                    canMove = false;
                }
            }
        } else if (!((a == 2 && b == 1) || (a == 1 && b == 2)))//如果不时"日"字
        {
            canMove = false;
        }
    }

    public void xiang1(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

        int a = maxI - minI;
        int b = maxJ - minJ;

        if (a == 2 && b == 2)//如果是"田"字
        {
            if (endJ > 4)//如果过河了
            {
                canMove = false;//不可以走
            }
            if (qiZi[(maxI + minI) / 2][(maxJ + minJ) / 2] != null)//如果"田"字中间有棋子
            {
                canMove = false;//不可以走
            }
        } else {
            canMove = false;//如果不是"田"字，不可以走
        }
    }

    public void xiang2(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
        int a = maxI - minI;
        int b = maxJ - minJ;
        if (a == 2 && b == 2)
        {
            if (endJ < 5)
            {
                canMove = false;
            }
            if (qiZi[(maxI + minI) / 2][(maxJ + minJ) / 2] != null)
            {
                canMove = false;
            }
        } else {
            canMove = false;
        }
    }

    public void shi(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

        int a = maxI - minI;
        int b = maxJ - minJ;

        if (a == 1 && b == 1)//如果是小斜线
        {
            if (startJ > 4)//如果是下方的士
            {
                if (endJ < 7) {
                    canMove = false;//如果下方的士越界，不可以走
                }
            } else {//如果是上方的仕
                if (endJ > 2) {
                    canMove = false;//如果上方的仕越界，不可以走
                }
            }
            if (endI > 5 || endI < 3)//如果左右越界，则不可以走
            {
                canMove = false;
            }
        } else {
            canMove = false;
        }
    }

    public void jiang(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

        int a = maxI - minI;
        int b = maxJ - minJ;

        if ((a == 1 && b == 0) || (a == 0 && b == 1)) {//如果走的是一小格
            if (startJ > 4)//如果是下方的将
            {
                if (endJ < 7)//如果越界
                {
                    canMove = false;//不可以走
                }
            } else {//如果是上方的将
                if (endJ > 2)
                {
                    canMove = false;
                }
            }
            if (endI > 5 || endI < 3)//如果左右越界，不可以走
            {
                canMove = false;
            }
        } else {
            canMove = false;
        }
    }

    public void pao(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

        if (maxI == minI)//如果走的竖线
        {
            if (qiZi[endI][endJ] != null)//如果终点有棋子
            {
                int count = 0;
                for (j = minJ + 1; j < maxJ; j++) {
                    if (qiZi[minI][j] != null)//判断起点与终点之间有几个棋子
                    {
                        count++;
                    }
                }
                if (count != 1) {//如果不是一个棋子
                    canMove = false;//不可以走
                }
            } else if (qiZi[endI][endJ] == null)//如果终点没有棋子
            {
                for (j = minJ + 1; j < maxJ; j++) {
                    if (qiZi[minI][j] != null)//如果起止点之间有棋子
                    {
                        canMove = false;//不可以走
                        break;
                    }
                }
            }
        } else if (maxJ == minJ)//如果走的是横线
        {
            if (qiZi[endI][endJ] != null)//如果终点有棋子
            {
                int count = 0;
                for (i = minI + 1; i < maxI; i++) {
                    if (qiZi[i][minJ] != null)//判断起点与终点之间有几个棋子
                    {
                        count++;
                    }
                }
                if (count != 1)//如果不是一个棋子
                {
                    canMove = false;//不可以走
                }
            } else if (qiZi[endI][endJ] == null)//如果终点没有棋子
            {
                for (i = minI + 1; i < maxI; i++) {
                    if (qiZi[i][minJ] != null)//如果起止点之间有棋子
                    {
                        canMove = false;//不可以走
                        break;
                    }
                }
            }
        } else if (maxJ != minJ && maxI != minI) {
            canMove = false;
        }
    }

    public void bing(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
        if (startJ < 5)//如果还没有过河
        {
            if (startI != endI)//如果不是向前走
            {
                canMove = false;
            }
            if (endJ - startJ != 1)//如果走的不是一格
            {
                canMove = false;
            }
        } else {//如果已经过河
            if (startI == endI) {//如果走的是竖线
                if (endJ - startJ != 1)//如果走的不是一格
                {
                    canMove = false;
                }
            } else if (startJ == endJ) {//如果走的是横线
                if (maxI - minI != 1) {//如果走的不是一格
                    canMove = false;
                }
            } else if (startI != endI && startJ != endJ) {//如果走的既不是竖线，也不是横线，则不可以走
                canMove = false;
            }
        }
    }

    public void zu(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
        if (startJ > 4) {//如果还没有过河
            if (startI != endI) {//如果不是向前走
                canMove = false;
            }
            if (endJ - startJ != -1)//如果走的不是一格
            {
                canMove = false;
            }
        } else {//如果已经过河
            if (startI == endI) {//如果走的是竖线
                if (endJ - startJ != -1) {//如果走的不是一格
                    canMove = false;
                }
            } else if (startJ == endJ) {//如果走的是横线
                if (maxI - minI != 1) {//如果走的不是一格
                    canMove = false;
                }
            } else if (startI != endI && startJ != endJ) {//如果走的既不是竖线，也不是横线，则不可以走
                canMove = false;
            }
        }
    }
}