/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import database.DBUtil;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Role;
import models.User;
import services.AccountService;
import services.UserService;

/**
 *
 * @author awarsyle
 */
public class TypeofUsersFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        try {
            // this code will execute before HomeServlet and UsersServlet
            HttpServletRequest r = (HttpServletRequest)request;
            HttpSession session = r.getSession();
            String username = (String) session.getAttribute("username");
            UserService us = new UserService();
            EntityManager em = DBUtil.getEmFactory().createEntityManager();
            User user = us.get(username);
            Role role = em.find(Role.class, 1);//admin 
            
            if (user.getRole().equals(role)) {//IF ADMIN
                // if they are authenticated, i.e. have a username in the session,
                // then allow them to continue on to the servlet
                chain.doFilter(request, response);
            } else {
                // they do not have a username in the sesion
                // so, send them to login page
                HttpServletResponse resp = (HttpServletResponse)response;
                resp.sendRedirect("home");
            }
            
            // this code will execute after HomeServlet and UsersServlet
        } catch (Exception ex) {
            Logger.getLogger(TypeofUsersFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void destroy() {
        
    }

    
}
