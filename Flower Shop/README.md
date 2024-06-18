# Web Technologies - Laboratory 11 - Exercise 01

## Online Library Management System

### Objective: Develop a back-end system for an online library management application. The system should expose REST endpoints for managing books and memberships, and use an SQL database for data storage.

### Key Features and Constraints:

1. **Add a Book Endpoint**:
    - **Request Fields**: `bookId` (unique), `title`, `author`, `publicationYear`, `genre`, `status`.
    - **Constraints**:
        - `bookId`: Unique and non-nullable.
        - `title`: Non-nullable, maximum 100 characters.
        - `author`: Non-nullable, maximum 50 characters.
        - `publicationYear`: Non-nullable, must be a valid year (not in the future).
        - `genre`: Must be one of predefined genres (e.g., FICTION, NONFICTION, SCIENCE, HISTORY).
        - `status`: Must be either AVAILABLE or BORROWED.
    - **Business Logic**: Reject addition if a book with the same `title` and `author` already exists.

2. **Update Book Status Endpoint**:
    - **Request Fields**: `bookId`, `status`.
    - **Constraints**:
        - Validate that `bookId` exists.
        - `status`: Must be either AVAILABLE or BORROWED.
    - **Business Logic**: Ensure a book can only be marked BORROWED if its current status is AVAILABLE.

3. **Add a Member Endpoint**:
    - **Request Fields**: `memberId`, `name`, `email`, `membershipType`.
    - **Constraints**:
        - `memberId`: Unique and non-nullable.
        - `name`: Non-nullable, maximum 50 characters.
        - `email`: Non-nullable, must be a valid email format.
        - `membershipType`: Must be either STANDARD or PREMIUM.
    - **Business Logic**: Reject addition if an email is already registered.

4. **Unit Tests**: Implement unit tests for all service methods with a target of at least 75% code coverage.

5. **Database Choice**: You are free to choose the type of SQL database.
