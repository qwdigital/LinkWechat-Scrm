package com.linkwechat.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class RedPacketUtils {

    private static final Random random = new Random();


    /**
     * 根据总数分割个数及限定区间进行数据随机处理
     * 数列浮动阀值为0.95
     *
     * @param totalMoney    - 被分割的总数
     * @param splitNum - 分割的个数
     * @param min      - 单个数字下限
     * @param max      - 单个数字上限
     * @return - 返回符合要求的数字列表
     */
    public static List<BigDecimal> genRanddomList(BigDecimal totalMoney, Integer splitNum, BigDecimal min, BigDecimal max, float thresh) {
        totalMoney = totalMoney.multiply(new BigDecimal(100));
        min = min.multiply(new BigDecimal(100));
        max = max.multiply(new BigDecimal(100));

        List<Integer> li = genRandList(totalMoney.intValue(), splitNum, min.intValue(), max.intValue(), thresh);
        List<BigDecimal> randomList = new CopyOnWriteArrayList<>();
        for (Integer v : li) {
            BigDecimal randomVlue = new BigDecimal(v).divide(new BigDecimal(100));
            randomList.add(randomVlue);
        }

        randomList = randomArrayList(randomList);
        return randomList;
    }

    /**
     * 根据总数分割个数及限定区间进行数据随机处理
     *
     * @param total - 被分割的总数
     * @param splitNum - 分割的个数
     * @param min - 单个数字下限
     * @param max - 单个数字上限
     * @param thresh - 数列浮动阀值[0.0, 1.0]
     */
    public static List<Integer> genRandList(int total, int splitNum, int min, int max, float thresh) {
        assert total >= splitNum * min && total <= splitNum * max : "请校验红包参数设置的合理性";
        assert thresh >= 0.0f && thresh <= 1.0f;
        // 平均分配
        int average = total / splitNum;
        List<Integer> list = new ArrayList<>(splitNum);
        int rest = total - average * splitNum;
        for (int i = 0; i < splitNum; i++) {
            if (i < rest) {
                list.add(average + 1);
            } else {
                list.add(average);
            }
        }
        // 如果浮动阀值为0则不进行数据随机处理
        if (thresh == 0) {
            return list;
        }
        // 根据阀值进行数据随机处理
        int randOfRange = 0;
        int randRom = 0;
        int nextIndex = 0;
        int nextValue = 0;
        int surplus = 0;//多余
        int lack = 0;//缺少
        for (int i = 0; i < splitNum - 1; i++) {
            nextIndex = i + 1;
            int itemThis = list.get(i);
            int itemNext = list.get(nextIndex);
            boolean isLt = itemThis < itemNext;
            int rangeThis = isLt ? max - itemThis : itemThis - min;
            int rangeNext = isLt ? itemNext - min : max - itemNext;
            int rangeFinal = (int) Math.ceil(thresh * (Math.min(rangeThis, rangeNext) + 100));
            randOfRange = random.nextInt(rangeFinal);
            randRom = isLt ? 1 : -1;
            int iValue = list.get(i) + randRom * randOfRange;
            nextValue = list.get(nextIndex) + randRom * randOfRange * -1;
            if (iValue > max) {
                surplus += (iValue - max);
                list.set(i, max);
            } else if (iValue < min) {
                list.set(i, min);
                lack += (min - iValue);
            } else {
                list.set(i, iValue);
            }
            list.set(nextIndex, nextValue);
        }
        if (nextValue > max) {
            surplus += (nextValue - max);
            list.set(nextIndex, max);
        }
        if (nextValue < min) {
            lack += (min - nextValue);
            list.set(nextIndex, min);
        }
        if (surplus - lack > 0) {//钱发少了 给低于max的凑到max
            for (int i = 0; i < list.size(); i++) {
                int value = list.get(i);
                if (value < max) {
                    int tmp = max - value;
                    if (surplus >= tmp) {
                        surplus -= tmp;
                        list.set(i, max);
                    } else {
                        list.set(i, value + surplus);
                        return list;
                    }
                }
            }
        } else if (lack - surplus > 0) {//钱发多了 给超过高于min的人凑到min
            for (int i = 0; i < list.size(); i++) {
                int value = list.get(i);
                if (value > min) {
                    int tmp = value - min;
                    if (lack >= tmp) {
                        lack -= tmp;
                        list.set(i, min);
                    } else {
                        list.set(i, min + tmp - lack);
                        return list;
                    }
                }
            }
        }
        return list;
    }

    /**
     * 打乱ArrayList
     */
    public static List<BigDecimal> randomArrayList(List<BigDecimal> sourceList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return sourceList;
        }
        List<BigDecimal> randomList = new CopyOnWriteArrayList<>();
        do {
            int randomIndex = Math.abs(new Random().nextInt(sourceList.size()));
            randomList.add(sourceList.remove(randomIndex));
        } while (sourceList.size() > 0);

        return randomList;
    }


    public static void main(String[] args) {

        //参考简书CoderZS https://www.jianshu.com/p/0174a026b9c9  总金额/红包个数<0.3  总金额/红包个数>0.5
        Long startTi = System.currentTimeMillis();
        List<BigDecimal> li = genRanddomList(new BigDecimal(1), 2, new BigDecimal(0.3), new BigDecimal(200),0.25f);
        li = randomArrayList(li);
        BigDecimal total = BigDecimal.ZERO;
//    System.out.println("======li=========li:"+li);
        System.out.println("======total=========total:" + total);
        System.out.println("======size=========size:" + li.size());
        Long endTi = System.currentTimeMillis();
        System.out.println("======耗时=========:" + (endTi - startTi) / 1000 + "秒");
    }
}

