package com.example.itstore.utils;
import com.example.itstore.model.CartItem;
import java.util.ArrayList;
import java.util.List;
public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartList;

    private CartManager() {
        cartList = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartItem> getCartList() {
        return cartList;
    }
    public void addToCart(CartItem newItem) {
        boolean isExist = false;
        for (CartItem item : cartList) {
            if (item.getProduct().getId() == newItem.getProduct().getId() &&
                    item.getVariantId() == newItem.getVariantId()) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            cartList.add(newItem);
        }
    }
}
