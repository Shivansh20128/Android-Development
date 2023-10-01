# AndroidDevelopment--Monitor-your-walk

This is an app that shows the number of steps and direction of your walk. 

It also finds out when you take a lift vs. stairs to go up/down the floors when you are inside a building. 

I have used pedestrian dead reckoning (PDR) to compute displacement from the accelerometer reading.

  1. For this, first, I compute the stride length (there are some heuristics to calculate stride length based on your height and weight)
  2. Then, compute the number of steps taken (use the accelerometer pattern)
  3. And determine the direction of moving (using a magnetometer)

Note that here, I have assumed that the user holds the phone in hand and the y-axis is towards the north or to your front, i.e., the phone is oriented well. I have also assumed that the user is not shaking the phone.

The app shows the number of steps taken and the direction of your walk in a UI.

# Running the app
1. Clone this repository.
   
        git clone https://github.com/Shivansh20128/AndroidDevelopment--Monitor-your-walk.git
3. Open the application in Android Studio.
4. Configure the jdk as given in the configuration files.
5. Run the app.
