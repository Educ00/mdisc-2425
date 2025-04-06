# Supplementary Specification (FURPS+)

## Functionality

_Specifies functionalities that:
&nbsp; &nbsp; (i) are common across several US/UC;
&nbsp; &nbsp; (ii) are not related to US/UC, namely: Audit, Reporting and Security._

## (i)

### Map and Scenario Configuration (US01, US02, US03, US04)
- Create maps with defined dimensions and names.
- Place elements such as cities and industries at specific coordinates.
- Validate city and industry names and ensure values are positive.
- Develop scenarios with rules for industry generation, port operations, and available locomotion types.

### Station and Railway Network Configuration (US05, US06, US07, US08)
- Construct various types of stations (Depot, Terminal, Station).
- Automatically assign names to stations based on the nearest city.
- Upgrade stations with buildings that have specific availability constraints.
- List and select stations with details about buildings and cargo demand/supply.
- Build railway lines by selecting stations from a registered list.

### Train and Locomotive Configuration (US09, US10, US11)
- Purchase locomotives based on scenario and date constraints.
- Assign locomotives to routes, ensuring only valid stations and cargoes are included.
- List all trains, grouped by locomotive type and sorted alphabetically.
- Display train details, including current cargoes.


## (ii)
- Validation of business rules must be respected when recording and updating data.
- All those who wish to use the application must be authenticated with a password of seven alphanumeric characters, including three capital letters and two digits.

## Usability

_Evaluates the user interface. It has several subcategories,
among them: error prevention; interface aesthetics and design; help and
documentation; consistency and standards._

- The application documentation must be in English.

## Reliability

_Refers to the integrity, compliance and interoperability of the software. The requirements to be considered are: frequency and severity of failure, possibility of recovery, possibility of prediction, accuracy, average time between failures._

- The application ought to employ object serialization to guarantee the persistence of the data in two successive runs.

## Performance

_Evaluates the performance requirements of the software, namely: response time, start-up time, recovery time, memory consumption, CPU usage, load capacity and application availability._

- (No specific requirements provided)

## Supportability

_The supportability requirements gathers several characteristics, such as:
testability, adaptability, maintainability, compatibility,
configurability, installability, scalability and more._

- The class structure must be designed to allow easy maintenance and the addition of new features following the best Object-Oriented (OO) practices.
- During system development, the team must: (i) adopt best practices for identifying requirements and for OO software analysis and design; (ii) adopt recognized coding standards (e.g., CamelCase); (iii) use Javadoc to generate useful documentation for Java code.
- The development team must implement unit tests for all methods, except for the methods that implement Input/Output operations. Unit tests should be implemented using the JUnit 5 framework. The JaCoCo plugin should be used to generate the coverage report.
- All the images/figures produced during the software development process should be recorded in SVG format.

## +

### Design Constraints

_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._

- The application must be developed in Java language.

### Implementation Constraints

_Specifies or constraints the code or construction of a system such
such as: mandatory standards/patterns, implementation languages,
database integrity, resource limits, operating system._

- The development team must implement unit tests for all methods, except for the methods that implement Input/Output operations. Unit tests should be implemented using the JUnit 5 framework. The JaCoCo plugin should be used to generate the coverage report.

### Interface Constraints

_Specifies or constraints the features inherent to the interaction of the
system being developed with other external systems._

- (No specific requirements provided)

### Physical Constraints

_Specifies a limitation or physical requirement regarding the hardware used to house the system, as for example: material, shape, size or weight._

- (No specific requirements provided)