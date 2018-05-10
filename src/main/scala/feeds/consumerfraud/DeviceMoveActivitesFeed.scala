package feeds.consumerfraud

import com.datastax.gatling.stress.core.BaseFeed
import com.typesafe.scalalogging.LazyLogging
import com.datastax.driver.core.UDTValue
import com.datastax.gatling.stress.libs.Cassandra
import scala.collection.JavaConverters._
import com.github.javafaker.Lorem


class DeviceMoveActivitesFeed extends BaseFeed with LazyLogging {
  
 
  
  def getActivities: Iterator[Map[String, Any]] = {
    def rowData = this.getRow
    Iterator.continually(rowData)
  }

  protected def getRow: Map[String, Any] = {
    Map(
      "mac_address" -> faker.internet().macAddress(), 
      "creation_timestamp" -> getRandomEpoch,
      "assigned_from_account" -> faker.number().randomNumber(16, true).toString(),
      "assigned_to_account" -> faker.number().randomNumber(16, true).toString(),
      "activity_code" -> faker.regexify("[A-Z]{2}_[0-9]{5}"),
      "activity_name"  -> faker.regexify("[A-Z_]{15}")


       
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
