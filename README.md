# Solution for Insight Data Engineering Code Challenge 2016

## Dependencies
- jGraphT: A free Java graph library that provides mathematical graph-theory objects and algorithms. (in .\lib folder)
- JSON.simple: A simple Java toolkit for JSON (in .\modules folder)
They are already in the project folder, so there will be no need to download them again.

## General workflow
- 1. Define a transaction class and it's corresponding comparator
- 2. Open up the input file and output file
- 3. read one line of text
- 4. Parse the text as a JSON object, and put its content in a transaction class
- 5. Check if the transaction object is valid, if not, skip the following step and go back to step 3.
- 6. Depending on the time of the current transaction, different actions has to be taken. All the valid transactions are stored in a priority queue with ascending order. See details in code.
- 7. Construct a undirected graph with the transactions in the last 60 seconds
- 8. Get the degree for each vertex and calculated the median
- 9. Output median degree to output.txt
- 10. if the next line is not empty, go back to step 3 and iterate.
- 11. Close the access to input and output file when all lines are read in the input file.