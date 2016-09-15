package br.com.livroandroid.buscapreco.Utils;

import android.content.Context;
import android.content.res.TypedArray;

import br.com.livroandroid.buscapreco.R;

public class Utils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
    }

    public static String encodeString(String palavra) {
        char one;
        StringBuffer n = new StringBuffer(palavra.length());
        for (int i = 0; i < palavra.length(); i++) {
            one = palavra.charAt(i);
            switch (one) {
                case ' ':
                    n.append('%');
                    n.append('2');
                    n.append('0');
                    break;
                default:
                    n.append(one);
            }
        }
        return n.toString();
    }
}
