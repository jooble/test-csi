package ru.jooble.test_csi;

import ru.jooble.test_csi.model.Price;

import java.util.*;
import java.util.stream.Collectors;

public class PriceHelper {

    public List<Price> combiningPrices(List<Price> oldPrices, List<Price> newPrices) {
        List<Price> combinedPrices = new ArrayList<>();
        List<Price> copyOldPrices = new ArrayList<>(oldPrices);

        combinedPrices.addAll(getPricesWithoutIntersect(copyOldPrices, newPrices));

        copyOldPrices.sort(Comparator.comparing(Price::getBegin));

        for (Price newPrice : newPrices) {

            List<Price> cleans = getAllIntersectPrice(newPrice, copyOldPrices);
            removeFullIntersect(newPrice, cleans);
            removeFullIntersect(newPrice, combinedPrices);

            if (cleans.size() == 0) {
                combinedPrices.add(newPrice);

            } else if (cleans.size() == 1) {
                Price p = cleans.get(0);

                if (p.getValue() == newPrice.getValue()) {
                    p.setEnd(newPrice.getEnd());
                    combinedPrices.add(p);

                } else {
                    if (p.getBegin().compareTo(newPrice.getBegin()) < 0
                            && p.getEnd().compareTo(newPrice.getEnd()) > 0) {

                        Price right = cloneWithoutDates(p);

                        right.setBegin(newPrice.getEnd());
                        right.setEnd(p.getEnd());
                        p.setEnd(newPrice.getBegin());

                        Collections.addAll(combinedPrices, p, newPrice, right);

                        copyOldPrices.add(right);
                        copyOldPrices.sort(Comparator.comparing(Price::getBegin));
                    } else {
                        if (newPrice.getBegin().compareTo(p.getBegin()) <= 0) {
                            p.setBegin(newPrice.getEnd());
                        } else {
                            p.setEnd(newPrice.getBegin());
                        }

                        Collections.addAll(combinedPrices, p, newPrice);
                    }
                }

            } else if (cleans.size() == 2) {
                Price left = cleans.get(0);
                Price right = cleans.get(1);

                if (left.getValue() == newPrice.getValue()) {
                    left.setEnd(newPrice.getEnd());
                    right.setBegin(newPrice.getEnd());

                    Collections.addAll(combinedPrices, left, right);
                } else {
                    left.setEnd(newPrice.getBegin());
                    right.setBegin(newPrice.getEnd());

                    Collections.addAll(combinedPrices, left, newPrice, right);
                }
            }

            cleans.clear();
        }

        return new ArrayList<>(combinedPrices
                .stream()
                .filter(price -> price.getBegin().compareTo(price.getEnd()) != 0)
                .collect(Collectors.toSet()));
    }

    private List<Price> getAllIntersectPrice(Price price, List<Price> prices) {
        return prices
                .stream()
                .filter(p -> isIntersect(price, p))
                .collect(Collectors.toList());
    }

    private List<Price> getPricesWithoutIntersect(List<Price> oldPrices, List<Price> newPrices) {
        return oldPrices
                .stream()
                .filter(p1 -> {
                    for (Price p2 : newPrices) {
                        if (isIntersect(p1, p2)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    private boolean isIntersect(Price p1, Price p2) {
        if (p1.getProductCode().equals(p2.getProductCode())
                && p1.getNumber() == p2.getNumber()
                && p1.getDepart() == p2.getDepart()) {

            return !(p1.getBegin().after(p2.getEnd())
                    || p1.getEnd().before(p2.getBegin()));

        }
        return false;
    }

    private void removeFullIntersect(Price price, List<Price> prices) {
        prices.removeIf(p -> {
            if (price.getBegin().compareTo(p.getBegin()) <= 0
                    && price.getEnd().compareTo(p.getEnd()) >= 0) {
                return true;
            }

            return false;
        });
    }

    private Price cloneWithoutDates(Price price) {
        Price newPrice = new Price();

        newPrice.setNumber(price.getNumber());
        newPrice.setProductCode(price.getProductCode());
        newPrice.setDepart(price.getDepart());
        newPrice.setValue(price.getValue());

        return newPrice;
    }
}
