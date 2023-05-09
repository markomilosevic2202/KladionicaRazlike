Feature: Register

  Scenario: verify that the registry function is working
     Given go to the address "https://www.maxbet.rs/ibet-web-client/#/home#top"
    When choose which period you are watching "48"
    When click on the page max-bet button football
    When click on the page max-bet button select all
    When click on the page max-bet button max bonus
    When wait for the whole page to load
    Then write all match in document

