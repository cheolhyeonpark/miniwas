import com.cheolhyeon.miniwas.lib.HTTPServletRequest;
import com.cheolhyeon.miniwas.lib.HTTPServletResponse;
import com.cheolhyeon.miniwas.lib.Servlet;

public class ListServlet extends Servlet {

    @Override
    public void doGet(HTTPServletRequest request, HTTPServletResponse response) {
        System.out.println("success");
    }
}
