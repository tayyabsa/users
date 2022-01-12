Feature: User Login

  Background: 
    * url 'http://localhost:8080/app'

  Scenario: User Login
    Given path '/v1/users/signup'
    Given request
    """
    {
        "userName": "tayyab",
        "password" : "password123",
        "email" : "abc@abc.com"
    }
    """
    When method POST
    Then status 200
    And match response ==
    """
    {
      "userId":"#string",
      "userName":"tayyab",
      "email":"abc@abc.com",
      "createdAt":"#string"
    }
    """

    Given path '/v1/users/login'
    Given request
    """
    {
      "password": "password123",
      "userName": "tayyab"
    }
    """
    When method POST
    Then status 200
    * def token = responseHeaders['Authorization']
    * print "Auth token" + token

    Given path '/v1/users'
    Given request
    """
    {
      "userName": "tayyab"
    }
    """
    And header Authorization = token
    When method POST
    Then status 200
