package ru.jooble.test_csi;

import org.junit.Test;
import ru.jooble.test_csi.model.Price;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PriceHelperTest {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private PriceHelper priceHelper = new PriceHelper();


    @Test
    public void testCombiningPrices() throws ParseException {
        List<Price> oldPrices = new ArrayList<>();
        oldPrices.add(new Price("122856", 1, 1,
                sdf.parse("01.01.2013 00:00:00"), sdf.parse("31.01.2013 23:59:59"), 11000));
        oldPrices.add(new Price("122856", 2, 1,
                sdf.parse("10.01.2013 00:00:00"), sdf.parse("20.01.2013 23:59:59"), 99000));
        oldPrices.add(new Price("6654", 1, 2,
                sdf.parse("01.01.2013 00:00:00"), sdf.parse("31.01.2013 00:00:00"), 5000));

        List<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price("122856", 1, 1,
                sdf.parse("20.01.2013 00:00:00"), sdf.parse("20.02.2013 23:59:59"), 11000));
        newPrices.add(new Price("122856", 2, 1,
                sdf.parse("15.01.2013 00:00:00"), sdf.parse("25.01.2013 23:59:59"), 92000));
        newPrices.add(new Price("6654", 1, 2,
                sdf.parse("12.01.2013 00:00:00"), sdf.parse("13.01.2013 00:00:00"), 4000));

        List<Price> result = priceHelper.combiningPrices(oldPrices, newPrices);

        assertEquals(6, result.size());

        assertTrue(result.contains(new Price("122856", 1, 1,
                sdf.parse("01.01.2013 00:00:00"), sdf.parse("20.02.2013 23:59:59"), 11000)));

        assertTrue(result.contains(new Price("122856", 2, 1,
                sdf.parse("10.01.2013 00:00:00"), sdf.parse("15.01.2013 00:00:00"), 99000)));

        assertTrue(result.contains(new Price("122856", 2, 1,
                sdf.parse("15.01.2013 00:00:00"), sdf.parse("25.01.2013 23:59:59"), 92000)));

        newPrices.add(new Price("6654", 1, 2,
                sdf.parse("01.01.2013 00:00:00"), sdf.parse("12.01.2013 00:00:00"), 5000));

        newPrices.add(new Price("6654", 1, 2,
                sdf.parse("12.01.2013 00:00:00"), sdf.parse("13.01.2013 00:00:00"), 4000));

        newPrices.add(new Price("6654", 1, 2,
                sdf.parse("13.01.2013 00:00:00"), sdf.parse("31.01.2013 00:00:00"), 5000));
    }

    @Test
    public void testCombiningPrices2() throws ParseException {
        List<Price> oldPrices = new ArrayList<>();

        oldPrices.add(new Price("12345", 1, 1,
                sdf.parse("01.01.2013 00:00:00"), sdf.parse("31.01.2013 23:59:59"), 50));

        List<Price> newPrices = new ArrayList<>();

        newPrices.add(new Price("12345", 1, 1,
                sdf.parse("08.01.2013 00:00:00"), sdf.parse("14.01.2013 23:59:59"), 60));

        List<Price> result = priceHelper.combiningPrices(oldPrices, newPrices);

        assertEquals(3, result.size());

        assertTrue(result.contains(new Price("12345", 1, 1,
                sdf.parse("01.01.2013 00:00:00"), sdf.parse("08.01.2013 00:00:00"), 50)));

        assertTrue(result.contains(new Price("12345", 1, 1,
                sdf.parse("08.01.2013 00:00:00"), sdf.parse("14.01.2013 23:59:59"), 60)));

        assertTrue(result.contains(new Price("12345", 1, 1,
                sdf.parse("14.01.2013 23:59:59"), sdf.parse("31.01.2013 23:59:59"), 50)));
    }

    @Test
    public void testCombiningPrices3() throws ParseException {
        List<Price> oldPrices = new ArrayList<>();

        oldPrices.add(new Price("12345", 1, 1,
                sdf.parse("01.01.2013 00:00:00"), sdf.parse("15.01.2013 23:59:59"), 100));
        oldPrices.add(new Price("12345", 1, 1,
                sdf.parse("15.01.2013 00:00:00"), sdf.parse("31.01.2013 23:59:59"), 120));

        List<Price> newPrices = new ArrayList<>();

        newPrices.add(new Price("12345", 1, 1,
                sdf.parse("11.01.2013 00:00:00"), sdf.parse("17.01.2013 23:59:59"), 110));

        List<Price> result = priceHelper.combiningPrices(oldPrices, newPrices);

        assertEquals(3, result.size());

        assertTrue(result.contains(new Price("12345", 1, 1,
                sdf.parse("01.01.2013 00:00:00"), sdf.parse("11.01.2013 00:00:00"), 100)));

        assertTrue(result.contains(new Price("12345", 1, 1,
                sdf.parse("11.01.2013 00:00:00"), sdf.parse("17.01.2013 23:59:59"), 110)));

        assertTrue(result.contains(new Price("12345", 1, 1,
                sdf.parse("17.01.2013 23:59:59"), sdf.parse("31.01.2013 23:59:59"), 120)));
    }

    @Test
    public void testCombiningPrices4() throws ParseException {
        List<Price> oldPrices = new ArrayList<>();

        oldPrices.add(new Price("12345", 1, 1,
                sdf.parse("01.01.2013 00:00:00"), sdf.parse("10.01.2013 23:59:59"), 80));
        oldPrices.add(new Price("12345", 1, 1,
                sdf.parse("10.01.2013 00:00:00"), sdf.parse("20.01.2013 23:59:59"), 87));
        oldPrices.add(new Price("12345", 1, 1,
                sdf.parse("20.01.2013 00:00:00"), sdf.parse("31.01.2013 23:59:59"), 90));

        List<Price> newPrices = new ArrayList<>();

        newPrices.add(new Price("12345", 1, 1,
                sdf.parse("08.01.2013 00:00:00"), sdf.parse("15.01.2013 23:59:59"), 80));
        newPrices.add(new Price("12345", 1, 1,
                sdf.parse("15.01.2013 23:59:59"), sdf.parse("28.01.2013 23:59:59"), 85));

        List<Price> result = priceHelper.combiningPrices(oldPrices, newPrices);

        assertEquals(3, result.size());

        assertTrue(result.contains(new Price("12345", 1, 1,
                sdf.parse("01.01.2013 00:00:00"), sdf.parse("15.01.2013 23:59:59"), 80)));

        assertTrue(result.contains(new Price("12345", 1, 1,
                sdf.parse("15.01.2013 23:59:59"), sdf.parse("28.01.2013 23:59:59"), 85)));

        assertTrue(result.contains(new Price("12345", 1, 1,
                sdf.parse("28.01.2013 23:59:59"), sdf.parse("31.01.2013 23:59:59"), 90)));
    }
}
