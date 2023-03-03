package application;

import Util.utils;
public class formatComment {
    private int model_type;
    private int line;
    private String comment;
    private String format_comment;

    public formatComment(String s,int i,int k){
        comment = s;
        //comment = s.replaceAll("(.{70,}?)\\s+", "$1\n") + "\n";
        String space = utils.getSpace(k);
        format_comment= space + s.replaceAll("(.{70,}?)\\s+", "$1\n"+space) + "\n";
        //单独加了一行\n
        line = utils.getLines(format_comment) - 1;
        model_type = i;
    }

    public int getLine() {
        return line;
    }

    public String getComment() {
        return comment;
    }

    public String getFormatComment() {
        return format_comment;
    }

}
