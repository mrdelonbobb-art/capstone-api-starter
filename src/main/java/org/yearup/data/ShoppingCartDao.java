package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);


    // add additional method signatures here
    void clearCart(int id);

    void addProduct(int id, int productId);

    void updateProduct(int id, int productId, int quantity);

    void removeProduct(int id, int productId);
}
