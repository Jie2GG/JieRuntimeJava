package jie.runtime;

public class Main {
    public static void main(String[] args) {
//        System.out.println(BinaryConvert.toInt32(new byte[]{2}, 0, true));
//        System.out.println(BinaryConvert.toInt32(new byte[]{2}, 0, false));

//        System.out.println(BinaryConvert.toInt32(new byte[]{0, 0, 1, 0}, 0, true));
//        System.out.println(BinaryConvert.toInt32(new byte[]{0, 0, 1, 0}, 0, false));
//        System.out.println(BinaryConvert.toInt32(new byte[]{0, 0, 1, 0}, 2, true));
//        System.out.println(BinaryConvert.toInt32(new byte[]{0, 0, 1, 0}, 2, false));

        System.out.println(BinaryConvert.toInt32(new byte[]{0, 0, 1, 0, 1}, 2, true));
        System.out.println(BinaryConvert.toInt32(new byte[]{0, 0, 1, 0, 1}, 2, false));
    }
}