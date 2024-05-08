# user-solutions

This is the solution to the assigment from [Clear Solutions](https://www.linkedin.com/company/clearsolutionsltd/about/) based on [these specifications](https://docs.google.com/document/d/1LosRgr72sJYcNumbZKET7uiIJ3Un_ORl25Psn1Dd9hw/edit?usp=sharing).

## General information

The project is within the [users](users/) repository. It is a Restful API , that creates , updates, reads and deletes entities of type User.
The endpoints within the [UserController](users/src/main/java/example/users/UserController.java) are explained in the following table:

| HTTP Method | Endpoint | Description | Parameters | Returns |
|-------------|----------|-------------|------------|---------|
| `GET` | `/users/{requestedId}` | Retrieves a user by their ID. | `requestedId`: The ID of the user to retrieve.<br>`principal`: The principal object representing the currently authenticated user. | A `ResponseEntity` containing the user if found, or a not found response if not found. |
| `GET` | `/users` | Retrieves a list of users based on the provided pagination parameters and the authenticated principal. | `pageable`: The pagination parameters for retrieving the users.<br>`principal`: The principal object representing the currently authenticated user. | A `ResponseEntity` containing the list of users. |
| `GET` | `/users/{startDate}/{endDate}` | Retrieves a list of users owned by the authenticated principal between the specified start and end dates. | `startDate`: The start date of the range.<br>`endDate`: The end date of the range.<br>`principal`: The principal object representing the currently authenticated user. | A `ResponseEntity` containing the list of users if found, or a not found response if the list is empty. |
| `POST` | `/users` | Creates a new user. | `newUser`: The user object containing the details of the new user.<br>`ucb`: The UriComponentsBuilder used to build the URI for the new user.<br>`principal`: The principal object representing the currently authenticated user. | A `ResponseEntity` with a status code of 201 (Created) and the URI of the new user in the Location header. |
| `PUT` | `/users/{requestedId}` | Updates a user with the provided information. | `requestedId`: The ID of the user to be updated.<br>`updatedUser`: The updated user object containing the new information.<br>`principal`: The principal object representing the currently authenticated user. | A `ResponseEntity` with no content if the user was successfully updated, or a `ResponseEntity` with a not found status if the user was not found. |
| `DELETE` | `/users/{requestedId}` | Deletes a user with the specified ID. | `requestedId`: The ID of the user to delete.<br>`principal`: The principal object representing the currently authenticated user. | A `ResponseEntity` with no content if the user was successfully deleted, or a `ResponseEntity` with not found status if the user does not exist or the authenticated user is not the owner. |

## How to run and test the Project

1. Open vscode or you prefered editor and/or IDE.
2. Open the terminal and run the command `git clone https://github.com/low-perry/user-solutions.git`
3. Enter the user-solutions directory `cd user-solutions` and if you are using vscode `code .`
4. After the editor and/or IDE are set to this directory then proceed to enter the directory where the project lies. `cd users`.
5. To run the tests run the command `./gradlew test`
6. To run the project in the test enviroment run `./gradlew bootTestRun`.

The project should be running on port `8080`. As specified by the properties file.

If the app is to be tested on the browser, you will be prompted to enter a username as password (The list of owners is explained later in this README file)
If the app is to be tested via Postman, on authorization choose Basic Auth and for a user choose, one of the users specified at the project details section. (Preferably `admin` : `abc123` for both prowser and postman).

## Project details

The application uses a h2 in memory database, with the schema represented in the [schema](users/src/main/resources/schema.sql) file.
And some custom data is being added anytime the app runs. The data entered is specified at the [data](users/src/test/resources/data.sql) file.
The only model is the [User](users/src/main/java/example/users/User.java) class, as required by the google doc, with an additional field of `String owner` to represent the creator of the User entity. The field is added to provide some basic authentication and authorization.
The necessary fields are validated , through jakarta annotations and a custom annotation [@Adult](users/src/main/java/example/users/validation/Adult.java) for the birthday field. I also introduced The [AgeHelper](users/src/main/java/example/users/validation/AgeHelper.java) to retrive the age limit from the properties file.
In case the data entered breaks the annotation's contract the server would respond with a `HttpStatus.BAD_REQUEST` and the fields that contain errors with the error message along side it.
e.g

For the post endpoint, if the body of the request conatins the following data:
```JSON
{
    "id": null,
    "name": "alex",
    "lastName": "brown",
    "email": "alex.brown@email.com",
    "birthday": "2008-09-15",
    "phoneNumber": "555555555",
    "address": "789 Oak St"
}
```
The repsonse would be the following:
```JSON
    {
    "birthday": "Must be at least 18 years old"
    }
```

Within the [SecurityConfig](users/src/main/java/example/users/SecurityConfig.java) there are three owners added for testing purposes. The owners are: 
| Owner | Password | Role |
|-------------|----------|----------|
|`admin`|`abc123`| `USER_OWNER` |
|`dario`|`abc123`| `NON_OWNER` |
|`paris`|`abc123`| `USER_OWNER` |

The exceptions are handled globally at [ApplicationExceptionHandler](users/src/main/java/example/users/advice/ApplicationExceptionHandler.java).
And there is custom exception named [WrongDateParameterException](users/src/main/java/example/exceptions/WrongDateParameterException.java) thrown by the endpoint that queries the database for users with birthdays that fall within a range of dates.




