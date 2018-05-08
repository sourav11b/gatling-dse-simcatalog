package feeds.consumerfraud

import com.datastax.gatling.stress.core.BaseFeed
import com.typesafe.scalalogging.LazyLogging
import com.datastax.driver.core.UDTValue
import com.datastax.gatling.stress.libs.Cassandra
import scala.collection.JavaConverters._
import com.github.javafaker.Lorem


class DeviceFeed() extends BaseFeed with LazyLogging {
  
 
  
  def getDevice: Iterator[Map[String, Any]] = {
    def rowData = this.getDeviceRow
    Iterator.continually(rowData)
  }

  protected def getDeviceRow: Map[String, Any] = {
    Map(
      "mac_address" -> faker.number().randomNumber(16, true).toString(), 
      "creation_timestamp" -> getRandomEpoch,
      "device_status" -> faker.options.option("ACTIVE","INACTIVE"),
      "device_type" -> faker.options.option("CM","MTA"),
      "device_make" -> faker.lorem().characters(5, 15, true),
      "device_model" -> faker.lorem().characters(5, 15, true),
      "active_accounts" -> Set[String](faker.number().randomNumber(16, true).toString(),faker.number().randomNumber(16, true).toString(),
        faker.number().randomNumber(16, true).toString(),
        faker.number().randomNumber(16, true).toString(),
        faker.number().randomNumber(16, true).toString()    
       ).asJava,


       
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
