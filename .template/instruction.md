### Event Monitoring

#### Faster Problem Resolution: Quickly trace and diagnose failures at specific stages in complex business processes, enabling real-time problem-solving.

#### Improved System Visibility: Easily understand and manage workflows in large-scale systems by visualizing related events through the Correlation Key.

#### Validation of Design and Implementation Consistency: Effortlessly verify whether newly developed features or system modifications align with the designed event-storming model.

#### Efficient Event Management: Intuitively check event types, timestamps, and detailed information from filtered event lists, enhancing operational efficiency.

#### Ensured Compliance with Business Requirements: Visualize progress to confirm that business requirements are being properly implemented.

#### Enhanced Team Collaboration: Facilitate smooth communication and collaboration between development and business teams by providing clear visibility into event flows


### How To Run

#### Correlation Key and Search Key Settings

##### In Edit Attributes in the Event Panel, set the attributes to be used as a Correlation Key and a Search Key.

###### Correlation Key: A unique identification number that connects different activities that occur in a single business process.
![image](https://github.com/user-attachments/assets/041d1fcd-bc5a-4e45-9bcf-1715b4a1362a)
###### Search Key: Key used for event filtering searches with correlation keys.
![image](https://github.com/user-attachments/assets/c88a79dc-d6ae-4265-92c8-03843d899c7b)

#### Run EventCollections
```
cd eventcollections
mvn spring-boot:run
```
Make sure it runs with port number 9999.


### Example Video
[Food Delivery Service](https://www.youtube.com/watch?v=Y3Si5eMNgTM)
