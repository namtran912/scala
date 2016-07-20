package test

import java.net.InetAddress

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
/**
  * Created by CPU11179-local on 7/19/2016.
  */
object DemoJetty {
  def main(args: Array[String]) : Unit = {
    val server : Server = new Server(8000)
    val handler : ServletContextHandler  = new ServletContextHandler(server, "/example")
    handler.addServlet(classOf[ExampleJetty], "/")
    server.start()

    /*val IP : InetAddress = InetAddress.getLocalHost()
    System.out.println("IP of my system is := "+IP.getHostAddress())*/
  }
}

