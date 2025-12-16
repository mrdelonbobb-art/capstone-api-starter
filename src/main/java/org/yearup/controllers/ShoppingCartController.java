package org.yearup.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController
{
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao,
                                  UserDao userDao,
                                  ProductDao productDao)
    {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // GET cart for logged-in user
    @GetMapping
    public ShoppingCart loadCart(Principal principal)
    {
        User user = userDao.getByUserName(principal.getName());
        return shoppingCartDao.getByUserId(user.getId());
    }

    // POST add product to cart
    @PostMapping("/products/{productId}")
    public void addProduct(@PathVariable int productId, Principal principal)
    {
        User user = userDao.getByUserName(principal.getName());
        shoppingCartDao.addProduct(user.getId(), productId);
    }

    // PUT update product quantity
    @PutMapping("/products/{productId}")
    public void updateProduct(@PathVariable int productId,
                              @RequestBody ShoppingCartItem item,
                              Principal principal)
    {
        User user = userDao.getByUserName(principal.getName());
        shoppingCartDao.updateProduct(user.getId(), productId, item.getQuantity());
    }

    // DELETE remove product from cart
    @DeleteMapping("/products/{productId}")
    public void removeProduct(@PathVariable int productId, Principal principal)
    {
        User user = userDao.getByUserName(principal.getName());
        shoppingCartDao.removeProduct(user.getId(), productId);
    }

    // DELETE clear cart
    @DeleteMapping
    public void clearCart(Principal principal)
    {
        User user = userDao.getByUserName(principal.getName());
        shoppingCartDao.clearCart(user.getId());
    }
}
