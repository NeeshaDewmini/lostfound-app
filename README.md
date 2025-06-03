# lostfound-app

A web application built with Spring Boot for managing lost and found items within an organization or university. Users can report lost items, staff/admins can view requests, and admins can approve or reject requests.

---

## Features

-  **User Roles**: Supports `USER`, `STAFF`, and `ADMIN`
-  **Item Management**: Create, view, update, and delete lost/found items
-  **Request System**: Users can request items they lost
-  **JWT Authentication**: Secure login and role-based access control
-  **Admin Approval**: Only admins can approve/reject requests
-  **View Personal Requests**: Users can view their submitted requests

---

## Technologies Used

- **Java 21**
- **Spring Boot 3**
- **Spring Security (JWT)**
- **MySQL**
- **JPA/Hibernate**
- **Maven**

---

## Folder Structure

│<br>
├── controller/ # REST controllers<br>
├── dto/ # Data Transfer Objects<br>
├── model/ # Entity classes<br>
├── repositories/ # JPA Repositories<br>
├── security/ # JWT filters and utilities<br>
├── service/ # Service layer<br>
└── application.properties

---

## Run the App

mvn spring-boot:run


## Sample Endpoints


**Auth**


POST /api/auth/signup

POST /api/auth/signin<br><br>

**Items**


POST /api/items (requires Bearer token)<br>

GET /api/items<br>

GET /api/items/status/{status}<br>

GET /api/items/{id}<br><br>

**Requests**<br>
POST /api/requests<br>

GET /api/requests/my<br>

PUT /api/requests/{id}?status=APPROVED (ADMIN only)<br>

DELETE /api/requests/{id}<br><br>

**Users**<br>
GET /api/users (ADMIN/STAFF<br>

GET /api/users/{id}<br>


## 👤 Developed By
N.Janeesha Dewmini






