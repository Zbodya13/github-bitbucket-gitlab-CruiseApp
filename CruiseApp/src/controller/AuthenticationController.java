package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.DAOUser;
import model.User;

/**
 * This class provide users authentication.
 */

@WebServlet("/auth")
public class AuthenticationController extends HttpServlet {
	private static final long serialVersionUID = 1L;	
	private static String LIST_SHIP_EN = "/admin/adminShip?action=list&sessionLocale=en";
	private static String LIST_SHIP_UA = "/admin/adminShip?action=list&sessionLocale=ua";
	private static String CUSTOM_UA = "/customUser?action=listShips&sessionLocale=ua";
	private static String CUSTOM_EN = "/customUser?action=listShips&sessionLocale=en";
	private static String AUTH = "/view/authForm.jsp";
	private DAOUser daoUser;
    

    public AuthenticationController() 
    {
        super();
        daoUser = new DAOUser();
        
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{	
		request.getSession().setAttribute("message", "null");
		request.getSession().setAttribute("errorMessage", "null");
		request.getSession().setAttribute("error", "null");
		if(request.getSession().getAttribute("sessionLocale")==null) 
		{
			request.getSession().setAttribute("sessionLocale", "ua");		
		}
		String login =  request.getParameter("login");
		String password = request.getParameter("password");				
		User user = daoUser.getByID(login,"en");		
		if(password.equals(user.getPassword()) && user.getRole().equals("admin"))
			{				
				request.getSession().setAttribute("user", user);		
				if(request.getSession().getAttribute("sessionLocale").equals("en")) 
				{				
				response.sendRedirect(request.getContextPath() + LIST_SHIP_EN);
				}else if(request.getSession().getAttribute("sessionLocale").equals("ua")) 
				{				
				response.sendRedirect(request.getContextPath() + LIST_SHIP_UA);
				}				
			}
		else if(password.equals(user.getPassword()) && user.getRole().equals("custom")) 
			{
				request.getSession().setAttribute("user", user);
				if(request.getSession().getAttribute("sessionLocale").equals("en")) 
				{				
				response.sendRedirect(request.getContextPath() + CUSTOM_EN);
				}else if(request.getSession().getAttribute("sessionLocale").equals("ua")) 
				{				
				response.sendRedirect(request.getContextPath() + CUSTOM_UA);
				}
			}
		else 
			{	
				request.getSession().setAttribute("errorMessage", "invalid_form");			
				request.getRequestDispatcher("view/authForm.jsp").forward(request, response);
			}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		if(req.getParameter("action").equals("first")) 
		{			
			req.getSession().setAttribute("message", "null");
			req.getSession().setAttribute("errorMessage", "null");
			req.getRequestDispatcher(AUTH).forward(req, resp);			
		}
		else if(req.getParameter("action").equals("error"))
		{
		req.getRequestDispatcher(AUTH).forward(req, resp);
		req.getSession().setAttribute("message", "null");
		req.getSession().setAttribute("errorMessage", "null");	
		}
	}



}
