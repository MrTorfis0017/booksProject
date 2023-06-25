Feature: Searching book

  Scenario: Searching books by id
    Given In the store the following books
      | id | name              | language | yearOfPublishing | genres            | author        |
      | *1 | Doctor Strange    | English  | 2022             | FANTASY, HORROR   | Tom Kruz      |
      | *2 | Divo              | Ukrainian| 1977             | TRILLER           | Michel Boutur |
    When user getting book from the storage by ids:
      |id  |
      | *1 |
    Then the book should be returned in responce with this parametres
      | id | name              | language | yearOfPublishing | genres            | author        |
      | *1 | Doctor Strange    | English  | 2022             | FANTASY, HORROR   | Tom Kruz      |