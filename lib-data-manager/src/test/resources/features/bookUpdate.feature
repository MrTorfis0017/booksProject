Feature: book updating

  Scenario: Update book at all
    Given Book exist:
      | id | name | language | yearOfPublishing | genres | author        |
      | *1 | It   | English  | 1986             | HORROR | Mike Wazowski |
    When user update book
      | id | name | language | yearOfPublishing | genres          | author       |
      | *1 | It 2 | Spanish  | 2020             | FANTASY, HORROR | Stephen King |
    Then this book should be updated
      | id | name | language | yearOfPublishing | genres          | author       |
      | *1 | It 2 | Spanish  | 2020             | FANTASY, HORROR | Stephen King |

  Scenario: Update name and yearOfPublishing
    Given Book exist:
      | id | name | language | yearOfPublishing | genres | author        |
      | *1 | 1984 | English  | 1949             | OTHER  | George Orwell |
    When user update book
      | id | name | yearOfPublishing |
      | *1 | 1985 | 1951             |
    Then this book should be updated
      | id | name | language | yearOfPublishing | genres | author        |
      | *1 | 1985 | English  | 1951             | OTHER  | George Orwell |