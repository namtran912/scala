package test

import javax.servlet.ServletConfig
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import org.eclipse.jetty.http.HttpStatus

/**
  * Created by CPU11179-local on 7/19/2016.
  */
class JettyService extends HttpServlet{

  override def init(config: ServletConfig): Unit = {
      Process.reader.readFile
  }

  override def doGet(request: HttpServletRequest , response: HttpServletResponse ): Unit = {
      response.setStatus(HttpStatus.OK_200)

      val ip = request.getParameter("ip")
      if (ip != null)
          response.getWriter().println(Process.getLocationCode(ip))
      else
          response.getWriter().println(Process.getLocationCode(Process.getIP))
  }

}