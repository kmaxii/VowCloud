package me.kmaxi.vowcloud.utils;


public class LineFormatter {


    public static LineData formatToLineData(String message) {
        LineData lineData = new LineData();

        message = ChatHandler.extractMessage(message);
        message = message.trim();
        lineData.setRealLine(message);
        message = message.toLowerCase();
        message = message.replaceAll("[^abcdefghijklmnopqrstuvwxyz?.!0123456789/]", "");

        lineData.setSoundLine(message);
        return lineData;
    }

}
