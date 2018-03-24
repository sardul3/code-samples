
/*
 *  SocNet class is a class that simulates the
 *  the social network where the users and their
 *  relation to each other are represented via a 
 *  graph. Here, the breadth first search is implemented
 *  and various characteristics of the network are
 *  presented. Also, the class, DiGraph.java is
 *  utilized to create the graph. From a given text file
 *  the relationship between the users is established and
 *  the network is created
 
 *  @author: Sagar Poudel
 *  @version: 05-05-2017
 */

// Importing the required classes
import java.io.*;
import java.util.*;


public class SocNet{

   // Various data-structures that are required for the implementation
   private DiGraph graph;
   private DiGraph followingGraph;
   
   // A map that helps store a user and his identity in the form of hashcode   
   private Map<Integer, String> convert;

/**
   * A constructor that helps initialize the data taken from the input file
   * and creates a graph of users with the edges that show their
   * relationship via the user's name and the computed hashcode
   
   * @param filename the name of the file where the data lies
*/
   public SocNet(String filename){
      // Initializing the graphs and the map
      graph = new DiGraph();
      followingGraph = new DiGraph();
      convert = new HashMap<>();
      try{
         Scanner scan = new Scanner(new File(filename));
         while(scan.hasNext()){
            
            /* The first name is stored as user
               and it's hashcode is computed */
            String user = scan.next();
            int userVertex = user.hashCode();
            
            /* The second name is stored as follower
               and it's hashcode is computed */
            String follower = scan.next();
            int followerVertex = follower.hashCode();
            
            /* The relationship between the users
               and the hashcode is established */
            convert.put(followerVertex, follower);
            convert.put(userVertex, user);
            
            // Adding the vertices to the DiGraph object
            graph.addVertex(userVertex);
            graph.addVertex(followerVertex);
            
            // Adding the vertices to the DiGraph object
            followingGraph.addVertex(userVertex);
            followingGraph.addVertex(followerVertex);
            
            /* The edges are added onto the graph which alongwith
               the vertices depict the relationships and the functioning
               of the social network */
            graph.addEdge(followerVertex, userVertex);
            followingGraph.addEdge(userVertex, followerVertex);
         
         
         }
      }
      /* Attempts to catch a FileNotFoundException */
      catch(FileNotFoundException e){
         System.out.println("File not Found!");
      }
   }

/**
   * This method returns the most popular user among the
   * social network members who is followed by others in
   * majority
   *
   * @return mostPopular the user who is most popular
*/     
   public String mostPopular(){
      int popular = 0;
      String mostPopular = "";
      // Looping through all the users
      for(int element : convert.keySet()){
         /* Looking for the user who's follower
            list has the largest of size */
         int size = graph.getAdjacent(element).size();
         if(size > popular){
            popular = size;
            // The name of the user gets saved and is later retrieved
            mostPopular = convert.get(element);
         }
      }
      return mostPopular;
   }

/**
   * A method to find out the user who follows other 
   * users more than any one else
   *
   * @return topFollower the user who mostly follows other users 
*/
   public String topFollower(){
      int follower = 0;
      String topFollower = "";
      // Looping through all the users
      for(int element : convert.keySet()){
         /* Utilizes the second graph object 
            created earlier */
         int size = followingGraph.getAdjacent(element).size();
         if(size > follower){
            // Gets the name of the topFollower from the map
            follower = size;
            topFollower = convert.get(element);
         }
      }
      return topFollower;
   }

/**
   * This method returns the set of users who are leaders as well.
   * By definion, leaders are those individuals who are 
   * followed by at least 30% of the total users and
   * their followers are more than following
   *
   * @return leaders the set of users who fulfill the above criteria and are popular
*/
   public Set<String> leaders(){
      // A new set to hold the result
      Set<String> leaders = new TreeSet<>();
      for(int element : convert.keySet()){
         int size = graph.getAdjacent(element).size();
         int followSize = followingGraph.getAdjacent(element).size();
         // More than 30% of the total users need to follow them
         if(size >= (0.3 * convert.size())){
            if(size > followSize){
               // Upon qualification, leaders are added to the list
               leaders.add(convert.get(element));
            }
         }
      }
          return leaders;
   }

/**
   * Computes the ratio of the total number of
   * edges in the graph to the total number of 
   * possible egdes
   *
   * @return the density of the graph
*/
   public double density(){
      // Uses the vertices method from the DiGraph class
      int N = graph.vertices();
      double numberEdges = (double) graph.edges();
      // This gives the density of the graph
      return (numberEdges / (N * (N-1)));
   }
 
/**
   * Returns the ratio of symmetrical egdes
   * present in the graph to the total number
   * of egdes of the graph
   *
   * @return the reciprocity of the graph
*/  
   public double reciprocity(){
      int count = 0;
      //Iterating through the elements
      for(int element : convert.keySet()){
         /* This nested loop looks for both-way relations 
            and symmetric edges */
         for(int entry : graph.getAdjacent(element)){
            if(followingGraph.getAdjacent(element).contains(entry)){
               count++;
            }     
         }
      }
      // For the accurate computation
      return ((double)count / graph.edges());
   }
  
/**
   * This method computes the most efficient number
   * of egdes that need to be traversed to get from
   * one node to the other. It utilizes the BFT private 
   * method that encloses the breadth first algorithm
   *
   * @param user1 the starting node
   * @param user2 the destination node
   * @return distance the minimum number of egdes 
*/
   public int distance(String user1, String user2){
      int distance = 0;
      // If the users are equal, their distance is zero
      if(!(user1.equals(user2))){
         distance = BFT(user1, user2).size();
         if(distance==0){
            /* As per instructions, for the vertices that don't
               end on each other, the max value of Integer is 
               displayed */
            distance = Integer.MAX_VALUE;
         }
      }
      return distance;
   }

/**
   * This method returns the vertices that
   * the edges lead to when following the 
   * shortest path from user1 to user2
   *
   * @param user1 the starting user
   * @param user2 the destination user
   * @return sb the string representation of the path 
*/ 
   public String path(String user1, String user2){
      // Creating a new StringBuilder object  
      StringBuilder sb = new StringBuilder();
      //If there is no path then NONE is returned
      if((BFT(user1,user2)).size() == 0)
         sb.append("[NONE]");
      else{
         /* The path is properly formatted and appended
            into the StringBuilder object to be returned
         */
         sb.append("["+user1);
         for(int i = BFT(user1,user2).size()-1; i>=0; i--){
            sb.append("|"+BFT(user1,user2).get(i));
         }
         sb.append("]");
      }
      return sb.toString();
   }
/**
   * Centrality method takes a user as an
   * argument and returns the mean length
   * of all the shortest path from the user
   * to all the other users
   *
   * @param user the name of the user 
   * @return the centrality of the user on the graph
*/   
   public double centrality(String user){
      double distance = 0;
      // Iterating through the available user list
      for(int element : convert.keySet()){
         /* Overall sum of the distance from a user to all
            other users */
         distance += distance(user, convert.get(element));
      }
      // Computes centrality
      return distance /(convert.size()-1);
   }

/**
   * This method returns the set of users who are 
   * accessable from a given user
   *
   * @param user the name of the user
   * @return the set of all the reachable members from that element
*/
   public Set<String> reachable(String user){
      Set<String> reachable = new TreeSet<String>();
      for(int element : convert.keySet()){
         // Checks to see if there is indeed any members
         if(! (path(user, convert.get(element)).equals("[NONE]"))){
            // If yes, they are added to the set and returned
            reachable.add(convert.get(element));
         }
      }
      return reachable;
   }  

/**
   * A private method that implements the breadth first
   * search and returns the shortest possible path between
   * the two users
   *
   * @param user1 the source user
   * @param user2 the destination user
   * @return the list of the most efficient ways to reach the destination
*/   
 private List<String> BFT(String user1, String user2){
   
      // path will store the final result
      List<String> path = new LinkedList<>();
      // Keeps track of the latest moves
      Map<Integer, Integer> prev = new HashMap<>();
      // Helps us avoid the repetative visit of the same vertices
      Set<Integer> visited = new HashSet<>();
      // A queue to implement breadth first search
      Queue<Integer> queue = new LinkedList<Integer>();
      
     /* converting the names to hashcodes */
      int source = user1.hashCode();
      int destination = user2.hashCode();
      int current = source;
     
      /* First node is added to the queue and is marked visited */
      queue.add(current);
      visited.add(current);
      
      while(!queue.isEmpty()){
         current = queue.poll();
         /* If the searched for user is found,
            we break out of the loop */
         if(current==destination){
            break;}
         else{
            for(int element : followingGraph.getAdjacent(current)){
               // If a new node or an area is entered
               if(!visited.contains(element)){
                  queue.add(element);
                  visited.add(element);
                  prev.put(element,current);
               } 
            }
         }
      }
      if(current!=destination){
         return path;
      }
      else{
         for(int i = destination; i != source; i = prev.get(i))
         {
            path.add(convert.get(i));
         }
         return path;
      }
   }  

}