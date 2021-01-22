import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import JavaClasses.AccountBean;

@WebServlet("/PrimaryPage")
public class PrimaryPageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        ArrayList<AccountBean> accounts;

        try {
            String url = "jdbc:mysql://127.0.0.1:3306/internet_provider_clients?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)){
                // Для Резалт Сетов этого Statement'а задал свойство для скролла указателя вниз-вверх. По умолчанию курсор двигается только вниз.
                // Каждый стейтмент может держать открытым только один ResultSet
                Statement statementForAccountsSet = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                Statement statementForTariffSet = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                ResultSet tariff_serviceResultSet = statementForTariffSet.executeQuery("SELECT * FROM tariff");
                ResultSet accountsResultSet = statementForAccountsSet.executeQuery("SELECT * FROM accounts");

                // создаётся ArrayList размером с количество записей клиентов. Чтобы получить число записей используются манипуляции с курсором резалт сета
                accountsResultSet.last();
                int accountsNumber = accountsResultSet.getRow();
                accounts = new ArrayList<AccountBean>(accountsNumber);
                accountsResultSet.beforeFirst(); // курсор обратно перед первой строкой

                // Заполнение ArrayList'а Bean'ами AccountBean
                int tariffId;
                while (accountsResultSet.next()) {
                    // Тут устанавливаются курсоры на нужную строку в таблицах тарифа и разлчных услуг, в соответствии с записью account
                    tariffId = accountsResultSet.getInt("tariff_id");
                    tariff_serviceResultSet.absolute(tariffId);

                    // добавление заполненного аккаунта в ArrayList
                    accounts.add(
                        new AccountBean(accountsResultSet.getString("account_id"), accountsResultSet.getString("first_name"),
                            accountsResultSet.getString("last_name"), tariff_serviceResultSet.getString("name"), tariff_serviceResultSet.getString("cost")));
                }
            }

            request.setAttribute("accounts", accounts);
            request.setAttribute("test", 999999);
            //getServletContext().getRequestDispatcher("/primary-page.jsp").forward(request, response);
            ServletContext servletContext = getServletContext();
            RequestDispatcher dispatcher = servletContext.getRequestDispatcher("/primary-page.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
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