# Mealmate

## Project Description

Mealmate is a web application built to facilitate online ordering for restaurant businesses. The system allows administrators to manage accounts, products, and orders, as well as generate sales reports. The frontend is built with Next.js, and the backend is powered by Spring Boot, with MySQL as the database.

## Features

- **Account Management**: Administrators can manage user accounts.
- **Product Management**: Manage the catalog of products available for order.
- **Order Management**: Handle and process customer orders.
- **Sales Reports**: Generate comprehensive sales reports.

## Installation

### Prerequisites

- Node.js and npm installed
- Java Development Kit (JDK) installed
- MySQL database

### Frontend Setup

1. Clone the repository:
    ```bash
    git clone https://github.com/suyq8/mealmate.git
    cd mealmate/frontend
    ```

2. Install dependencies:
    ```bash
    npm install
    ```

3. Run the development server:
    ```bash
    npm run dev
    ```

### Backend Setup

1. Navigate to the backend directory:
    ```bash
    cd mealmate/backend
    ```

2. Configure the `application.properties` file with your MySQL database credentials:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/yourdbname
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    ```
