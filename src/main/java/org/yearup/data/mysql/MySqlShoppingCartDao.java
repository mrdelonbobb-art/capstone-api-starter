package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao
{
    public MySqlShoppingCartDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        ShoppingCart cart = new ShoppingCart();
        List<ShoppingCartItem> items = new ArrayList<>();

        String sql = "SELECT sci.product_id, sci.quantity, p.name, p.price " +
                "FROM shopping_cart_items sci " +
                "JOIN products p ON sci.product_id = p.id " +
                "WHERE sci.user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));

                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(rs.getInt("quantity"));

                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        cart.setItems((Map<Integer, ShoppingCartItem>) items);
        return cart;
    }

    @Override
    public void clearCart(int userId)
    {
        String sql = "DELETE FROM shopping_cart_items WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void addProduct(int userId, int productId)
    {
        // If product already exists in cart, increment quantity
        String selectSql = "SELECT quantity FROM shopping_cart_items WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO shopping_cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE shopping_cart_items SET quantity = quantity + 1 WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection())
        {
            // check if exists
            try (PreparedStatement selectStmt = connection.prepareStatement(selectSql))
            {
                selectStmt.setInt(1, userId);
                selectStmt.setInt(2, productId);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next())
                {
                    // update quantity
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSql))
                    {
                        updateStmt.setInt(1, userId);
                        updateStmt.setInt(2, productId);
                        updateStmt.executeUpdate();
                    }
                }
                else
                {
                    // insert new row
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertSql))
                    {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, productId);
                        insertStmt.setInt(3, 1);
                        insertStmt.executeUpdate();
                    }
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(int userId, int productId, int quantity)
    {
        String sql = "UPDATE shopping_cart_items SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, quantity);
            stmt.setInt(2, userId);
            stmt.setInt(3, productId);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void removeProduct(int id, int productId) {

    }
}
