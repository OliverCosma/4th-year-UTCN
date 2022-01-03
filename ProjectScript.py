#!/usr/bin/env python



import rospy
from geometry_msgs.msg import Twist
from sensor_msgs.msg import LaserScan
from nav_msgs.msg import Odometry

class Circling(): #main class
   
    def __init__(self): #main function
        global circle
        circle = Twist() #create object of twist type  
        self.pub = rospy.Publisher("/cmd_vel", Twist, queue_size=10) #publish message
        self.sub = rospy.Subscriber("/scan", LaserScan, self.callback) #subscribe message 
        self.sub = rospy.Subscriber("/odom", Odometry, self.odometry) #subscribe message

        # the function to avoid obstacles
    def callback(self, msg): 
        print 'Sensor data:'
        print 'Front: {}'.format(msg.ranges[0]) 
        print 'Left:  {}'.format(msg.ranges[90]) 
        print 'Right: {}'.format(msg.ranges[270]) 
        print 'Back: {}'.format(msg.ranges[180]) 
      
      	#Obstacle Avoidance
        self.distance = 2.0
        if msg.ranges[0] > self.distance and msg.ranges[15] > self.distance and msg.ranges[345] > self.distance: 
        #first - there are no obstacles
            circle.linear.x = 0.5 #start moving linear 
            circle.angular.z = 0.2 #rotate a little bit
            rospy.loginfo("The robot is moving around a circle")
        #second - there are obsacles 
        else: 
            rospy.loginfo("Danger! Obstacle!") #state case of detection
            circle.linear.x = 0.0 # stop
            circle.angular.z = 0.5 # rotate the robot 
            #third part - rotate the robot and then verify if other obstacles
            if msg.ranges[0] > self.distance and msg.ranges[15] > self.distance and msg.ranges[345] > self.distance and msg.ranges[45] > self.distance and msg.ranges[315] > self.distance:
                
                circle.linear.x = 0.5 #start again
                circle.angular.z = 0.1 #rotate a little bit 
        self.pub.publish(circle) # publish the move object

    def odometry(self, msg): #odometry - function to determine position change over time 
        print msg.pose.pose #print position and orientation of our robot in time 

#main part 
if __name__ == '__main__':
    rospy.init_node('obstacle_avoidance_node') #initilize node
    Circling() #run the function Circle (the class)
    rospy.spin() #loop the function 

