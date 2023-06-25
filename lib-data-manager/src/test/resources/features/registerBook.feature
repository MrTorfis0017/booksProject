Feature: Register book

  Scenario: Register book with exist record in database
    Given Book exist:
      | id | name                     | language    | yearOfPublishing | genres           | author      |
      | *1 | Adventures of Tom Sawyer | UA          | 1876             | NOVEL, ADVENTURE | Mark Twain |
    When user register book
      | id | name                           | language | yearOfPublishing | genres           | author      |
      | *2 | Adventures of Huckleberry Finn | UA       | 1885             | NOVEL, ADVENTURE | Mark Twain |
    Then book should be registered
      | id | name                           | language    | yearOfPublishing | genres           | author      |
      | *1 | Adventures of Tom Sawyer       | UA          | 1876             | NOVEL, ADVENTURE | Mark Twain |
      | *2 | Adventures of Huckleberry Finn | UA          | 1885             | NOVEL, ADVENTURE | Mark Twain |

    Scenario: Register existent book
      Given Book exist:
        | id | name     | language | yearOfPublishing | genres           | author     |
        | *1 | Book one | UA       | 1876             | NOVEL, ADVENTURE | Mark Twain |
      When user register book
        | id | name     | language | yearOfPublishing | genres           | author     |
        | *2 | Book one | UA       | 1876             | NOVEL, ADVENTURE | Mark Twain |
      Then the book should not be registered