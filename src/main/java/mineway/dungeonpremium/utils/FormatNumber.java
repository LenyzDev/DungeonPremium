package mineway.dungeonpremium.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FormatNumber {

    private double value;
    private int decimalsQuantity;
    private boolean limited;

    public FormatNumber(int value, int decimalsQuantity, boolean limited){
        this.value = value;
        this.decimalsQuantity = decimalsQuantity;
        this.limited = limited;
    }

    public FormatNumber(double value, int decimalsQuantity, boolean limited){
        this.value = value;
        this.decimalsQuantity = decimalsQuantity;
        this.limited = limited;
    }

    public String build(){
        if(limited && value > 999999999){
            return buildWithLetters();
        }
        return String.format(Locale.GERMAN, "%,."+decimalsQuantity+"f", Double.parseDouble(String.valueOf(value)));
    }

    public String buildWithLetters(){
        List<String> suffixes = Arrays.asList("", "K", "M", "B", "T", "Q", "QQ", "S", "SP", "O", "N", "D");
        int index = 0;

        double tmp;
        while ((tmp = value / 1000) >= 1) {
            value = tmp;
            ++index;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value) + suffixes.get(index);
    }
}
