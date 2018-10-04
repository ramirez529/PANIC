package com.avaya.AAAMIADBWebService;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.avaya.collaboration.util.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * 
 * This class is needed if you are trying to make your application accessible through a HTTP servlet. 
 * Refer DynamicTeamFormation Sample service to understand more about how to use this.
 * 
 * For applications which provide call related features only and web service is not required, remove this class.
 * 
 * 
 * Servlet implementation class HelloServlet
 * 
 */
@WebServlet("/customer")
public class customer extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
    private final Logger logger;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public customer() 
    {
        super();
        logger = Logger.getLogger(getClass());
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 *  GET https://135.169.18.7/services/AAADEVURIELPrueba5/customer
     * output Json {"accountnum":1303,"email":"urielmansilla@avaya.com","firstname":"Uriel","lastname":"Mansi","phone":"55598765434","preference":"sms"}
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		/*CONEXIÓN A POSTGRES*/
			response.getWriter().println();
			String JDBC_DRIVER = "org.postgresql.Driver";
	        String url = "jdbc:postgresql://10.0.0.12:5432/postgres";
	        Properties props = new Properties();
	        props.setProperty("user", "postgres");
	        props.setProperty("password", "postgres");


	        PrintWriter out = response.getWriter();
	        Connection conn = null;
	        /*SELECCION DEL DRIVER POSTGRES*/
	        try {
	            Class<?> jdbcDriverClass = Class.forName(JDBC_DRIVER);
	            Driver driver = (Driver) jdbcDriverClass.newInstance();
	            DriverManager.registerDriver(driver);
	        } catch (Exception e) {
	        	logger.info("customer: l-74 Error: "+e.getMessage());
	        	/*RESPUESSTA ERROR EN FORMATO JSON*/
	        	JsonObject error = new JsonObject();
	            error.addProperty("Status", "Error");
	            error.addProperty("Error", e.getMessage());
	            out.println(error);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            e.printStackTrace();
	        }
	        /*LA VARIABLE CONN REALIZA LA CONEXION A LA BD*/
			try {
	            conn = DriverManager.getConnection(url, props);
	            logger.info("MySwrvlet l-83: Conexión realizada a BD");

			}catch (SQLException ex) {
				logger.info("customer: l-87 Error: "+ex.getMessage());
		    	out.println(ex.getMessage());
		        System.out.println(ex.getMessage());
		    }
			/*EVALÚA SI SE ENVÍA UN PARAMETRO EN ESTE CASO SÍ ENVÍA UN PARAMETRO*/
			if(request.getParameter("accountnum") != null){
				String accountnum_string = request.getParameter("accountnum");
				int accountnum = Integer.parseInt(accountnum_string);
				/*QUERY PARA EL STATEMENT*/
		        String SQL = "SELECT * FROM postgres.public.customer WHERE accountnum = "+accountnum+";";
		        try (Statement stmt = conn.createStatement();
		                ResultSet rs = stmt.executeQuery(SQL)) {
		            List<CustomerBean> list = new LinkedList<>();
		            while (rs.next()) {
		            	CustomerBean user = new CustomerBean();
		                user.accountnum = rs.getInt("accountnum");
		                user.email = rs.getString("email");
		                user.firstname = rs.getString("firstname");
		                user.lastname = rs.getString("lastname");
		                user.phone = rs.getString("phone");
		                user.preference = rs.getString("preference");
		                list.add(user);
		            }
		            /*RESPUESTA EN JSON Y SE CIERRA LA CONEXION*/
		            Gson jsonGson = new GsonBuilder().setPrettyPrinting().create();
		            out.println(jsonGson.toJson(list));
		            conn.close();
		            stmt.close();
		            rs.close();
		        } catch (SQLException ex) {
		        	logger.info("customer: l-116 Error: "+ex.getMessage());
		        	/*RESPUESSTA ERROR EN FORMATO JSON*/
		        	JsonObject error = new JsonObject();
		            error.addProperty("Status", "Error");
		            error.addProperty("Error", ex.getMessage());
		            out.println(error);
		            response.setStatus(404);
		            response.setContentType("application/json");
		            response.setCharacterEncoding("UTF-8");
		        }
			}
			/*EVALÚA SI SE ENVÍA UN PARAMETRO EN ESTE CASO NO SE ENVÏA PARÁMETRO*/
			if(request.getParameter("accountnum") == null){
			/*QUERY PARA EL STATEMENT*/
			String SQL = "SELECT * FROM postgres.public.customer;";
	        try (Statement stmt = conn.createStatement();
	                ResultSet rs = stmt.executeQuery(SQL)) {
	            List<CustomerBean> list = new LinkedList<>();
	            while (rs.next()) {
	            	CustomerBean user = new CustomerBean();
	                user.accountnum = rs.getInt("accountnum");
	                user.email = rs.getString("email");
	                user.firstname = rs.getString("firstname");
	                user.lastname = rs.getString("lastname");
	                user.phone = rs.getString("phone");
	                user.preference = rs.getString("preference");
	                list.add(user);
	            }
	            /*RESPUESTA EN JSON Y SE CIERRA LA CONEXION*/
	            Gson jsonGson = new GsonBuilder().setPrettyPrinting().create();
	            out.println(jsonGson.toJson(list));
	            conn.close();
	            stmt.close();
	            rs.close();
	        } catch (SQLException ex) {
	        	logger.info("customer: l-116 Error: "+ex.getMessage());
	        	/*RESPUESSTA ERROR EN FORMATO JSON*/
	        	JsonObject error = new JsonObject();
	            error.addProperty("Status", "Error");
	            error.addProperty("Error", ex.getMessage());
	            out.println(error);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	        }
			}

	}

//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 * POST https://135.169.18.7/services/AAADEVURIELPrueba5/customer
//   * input Json {"accountnum":1303,"email":"urielmansilla@avaya.com","firstname":"Uriel","lastname":"Mansi","phone":"55598765434","preference":"sms"}
//	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
			logger.info("Ingresando al método POST");


			String JDBC_DRIVER = "org.postgresql.Driver";
	        String url = "jdbc:postgresql://10.0.0.12:5432/postgres";
	        Properties props = new Properties();
	        props.setProperty("user", "postgres");
	        props.setProperty("password", "postgres");
	        
	        Driver driver = null;
	        Connection conn = null;
	        long id = 0;
	        
	        PrintWriter out = response.getWriter();
	        out.println("Método POST");
	        
	        try {
	            Class jdbcDriverClass = Class.forName(JDBC_DRIVER);
	            driver = (Driver) jdbcDriverClass.newInstance();
	            DriverManager.registerDriver(driver);
	        } catch (Exception e) {
	        	logger.info("customer: l-148 Error: "+e.getMessage());
	            e.printStackTrace();
	            /*RESPUESSTA ERROR EN FORMATO JSON*/
	            JsonObject error = new JsonObject();
	            error.addProperty("Status", "Error");
	            error.addProperty("Error", e.getMessage());
	            out.println(error);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	        }
	        try {
	            conn = DriverManager.getConnection(url, props);
	            logger.info("customer: l-142 Conexión a base de datos completada");
	        } catch (SQLException e) {
	        	logger.info("customer: l-159 Error: "+e.getMessage());
	        	/*RESPUESSTA ERROR EN FORMATO JSON*/
	        	JsonObject error = new JsonObject();
	            error.addProperty("Status", "Error");
	            error.addProperty("Error", e.getMessage());
	            out.println(error);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");	
	        }
	        /* OBTENIENDO JSON*/
	        BufferedReader reader = request.getReader();
	        Gson gson = new Gson();
	        CustomerBean myBean = gson.fromJson(reader, CustomerBean.class);
            try (
                Statement stmt = conn.createStatement();) {
            	logger.info("customer: l-165 Creando Statement");
            	
                /*ASIGNANDO VALORES INPUT A VARIABLES LOCALES*/
                int accountnum = myBean.accountnum;
                String email = myBean.email;
                String firstname = myBean.firstname;
                String lastname = myBean.lastname;
                String phone = myBean.phone;
                String preference = myBean.preference;
                
                
                logger.info("customer: l-173 Datos:   insert into customer (accountnum, email ,firstname,lastname, phone, preference) values (" + accountnum + ", '" + email + "' ,'" + firstname + "','" + lastname + "','" + phone + "', '" + preference + "' )");
                /*STATEMENT*/
                int affectedRows = stmt.executeUpdate("insert into customer (accountnum, email ,firstname,lastname, phone, preference) values (" + accountnum + ", '" + email + "' ,'" + firstname + "','" + lastname + "','" + phone + "', '" + preference + "' )");
                if (affectedRows > 0) {
                    // get the ID back
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getLong(1);
                        }
                    } catch (SQLException ex) {
                    	logger.info("customer: l-182 Error: "+ex.getMessage());
                    	/*RESPUESSTA ERROR EN FORMATO JSON*/
    		        	JsonObject error = new JsonObject();
    		            error.addProperty("Status", "Error");
    		            error.addProperty("Error", ex.getMessage());
    		            out.println(error);
    		            response.setStatus(403);
    		            response.setContentType("application/json");
    		            response.setCharacterEncoding("UTF-8");
                    }
                    logger.info("Numero de columnas insertadas: "+affectedRows);
                    /*RESPUESSTA OK EN FORMATO JSON*/
                    JsonObject ok = new JsonObject();
                    ok.addProperty("Status", "Ok");
                    out.println(ok);
                    response.setStatus(200);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                }

                conn.close();
            } catch (SQLException ex) {
            	logger.info("customer: l-194 Error: "+ex.getMessage());
            	/*RESPUESSTA ERROR EN FORMATO JSON*/
	        	JsonObject error = new JsonObject();
	            error.addProperty("Status", "Error");
	            error.addProperty("Error", ex.getMessage());
	            out.println(error);
	            response.setStatus(403);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");

            }
       	
	}
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Ingresando al método PUT");
		String JDBC_DRIVER = "org.postgresql.Driver";
        String url = "jdbc:postgresql://10.0.0.12:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "postgres");
        
        Driver driver = null;
        Connection conn = null;

        PrintWriter out = response.getWriter();

        
        try {
            Class jdbcDriverClass = Class.forName(JDBC_DRIVER);
            driver = (Driver) jdbcDriverClass.newInstance();
            DriverManager.registerDriver(driver);
        } catch (Exception e) {
        	logger.info("customer: l-230 Error: "+e.getMessage());
            e.printStackTrace();
            /*RESPUESSTA ERROR EN FORMATO JSON*/
            JsonObject error = new JsonObject();
            error.addProperty("Status", "Error");
            error.addProperty("Error", e.getMessage());
            out.println(error);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        }
        try {
            conn = DriverManager.getConnection(url, props);
            logger.info("customer: l-237 Conexión a base de datos completada");
        } catch (SQLException e) {
        	logger.info("customer: l-241 Error: "+e.getMessage());
        	/*RESPUESSTA ERROR EN FORMATO JSON*/
            JsonObject error = new JsonObject();
            error.addProperty("Status", "Error");
            error.addProperty("Error", e.getMessage());
            out.println(error);
            response.setStatus(400);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        }
        
        try (
            Statement stmt = conn.createStatement();) {
        	logger.info("customer: l-251 Creando Statement");
        	/* OBTENIENDO JSON*/
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            CustomerBean myBean = gson.fromJson(reader, CustomerBean.class);
            /*ASIGNANDO VALORES INPUT A VARIABLES LOCALES*/
            int accountnum = myBean.accountnum;
            String email = myBean.email;
            String firstname = myBean.firstname;
            String lastname = myBean.lastname;
            String phone = myBean.phone;
            String preference = myBean.preference;
            /*STATEMENT*/
            int affectedRows = stmt.executeUpdate("UPDATE customer SET accountnum='" + accountnum
                    + "', email='" + email
                    + "', firstname='" + firstname
                    + "', lastname='" + lastname
                    + "', phone='" + phone
                    + "', preference='" + preference
                    + "' WHERE accountnum = " + accountnum + ";");
            if (affectedRows > 0) {
                logger.info("Numero de columnas insertadas: "+affectedRows);
                /*RESPUESSTA OK EN FORMATO JSON*/
                JsonObject ok = new JsonObject();
                ok.addProperty("Status", "Ok");
                out.println(ok);
                response.setStatus(200);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
            }
            if (affectedRows == 0){
            	JsonObject error = new JsonObject();
                error.addProperty("Status", "Error");
                error.addProperty("Error", "No se ha realizado ningún cambio / No existe accountnum "+accountnum+"");
                out.println(error);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
            }
            conn.close();
        } catch (Exception ex) {
        	logger.info("customer: l-285 Error: "+ex.getMessage());
        	/*RESPUESSTA ERROR EN FORMATO JSON*/
            JsonObject error = new JsonObject();
            error.addProperty("Status", "Error");
            error.addProperty("Error", ex.getMessage());
            out.println(error);
            response.setStatus(409);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

        }
            
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		logger.info("Ingresando al método DELETE");
		/*CONEXIÓN JDBC*/
		String JDBC_DRIVER = "org.postgresql.Driver";
        String url = "jdbc:postgresql://10.0.0.12:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "postgres");
        
        Driver driver = null;
        Connection conn = null;
        PrintWriter out = response.getWriter();

        
        try {
        	/*VALIDACIÓN DEL DRIVER*/
            Class jdbcDriverClass = Class.forName(JDBC_DRIVER);
            driver = (Driver) jdbcDriverClass.newInstance();
            DriverManager.registerDriver(driver);
        } catch (Exception e) {
        	logger.info("customer: l-313 Error: "+e.getMessage());
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("Status", "Error");
            error.addProperty("Error", e.getMessage());
            out.println(error);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

        }
        try {
        	/*CONEXIÓN A BASE DE DATOS*/
            conn = DriverManager.getConnection(url, props);
            logger.info("customer: l-320 Conexión a base de datos completada");
        } catch (SQLException e) {
        	logger.info("customer: l-324 Error: "+e.getMessage());
        	/*RESPUESSTA ERROR EN FORMATO JSON*/
            JsonObject error = new JsonObject();
            error.addProperty("Status", "Error");
            error.addProperty("Error", e.getMessage());
            out.println(error);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        }
        
        try (Statement stmt = conn.createStatement();) {
        	logger.info("customer: l-334 Creando Statement");
        	/* OBTENIENDO JSON*/
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            CustomerBean myBean = gson.fromJson(reader, CustomerBean.class);
        	
            /*ASIGNANDO VALORES INPUT A VARIABLES LOCALES*/
            int accountnum = myBean.accountnum;
            /*STATEMENT*/
            int affectedRows = stmt.executeUpdate("DELETE FROM customer\n" +
                                                        "	WHERE accountnum = "+accountnum+";");
            if (affectedRows > 0)   {
            logger.info("Numero de columnas insertadas: "+affectedRows);
                /*RESPUESSTA OK EN FORMATO JSON*/
                JsonObject ok = new JsonObject();
                ok.addProperty("Status", "Ok");
                out.println(ok);
                response.setStatus(200);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                conn.close();
            }
            if (affectedRows == 0){
            	/*RESPUESSTA ERROR EN FORMATO JSON*/
            	JsonObject error = new JsonObject();
                error.addProperty("Status", "Error");
                error.addProperty("Error", "No se ha realizado ningún cambio / No existe accountnum "+accountnum+"");
                out.println(error);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
            }
                
        } catch (Exception ex) {
        	logger.info("customer: l-358 Error: "+ex.getMessage());
        	/*RESPUESSTA ERROR EN FORMATO JSON*/
            JsonObject error = new JsonObject();
            error.addProperty("Status", "Error");
            error.addProperty("Error", ex.getMessage());
            out.println(error);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

        }
	}
}
