import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

public class BloodDonor extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {

// Get the servlets configurations.
   ServletContext sc = getServletContext();

// Set the response type to html as it is to be rendered in browser.
   response.setContentType("text/html");
   PrintWriter out = response.getWriter();
   
// Set the title for the page.
   String docType = "<!DOCTYPE HTML>\n";
   out.println(docType +
                "<HTML>\n" +
                "<HEAD><TITLE>BloodDonor Applicants</TITLE></HEAD>\n" +
                "<BODY>\n" +
                "<H1>BloodDonor Applicants</H1>\n");

// Get the Data from the Database 
    try {
		Class.forName("oracle.jdbc.OracleDriver");
		System.out.println("Oracle Database Driver loaded !!");

		String url="jdbc:oracle:thin:@localhost:1521:xe";
		String user = "CON1";
		String pwd = "Sricharan1";
		
		//Get the required fields from http request.
		String name = request.getParameter("name");
		String telephone = request.getParameter("telephone");
		String email = request.getParameter("email");
		String birthdate = request.getParameter("birthDate");
		String age = request.getParameter("age");
		String gender = request.getParameter("gender");
		String group = request.getParameter("group");
		
		// Get the database connection
		Connection DB_mobile_conn = DriverManager.getConnection(url,user,pwd);
		System.out.println("Database Connect ok");//DONOR_SEQ.CURRVAL
		
		//Create the insert statement.
		PreparedStatement prep_stmt = DB_mobile_conn.prepareStatement("INSERT INTO BLOOD_DONOR VALUES (DONOR_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)");
		prep_stmt.setString(1,name);
		prep_stmt.setString(2,telephone);
		prep_stmt.setString(3,email);
		prep_stmt.setString(4,birthdate);
		prep_stmt.setString(5,age);
		prep_stmt.setString(6,gender);
		prep_stmt.setString(7,group);
		
		// Execute the insert statement
		prep_stmt.executeUpdate();
		
		// Create the select query to show all the database records.
		String query = "select * from BLOOD_DONOR ORDER BY donor_id";
		
		// Create the select statement for the database
			Statement query_stmt=DB_mobile_conn.createStatement();
			ResultSet query_rs=query_stmt.executeQuery(query);
			int columnCount = query_rs.getMetaData().getColumnCount();
			//Printing the query
			out.println("Query is <i>"+query+"</i><br>");
			out.println("<br></br>");
			out.println("<table border=1 cellpadding=3 cellspacing=0>");
			out.println("<tr bgcolor=#C0C0C0>");
			//Print the column names
			for (int i = 1; i <= columnCount; i++) {
				out.println("<td>");
				out.println("<b>"+query_rs.getMetaData().getColumnName(i)+"</b>");
				out.println("</td>");
			}
			out.println("</tr>");
			//Print the table data
			while (query_rs.next()) {
				out.println("<tr>");
				for (int i = 1; i <= columnCount; i++) {
					out.println("<td>");
					out.println(query_rs.getString(i));
					out.println("</td>");
				}
				out.println("</tr>");
				sc.log("Column Returned");
			}
			out.println("</table><br>");
			query_rs.close();
			query_stmt.close();
		
		
    } catch (Exception exp) {
		out.println("Exception = " +exp);
		System.out.println("Exception = " +exp);
		exp.printStackTrace();
    }
	
    out.println(docType +"</BODY></HTML>");
  }
  
   public void doPost(HttpServletRequest request, HttpServletResponse response)
   throws IOException, ServletException
    {
        doGet(request, response);
    }
}
