package me.kmaxi.vowcloud.utils;


public class LineFormatter {


    public static LineData formatToLineData(String message) {
        LineData lineData = new LineData();

        message = ChatHandler.extractMessage(message);
        message = message.trim();
        lineData.setRealLine(message);

       // System.out.println("Real line: " + message);
        message = Utils.HTTPEncode(message);
       // System.out.println("Encoded line: " + message);
        //Decode the message and print it out
    //    String decoded = Utils.HTTPDecode(message);
     //   System.out.println("Decoded line: " + decoded);


        message = message.toLowerCase();
        message = message.replaceAll("[^abcdefghijklmnopqrstuvwxyz?.!0123456789/]", "");

        lineData.setSoundLine(message);
        return lineData;
    }

}
