
# Food Delivery App

FoodDeliveryApp is a Spring Boot Application for Developing REST Apis for Food Order App.


## Dependancies

Elastic Search :

Step 1. Change directory in command line using cd {project directory}/src/main/resources (docker-compose.yaml file location)

Step 2. Then Run "docker-compose up" from command line.

 
```bash
cd {project directory}/src/main/resources

docker-compose up
```

# Time Constraint

1. Should use BCryptPasswordEncoder but used NoOpPasswordEncoder due to time constraints.

2. RESTAURANT_ID deleting in CONSUMER_ORDER table, could have created a RESTAURANT_SNAPSHOT table for CONSUMER_ORDER's restaurant reference. Mainly for fetching Restaurant name.

3. Restaurant can only be linked to an already created owner from data.sql during application startup. No new restaurant owner will be created on restaurant creation.

4. Pagination removed from Items filter API due to time constraints.


## GitHub Link
[Food Delivery App](https://github.com/dagarachit/FoodDeliveryApp)

"# FoodDeliveryApp-v2" 
