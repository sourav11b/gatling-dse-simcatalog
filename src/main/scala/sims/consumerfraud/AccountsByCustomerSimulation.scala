package sims.consumerfraud

import com.datastax.gatling.plugin.CqlPredef._
import com.datastax.gatling.stress.core.BaseSimulation
import com.datastax.gatling.stress.libs.SimConfig
import io.gatling.core.Predef._
import actions.consumerfraud.ServicesByOrderActions
import feeds.consumerfraud.ServicesByAccountFeed
import actions.consumerfraud.AccountActions
import actions.consumerfraud.AccountActions
import feeds.consumerfraud.AccountFeed
import actions.consumerfraud.AccountsByCustomerActions
import feeds.consumerfraud.AccountByCustomerFeed

class AccountsByCustomerSimulation extends BaseSimulation {

  val simName = "knownFraudDetection"
  val scenarioName = "accountByCustomerInsert"

  // load conf based on the simGroup and simName from defaults.conf
  val simConf = new SimConfig(conf, simName, scenarioName)

  // init orderActions aka queries
  val accountActions = new AccountsByCustomerActions(cass, simConf)

  // Load feed for generating data
  val writeFeed = new AccountByCustomerFeed(cass, simConf.getSimulationConfStr("keyspace"))

  // build scenario to run with feed and write action
  val writeScenario = scenario(scenarioName)
      .feed(writeFeed.getAccount)
      .exec(accountActions.writeAccounts)


  // setup the traffic to run w/ the scenario
  setUp(

    loadGenerator.rampUpToConstant(writeScenario, simConf)

  ).assertions(
    // Can add asssertions if wanting to ensure response times are better than X
    //        global.responseTime.percentile4.lessThan()
    //        global.responseTime.max.lessThan(10),
    //        global.successfulRequests.percent.greaterThan(95)
  ).protocols(cqlProtocol)
 
}