package feeds.consumerfraud

import com.datastax.gatling.stress.core.BaseFeed
import com.typesafe.scalalogging.LazyLogging
import com.datastax.driver.core.UDTValue
import com.datastax.gatling.stress.libs.Cassandra
import scala.collection.JavaConverters._
import com.github.javafaker.Lorem


class NetworkStatisticsByDeviceFeed() extends BaseFeed with LazyLogging {
  
 
  
  def getStats: Iterator[Map[String, Any]] = {
    def rowData = this.getRow
    Iterator.continually(rowData)
  }

  protected def getRow: Map[String, Any] = {
    Map(
       "year_bucket" ->  getRandomEpoch.getYear,
      "mac_address" -> faker.internet().macAddress(), 
      "creation_timestamp" -> getRandomEpoch,
      "class_of_service" -> faker.regexify("[a-zA-Z0-9_.-]{15}"),
      "ip_address" -> faker.internet().ipV6Address(),
      "sys_up_time" -> faker.number().numberBetween(600, 525600).toLong


       
    )
  }
  
    


/*
  def write = {
    def rowData = getRowData

    // optionally log out the data that was generated for future use
    // logger.info("{},{},{}", Array(rowData.get("order_no"), rowData.get("cust_id"), rowData.get("email")))

    Iterator.continually(rowData)
  }


  def getRowData = {
    Map(
      "account_number" -> faker.number().randomNumber(16, true).toString(), 
      "creation_timestamp" -> faker.options.option("DATA","VOICE","VIDEO"),
      "service_status" -> faker.options.option("ACTIVE","INACTIVE"),
      "addition_timestamp" -> getRandomEpoch,
      "class_of_service" -> faker.options.option("class_of_service_1","class_of_service_2","class_of_service_3")
    )
  }
  
  */
}
