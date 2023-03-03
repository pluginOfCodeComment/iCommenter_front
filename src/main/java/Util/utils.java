package Util;

import java.util.Objects;

public class utils {
    public static int getTsize(String text){
        if(Objects.equals(text, "")){
            return -1;
        }
        int sum = 0;
        for(int i = 0; text.charAt(i) == 32 && i < text.length(); i++){
            sum++;
        }
        return sum / 4;
    }

    public static int getLines(String text){
        if(text == null || text.isEmpty()){
            return 0;
        }
        int lines = 1;
        int pos = 0;
        while ((pos = text.indexOf("\n", pos) + 1) != 0) {
            lines++;
        }
        return lines;
    }

    public static String getSpace(int k){
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < k * 4; i++){
            stringBuffer.append(' ');
        }
        return stringBuffer.toString();
    }
}
