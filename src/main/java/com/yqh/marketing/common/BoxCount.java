package com.yqh.marketing.common;

import com.yqh.marketing.basedevss.base.data.Record;
import com.yqh.marketing.basedevss.base.util.DateUtils;

import java.util.Random;

/**
 * Created by chenarne on 16/2/3.
 */
public class BoxCount {
    //=========================下面是包装计算的工具饭方法=========================
    int S_4_BASE = 4;
    int S_6_BASE = 6;
    int S_8_BASE = 8;

    //    能装4个的盒子成本是2元     0.5
//    能装6个的盒子成本是2.1元   0.35 最低
//    能装8个的盒子成本是3.1元   0.3875
    float S_4_SUMMER_PRICE = 2f;
    float S_6_SUMMER_PRICE = 2.1f;
    float S_8_SUMMER_PRICE = 3.1f;

    //    能装4个的盒子成本是4.5元     1.125
//    能装6个的盒子成本是5.4元   0.9
//    能装8个的盒子成本是7.1元   0.8875 最低
    float S_4_WINTER_PRICE = 4.5f;
    float S_6_WINTER_PRICE = 5.4f;
    float S_8_WINTER_PRICE = 7.1f;

    float S_PAOPAO_PRICE = 0.05f;  //填充物的价格

    public static Record doSearchSummerYogurt(int inputNum) {
        //夏季 4，6，8的最小公倍数 为24
        int[] returnArray = new int[3];
        int iBomxNumSummer = 0;  //倍数

        if (inputNum < 24){
            returnArray = getLessArraySummer(inputNum);
        }else{
            float dBomNumSummer = inputNum / 24;
            if (inputNum % 24 == 0){
                iBomxNumSummer = (int)dBomNumSummer;
            }else{
                if (inputNum<=24){
                    iBomxNumSummer =1;
                }else{
                    iBomxNumSummer = Integer.parseInt(String.valueOf(dBomNumSummer).substring(0, String.valueOf(dBomNumSummer).indexOf(".")));//整倍数,多少倍
                }
            }

            int lessCountSummer = 0;
            if (iBomxNumSummer==0)
                lessCountSummer = 0;//剩余的盒数
            else
                lessCountSummer = inputNum - iBomxNumSummer * 24;//剩余的盒数
            returnArray = getLessArraySummer(lessCountSummer);
        }

        //余下的一定是1~23的数字 用6个盒子装，最划算
        int box_4_COUNT = returnArray[0];
        int box_6_COUNT = iBomxNumSummer * 4 + returnArray[1];
        int box_8_COUNT = returnArray[2];

        Record out_rec_summer = new Record();
        out_rec_summer.put("box_4_COUNT",box_4_COUNT);
        out_rec_summer.put("box_6_COUNT",box_6_COUNT);
        out_rec_summer.put("box_8_COUNT",box_8_COUNT);
        return out_rec_summer;
    }

    public static Record doSearchSummerSnack(int inputNum) {
        //空盒子装坚果，分别能装19， 25， 32
        //都用4个的盒子装，最划算
        int iBomxNumSummer = 0;  //倍数
        float dBomNumSummer = inputNum / 19;
        if (inputNum % 19 == 0){
            iBomxNumSummer = (int)dBomNumSummer;
        }else{
            if (inputNum <= 19){
                iBomxNumSummer=1;
            }else{
                iBomxNumSummer = Integer.parseInt(String.valueOf(dBomNumSummer).substring(0, String.valueOf(dBomNumSummer).indexOf(".")));//整倍数,多少倍
            }
        }

        int lessCountSummer = 0;
        if (iBomxNumSummer==0)
            lessCountSummer = 0;//剩余的坚果
        else
            lessCountSummer = inputNum - iBomxNumSummer * 19;//剩余的坚果

        if (lessCountSummer > 0)
            iBomxNumSummer += 1;
        //余下的一定是1~19的数字 用4个盒子装，最划算
        int box_4_COUNT = iBomxNumSummer;
        Record out_rec_summer = new Record();
        out_rec_summer.put("box_4_COUNT",box_4_COUNT);
        out_rec_summer.put("box_6_COUNT",0);
        out_rec_summer.put("box_8_COUNT",0);
        return out_rec_summer;
    }


    public static Record doSearchWinterYogurt(int inputNum) {
        int[] returnArray = new int[3];
        int iBomxNumWinter = 0;  //倍数

        if (inputNum < 24){
            returnArray = getLessArrayWinter(inputNum);
        }else{
            float dBomnumWinter = inputNum / 24;
            if (inputNum % 24 == 0){
                iBomxNumWinter = (int)dBomnumWinter;
            }else{
                if (inputNum<=24){
                    iBomxNumWinter =1;
                }else{
                    iBomxNumWinter = Integer.parseInt(String.valueOf(dBomnumWinter).substring(0, String.valueOf(dBomnumWinter).indexOf(".")));//整倍数,多少倍
                }
            }

            int lessCountSummer = 0;
            if (iBomxNumWinter==0)
                lessCountSummer = 0;//剩余的盒数
            else
                lessCountSummer = inputNum - iBomxNumWinter * 24;//剩余的盒数
            returnArray = getLessArrayWinter(lessCountSummer);
        }

        //用一个8的盒子装最划算
        int box_4_COUNT = returnArray[0];
        int box_6_COUNT = returnArray[1];
        int box_8_COUNT = (iBomxNumWinter * 3) + returnArray[2];

        Record out_rec_winter = new Record();
        out_rec_winter.put("box_4_COUNT", box_4_COUNT);
        out_rec_winter.put("box_6_COUNT", box_6_COUNT);
        out_rec_winter.put("box_8_COUNT", box_8_COUNT);
        return out_rec_winter;
    }


    public static int[] getLessArraySummer(int lessCount) {
        int[] myArray = new int[3];
        if (lessCount==0){
            myArray[0]= 0;
            myArray[1]= 0;
            myArray[2]= 0;
        }
        if (lessCount <= 4 && lessCount >0){
            myArray[0]= 1;
            myArray[1]= 0;
            myArray[2]= 0;
        }
        if (lessCount==5 || lessCount==6){
            myArray[0]= 0;
            myArray[1]= 1;
            myArray[2]= 0;
        }
        if (lessCount==7 || lessCount==8){
            myArray[0]= 0;
            myArray[1]= 0;
            myArray[2]= 1;
        }
        if (lessCount==9 || lessCount==10){
            myArray[0]= 1;
            myArray[1]= 1;
            myArray[2]= 0;
        }
        if (lessCount==11 || lessCount==12){
            myArray[0]= 0;
            myArray[1]= 2;
            myArray[2]= 0;
        }
        if (lessCount==13 || lessCount==14){
            myArray[0]= 0;
            myArray[1]= 1;
            myArray[2]= 1;
        }
        if (lessCount==15 || lessCount==16){
            myArray[0]= 0;
            myArray[1]= 0;
            myArray[2]= 2;
        }
        if (lessCount==17 || lessCount==18){
            myArray[0]= 0;
            myArray[1]= 3;
            myArray[2]= 0;
        }
        if (lessCount==19 || lessCount==20){
            myArray[0]= 0;
            myArray[1]= 2;
            myArray[2]= 1;
        }
        if (lessCount==21 || lessCount==22){
            myArray[0]= 0;
            myArray[1]= 1;
            myArray[2]= 2;
        }
        if (lessCount==23){
            myArray[0]= 0;
            myArray[1]= 4;
            myArray[2]= 0;
        }
        return myArray;
    }
    public static int[] getLessArrayWinter(int lessCount) {
        int[] myArray = new int[3];
        if (lessCount==0){
            myArray[0]= 0;
            myArray[1]= 0;
            myArray[2]= 0;
        }
        if (lessCount <= 4  && lessCount >0){
            myArray[0]= 1;
            myArray[1]= 0;
            myArray[2]= 0;
        }
        if (lessCount==5 || lessCount==6){
            myArray[0]= 0;
            myArray[1]= 1;
            myArray[2]= 0;
        }
        if (lessCount==7 || lessCount==8){
            myArray[0]= 0;
            myArray[1]= 0;
            myArray[2]= 1;
        }
        if (lessCount==9 || lessCount==10){
            myArray[0]= 1;
            myArray[1]= 1;
            myArray[2]= 0;
        }
        if (lessCount==11 || lessCount==12){
            myArray[0]= 0;
            myArray[1]= 2;
            myArray[2]= 0;
        }
        if (lessCount==13 || lessCount==14){
            myArray[0]= 0;
            myArray[1]= 1;
            myArray[2]= 1;
        }
        if (lessCount==15 || lessCount==16){
            myArray[0]= 0;
            myArray[1]= 0;
            myArray[2]= 2;
        }
        if (lessCount==17 || lessCount==18){
            myArray[0]= 0;
            myArray[1]= 3;
            myArray[2]= 0;
        }
        if (lessCount==19 || lessCount==20){
            myArray[0]= 0;
            myArray[1]= 2;
            myArray[2]= 1;
        }
        if (lessCount==21 || lessCount==22){
            myArray[0]= 0;
            myArray[1]= 1;
            myArray[2]= 2;
        }
        if (lessCount==23){
            myArray[0]= 0;
            myArray[1]= 0;
            myArray[2]= 3;
        }
        return myArray;
    }

    //总共可以装多少坚果
    public static int canInsertJgReplacePaoPao(Record allBox,int allInsertPaoPao) {
        //allInsertPaoPao是剩余空间数
        //这个剩余空间，尽可能的用来装坚果了，也就是说，选择最大的箱子来剩余
        //而且，得看，有没有能装8个的箱子
        //之前的分配方案，已经决定了，剩余的空间，绝对小于已有的盒子
        int all_can_insert_jg = 0;
        int box_6_COUNT = (int)allBox.getInt("box_6_COUNT");
        int box_8_COUNT = (int)allBox.getInt("box_8_COUNT");
        if (box_8_COUNT > 0){
            all_can_insert_jg = canInsertJgBase(8,allInsertPaoPao);
        }else{
            if (box_6_COUNT > 0){
                all_can_insert_jg = canInsertJgBase(6,allInsertPaoPao);
            }else{
                all_can_insert_jg = canInsertJgBase(4,allInsertPaoPao);
            }
        }
        return all_can_insert_jg;
    }

    public static int canInsertJgBase(int BOX_TYPE,int lessBoxFree) {
        //lessBoxFree是剩余空间数
        int returnNum = 0;
        if (BOX_TYPE==4){
            if (lessBoxFree==0)
                returnNum = 3;
            if (lessBoxFree==1)
                returnNum = 7;
            if (lessBoxFree==2)
                returnNum = 11;
            if (lessBoxFree==3)
                returnNum = 15;
            if (lessBoxFree==4)
                returnNum = 19;
        }
        if (BOX_TYPE==6){
            if (lessBoxFree==0)
                returnNum = 3;
            if (lessBoxFree==1)
                returnNum = 7;
            if (lessBoxFree==2)
                returnNum = 10;
            if (lessBoxFree==3)
                returnNum = 14;
            if (lessBoxFree==4)
                returnNum = 18;
            if (lessBoxFree==5)
                returnNum = 21;
            if (lessBoxFree==6)
                returnNum = 25;
        }
        if (BOX_TYPE==8){
            if (lessBoxFree==0)
                returnNum = 4;
            if (lessBoxFree==1)
                returnNum = 8;
            if (lessBoxFree==2)
                returnNum = 12;
            if (lessBoxFree==3)
                returnNum = 15;
            if (lessBoxFree==4)
                returnNum = 18;
            if (lessBoxFree==5)
                returnNum = 22;
            if (lessBoxFree==6)
                returnNum = 26;
            if (lessBoxFree==7)
                returnNum = 29;
            if (lessBoxFree==8)
                returnNum = 32;
        }
        return returnNum;
    }

    public static String len1To4(int inNum){
        String outStr = "";
        if (String.valueOf(inNum).length()==1){
            outStr = "000"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==2){
            outStr = "00"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==3){
            outStr = "0"+String.valueOf(inNum);
        }
        if (String.valueOf(inNum).length()==4){
            outStr = String.valueOf(inNum);
        }
        return outStr;
    }

    public static String getNowDateNUm(){
        String DATE = DateUtils.now().replace("-", "").replace(" ", "").replace(":", "");
        //再加3个随机数
        Random random = new Random();
        int num = -1 ;
        num = (int)(random.nextDouble()*(1000 - 100) + 100);
        return DATE+String.valueOf(num);
    }
}
