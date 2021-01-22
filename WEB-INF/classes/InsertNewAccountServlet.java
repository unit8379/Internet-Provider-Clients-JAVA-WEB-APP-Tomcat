import java.io.PrintWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/InsertNewAccountToDatabase")
public class InsertNewAccountServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        int tariffId = Integer.parseInt(request.getParameter("tariffId"));

        try {
            String url = "jdbc:mysql://127.0.0.1:3306/internet_provider_clients?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)){
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT MAX(account_id) FROM accounts");
                resultSet.next();
                int nextAccountId = resultSet.getInt(1) + 1;

                statement.executeUpdate("INSERT INTO accounts VALUES ("+nextAccountId+", '"+firstName+"', '"+lastName+"', "+tariffId+")");
                
                writer.println("<!DOCTYPE html>"+
                "<html lang=\"en\">"+
                
                "<head>"+
                    "<meta charset=\"UTF-8\">"+
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"+
                    "<title>Ответ от сервера</title>"+
                    "<link rel=\"shortcut icon\" href=\"./content/internet.png\" type=\"image/png\">"+
                    "<link rel=\"stylesheet\" href=\"./bootstrap-4.5.3-dist/css/bootstrap.css\">"+
                    "<link rel=\"stylesheet\" href=\"add-client-form-style.css\">"+
                "</head>"+
                
                "<body>"+
                    "<div class=\"container-md\">"+
                        "<p class=\"lead\">"+
                            "Запись добавлена!"+
                        "</p>"+
                        "<a href=\"PrimaryPage\">"+
                            "<button id=\"returnButton\" class=\"btn btn-secondary\">Вернуться к списку</button>"+
                        "</a>"+
                    "</div>"+
                "</body>"+
                "</html>");
            }
        }
        catch(Exception e){
            writer.println("<p>Database connection failed...</p>");
            writer.println("<p>"+e.getMessage()+"</p>");
            writer.println("<a href=\"PrimaryPage\"><button>Вернуться к списку</button></a>");
            e.printStackTrace();
        }
        finally {
            writer.close();
        }
    }
}