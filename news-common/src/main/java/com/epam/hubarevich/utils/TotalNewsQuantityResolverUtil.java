package com.epam.hubarevich.utils;

import com.epam.hubarevich.service.exception.LogicException;

/**
 * Util class creates int [] massive for pagination on jsp
 */

public class TotalNewsQuantityResolverUtil {

    public static int[] getTotalPagesQuantity(int newsQuantity) throws LogicException {

        int pagesQuantity = 0;
        if (newsQuantity % 5 > 0) {
            pagesQuantity = newsQuantity / 5 + 1;
        } else {

            pagesQuantity = newsQuantity / 5;
        }
        int[] array = new int[pagesQuantity];
        for (int i = 1; i <= pagesQuantity; i++) {
            array[i - 1] = i;
        }
        return array;
    }

}
