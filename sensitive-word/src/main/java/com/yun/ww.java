package com.yun;



import java.util.ArrayList;

/**
 * @Project: sensitive-word
 * @Description:
 * @Author: wudr
 * @Date: 2019/3/27 18:07
 */
public class ww {

    public static void main(String[] args) {
////        StringBuffer stringBuffer = new StringBuffer();
////        for (int i = 0; i < text.length(); i++) {
////            stringBuffer.append(ToSenseWord(text.charAt(i)));
////            //stringBuilder[i] = ToSenseWord(text[i]);
////        }
        System.out.println("123");
        System.out.println("Integer:"+ MySizeOf.sizeOf(new Integer(1)));
        System.out.println("ArrayList:"+ MySizeOf.sizeOf(new ArrayList<>(1024*1024)));
    }

    private static char ToSenseWord(char c)
    {
        if (c >= 'A' && c <= 'Z') return (char)(c | 0x20);
        if (true) {
            if (c == 12288) return ' ';
            if (c >= 65280 && c < 65375) {
                int k = (c - 65248);
                if ('A' <= k && k <= 'Z') {
                    k = k | 0x20;
                }
                return (char)k;
            }
        }
        if (true) {
            if (c >= 0x4e00 && c <= 0x9fa5) {
//                return Dict.Simplified[c - 0x4e00];
            }
        }
        return c;
    }
}
