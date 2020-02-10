# SpringBootProject

# spring-boot-project
CRUD REST API Using Spring Boot, Spring Data and MongoDB
## Prerequisite
1. JDK 1.8 or above
2. MongoDB 4.0.1
3. Apache Maven 3

## Application Structure
1. Database - MongoDB NoSQL database to store and retrieve data.
2. RestController Layer - To control requests and responses.
3. Repository Layer - To store and retrieve data from database.

## Steps to Run the project 

## Step 1: Create database and collection in MongoDB

 ```
      use sales_db
 ```
 
 Create collection in the DB
  
 ```
      db.createCollection("inventory");
      db.createCollection("order");
 ```
 
 Insert some data to collection
 
 ```
 inventory:
{ 
   "prodId" : 2.0, 
    "name" : "Ganja2", 
    "description" : "smokey", 
    "price" : 19.0, 
    "quantity" : 290.0
}
 ```
 ```
 order:
{  
    "orderId" : NumberInt(3), 
    "items" : [
        {
            "prodId" : NumberInt(2), 
            "quantity" : NumberInt(65)
        }
    ], 
    "custName" : "Athi", 
    "orderDate" : "01-01-2020", 
    "orderStatus" : "Failed", 
    "_class" : "treez.model.Order"
}
 ```
 
 ## Step 2: Download and run the project
 
1. Download The Project
2. Go to the project folder where pom.xml file is found
3. Run following command
      ```
      mvn eclipse:eclipse
      ```
      ```
      mvn clean compile
      ```
      ```
      mvn spring-boot:run
      ```
      
## Step 3: Test GET/POST/PUT/DELETE REST endpoints
