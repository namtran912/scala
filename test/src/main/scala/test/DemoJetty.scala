package test

import java.io.FileWriter

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler

object DemoJetty {
    def main(args: Array[String]): Unit = {
        //Process.reader.modify
        val server: Server = new Server(8000)
        val handler: ServletContextHandler = new ServletContextHandler(server, "/location")
        handler.addServlet(classOf[JettyService], "/")
        server.start
    }
}
