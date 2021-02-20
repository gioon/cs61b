public class HelloNumbers {
    public static void main(String[] args) {
        int x = 0;
        int total = 0;
        while (x < 10) {
            total = total + x;
            System.out.print(total + " ");
            // System.out.print(total + ' ');
            // 不能用单引号，结果会是 32333538424753606877
            // 单引号是 char 类型，会将空格转为数字与 total 相加再 print
            // 双引号是 String 类型
            x = x + 1;
        }
        System.out.println();
        System.out.println(5 + "10"); // 510 python 会出错
        System.out.println(5 + 10); // 15
    }
}
