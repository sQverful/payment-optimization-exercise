# Payment Optimization Exercise

## Problem Statement

We have bank branches in multiple countries and due to regulatory restrictions, we can't make direct payments from all bank branches.

Currently, we have connectivity between the following branches:
- A to B
- A to C
- C to B
- B to D
- C to E
- D to E
- E to D
- D to F
- E to F

To make a payment between A and D, for example, we could use the following sequence: A to B, B to D.

Branches charge to process payments. Any payment that leaves a branch is subject to a charge. The receipt of funds into a branch is free of charge. The cost for each branch to transfer funds is shown below:

- A: 5
- B: 50
- C: 10
- D: 10
- E: 20
- F: 5

Your task is to develop a solution that is able to compute the cheapest way to make a payment between two branches.

Full description: [Technical Exercise - Payment Optimization PDF](https://github.com/your-username/your-project/blob/master/docs/Technical_Exercise_-_Payment_Optimization.pdf).

## Solution

### Tech Stack:

- **Java 17**:
- **Maven 4.0.0 (compatible with Maven 3.x or higher)**
- **Spring Boot 3.4.0**

### Implementation notes

App runs on :8080 port

The implementation consists of the following components:

1. **PaymentService Interface**: Defines the method to process payments.
2. **PaymentServiceImpl**: Implements the PaymentService interface.
3. **Branch and BranchConnection Models**: Represent the branches and connections between them.
4. **BranchRepository and BranchConnectionsRepository**: Handle data access for branches and their connections.
5. **PaymentController**: Exposes the REST API endpoints.
6. **GraphUtil**: Utility class which builds directed weighted graph to find the shortest path using Dijkstra's algorithm.

## Quick Start

To run the application use the following commands:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/
   ```

2. **Navigate to the project directory:**
   ```bash
   cd payment-optimization
   ```

3. **Compile, com & package:**
   ```bash
    ./mvnw clean install
   ```

4. **Run the application:**
   ```bash
    ./mvnw spring-boot:run
   ```
### Available endpoints:
*if you have Postman you can import a collection Payments Optimization Api.postman_collection.json
([Guide](https://docs.tink.com/entries/articles/postman-collection-for-account-check))

**Get all branches:**

   ```bash
   curl -X GET http://localhost:8080/api/v1/branches
   ```
**Create new branch:**

   ```bash
   curl -X POST http://localhost:8080/api/v1/branches \
     -H "Content-Type: application/json" \
     -d '{
           "name": "G",
           "transferCost": 5
         }'

   ```
   
**Get all branch connections:**

   ```bash
   curl -X GET http://localhost:8080/api/v1/branches/connections
   ```
**Create connection between branches**

   ```bash
   curl -X POST http://localhost:8080/api/v1/branches/connections \
     -H "Content-Type: application/json" \
     -d '{
           "originBranch": "A",
           "destinationBranch": "G"
         }'

   ```

**Process payment**
   ```bash
   curl -X POST http://localhost:8080/api/v1/payment/process \
     -H "Content-Type: application/json" \
     -d '{
           "originBranch": "A",
           "destinationBranch": "D"
         }'
   ```
