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
}
