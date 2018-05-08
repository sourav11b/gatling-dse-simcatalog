package feeds.consumerfraud

import com.datastax.gatling.stress.core.BaseFeed
import com.typesafe.scalalogging.LazyLogging
import scala.collection.JavaConverters._


class ServicesByAccountFeed extends BaseFeed with LazyLogging {

  def write = {
    def rowData = getRowData

    // optionally log out the data that was generated for future use
    // logger.info("{},{},{}", Array(rowData.get("order_no"), rowData.get("cust_id"), rowData.get("email")))

    Iterator.continually(rowData)
  }


  def getRowData = {
    Map(
      "account_number" -> faker.number().randomNumber(16, true).toString(), 
      "service_type" -> faker.options.option("DATA","VOICE","VIDEO"),
      "service_status" -> faker.options.option("ACTIVE","INACTIVE"),
      "addition_timestamp" -> getRandomEpoch,
      "class_of_service" -> faker.options.option("class_of_service_1","class_of_service_2","class_of_service_3")
    )
  }
}
