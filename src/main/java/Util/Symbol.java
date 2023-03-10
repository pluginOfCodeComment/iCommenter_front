package Util;

public class Symbol {
    private static final String format1 = "\"\"\"\n";
    private static final String format2 = "'''\n";
    private static final String format3 = "#";

    public static String get(int index){
        return index == 0 ? format1 : index == 1 ? format2 : format3;
    }
    public static int length(int index){
        return index == 2 ? 1 : 3;
    }
}
