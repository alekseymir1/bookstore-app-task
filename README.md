# Online Bookstore Application

## Overview

This Spring Boot-based REST API provides a system for managing an online bookstore's inventory, purchase process, and customer loyalty program.

## Project Focus

The project includes endpoint and service and dto layer.

The project does not include:
- UI implementation
- Authentication and authorization
- Extensive error handling

Basic unit tests are included for the service layer.

## Features

### 1. Inventory Management
- Add new books to the inventory
- Modify details of existing books
- Remove books from the inventory

### 2. Book Pricing
The price of books is calculated based on:
- Book type (New Releases, Regular, Old Editions)
- Purchase size (bundle discounts for 3+ books)
- Loyalty points

#### Book Types and Pricing Rules
- **New Releases**: Always 100% of the base price
- **Regular**: 100% of the base price, with a 10% discount for bundles of 3+ books
- **Old Editions**: 20% discount from the base price, with an additional 5% discount for bundles of 3+ books

### 3. Loyalty Points System
- 1 loyalty point is awarded for each purchased book
- When 10 loyalty points are accumulated, the customer can get one Regular or Old Edition book for free
- After redeeming a free book, loyalty points reset to 0

## Architecture

The application follows a layered architecture with:

1. **Controllers**: Handle HTTP requests and responses
   - `BookController`: Manages book inventory and purchases
   - `CustomerController`: Manages customers and loyalty points

2. **Services**: Contain business logic
   - `BookService`: Handles book-related operations
   - `CustomerService`: Handles customer-related operations
   - `LoyaltyService`: Manages loyalty points and free book redemption

3. **Repositories**: Provide data access
   - `BookRepository`: Accesses book data
   - `CustomerRepository`: Accesses customer data

4. **Models**: Represent domain entities
   - `Book`: Represents a book with id, title, base price, and type
   - `Customer`: Represents a customer with id, name, and loyalty points
   - `BookType`: Enum for different types of books (NEW_RELEASE, REGULAR, OLD_EDITION)

5. **DTOs**: Transfer data between layers
   - `PurchaseRequest`: Contains customer ID and book IDs for a purchase
   - `PurchaseResponse`: Contains total price, loyalty points earned, and free books

   

## How to Run

1. Clone the repository
2. Navigate to the project directory
3. Run `mvn spring-boot:run`
4. The application will be available at `http://localhost:8080`

## API Documentation with Swagger UI

This application includes Swagger UI for interactive API documentation and testing. Swagger UI provides a user-friendly interface to explore and test all available endpoints.

### Accessing Swagger UI

1. Start the application using the instructions above
2. Open your browser and navigate to: `http://localhost:8080/swagger-ui.html`

### Using Swagger UI

1. The Swagger UI page displays all available API endpoints organized by controller (Book, Customer, Purchase)
2. Click on any endpoint to expand it and see detailed information:
   - Description of the endpoint
   - Required parameters
   - Request body schema (for POST and PUT requests)
   - Possible response codes and their meanings
   - Response body schema
3. To test an endpoint:
   - Click the "Try it out" button
   - Fill in any required parameters or request body
   - Click "Execute" to send the request
   - View the response below, including status code, response headers, and response body

### API Documentation Endpoints

- Swagger UI: `http://localhost:8080/swagger-ui.html`
