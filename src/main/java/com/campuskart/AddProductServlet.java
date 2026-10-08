package com.campuskart;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/add-product")
@MultipartConfig(maxFileSize=5*1024*1024,maxRequestSize=6*1024*1024)
public class AddProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException {
        HttpSession h=r.getSession(false);
        if(h==null || h.getAttribute("userId")==null){s.sendRedirect("login.jsp");return;}

        String n=v(r,"name"),cat=v(r,"category"),cond=v(r,"condition"),age=v(r,"age"),
               desc=v(r,"description"),pt=v(r,"price");
        Part img=r.getPart("image");

        try {
            BigDecimal price=new BigDecimal(pt);
            if(price.signum()<=0||n.isBlank()||cat.isBlank()||cond.isBlank()||age.isBlank()
                    ||desc.isBlank()||img==null||img.getSize()==0){
                s.sendRedirect("marketplace?error=Please+complete+all+product+fields");
                return;
            }

            String type=img.getContentType();
            if(type==null||!type.startsWith("image/")){
                s.sendRedirect("marketplace?error=Please+upload+an+image");
                return;
            }

            try(Connection c=DBConnection.getConnection();
                PreparedStatement p=c.prepareStatement(
                    "INSERT INTO products(seller_id,name,price,category,item_condition,item_age,description,image_data,image_type) VALUES(?,?,?,?,?,?,?,?,?)")){
                p.setInt(1,(Integer)h.getAttribute("userId"));
                p.setString(2,n);
                p.setBigDecimal(3,price);
                p.setString(4,cat);
                p.setString(5,cond);
                p.setString(6,age);
                p.setString(7,desc);
                p.setBinaryStream(8,img.getInputStream());
                p.setString(9,type);
                p.executeUpdate();
            }
            s.sendRedirect("marketplace?success=Item+listed");
        } catch(Exception e) {
            throw new IOException(e);
        }
    }

    private String v(HttpServletRequest r,String n){
        String x=r.getParameter(n);
        return x==null?"":x.trim();
    }
}