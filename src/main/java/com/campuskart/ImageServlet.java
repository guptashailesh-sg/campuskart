package com.campuskart;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rawId = request.getParameter("id");
        if (rawId == null || !rawId.trim().matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid product ID");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(rawId.trim());
        } catch (NumberFormatException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
            return;
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT image_data, image_type FROM products WHERE id = ?")) {
            statement.setInt(1, productId);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product image not found");
                    return;
                }

                String contentType = result.getString("image_type");
                if (contentType == null || !contentType.startsWith("image/")) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product image type is unavailable");
                    return;
                }

                response.setContentType(contentType);
                try (InputStream input = result.getBinaryStream("image_data");
                     OutputStream output = response.getOutputStream()) {
                    if (input == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product image is empty");
                        return;
                    }
                    byte[] buffer = new byte[8192];
                    int count;
                    while ((count = input.read(buffer)) != -1) {
                        output.write(buffer, 0, count);
                    }
                }
            }
        } catch (SQLException ex) {
            log("Unable to load image for product ID " + productId, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load product image");
        }
    }
}