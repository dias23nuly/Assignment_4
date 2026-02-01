package service.interfaces;

public interface PricedItem {
    int getPriceKzt();

    default String formatPrice() {
        if (getPriceKzt() <= 0) return "FREE";
        return getPriceKzt() + " KZT";
    }

    static boolean isFree(int priceKzt) {
        return priceKzt <= 0;
    }
}
