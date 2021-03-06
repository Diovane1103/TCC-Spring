package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.Random

class PetClinicPetsStressTest extends Simulation {

  val httpProtocol = ConfigTest.httpProtocolPattern()

  def goToOwnerListPage() = {
    exec(http("Get owners List")
      .get("/api/owners/")
      .check(status.is(200)))
  }

  def goToASpecificOwner() = {
    exec(http("Getting specific Owner")
      .get("/api/owners/1")
      .check(status.is(200)))
  }

  def goToAddPetAndAddOne() = {
    exec(http("Adding Pet")
      .post("/api/pets")
      .body(StringBody("""{
                          "id": 3,
                          "name": "Lucas",
                          "birthDate": "2013/05/7",
                          "type": {
                            "id": 1,
                            "name": "cat"
                          },
                          "owner": {
                            "id": 1,
                            "firstName": "George",
                            "lastName": "Franklin",
                            "address": "110 W. Liberty St.",
                            "city": "Madison",
                            "telephone": "6085551023"
                          },
                            "visits": []
                         }""")).asJSON
      .check(status.is(201)))
  }

  def goToASpecificPet() = {
    exec(http("Getting specific Pet")
    .get("/api/pets/1")
    .check(status.is(200)))
  }

  def goToEditAPet() = {
    exec(http("Editing a Pet")
    .put("/api/pets/3")
    .body(StringBody("""{
                        "id": 3,
                        "name": "Lucas",
                        "birthDate": "2013/05/7",
                        "type": {
                          "id": 1,
                          "name": "cat"
                        },
                        "owner": {
                          "id": 1,
                          "firstName": "George",
                          "lastName": "Franklin",
                          "address": "110 W. Liberty St.",
                          "city": "Madison",
                          "telephone": "6085551023"
                        },
                          "visits": []
                       }""")).asJSON
      .check(status.is(204)))
  }

  def goToGetPetTypes() = {
    exec(http("Get the pet types")
    .get("/api/pets/pettypes")
    .check(status.is(200)))
  }

  val test = scenario("Pets scenario")
      .exec(goToOwnerListPage())
      .pause(2)
      .exec(goToASpecificOwner())
      .pause(2)
      .exec(goToAddPetAndAddOne())
      .pause(2)
      .exec(goToASpecificPet())
      .pause(2)
      .exec(goToEditAPet())
      .pause(2)
      .exec(goToGetPetTypes())
      .pause(2)

//  setUp(
//    test.inject(
//      atOnceUsers(10),
//      rampUsersPerSec(10) to(100) during(15 seconds),
//      constantUsersPerSec(100) during(60 seconds) randomized
//    )
//  ).protocols(httpProtocol)

    setUp(
      test.inject(
        atOnceUsers(100),
        rampUsersPerSec(100) to(1000) during(15 seconds),
        constantUsersPerSec(1000) during(60 seconds) randomized
      )
    ).protocols(httpProtocol)
}
