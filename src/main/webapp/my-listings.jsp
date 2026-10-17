<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<% request.setCharacterEncoding("UTF-8"); response.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>CampusKart | My Listings</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<header class="topbar">
    <a class="logo" href="marketplace">Campus<span>Kart</span></a>
    <nav>
        <a href="marketplace">Marketplace</a>
        <a class="active" href="my-listings">My Listings</a>
        <span class="user"><span class="avatar" aria-hidden="true"><%= session.getAttribute("userName") == null || session.getAttribute("userName").toString().trim().isEmpty() ? "?" : session.getAttribute("userName").toString().trim().substring(0, 1).toUpperCase() %></span><%= session.getAttribute("userName") %></span>
        <a class="btn small ghost" href="logout">Logout</a>
    </nav>
</header>

<main class="container">
    <section class="page-title">
        <p class="eyebrow">YOUR CAMPUSKART</p>
        <h1>My listings</h1>
        <p class="muted">Manage the items you have posted and update their availability.</p>
    </section>

    <%
        List<Map<String, Object>> products =
            (List<Map<String, Object>>) request.getAttribute("products");

        if (products == null || products.isEmpty()) {
    %>
        <div class="empty">
            <h2>You haven't listed anything yet</h2>
            <p class="muted">Have a book, calculator, or other campus essential to pass on?</p>
            <a class="btn primary" href="marketplace">Browse marketplace</a>
        </div>
    <%
        } else {
    %>
        <div class="listing-list">
        <%
            for (Map<String, Object> product : products) {
                String productId = String.valueOf(product.get("id"));
                String status = String.valueOf(product.get("status"));
        %>
            <div class="my-listing">
                <img src="image?id=<%= productId %>" alt="Product image" loading="lazy">
                <div class="listing-info">
                    <span class="pill"><%= product.get("category") %></span>
                    <h3><%= product.get("name") %></h3>
                    <div class="price">&#8377;<%= product.get("price") %></div>
                    <p><%= product.get("item_condition") %> &#183; <%= product.get("item_age") %></p>
                    <p class="muted"><%= product.get("description") %></p>
                </div>
                <div class="listing-action">
                <%
                    if ("AVAILABLE".equals(status)) {
                %>
                    <span class="status available">● Available</span>
                    <form action="mark-sold" method="post"
                          onsubmit="return confirm('Mark this item as sold?');">
                        <input type="hidden" name="id" value="<%= productId %>">
                        <button class="btn danger" type="submit">Mark as sold</button>
                    </form>
                <%
                    } else {
                %>
                    <span class="status sold">● Sold</span>
                    <small>Sold listings are automatically removed after 48 hours.</small>
                <%
                    }
                %>
                </div>
            </div>
        <%
            }
        %>
        </div>
    <%
        }
    %>
</main>
</body>
</html>
