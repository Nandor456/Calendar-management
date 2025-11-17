package edu.bbte.idde.mnim2377.servlet.templateservlet;

import edu.bbte.idde.mnim2377.servlet.thymeleaf.ThymeleafEngineFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        ThymeleafEngineFactory.buildEngine(getServletContext());
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // felépítjük a dinamikus tartalmat (model)
        // itt lehetnének adatbázis-lekérések, stb.
        Map<String, Object> model = new ConcurrentHashMap<>();
        model.put("message", "I am your father");

        // rendering
        ThymeleafEngineFactory.process(req, resp, "index.html", model);
    }

}
