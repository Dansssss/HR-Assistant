package test;

public class demo {
    public static void main(String[] args) {
        String A=" 1666846254925,123,23,,, ";
        String[] split = A.split(",");
        for (String a:
             split) {
            System.out.println(a);
        }
        System.out.println(split.length);
    }
}
