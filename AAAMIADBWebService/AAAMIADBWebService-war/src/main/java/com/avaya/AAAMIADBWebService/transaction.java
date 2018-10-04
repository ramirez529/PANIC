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
@WebServlet("/transaction")
public class transaction extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
    private final Logger logger;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public transaction() 
    {
        super();
        logger = Logger.getLogger(getClass());
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 *  GET https://135.169.18.7/services/AAADEVURIELPrueba5/transaction
     * output Json {"accountnum":1303,"email":"urielmansilla@avaya.com","firstname":"Uriel","lastname":"Mansi","phone":"55598765434","preference":"sms"}
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
			response.getWriter().println();
			String JDBC_DRIVER = "org.postgresql.Driver";
	        String url = "jdbc:postgresql://10.0.0.12:5432/postgres";
	        Properties props = new Properties();
	        props.setProperty("user", "postgres");
	        props.setProperty("password", "postgres");
//	        props.setProperty("ssl", "true");

	        PrintWriter out = response.getWriter();
	        Connection conn = null;

	        try {
	            Class<?> jdbcDriverClass = Class.forName(JDBC_DRIVER);
	            Driver driver = (Driver) jdbcDriverClass.newInstance();
	            DriverManager.registerDriver(driver);
	        } catch (Exception e) {
	        	logger.info("transaction: l-75 Error: "+e.getMessage());
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
	            logger.info("transaction l-82: Conexión realizada a BD");

			}catch (SQLException ex) {
				logger.info("transaction: l-85 Error: "+ex.getMessage());
		    	out.println(ex.getMessage());
		    	/*RESPUESSTA ERROR EN FORMATO JSON*/
	            JsonObject error = new JsonObject();
	            error.addProperty("Status", "Error");
	            error.addProperty("Error", ex.getMessage());
	            out.println(error);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
		    }
			/*SOLO EVALUA UN ID*/
			if(request.getParameter("transid") != null){
				String transid_string = request.getParameter("transid");
				int transid = Integer.parseInt(transid_string);
				String SQL = "SELECT * FROM postgres.public.transaction WHERE transid = "+transid+";";
				try (Statement stmt = conn.createStatement();
		                ResultSet rs = stmt.executeQuery(SQL)) {
		            List<transactionBean> list = new LinkedList<>();
		            while (rs.next()) {
		            	transactionBean trans = new transactionBean();
		            	trans.transid = rs.getInt("transid");
		            	trans.amount = rs.getString("amount");
		            	trans.accountnum = rs.getInt("accountnum");
		            	trans.transdate = rs.getString("transdate");
		            	trans.merchantname = rs.getString("merchantname");
		                list.add(trans);
		            }
		            Gson jsonGson = new GsonBuilder().setPrettyPrinting().create();
		            out.println(jsonGson.toJson(list));
		            conn.close();
		            stmt.close();
		            rs.close();
		        } catch (SQLException ex) {
		        	logger.info("transaction: l-112 Error: "+ex.getMessage());
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
			/*ENVÍA TODOS LOS REGISTROS*/	
			if(request.getParameter("transid") == null){
			String SQL = "SELECT * FROM postgres.public.transaction;";
	        try (Statement stmt = conn.createStatement();
	                ResultSet rs = stmt.executeQuery(SQL)) {
	            List<transactionBean> list = new LinkedList<>();
	            while (rs.next()) {
	            	transactionBean trans = new transactionBean();
	            	trans.transid = rs.getInt("transid");
	            	trans.amount = rs.getString("amount");
	            	trans.accountnum = rs.getInt("accountnum");
	            	trans.transdate = rs.getString("transdate");
	            	trans.merchantname = rs.getString("merchantname");
	                list.add(trans);
	            }
	            Gson jsonGson = new GsonBuilder().setPrettyPrinting().create();
	            out.println(jsonGson.toJson(list));
	            conn.close();
	            stmt.close();
	            rs.close();
	        } catch (SQLException ex) {
	        	logger.info("transaction: l-112 Error: "+ex.getMessage());
	        	/*RESPUESSTA ERROR EN FORMATO JSON*/
	            JsonObject error = new JsonObject();
	            error.addProperty("Status", "Error");
	            error.addProperty("Error", ex.getMessage());
	            response.setStatus(400);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	        }
			}

	}

//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 * POST https://135.169.18.7/services/AAADEVURIELPrueba5/transaction
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
	        /*VALIDANDO DRIVER*/
	        try {
	            Class jdbcDriverClass = Class.forName(JDBC_DRIVER);
	            driver = (Driver) jdbcDriverClass.newInstance();
	            DriverManager.registerDriver(driver);
	        } catch (Exception e) {
	        	logger.info("transaction: l-145 Error: "+e.getMessage());
	        	/*RESPUESSTA ERROR EN FORMATO JSON*/
	            JsonObject error = new JsonObject();
	            error.addProperty("Status", "Error");
	            error.addProperty("Error", e.getMessage());
	            out.println(error);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	        }
	        /*CREANDO CONEXIÓN*/
	        try {
	            conn = DriverManager.getConnection(url, props);
	            logger.info("transaction: l-151 Conexión a base de datos completada");
	            System.out.println("Connected to the PostgreSQL server successfully.");
	        } catch (SQLException e) {
	        	logger.info("transaction: l-156 Error: "+e.getMessage());
	        	/*RESPUESSTA ERROR EN FORMATO JSON*/
	            JsonObject error = new JsonObject();
	            error.addProperty("Status", "Error");
	            error.addProperty("Error", e.getMessage());
	            out.println(error);
	            response.setStatus(400);
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	        }
	        /* OBTENIENDO JSON*/
	        BufferedReader reader = request.getReader();
	        Gson gson = new Gson();
	        transactionBean myBean = gson.fromJson(reader, transactionBean.class);
            try (Statement stmt = conn.createStatement();) {
            	logger.info("transaction: l-164 Creando Statement");
                /*ASIGNANDO VALORES INPUT A VARIABLES LOCALES*/
                int transid = myBean.transid;
                String amount = myBean.amount;
                int accountnum = myBean.accountnum;
                String transdate = myBean.transdate;
                String merchantname = myBean.merchantname;
                
                
                logger.info("transaction: l-173 Datos: insert into transaction (transid, amount ,accountnum,transdate, merchantname) values (" + transid + ", '" + amount + "' ," + accountnum + ",'" + transdate + "', '" + merchantname + "' )");
                /*STATEMENT*/
                int affectedRows = stmt.executeUpdate("insert into transaction (transid, amount ,accountnum,transdate, merchantname) values (" + transid + ", '" + amount + "' ," + accountnum + ",'" + transdate + "', '" + merchantname + "' )");
                if (affectedRows > 0) {
                    // get the ID back
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getLong(1);
                        }
                    } catch (SQLException ex) {
                    	logger.info("transaction: l-183 Error: "+ex.getMessage());
                    	/*RESPUESSTA ERROR EN FORMATO JSON*/
                        JsonObject error = new JsonObject();
                        error.addProperty("Status", "Error");
                        error.addProperty("Error", ex.getMessage());
                        out.println(error);
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
            	logger.info("transaction: l-193 Error: "+ex.getMessage());
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
        long id = 0;
        
        PrintWriter out = response.getWriter();

        
        try {
            Class jdbcDriverClass = Class.forName(JDBC_DRIVER);
            driver = (Driver) jdbcDriverClass.newInstance();
            DriverManager.registerDriver(driver);
        } catch (Exception e) {
        	logger.info("transaction: l-220 Error: "+e.getMessage());
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
            logger.info("transaction: l-227 Conexión a base de datos completada");
        } catch (SQLException e) {
        	logger.info("transaction: l-230 Error: "+e.getMessage());
        	/*RESPUESSTA ERROR EN FORMATO JSON*/
            JsonObject error = new JsonObject();
            error.addProperty("Status", "Error");
            error.addProperty("Error", e.getMessage());
            out.println(error);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        }
        
        try (Statement stmt = conn.createStatement();) {
        	logger.info("MyServlet: l-251 Creando Statement");
        	/* OBTENIENDO JSON*/
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            transactionBean myBean = gson.fromJson(reader, transactionBean.class);
            
            /*ASIGNANDO VALORES INPUT A VARIABLES LOCALES*/
        	int transid = myBean.transid;
            String amount = myBean.amount;
            int accountnum = myBean.accountnum;
            String transdate = myBean.transdate;
            String merchantname = myBean.merchantname;
            /*STATEMENT*/
            int affectedRows = stmt.executeUpdate("UPDATE transaction SET transid='" + transid
                    + "', amount='" + amount
                    + "', accountnum='" + accountnum
                    + "', transdate='" + transdate
                    + "', merchantname='" + merchantname
                    + "' WHERE transid = " + transid + ";");
            if (affectedRows > 0) {
                // get the ID back
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
                error.addProperty("Error", "No se ha realizado ningún cambio / No existe transid "+transid+"");
                out.println(error);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
            }
            conn.close();
        } catch (Exception ex) {
        	logger.info("MyServlet: l-285 Error: "+ex.getMessage());
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
		String JDBC_DRIVER = "org.postgresql.Driver";
        String url = "jdbc:postgresql://10.0.0.12:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "postgres");
        
        Driver driver = null;
        Connection conn = null;
        long id = 0;
        
        PrintWriter out = response.getWriter();

        
        try {
            Class jdbcDriverClass = Class.forName(JDBC_DRIVER);
            driver = (Driver) jdbcDriverClass.newInstance();
            DriverManager.registerDriver(driver);
        } catch (Exception e) {
        	logger.info("transaction: l-302 Error: "+e.getMessage());
            e.printStackTrace();
            /*RESPUESSTA ERROR EN FORMATO JSON*/
            JsonObject error = new JsonObject();
            error.addProperty("Status", "Error");
            error.addProperty("Error", e.getMessage());
            out.println(error);
            response.setStatus(400);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        }
        try {
            conn = DriverManager.getConnection(url, props);
            logger.info("transaction: l-309 Conexión a base de datos completada");
        } catch (SQLException e) {
        	logger.info("transaction: l-313 Error: "+e.getMessage());
        	/*RESPUESSTA ERROR EN FORMATO JSON*/
            JsonObject error = new JsonObject();
            error.addProperty("Status", "Error");
            error.addProperty("Error", e.getMessage());
            out.println(error);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        }
        /* OBTENIENDO JSON*/
        
        try (Statement stmt = conn.createStatement();) {
        	logger.info("transaction: l-323 Creando Statement");
        	BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            transactionBean myBean = gson.fromJson(reader, transactionBean.class);
            /*ASIGNANDO VALORES INPUT A VARIABLES LOCALES*/
            int transid = myBean.transid;
            /*STATEMENT*/
            int affectedRows = stmt.executeUpdate("DELETE FROM transaction\n" +
                                                        "	WHERE transid = "+transid+";");
            if (affectedRows > 0) {
                // get the ID back
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
                error.addProperty("Error", "No se ha realizado ningún cambio / No existe transid "+transid+"");
                out.println(error);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
            }
            conn.close();
        } catch (Exception ex) {
        	logger.info("MyServlet: l-285 Error: "+ex.getMessage());
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
}