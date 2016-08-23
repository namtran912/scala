package Redis

/**
  * Created by CPU11179-local on 8/18/2016.
  */


trait IRedis {
    def Connect(ip: String*): Boolean
    def hget(uId: String, itemId: String, kindOfLimit: String): Array[Long]
    def hupdate(uId: String, itemId: String, kindOfLimit: String): Unit

}
