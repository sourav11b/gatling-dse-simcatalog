package feeds.consumerfraud

import com.datastax.gatling.stress.core.BaseFeed
import com.typesafe.scalalogging.LazyLogging
import com.datastax.driver.core.UDTValue
import com.datastax.gatling.stress.libs.Cassandra
import scala.collection.JavaConverters._


class AccountFeed(cass: Cassandra, keyspace: String) extends BaseFeed with LazyLogging {
  
 
  
  def getAddressUDT = {
    val address_type = cass.getCluster.getMetadata.getKeyspace(keyspace).getUserType("address")
    val address = address_type.newValue()

    val row = getAddressRow
    row.foreach(p => 
      if(p._1 == "phones")
        address.setSet[String](p._1,Set[String](faker.phoneNumber().phoneNumber(), faker.phoneNumber().phoneNumber(), faker.phoneNumber().phoneNumber()).asJava)
      else
      address.setString(p._1, p._2.toString)
      
    )

    address
  }
  
  def getAddressesUDT: Iterator[Map[String, Set[UDTValue]]] = {
    def addressData = {
      Map("address" -> (1 to 4).map(_ => {
   
          getAddressUDT

      }).filter(_ != false).foldLeft(Set[UDTValue]())((s, a) => s + a.asInstanceOf[UDTValue]))
    }

    Iterator.continually(addressData)
  }
  
   protected def getAddressRow: Map[String, Any] = {
    Map(
      "street" -> faker.address.streetAddress(),
      "city" -> faker.address.city,
      "state" -> faker.address.stateAbbr,
      "zip_code" -> faker.address().zipCode(),
      "phones" -> ""
    )
  }

   def getAccount: Iterator[Map[String, Any]] = {
    def rowData = this.getAccountRow
    Iterator.continually(rowData)
  }

  protected def getAccountRow: Map[String, Any] = {
    Map(
      "account_number" -> faker.number().randomNumber(16, true).toString(), 
      "creation_timestamp" -> getRandomEpoch,
      "first_name" -> faker.name().firstName(),
      "last_name" -> faker.name().lastName(),
      "account_status" -> faker.options.option("ACTIVE","DISABLED"),
      "customer_id" -> faker.internet().emailAddress(),
      "active_devices" -> Set[String](faker.internet().macAddress(),faker.internet().macAddress(),faker.internet().macAddress()).asJava,
      "address" -> getAddressUDT

       
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
