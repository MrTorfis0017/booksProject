Feature: Saving audit-info in DB

  Scenario: save changes in empty database
    Given There are no books with the following IDs in the database
      | bookId |
      | 1      |
      | 2      |
    When consumer saving book changes with parameters
      |id | bookId | name        | language | yearOfPublishing | authorId | authorFirstName| authorSecondName | authorNationality| changeDate          | changedBy | genres                  |
      |*1 | 1      | The Witcher | UA       | 2018             | 1        |  Vladislav     | Gorobec          | UA               | 2023-06-05 13:00:00 | admin     |  3 HORROR, 4 FANTASY    |
      |*2 | 2      | Java        | EN       | 2018             | 2        |  Enric         | Robert           | Poland           | 2020-01-02 13:00:00 | admin     |  5 Technical, 6 Classics|
    Then book audit record should be created in DB with parameters
      |id | bookId | version| name        | language | yearOfPublishing | authorId | authorFirstName| authorSecondName | authorNationality| changeDate          | changedBy | genres                  |
      |*1 | 1      | 1     | The Witcher | UA       | 2018             | 1        |  Vladislav     | Gorobec          | UA               | 2023-06-05 13:00:00 | admin     |  3 HORROR, 4 FANTASY    |
      |*2 | 2      | 1     | Java        | EN       | 2018             | 2        |  Enric         | Robert           | Poland           | 2020-01-02 13:00:00 | admin     |  5 Technical, 6 Classics|

  Scenario: some book`s versions already exist in audit DB and yearOfPublishing is changing
    Given book record exists in audit DB with parameters
      |id | bookId | name        | language | yearOfPublishing | authorId | authorFirstName| authorSecondName | authorNationality| changeDate          | changedBy | genres                  |
      |*1 | 1      | The Witcher | UA       | 2018             | 1        |  Vladislav     | Gorobec          | UA               | 2023-06-05 13:00:00 | admin     |  3 HORROR, 4 FANTASY    |
    When consumer saving book changes with parameters
      |id | bookId | name        | language | yearOfPublishing | authorId | authorFirstName| authorSecondName | authorNationality| changeDate          | changedBy | genres                  |
      |*2 | 1      | The Witcher | EU       | 2019             | 1        |  Gleb          | Gorobec          | UA               | 2023-06-05 13:00:00 | admin     |  5 Technical, 6 Classics|
    Then book audit record should be created in DB with parameters
      |id | bookId | version | name        | language | yearOfPublishing | authorId | authorFirstName| authorSecondName | authorNationality| changeDate          | changedBy | genres                  |
      |*2 | 1      | 2       | The Witcher | EU       | 2019             | 1        |  Gleb          | Gorobec          | UA               | 2023-06-05 13:00:00 | admin     |  5 Technical, 6 Classics|
