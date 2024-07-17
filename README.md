
# Food Delivery App

FoodDeliveryApp is a Spring Boot Application for Developing REST Apis for Food Order App.


# Dependancies

Elastic Search :

Step 1. Change directory in command line using cd {project directory}/src/main/resources (docker-compose.yaml file location)

Step 2. Then Run "docker-compose up" from command line.

 
```bash
cd {project directory}/src/main/resources

docker-compose up
```
# Project Setup
Servers context-path is : /FoodDeliveryApp

Import project in Eclipse and let maven fetch all jar from its repository. Let the project build.

Make sure elastic search is up and running, can check by hitting http://localhost:9200/.

Once the project is running successfully, open swagger at http://localhost:8080/FoodDeliveryApp/swagger-ui/index.html and now we can start testing al the Api's.

H2-Database console is available at http://localhost:8080/FoodDeliveryApp/h2-console/login.jsp

# Project Details

## High Level API Functionality

### Anyone with appropriate credentials can access below API's

Method : POST, URL - /auth/register  
Function : Used to Register/Create User.

Method : POST, URL - /auth/login  
Function : Used to Login as an existing User.

### Restaurant Admin Roles' USERS : Can Perform Below Tasks. 
[Default Login Credentials to start off testing - UserName : rudvijn, Password : test]

Method : POST, URL - /api/v1/restaurants/ -  CreateRestaurantRequestDTO (request Dto), CreateRestaurantResponseDTO (response Dto)  
Function : Used to Add Restaurant Account.

Method : PUT, URL - /api/v1/restaurants/ - UpdateRestaurantRequestDTO (request Dto), UpdateRestaurantResponseDTO (response Dto)  
Function : Used to Update Restaurant Account.

Method : DELETE, URL - /api/v1/restaurants/{id}  
Function : Used to Delete Restaurant Account.

### Restaurant Owner Roles' USERS : Can Perform Below Tasks. 
[Default Login Credentials to start off testing - UserName : rachitd, Password : test]

Method : POST, /api/v1/restaurants/item - CreateRestaurantItemRequestDTO (request Dto), CreateRestaurantItemResponseDTO (response Dto)  
Function : Used to create Restaurant Items.

Method : PATCH, /api/v1/restaurants/{restaurantId}/order/{orderId}/status/{orderStatus}  
Function : Updates Order status for specific order of that specific restaurant.  
Checks : checks if sent orderStatus is actually the next orderStatus and whether the order is of the correct restuarant.

Method : PATCH, /api/v1/restaurants/{restaurantId}/order/{orderId}/approve  
Function : Updates Order status to OrderStatus.RESTAURANT_ACCEPTED for specific order of that specific restaurant.  
Checks : same checks as above.

Method : PATCH,  /api/v1/restaurants/updateTimings - UpdateRestaurantTimingRequestDTO passed as a request.  
Function : Updates Restaurant Timings.

Method : PATCH, /api/v1/restaurants/item/{itemId}/price/{price}  
Function : Updates Restaurant Item Price.

Method : PATCH, /api/v1/restaurants/item/{itemId}/available/{isAvailable}  
Function : Updates Restaurant Item Availability.

Method : DELETE, URL - /api/v1/restaurants/item/{restaurantItemId}  
Function : Deletes Restaurant's Item.

### Restaurant Customer Roles' USERS : Can Perform Below Tasks.
[Default Login Credentials to start off testing - UserName : rohit, Password : test]

Method : POST, URL - /api/v1/consumers/order  
Function : Places an Order.

Method : POST, URL - /api/v1/consumers/filter  
Function : Filter Restaurants Basis of Name, isVeg, Rating, Cuisine, provides top-Rated itme as well.

Method : GET, URL - /api/v1/consumers/{consumerId}/restaurant/browse/pageNo/{pageNo}/pageSize/{pageSize}  
Function : Browse a restaurant basis of location proximity.

Method : GET, URL - /api/v1/consumers/personalized/{consumerId}/pageNo/{pageNo}/pageSize/{pageSize}  
Function : Provides Personalized Food Items of a Customer basis no. of times an order was ordered from a restaurant.

Method : GET, URL - /api/v1/consumers/order/status/{orderId}  
Function : Provides Order Status for an Order.

Method : GET, URL - /api/v1/consumers/order/pastOrder/{consumerId}/pageNo/{pageNo}/pageSize/{pageSize}  
Function : Provides Past Orders for a Customer.


### Time Constraint

1. Should use BCryptPasswordEncoder but used NoOpPasswordEncoder due to time constraints.

2. RESTAURANT_ID deleting in CONSUMER_ORDER table, could have created a RESTAURANT_SNAPSHOT table for CONSUMER_ORDER's restaurant reference. Mainly for fetching Restaurant name.

3. Restaurant can only be linked to an already created owner from data.sql during application startup. No new restaurant owner will be created on restaurant creation.

4. Pagination removed from Items filter API due to time constraints.

5. Restaurant Browse based on timing not happening, only basis it nearby location is happening


## GitHub Link
[Food Delivery App](https://github.com/dagarachit/FoodDeliveryApp)

"# FoodDeliveryApp-v2" 
