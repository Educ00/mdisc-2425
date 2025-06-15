# US006 - Upgrade a Station

## 3. Design

### 3.1. Rationale

### 3.1. Rationale

| Interaction ID | Question: Which class is responsible for...                        | Answer                    | Justification (with patterns)                                                                     |
|:--------------:|:-------------------------------------------------------------------|:--------------------------|:--------------------------------------------------------------------------------------------------|
|     Step 1     | ... interacting with the actor?                                    | UpgradeStationUI          | Pure Fabrication: there is no reason to assign this responsibility to any existing domain class.  |
|                | ... coordinating the use case?                                     | UpgradeStationController  | Controller: coordinates the flow between UI, repositories, and domain entities for this use case. |
|     Step 2     | ... obtaining the repositories singleton?                          | Repositories              | Information Expert: maintains access to all repositories.                                         |
|     Step 3     | ... obtaining the StationRepository?                               | Repositories              | Information Expert: provides access to StationRepository.                                         |
|     Step 4     | ... knowing the stations available for upgrade?                    | StationRepository         | Information Expert: manages and provides available stations for upgrade.                          |
|     Step 5     | ... creating the list of Station?                                  | StationRepository         | Creator: responsible for creating the list of Station objects.                                    |
|     Step 6     | ... mapping Station to StationDTO?                                 | StationMapper             | Pure Fabrication: separates mapping logic from domain classes.                                    |
|     Step 7     | ... creating the list of StationDTO?                               | StationMapper             | Creator: responsible for creating DTOs from domain objects.                                       |
|     Step 8     | ... returning the list of StationDTO to the controller?            | StationMapper             | Pure Fabrication: responsible for data transformation and transfer.                               |
|     Step 9     | ... passing the list of StationDTO to the UI?                      | UpgradeStationController  | Controller: coordinates data flow between domain and UI.                                          |
|    Step 10     | ... showing available stations to the player?                      | UpgradeStationUI          | Pure Fabrication: responsible for presenting data to the user.                                    |
|    Step 11     | ... getting the Station by ID?                                     | StationRepository         | Information Expert: provides the station corresponding to the given ID.                           |
|    Step 12     | ... obtaining the current date?                                    | Scenario                  | Information Expert: responsible for providing the current scenario date.                          |
|    Step 13     | ... obtaining upgrades installed in the station?                   | Station                   | Information Expert: knows the upgrades installed in itself.                                       |
|    Step 14     | ... obtaining the UpgradeBuildingRepository?                       | Repositories              | Information Expert: provides access to UpgradeBuildingRepository.                                 |
|    Step 15     | ... knowing the available upgrade buildings for the station?       | UpgradeBuildingRepository | Information Expert: manages and provides available upgrades for the station.                      |
|    Step 16     | ... creating the list of UpgradeBuilding?                          | UpgradeBuildingRepository | Creator: responsible for creating the list of UpgradeBuilding objects.                            |
|    Step 17     | ... mapping UpgradeBuilding to UpgradeBuildingDTO?                 | UpgradeBuildingMapper     | Pure Fabrication: separates mapping logic from domain classes.                                    |
|    Step 18     | ... creating the list of UpgradeBuildingDTO?                       | UpgradeBuildingMapper     | Creator: responsible for creating DTOs from domain objects.                                       |
|    Step 19     | ... returning the list of UpgradeBuildingDTO to the controller?    | UpgradeBuildingMapper     | Pure Fabrication: responsible for data transformation and transfer.                               |
|    Step 20     | ... passing the list of UpgradeBuildingDTO to the UI?              | UpgradeStationController  | Controller: coordinates data flow between domain and UI.                                          |
|    Step 21     | ... showing valid upgrade buildings to the player?                 | UpgradeStationUI          | Pure Fabrication: responsible for presenting data to the user.                                    |
|    Step 22     | ... getting the UpgradeBuilding by ID?                             | UpgradeBuildingRepository | Information Expert: provides the upgrade corresponding to the given ID.                           |
|    Step 23     | ... showing the upgrade preview and asking for confirmation?       | UpgradeStationUI          | Pure Fabrication: responsible for user interaction.                                               |
|    Step 24     | ... applying the upgrade to the station?                           | UpgradeStationController  | Controller: coordinates the upgrade application process.                                          |
|    Step 25     | ... checking if the upgrade belongs to the same exclusivity group? | UpgradeBuilding           | Information Expert: responsible for checking exclusivity groups.                                  |
|    Step 26     | ... replacing an installed upgrade with another?                   | Station                   | Information Expert: responsible for managing installed upgrades.                                  |
|    Step 27     | ... adding an upgrade to the station?                              | Station                   | Information Expert: responsible for managing installed upgrades.                                  |
|    Step 28     | ... informing the success of the operation?                        | UpgradeStationUI          | Pure Fabrication: responsible for presenting messages to the user.                                |

### Systematization ##

According to the taken rationale, the conceptual classes promoted to software classes are:

* Station
* UpgradeBuilding
* Scenario

Other software classes (i.e. Pure Fabrication, Controller) identified:

* UpgradeStationUI
* UpgradeStationController
* Repositories
* StationRepository
* UpgradeBuildingRepository
* ApplicationSession
* CurrentSession
* StationMapper
* UpgradeBuildingMapper
* StationDTO
* UpgradeBuildingDTO

## 3.2. Sequence Diagram (SD)

### Full Diagram

This diagram shows the full sequence of interactions between the classes involved in the realization of this user story.

![Sequence Diagram - Full](svg/US006-SD-full.svg)


## 3.3. Class Diagram (CD)

![Class Diagram](svg/US006-CD.svg)