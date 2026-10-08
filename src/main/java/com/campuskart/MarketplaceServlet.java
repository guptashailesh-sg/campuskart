package com.campuskart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/marketplace")
public class MarketplaceServlet extends HttpServlet {
    protected void doGet(HttpServletRequest r,HttpServletResponse s) throws ServletException,IOException {
        HttpSession h=r.getSession(false);
        if(h==null||h.getAttribute("userId")==null){
            s.sendRedirect("login.jsp");
            return;
        }

        CleanupUtil.removeExpiredSoldListings();

        List<Map<String,Object>> items=new ArrayList<>();
        String q="SELECT p.id,p.name,p.price,p.category,p.item_condition,p.item_age,p.description,"
                +"u.name seller,u.student_id,u.email,u.department,u.class_year,u.division,u.contact "
                +"FROM products p JOIN users u ON p.seller_id=u.id "
                +"WHERE p.status='AVAILABLE' ORDER BY p.created_at DESC";

        try(Connection c=DBConnection.getConnection();
            PreparedStatement p=c.prepareStatement(q);
            ResultSet x=p.executeQuery()){

            while(x.next()){
                Map<String,Object> m=new HashMap<>();
                String[] a={"id","name","price","category","item_condition","item_age","description",
                            "seller","student_id","email","department","class_year","division","contact"};
                for(String k:a)m.put(k,x.getObject(k));
                items.add(m);
            }
        } catch(SQLException e){
            throw new ServletException(e);
        }

        r.setAttribute("products",items);
        r.getRequestDispatcher("/marketplace.jsp").forward(r,s);
    }
}