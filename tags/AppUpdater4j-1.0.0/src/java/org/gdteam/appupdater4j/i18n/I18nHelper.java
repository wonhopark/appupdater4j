package org.gdteam.appupdater4j.i18n;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18nHelper {

    private static I18nHelper instance = new I18nHelper();
    
    private Locale locale;
    private ResourceBundle resourceBundle;
    private DecimalFormatSymbols decimalFormatSymbols;
    private List<String> sizeSymbols;
    
    private I18nHelper() {
        this.setLocale(Locale.getDefault());
    }
    
    public void setLocale(Locale locale) {
        this.locale = locale;
        this.resourceBundle = ResourceBundle.getBundle("i18n", locale);
        this.decimalFormatSymbols = new DecimalFormatSymbols(locale);
        
        sizeSymbols = new ArrayList<String>();
        sizeSymbols.add(this.getString("size.b"));
        sizeSymbols.add(this.getString("size.kb"));
        sizeSymbols.add(this.getString("size.mb"));
        sizeSymbols.add(this.getString("size.gb"));
        
    }
    
    public String getString(String key) {
        return this.resourceBundle.getString(key);
    }

    public static I18nHelper getInstance() {
        return instance;
    }
    
    /**
     * Get human friendly size of length (ex: 3.4 MB)
     * @param length
     * @return
     */
    public String getHumanFriendlySize(double length) {
        return getHumanFriendlySizeRec(length, 0);
    }
    
    private String getHumanFriendlySizeRec(double length, int index) {
        DecimalFormat df = new DecimalFormat("#.#", this.decimalFormatSymbols);
        if (length <= 1024) {
            return df.format(length) + " " + this.sizeSymbols.get(index);
        } else {
            //length > 1024
            double newLength = length / 1024;
            if (this.sizeSymbols.size() > index) {
                //there are available sizes... Continue
                return getHumanFriendlySizeRec(newLength, index + 1);
            } else {
                //No avaialabe size.
                return df.format(newLength) + " " + this.sizeSymbols.get(index);
            }
        }
    }
    
    /**
     * Get countdown text from duration in milliseconds (ex: 1:23)
     * @param duration
     * @return The countdown text from duration in milliseconds (ex: 1:23)
     */
    public String getCountDownText(long duration) {
        long hour = duration / 3600;
        long min = (duration - (hour * 3600)) / 60;
        long sec = duration - (hour * 3600) - (min * 60);

        StringBuilder res = new StringBuilder();
        if (hour > 0){
            res.append(hour).append(":");
        }
        
        if (min < 10){
            res.append("0").append(min).append(":");
        } else {
            res.append(min).append(":");
        }
        
        if (sec < 10){
            res.append("0").append(sec).append(" s");
        } else {
            res.append(sec).append(" s");
        }
        
        return res.toString();
    }
    
    
}
