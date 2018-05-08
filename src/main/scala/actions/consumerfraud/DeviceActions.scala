package actions.consumerfraud

import com.datastax.driver.core.ConsistencyLevel
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.querybuilder.QueryBuilder._
import com.datastax.gatling.plugin.CqlPredef._
import com.datastax.gatling.stress.core.BaseAction
import com.datastax.gatling.stress.libs.{ Cassandra, SimConfig }
import io.gatling.core.Predef._

/**
 * Order Actions
 *
 * @param cassandra Cassandra
 * @param simConf   SimConf
 */
class DeviceActions(cassandra: Cassandra, simConf: SimConfig) extends BaseAction(cassandra, simConf) {

  // create keyspace/table if they do not exist
  createKeyspace
  createTables()

  // A regular string query can be used as well as the QueryBuilder
  private val writeQuery = QueryBuilder.insertInto(keyspace, table)
    .value("mac_address", raw("?"))
    .value("creation_timestamp", raw("?"))
    .value("device_status", raw("?"))
    .value("device_type", raw("?"))
    .value("device_make", raw("?"))
    .value("device_model", raw("?"))    
    .value("active_accounts", raw("?"))


  def writeDevice = {

    val preparedStatement = session.prepare(writeQuery)

    group(Groups.INSERT) {
      exec(cql("device")
        .executePrepared(preparedStatement)
        .withParams(
          "${mac_address}",
          "${creation_timestamp}",
          "${device_status}",
          "${device_type}",
          "${device_make}",
          "${device_model}",
          "${active_accounts}")
        .consistencyLevel(ConsistencyLevel.LOCAL_QUORUM) // ConsistencyLevel can be set per query
        .check(rowCount is 0) // an insert should not return rows
      )      
   }
  }

  /*

  def writeOrderWithLwt = {

    val query = writeOrderQuery.ifNotExists()
    val preparedStatement2 = session.prepare(query)

    group(Groups.INSERT) {
      exec(cql("OrderLwt")
          .executePrepared(preparedStatement2)
          .withParams(
            "${order_no}",
            "${alt_fname}",
            "${alt_lname}",
            "${city}",
            "${cust_id}",
            "${data}",
            "${email}",
            "${esd}",
            "${fname}",
            "${fulfill_type}",
            "${group_no}",
            "${hold_status}",
            "${hold_type}",
            "${item_id}",
            "${line_code}",
            "${line_status}",
            "${lname}",
            "${modified_dt}",
            "${offer_id}",
            "${opd}",
            "${order_date}",
            "${order_type}",
            "${pallet_asn}",
            "${partner_item_id}",
            "${phone}",
            "${pi_hash}",
            "${pkg_asn}",
            "${po_line_code}",
            "${po_line_status}",
            "${po_no}",
            "${rma}",
            "${seller_id}",
            "${shard_id}",
            "${ship_method}",
            "${ship_node}",
            "${source}",
            "${state}",
            "${store_id}",
            "${store_tc_no}",
            "${tc_no}",
            "${tracking_no}",
            "${upc}"
          )
          .consistencyLevel(ConsistencyLevel.LOCAL_SERIAL) // Consitency of LWT can also be set
          .check(columnValue("[applied]") is true) // since this uses LWT we want to make sure that it succeeded
      )
    }
  }



  val readOrderQuery = QueryBuilder.select().from(keyspace, table).where(QueryBuilder.eq("order_no", raw("?")))


  def readOrder = {

    val preparedStatement = session.prepare(readOrderQuery)

    group(Groups.SELECT) {
      exec(cql("Order")
          .executePrepared(preparedStatement)
          .withParams(
            "${order_no}"
          )
          .check(rowCount greaterThan 0)
      )
    }
  }


  def readOrderAndSaveToSession = {

    val preparedStatement = session.prepare(readOrderQuery)

    group(Groups.SELECT) {
      exec(cql("Order2")
          .executePrepared(preparedStatement)
          .withParams(
            "${order_no}"
          )
          .check(rowCount greaterThan 0)

          // This will save the value of col "email" to a "testParam" in the current session
          // This can be used like a feed in the withParams("$testParam") for reuse
          .check(columnValue("email").find.saveAs("testParam"))

      ).exitHereIfFailed // Print out for debugging
          .exec { session =>
        println(session)
        session
      }
    }
  }

*/
  def createTables(): Unit = {
    runQueries(Array(

   s"""
   |  CREATE TABLE IF NOT EXISTS  known_fraud_detection.device (
   |  mac_address	text, 
   |  creation_timestamp  timestamp, 
   |  device_status text, 
   |  device_type text,
   |  device_make text,
   |  device_model text,
   |  active_accounts set<text>,
   |  PRIMARY KEY ( mac_address	, creation_timestamp ) )
   |  WITH CLUSTERING ORDER BY ( creation_timestamp  DESC )
   """.stripMargin       
    ))

  }

}
