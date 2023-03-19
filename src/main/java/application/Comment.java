package application;

import Util.Tools;

public class Comment {
    private int line;
    private String comment;
    private String format_comment;

    public Comment(String s, int k){
        comment = s;
        //comment = s.replaceAll("(.{70,}?)\\s+", "$1\n") + "\n";
        String space = Tools.getSpace(k);
        format_comment= space + s.replaceAll("(.{70,}?)\\s+", "$1\n"+space) + "\n";

        //单独加了一行\n
        line = Tools.getLines(format_comment) - 1;
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
