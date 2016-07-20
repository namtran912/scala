package test

import javax.servlet.ServletConfig
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import org.eclipse.jetty.http.HttpStatus

/**
  * Created by CPU11179-local on 7/19/2016.
  */
class ExampleJetty extends HttpServlet{

    val reader : Reader = new Reader("IP.CSV")
    override def init(config : ServletConfig) : Unit = {
        reader.readFile
    }

    override def doGet(request : HttpServletRequest , response : HttpServletResponse ) : Unit = {
        response.setStatus(HttpStatus.OK_200)

        val userName = request.getParameter("ip")
        if (userName != null)
            response.getWriter().println(reader.getLocation(reader.ip2Long(userName)))
        else
            response.getWriter().println(reader.getLocation(reader.ip2Long(reader.getIP)))
    }

}
