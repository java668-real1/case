package com.janey668;

import java.util.Stack;
import java.util.UUID;

public class UUIDTest {
    private static final char[] _UU64 = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz".toCharArray();
    // 打乱编码,必须使用本集合进行解码
    public static final char[] array = { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h',
            'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm', '8', '5', '2', '7', '3', '6', '4', '0', '9', '1', 'Q',
            'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C',
            'V', 'B', 'N', 'M', '+', '-' };
    public static void main(String[] args) {

        System.out.println(UUID.fromString("FF011766-17C5-45A2-82B3-0A45169F4071"));
    }


    /**
     * 编码,从10进制转换到64进制
     *
     * @param number
     *            long类型的10进制数,该数必须大于0
     * @return string类型的64进制数
     */
    public static String encode(long number) {
        Long rest = number;
        // 创建栈
        Stack<Character> stack = new Stack<Character>();
        StringBuilder result = new StringBuilder(0);
        while (rest >= 1) {
            // 进栈,
            // 也可以使用(rest - (rest / 64) * 64)作为求余算法
            stack.add(array[new Long(rest % 64).intValue()]);
            rest = rest / 64;
        }
        for (; !stack.isEmpty();) {
            // 出栈
            result.append(stack.pop());
        }
        return result.toString();

    }


    /**
     * 将紧凑格式的 UU16 字符串变成标准 UUID 格式的字符串
     *
     * @param uu16
     * @return 标准 UUID 字符串
     */
    public static String UU(String uu16) {
        StringBuilder sb = new StringBuilder();
        sb.append(uu16.substring(0, 8));
        sb.append('-');
        sb.append(uu16.substring(8, 12));
        sb.append('-');
        sb.append(uu16.substring(12, 16));
        sb.append('-');
        sb.append(uu16.substring(16, 20));
        sb.append('-');
        sb.append(uu16.substring(20));
        return sb.toString();
    }

    /**
     * @return 64进制表示的紧凑格式的 UUID
     */
    public static String UU64() {
        return UU64(java.util.UUID.randomUUID());
    }

    /**
            *
    返回一个 UUID ，并用 64进制转换成紧凑形式的字符串，内容为 [\\-0-9a-zA-Z_]
            * <p>
     *
    比如一个类似下面的 UUID:
            *
            * <pre>
     *a6c5c51c-689c-4525-9bcd-c14c1e107c80
     *一共 128位，
    分做L64 和
    R64，分为为两个 64位数（两个 long）
            *>L =uu.getLeastSignificantBits();
     *>UUID =uu.getMostSignificantBits();
     *而一个 64进制数，是 6位，因此我们取值的顺序是
     *1.从L64位取10次，每次取6位
     *2.从L64位取最后的4位 ＋R64位头2位拼上
     *3.从R64位取10次，每次取6位
     *4.剩下的两位最后取
     *这样，就能用一个 22长度的字符串表示一个 32长度的UUID，压缩了 1/3
            * </pre>
            *
            *
    @param
    uu UUID
    对象
     *@return 64
    进制表示的紧凑格式的 UUID
     */

    public static String UU64(java.util.UUID uu) {
        int index = 0;
        char[] cs = new char[22];
        long L = uu.getMostSignificantBits();
        long R = uu.getLeastSignificantBits();
        long mask = 63;
        // 从L64位取10次，每次取6位
        for (int off = 58; off >= 4; off -= 6) {
            long hex = (L & (mask << off)) >>> off;
            cs[index++] = _UU64[(int) hex];
        }
        // 从L64位取最后的4位 ＋ R64位头2位拼上
        int l = (int) (((L & 0xF) << 2) | ((R & (3 << 62)) >>> 62));
        cs[index++] = _UU64[l];
        // 从R64位取10次，每次取6位
        for (int off = 56; off >= 2; off -= 6) {
            long hex = (R & (mask << off)) >>> off;
            cs[index++] = _UU64[(int) hex];
        }
        // 剩下的两位最后取
        cs[index++] = _UU64[(int) (R & 3)];
        // 返回字符串
        return new String(cs);
    }
}
