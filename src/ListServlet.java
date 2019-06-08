import com.cheolhyeon.miniwas.lib.HTTPServletRequest;
import com.cheolhyeon.miniwas.lib.HTTPServletResponse;
import com.cheolhyeon.miniwas.lib.Servlet;

import java.io.PrintWriter;

public class ListServlet extends Servlet {

    @Override
    public void doGet(HTTPServletRequest request, HTTPServletResponse response) {
        System.out.println("success");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Hello World!</h1>\n" +
                "    <script>\n" +
                "        alert(\"Hello World!\");\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>");
    }
}
