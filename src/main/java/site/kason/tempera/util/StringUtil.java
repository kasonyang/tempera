package site.kason.tempera.util;
/**
 *
 * @author Kason Yang
 */
public class StringUtil {
    
    public static int calculateLineCount(int[] chars){
        int count = 1;
        for(int i=0;i<chars.length;i++){
            int c = chars[i];
            if(c=='\r'){
                count++;
                if(chars[i+1]=='\n') i++;
            }else if(c=='\n'){
                count++;
            }
        }
        return count;
    }

}
