// example of program that calculates the  median degree of a 
// venmo transaction graph
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.jgrapht.*;
import org.jgrapht.graph.*;

// Define a Transaction class for each Transaction
class Transaction{
    String actor;
    String target;
    LocalDateTime created_time;

    // constructor takes JSON object as input
    public Transaction(JSONObject object) {
        this.actor = object.get("actor").toString();
        this.target = object.get("target").toString();

        String str = object.get("created_time").toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.created_time = LocalDateTime.parse(str, formatter);
    }

    // method to check if current transaction is valid
    public boolean isValid(){
        return this.actor!=null && this.target!=null && this.created_time!=null;
    }
}

// Define comparator for auto-sorting transactions in priority queue
class TransactionComparator implements Comparator<Transaction>{
    @Override
    public int compare(Transaction o1, Transaction o2) {
        if(o1.created_time.isBefore(o2.created_time)) return -1;
        else if(o1.created_time.isAfter(o2.created_time)) return 1;
        else return 0;
    }
}


public class median_degree{
    public static void main(String[] args) {
        PriorityQueue<Transaction> pq = new PriorityQueue<>(1, new TransactionComparator());
        System.out.println(pq.size());
        JSONParser parser = new JSONParser();
        LocalDateTime maxTimeStamp = null;
        double medianDegree = 0.00;
        UndirectedGraph<String, DefaultEdge> g = null;

        // temporary variables for parsing Json objects and Transactions
        Object obj = null;
        Transaction trans = null;
        try {
            // Read lines from the input file
//            File fin = new File(".\\venmo_input\\venmo-trans.txt");
            File fin = new File(args[0]);
            File fout = new File(args[1]);
            BufferedReader br = new BufferedReader(new FileReader(fin));
            BufferedWriter bw = new BufferedWriter(new FileWriter(fout));

            String line = null;
            while ((line = br.readLine()) != null && line.length()!=0) {
                obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                trans = new Transaction(jsonObject);
                System.out.println("The transaction is from "+trans.actor+" to "+trans.target + " at " + trans.created_time.toString());

                // if the transaction is valid, proceed to updating the priority queue of transactions
                if(trans.isValid()){
                    if(maxTimeStamp==null||pq.isEmpty()) {
                        pq.add(trans);
                        maxTimeStamp = trans.created_time;
                    }
                    else{
                        // if the new transaction is the latest one
                        if(maxTimeStamp.isBefore(trans.created_time)){
                            while(!pq.isEmpty()&& pq.peek().created_time.isBefore(trans.created_time.minusSeconds(60))){
                                pq.poll();
                            }
                            pq.add(trans);
                            maxTimeStamp = trans.created_time;
                        }
                        // if the new transaction falls into the 60 sec window
                        else if(maxTimeStamp.minusSeconds(60).isBefore(trans.created_time)) pq.add(trans);
                        // if the new transaction is before 60 sec from the max timestamp
                        else{
                            // add the last seen median degree to the result file
                            bw.write(String.format("%.2f",medianDegree)+"\n");
                            continue;
                        }
                    }

                    //Construct the graph with the transactions in the last 60 seconds
                    g = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
                    for(Transaction t: pq){
                        g.addVertex(t.actor);
                        g.addVertex(t.target);
                        g.addEdge(t.actor,t.target);
                    }

                    // calculate the median degree
                    int[] degrees = new int[g.vertexSet().size()];
                    int i=0;
                    for(String vertex: g.vertexSet()){
                        degrees[i] = g.degreeOf(vertex);
                        i++;
                    }
                    Arrays.sort(degrees);
                    int n = degrees.length;
                    if(n%2==0) medianDegree = ( degrees[n/2-1] + degrees[n/2])/2.0;
                    else medianDegree = degrees[n/2];

                    // output median degree to output.txt
                    bw.write(String.format("%.2f",medianDegree)+"\n");


                }

            }

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}