# Petstore Component java

## Description

A fully working and operational component template to use for starting development of your
own component for the [elastic.io platform](http://www.elastic.io "elastic.io platform").
This component comes with a basic architecture which you can use on the platform.

The component interacts with the [petstore sample server](https://petstore.elastic.io/docs/).

Our documentation on [building components in java](https://docs.elastic.io/guides/building-java-component.html) has more about each including file and their function.

## Requirements

To use this component you must be registered platform user. Please see our home
page at [https://www.elastic.io](https://www.elastic.io) to learn how to register.

### Authentication

To authenticate you use `secret` as an API key. For more details see the
[Petstore API docs](https://petstore.elastic.io/docs/).

## Triggers

We have 3 triggers which `GET` the information from the `/pet` endpoint of the API.
The difference is in the method used. Please explore the original code to learn
about the differences. You are welcome to extend the capabilities as you see fit.

### Get Pets By Status (HttpClient)

Retrieves pets from the Petstore API by given pet status using Apache HttpClient.

Here we have a configuration field `Pet Status` which can have only 3 values:

*   `Available` - select to get all pets with status `Available`.
*   `Pending` - select for `Pending`.
*   `Sold` - select for `Sold`.

### Get Pets By Status (JAX-RS)

Retrieves pets from the Petstore API by given pet status using Java API for
RESTful Web Services (JAX-RS). The configuration fields same as for above.

### Get Pets By Status With Dynamic Select Model

Retrieves pets from the Petstore API by given pet status. The available statuses
(`Available`, `Pending` and `Sold`) are retrieved from the Petstore API dynamically.

## Actions

### Create a Pet

Creates a new Pet by making a `POST` to `/pet` endpoint of the API. The input
fields are:

*   `petId` (required) - the Id of the pet.
*   `name` - the name of the pet.
*   `status` - the status which can have one `Available`, `Pending` and `Sold` values.
