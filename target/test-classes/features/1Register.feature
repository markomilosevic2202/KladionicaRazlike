Feature: Register

#  Scenario: writing odds for ordinary matches
#    Given go to the address "https://www.maxbet.rs/ibet-web-client/#/home#top"
#    When choose which period you are watching "48"
#    When click on the page max-bet button football
#    When click on the page max-bet button select all
#    When click on the page max-bet button max bonus
#    When wait for the whole page to load
#    Then write all match in document
#    Given go to the address "https://www.orbitxch.com/customer/sport/1"
#    When take all the matches according to the given criteria
#    Then compare odds
#   And sort data en write in excel
#    Given go to the address "https://www.orbitxch.com/customer/sport/1"
#    And clear list
#    And find all the opposite odds for ordinary match
#    And sort data en write in excel ordinary odds plus



#  Scenario: entering odds for bonus odds
#    Given go to the address "https://www.maxbet.rs/ibet-web-client/#/home#top"
#    When choose which period you are watching "24"
#    When click on the page max-bet button football
#    When click on the page max-bet button max bonus
#    When wait for the whole page to load
#    Then write bonus match in document
#    Given go to the address "https://www.orbitxch.com/customer/sport/1"
#    When take all the matches according to the given criteria
#    Then compare bonus odds
#    And sort data en write in excel bonus odds
#    Then find all the opposite odds
#    And sort data en write in excel bonus odds plus
#    Then send email

  Scenario: entering odds for bonus odds
    Given go to the address "https://meridianbet.rs/sr/kladjenje"
    When click on the page meridian button football
    When click on the page meridian button ""
    When wait for the whole page to load meridian